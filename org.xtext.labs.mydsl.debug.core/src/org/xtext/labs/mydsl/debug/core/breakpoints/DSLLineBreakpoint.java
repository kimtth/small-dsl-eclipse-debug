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
package org.xtext.labs.mydsl.debug.core.breakpoints;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.LineBreakpoint;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;
import org.xtext.labs.mydsl.debug.core.model.DSLDebugTarget;
import org.xtext.labs.mydsl.debug.core.model.DSLThread;
import org.xtext.labs.mydsl.debug.core.model.IDSLEventListener;

/**
 * PDA line breakpoint
 */
public class DSLLineBreakpoint extends LineBreakpoint implements IDSLEventListener {
	
	// target currently installed in
	private DSLDebugTarget fTarget;
	private int fLineNumber; //kim: add
	
	/**
	 * Default constructor is required for the breakpoint manager
	 * to re-create persisted breakpoints. After instantiating a breakpoint,
	 * the <code>setMarker(...)</code> method is called to restore
	 * this breakpoint's attributes.
	 */
	public DSLLineBreakpoint() {
	}
	
	/**
	 * Constructs a line breakpoint on the given resource at the given
	 * line number. The line number is 1-based (i.e. the first line of a
	 * file is line number
	 * 
	 * @param resource file on which to set the breakpoint
	 * @param lineNumber 1-based line number of the breakpoint
	 * @throws CoreException if unable to create the breakpoint
	 */
	public DSLLineBreakpoint(final IResource resource, final int lineNumber) throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = resource.createMarker("org.xtext.labs.mydsl.debug.core.lineBreakpoint");
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + lineNumber + "]");
			}
		};
		run(getMarkerRule(resource), runnable);
	}
	
	public String getModelIdentifier() {
		return DebugCorePlugin.ID_DSL_DEBUG_MODEL;
	}
	
	/**
	 * Returns whether this breakpoint is a run-to-line breakpoint
	 * 
	 * @return whether this breakpoint is a run-to-line breakpoint
	 */
	public boolean isRunToLineBreakpoint() {
		return false;
	}
    
    /**
     * Installs this breakpoint in the given interprettor.
     * Registeres this breakpoint as an event listener in the
     * given target and creates the breakpoint specific request.
     * 
     * @param target PDA interprettor
     * @throws CoreException if installation fails
     */
    public void install(DSLDebugTarget target) throws CoreException {
    	fTarget = target;
    	target.addEventListener(this);
    	createRequest(target);
    }
    
    /**
     * Create the breakpoint specific request in the target. Subclasses
     * should override.
     * 
     * @param target PDA interprettor
     * @throws CoreException if request creation fails
     */
    protected void createRequest(DSLDebugTarget target) throws CoreException {
    	target.sendRequest("brk#" + getLineNumber());
    }
    
    /**
     * Removes this breakpoint's event request from the target. Subclasses
     * should override.
     * 
     * @param target PDA interprettor
     * @throws CoreException if clearing the request fails
     */
    protected void clearRequest(DSLDebugTarget target) throws CoreException {
    	target.sendRequest("unbrk#" + getLineNumber());
    }
    
    /**
     * Removes this breakpoint from the given interprettor.
     * Removes this breakpoint as an event listener and clears
     * the request for the interprettor.
     * 
     * @param target PDA interprettor
     * @throws CoreException if removal fails
     */
    public void remove(DSLDebugTarget target) throws CoreException {
    	target.removeEventListener(this);
    	clearRequest(target);
    	fTarget = null;
    	
    }
    
    /**
     * Returns the target this breakpoint is installed in or <code>null</code>.
     * 
     * @return the target this breakpoint is installed in or <code>null</code>
     */
    protected DSLDebugTarget getDebugTarget() {
    	return fTarget;
    }
    
    /**
     * Notify's the PDA interprettor that this breakpoint has been hit.
     */
    protected void notifyThread() {
    	if (fTarget != null) {
			try {
				IThread[] threads = fTarget.getThreads();
				if (threads.length == 1) {
	    			DSLThread thread = (DSLThread)threads[0];
	    			thread.suspendedBy(this);
	    		}
			} catch (Exception e) {
			}    		
    	}
    }

	public void handleEvent(String event) {
		if (event.startsWith("suspended breakpoint")) {
			handleHit(event);
		}
	}
    
	/**
     * Determines if this breakpoint was hit and notifies the thread.
     * 
     * @param event breakpoint event
     */
    private void handleHit(String event) {
    	int lastSpace = event.lastIndexOf(' ');
    	if (lastSpace > 0) {
    		String line = event.substring(lastSpace + 1);
    		int lineNumber = Integer.parseInt(line);
    		//lineNumber++; 無駄な時間をこれのせいで使かってしまった。変なソース 
    		//remove line. the interpreter base index is same as editor. index start from 1.
    		try {
    			// System.out.println("marker=: " + getLineNumber() + " / req=lineNumber: " + lineNumber);
				if (getLineNumber() == lineNumber) {
					notifyThread();
				}
    		} catch (CoreException e) {
    		}
    	}
    }		
}
