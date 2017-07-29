/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bjorn Freeman-Benson - initial API and implementation
 *******************************************************************************/
package org.xtext.labs.mydsl.debug.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.IBreakpointManagerListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;
import org.xtext.labs.mydsl.debug.core.breakpoints.DSLLineBreakpoint;
import org.xtext.labs.mydsl.debug.core.breakpoints.DSLRunToLineBreakpoint;

public class DSLDebugTarget extends DSLDebugElement
		implements IDebugTarget, IBreakpointManagerListener, IDSLEventListener {

	// associated system process (VM)
	private IProcess fProcess;

	// containing launch object
	private ILaunch fLaunch;

	// sockets to communicate with VM
	private Socket fRequestSocket;
	private PrintWriter fRequestWriter;
	private BufferedReader fRequestReader;
	private Socket fEventSocket;
	private BufferedReader fEventReader;

	// terminated state
	private boolean fTerminated = false;

	// threads
	private IThread[] fThreads;
	private DSLThread fThread;

	// event dispatch job
	private EventDispatchJob fEventDispatch;
	// event listeners
	private Vector fEventListeners = new Vector();

	/**
	 * Listens to events from the PDA VM and fires corresponding debug events.
	 */
	class EventDispatchJob extends Job {

		public EventDispatchJob() {
			super("DSL Event Dispatch");
			setSystem(true);
		}

		protected IStatus run(IProgressMonitor monitor) {
			String event = "";
			while (!isTerminated() && event != null) {
				try {
					event = fEventReader.readLine();
					if (event != null && !event.isEmpty()) { // kim: add isEmpty
						System.out.println("event: " + event); // kim: for
																// debugging
						Object[] listeners = fEventListeners.toArray();
						for (int i = 0; i < listeners.length; i++) {
							((IDSLEventListener) listeners[i]).handleEvent(event);
						}
					}
				} catch (IOException e) {
					System.out.println("exception occur");
					terminated();
				}
			}
			return Status.OK_STATUS;
		}

	}

	/**
	 * Registers the given event listener. The listener will be notified of
	 * events in the program being interpretted. Has no effect if the listener
	 * is already registered.
	 * 
	 * @param listener
	 *            event listener
	 */
	public void addEventListener(IDSLEventListener listener) {
		if (!fEventListeners.contains(listener)) {
			fEventListeners.add(listener);
		}
	}

	/**
	 * Deregisters the given event listener. Has no effect if the listener is
	 * not currently registered.
	 * 
	 * @param listener
	 *            event listener
	 */
	public void removeEventListener(IDSLEventListener listener) {
		fEventListeners.remove(listener);
	}

	/**
	 * Constructs a new debug target in the given launch for the associated PDA
	 * VM process.
	 * 
	 * @param launch
	 *            containing launch
	 * @param process
	 *            PDA VM
	 * @param requestPort
	 *            port to send requests to the VM
	 * @param eventPort
	 *            port to read events from
	 * @exception CoreException
	 *                if unable to connect to host
	 */
	public DSLDebugTarget(ILaunch launch, IProcess process, int requestPort, int eventPort) throws CoreException {
		super(null);
		fLaunch = launch;
		fProcess = process;
		addEventListener(this);
		try {
			// give interpreter a chance to start
			try {
				Thread.sleep(2000); // Kim: waiting until launching server.
			} catch (InterruptedException e) {
			}
			fRequestSocket = new Socket("127.0.0.1", requestPort);
			fRequestWriter = new PrintWriter(fRequestSocket.getOutputStream());
			fRequestReader = new BufferedReader(new InputStreamReader(fRequestSocket.getInputStream()));
			// give interpreter a chance to open next socket
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			fEventSocket = new Socket("127.0.0.1", eventPort);
			fEventReader = new BufferedReader(new InputStreamReader(fEventSocket.getInputStream()));
		} catch (UnknownHostException e) {
			requestFailed("Unable to connect to DSL VM", e);
		} catch (IOException e) {
			requestFailed("Unable to connect to DSL VM", e);
		}

		fThread = new DSLThread(this);
		fThreads = new IThread[] { fThread };
		fEventDispatch = new EventDispatchJob();
		fEventDispatch.schedule();

		IBreakpointManager breakpointManager = getBreakpointManager();
		breakpointManager.addBreakpointListener(this);
		breakpointManager.addBreakpointManagerListener(this);
	}

	public IProcess getProcess() {
		return fProcess;
	}

	public IThread[] getThreads() throws DebugException {
		return fThreads;
	}

	public boolean hasThreads() throws DebugException {
		return false;
	}

	public String getName() throws DebugException {
		return "MY DSL";
	}

	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		if (!isTerminated() && breakpoint.getModelIdentifier().equals(getModelIdentifier())) {
			try {
				String program = getLaunch().getLaunchConfiguration().getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM,
						(String) null);
				if (program != null) {
					IResource resource = null;
					if (breakpoint instanceof DSLRunToLineBreakpoint) {
						DSLRunToLineBreakpoint rtl = (DSLRunToLineBreakpoint) breakpoint;
						resource = rtl.getSourceFile();
					} else {
						IMarker marker = breakpoint.getMarker();
						if (marker != null) {
							resource = marker.getResource();
						}
					}
					if (resource != null) {
						IPath p = new Path(program);
						return resource.getFullPath().equals(p);
					}
				}
			} catch (CoreException e) {
			}
		}
		return false;
	}

	public IDebugTarget getDebugTarget() {
		return this;
	}

	public ILaunch getLaunch() {
		return fLaunch;
	}

	public boolean canTerminate() {
		return getProcess().canTerminate();
	}

	public boolean isTerminated() {
		return fTerminated || getProcess().isTerminated();
	}

	public void terminate() throws DebugException {
		getThread().terminate();
	}

	public boolean canResume() {
		return !isTerminated() && isSuspended();
	}

	public boolean canSuspend() {
		return !isTerminated() && !isSuspended();
	}

	public boolean isSuspended() {
		return !isTerminated() && getThread().isSuspended();
	}

	public void resume() throws DebugException {
		getThread().resume();
	}

	public void suspend() throws DebugException {
		getThread().suspend();
	}

	public void breakpointAdded(IBreakpoint breakpoint) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if ((breakpoint.isEnabled() && getBreakpointManager().isEnabled()) || !breakpoint.isRegistered()) {
					DSLLineBreakpoint DSLBreakpoint = (DSLLineBreakpoint) breakpoint;
					DSLBreakpoint.install(this);
				}
			} catch (CoreException e) {
			}
		}
	}

	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				DSLLineBreakpoint DSLBreakpoint = (DSLLineBreakpoint) breakpoint;
				DSLBreakpoint.remove(this);
			} catch (CoreException e) {
			}
		}
	}

	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if (breakpoint.isEnabled() && getBreakpointManager().isEnabled()) {
					breakpointAdded(breakpoint);
				} else {
					breakpointRemoved(breakpoint, null);
				}
			} catch (CoreException e) {
			}
		}
	}

	public boolean canDisconnect() {
		return false;
	}

	public void disconnect() throws DebugException {
	}

	public boolean isDisconnected() {
		return false;
	}

	public boolean supportsStorageRetrieval() {
		return false;
	}

	public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
		return null;
	}

	/**
	 * Notification we have connected to the VM and it has started. Resume the
	 * VM.
	 */
	private void started() {
		fireCreationEvent();
		installDeferredBreakpoints();
		try {
			resume();
		} catch (DebugException e) {
		}
	}

	/**
	 * Install breakpoints that are already registered with the breakpoint
	 * manager.
	 */
	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = getBreakpointManager().getBreakpoints(getModelIdentifier());
		for (int i = 0; i < breakpoints.length; i++) {
			breakpointAdded(breakpoints[i]);
		}
	}

	/**
	 * Called when this debug target terminates.
	 */
	private synchronized void terminated() {
		fTerminated = true;
		fThread = null;
		fThreads = new IThread[0];
		IBreakpointManager breakpointManager = getBreakpointManager();
		breakpointManager.removeBreakpointListener(this);
		breakpointManager.removeBreakpointManagerListener(this);
		fireTerminateEvent();
		removeEventListener(this);
	}

	public String sendRequest(String request) throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println(request);
			fRequestWriter.flush();
			try {
				// wait for reply
				return fRequestReader.readLine();
			} catch (IOException e) {
				requestFailed("Request failed: " + request, e);
			}
		}
		return null;
	}

	/**
	 * When the breakpoint manager disables, remove all registered breakpoints
	 * requests from the VM. When it enables, reinstall them.
	 */
	public void breakpointManagerEnablementChanged(boolean enabled) {
		IBreakpoint[] breakpoints = getBreakpointManager().getBreakpoints(getModelIdentifier());
		for (int i = 0; i < breakpoints.length; i++) {
			if (enabled) {
				breakpointAdded(breakpoints[i]);
			} else {
				breakpointRemoved(breakpoints[i], null);
			}
		}
	}

	public void handleEvent(String event) {
		if (event.equals("started")) {
			started();
		} else if (event.equals("terminated")) {
			terminated();
		}
	}

	/**
	 * Returns this debug target's single thread, or <code>null</code> if
	 * terminated.
	 * 
	 * @return this debug target's single thread, or <code>null</code> if
	 *         terminated
	 */
	protected synchronized DSLThread getThread() {
		return fThread;
	}
}
