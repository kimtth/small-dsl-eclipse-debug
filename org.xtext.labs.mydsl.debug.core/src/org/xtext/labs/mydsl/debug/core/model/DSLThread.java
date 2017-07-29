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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

/**
 * A PDA thread. A PDA VM is single threaded.
 */
public class DSLThread extends DSLDebugElement implements IThread, IDSLEventListener {

	/**
	 * Breakpoint this thread is suspended at or <code>null</code> if none.
	 */
	private IBreakpoint fBreakpoint;

	/**
	 * Whether this thread is stepping
	 */
	private boolean fStepping;

	/**
	 * Whether this thread is suspended
	 */
	public boolean fSuspended;

	/**
	 * Most recent error event or <code>null</code>
	 */
	private String fErrorEvent;

	/**
	 * Table mapping stack frames to current variables
	 */
	private Map<IStackFrame, IVariable[]> fVariables = new HashMap<IStackFrame, IVariable[]>();

	/**
	 * Constructs a new thread for the given target
	 * 
	 * @param target
	 *            VM
	 */
	public DSLThread(DSLDebugTarget target) {
		super(target);
		getDSLDebugTarget().addEventListener(this);
	}

	public IStackFrame[] getStackFrames() throws DebugException {
		if (isSuspended()) {
			String framesData = sendRequest("stack");
			if (framesData != null) {
				String[] frames = framesData.split("#");
				IStackFrame[] theFrames = new IStackFrame[frames.length];
				for (int i = 0; i < frames.length; i++) {
					String data = frames[i];
					theFrames[frames.length - i - 1] = new DSLStackFrame(this, data, i);
				}
				return theFrames;
			}
		}
		return new IStackFrame[0];
	}

	public boolean hasStackFrames() throws DebugException {
		return isSuspended();
	}

	public int getPriority() throws DebugException {
		return 0;
	}

	public IStackFrame getTopStackFrame() throws DebugException {

		IStackFrame[] frames = getStackFrames();
		if (frames.length > 0) {
			return frames[0];
		}

		System.out.println("top stack is null");
		return null;
	}

	public String getName() {
		return "Main thread";
	}

	public IBreakpoint[] getBreakpoints() {
		if (fBreakpoint == null) {
			return new IBreakpoint[0];
		}
		return new IBreakpoint[] { fBreakpoint };
	}

	/**
	 * Notifies this thread it has been suspended by the given breakpoint.
	 * 
	 * @param breakpoint
	 *            breakpoint
	 */
	public void suspendedBy(IBreakpoint breakpoint) {
		// System.out.println("can1");
		fBreakpoint = breakpoint;
		if (fSuspended == false) {
			fSuspended = true; // kim: retry setting fSuspended value
		}
		suspended(DebugEvent.BREAKPOINT);
	}

	public boolean canResume() {
		// TODO kim: resume state is always true. for temporally => back to
		// isSuspended()
		return isSuspended();
	}

	public boolean canSuspend() {
		// we cannot interrupt our debugger once it is running
		return false;
	}

	public boolean isSuspended() {
		// for debugging: System.out.println("isSuspended() Thread " +
		// fSuspended +"/"+ !isTerminated());
		return fSuspended && !isTerminated();
	}

	public void resume() throws DebugException {
		sendRequest("resume");
	}

	public void suspend() throws DebugException {
		sendRequest("suspend");
	}

	public boolean canStepInto() {
		return false;
	}

	public boolean canStepOver() {
		// System.out.println("can2" + isSuspended());
		return isSuspended();
	}

	public boolean canStepReturn() {
		return false;
	}

	public boolean isStepping() {
		return fStepping;
	}

	public void stepInto() throws DebugException {
		// kim: add, not support step into
		throw new DebugException(
				new Status(IStatus.ERROR, "org.xtext.labs.mydsl.debug.core", "stepInto() not supported"));
	}

	public void stepOver() throws DebugException {
		sendRequest("step");
	}

	public void stepReturn() throws DebugException {
		// kim: add, not support step into
		throw new DebugException(
				new Status(IStatus.ERROR, "org.xtext.labs.mydsl.debug.core", "stepReturn() not supported"));
	}

	public boolean canTerminate() {
		return !isTerminated();
	}

	public boolean isTerminated() {
		return getDebugTarget().isTerminated();
	}

	public void terminate() throws DebugException {
		sendRequest("exit");
	}

	/**
	 * Sets whether this thread is stepping
	 * 
	 * @param stepping
	 *            whether stepping
	 */
	private void setStepping(boolean stepping) {
		fStepping = stepping;
	}

	/**
	 * Sets whether this thread is suspended
	 * 
	 * @param suspended
	 *            whether suspended
	 */
	private void setSuspended(boolean suspended) {
		fSuspended = suspended;
	}

	/**
	 * Sets the most recent error event encountered, or <code>null</code> to
	 * clear the most recent error
	 * 
	 * @param event
	 *            one of 'unimpinstr' or 'nosuchlabel' or <code>null</code>
	 */
	private void setError(String event) {
		fErrorEvent = event;
	}

	/**
	 * Returns the most revent error event encountered since the last suspend,
	 * or <code>null</code> if none.
	 * 
	 * @return the most revent error event encountered since the last suspend,
	 *         or <code>null</code> if none
	 */
	public Object getError() {
		return fErrorEvent;
	}

	public void handleEvent(String event) {
		// clear previous state
		fBreakpoint = null;
		setStepping(false);

		// handle events
		if (event.startsWith("resumed")) {
			setSuspended(false);
			if (event.endsWith("step")) {
				setStepping(true);
				resumed(DebugEvent.STEP_OVER);
			} else if (event.endsWith("client")) {
				resumed(DebugEvent.CLIENT_REQUEST);
			}
		} else if (event.startsWith("suspended")) {
			setSuspended(true);

			if (event.endsWith("client")) {
				suspended(DebugEvent.CLIENT_REQUEST);
			} else if (event.endsWith("step")) {
				suspended(DebugEvent.STEP_END);
			} else if (event.startsWith("suspended event") && getError() != null) { // ??
				exceptionHit();
			}
		} else if (event.equals("started")) {
			fireCreationEvent();
		} else {
			setError(event);
		}

	}

	/**
	 * Notification the target has resumed for the given reason. Clears any
	 * error condition that was last encountered and fires a resume event, and
	 * clears all cached variables for stack frames.
	 * 
	 * @param detail
	 *            reason for the resume
	 */
	private void resumed(int detail) {
		setError(null);
		// synchronized (fVariables) {
		// fVariables.clear();
		// }
		fireResumeEvent(detail);
	}

	/**
	 * Notification the target has suspended for the given reason
	 * 
	 * @param detail
	 *            reason for the suspend
	 */
	private void suspended(int detail) {
		fireSuspendEvent(detail);
	}

	/**
	 * Notification an error was encountered. Fires a breakpoint suspend event.
	 */
	private void exceptionHit() {
		suspended(DebugEvent.BREAKPOINT);
	}

	/**
	 * Sets the current variables for the given stack frame. Called by PDA stack
	 * frame when it is created.
	 * 
	 * @param frame
	 * @param variables
	 */
	protected void setVariables(IStackFrame frame, IVariable[] variables) {
		synchronized (fVariables) {
			fVariables.put(frame, variables);
		}
	}

	/**
	 * Returns the current variables for the given stack frame, or
	 * <code>null</code> if none.
	 * 
	 * @param frame
	 *            stack frame
	 * @return variables or <code>null</code>
	 */
	protected IVariable[] getVariables(IStackFrame frame) {
		synchronized (fVariables) {
			return (IVariable[]) fVariables.get(frame);
		}
	}
}
