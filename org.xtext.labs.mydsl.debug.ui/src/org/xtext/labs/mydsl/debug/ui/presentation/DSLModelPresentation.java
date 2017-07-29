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
package org.xtext.labs.mydsl.debug.ui.presentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.internal.ui.sourcelookup.SourceLookupFacility;
import org.eclipse.debug.internal.ui.sourcelookup.SourceLookupManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;
import org.xtext.labs.mydsl.debug.core.breakpoints.DSLLineBreakpoint;
import org.xtext.labs.mydsl.debug.core.model.DSLDebugTarget;
import org.xtext.labs.mydsl.debug.core.model.DSLStackFrame;
import org.xtext.labs.mydsl.debug.core.model.DSLThread;

/**
 * Renders PDA debug elements
 */
public class DSLModelPresentation extends LabelProvider implements IDebugModelPresentation {

	public void setAttribute(String attribute, Object value) {
	}

	public String getText(Object element) {
		if (element instanceof DSLDebugTarget) {
			return getTargetText((DSLDebugTarget) element);
		} else if (element instanceof DSLThread) {
			return getThreadText((DSLThread) element);
		} else if (element instanceof DSLStackFrame) {
			lookup((DSLStackFrame) element); // add:kim for testing
			return getStackFrameText((DSLStackFrame) element);
		}
		
		return null;
	}

	/**
	 * Returns a label for the given debug target
	 * 
	 * @param target
	 *            debug target
	 * @return a label for the given debug target
	 */
	private String getTargetText(DSLDebugTarget target) {
		try {
			String pgmPath = target.getLaunch().getLaunchConfiguration().getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM,
					(String) null);
			if (pgmPath != null) {
				IPath path = new Path(pgmPath);
				String label = "";
				if (target.isTerminated()) {
					label = "<terminated>";
				}
				return label + "DSL [" + path.lastSegment() + "]";
			}
		} catch (CoreException e) {
		}
		return "DSL";

	}

	/**
	 * Returns a label for the given stack frame
	 * 
	 * @param frame
	 *            a stack frame
	 * @return a label for the given stack frame
	 */
	private String getStackFrameText(DSLStackFrame frame) {
		try {
			return frame.getName() + " (line: " + frame.getLineNumber() + ")";
		} catch (DebugException e) {
		}
		return null;
	}

	// add:kim -> this is not recommend way to highlight source line.
	// maybe The Source code lookup try to find file's cache first before
	// launching SourceLookupManager.
	// so it could not trigger highlighting source code line after resume or
	// breakpoint action. so this method forcedly re-trigger highlighting
	// action.
	@SuppressWarnings("restriction")
	private void lookup(DSLStackFrame frame) {
		try {
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow[] win = wb.getWorkbenchWindows();
			IWorkbenchPage page = win[0].getActivePage();

			SourceLookupManager.getDefault().displaySource(frame, page, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a label for the given thread
	 * 
	 * @param thread
	 *            a thread
	 * @return a label for the given thread
	 */
	private String getThreadText(DSLThread thread) {
		String label = thread.getName();
		if (thread.isStepping()) {
			label += " (stepping)";
		} else if (thread.isSuspended()) {
			IBreakpoint[] breakpoints = thread.getBreakpoints();
			if (breakpoints.length == 0) {
				if (thread.getError() == null) {
					label += " (suspended)";
				} else {
					label += " (" + thread.getError() + ")";
				}
			} else {
				IBreakpoint breakpoint = breakpoints[0];
				if (breakpoint instanceof DSLLineBreakpoint) {
					DSLLineBreakpoint dslBreakpoint = (DSLLineBreakpoint) breakpoint;
					if (breakpoint instanceof DSLLineBreakpoint) {
						label += " (suspended at line breakpoint)";
					} else if (dslBreakpoint.isRunToLineBreakpoint()) {
						label += " (run to line)";
					}
				}
			}
		} else if (thread.isTerminated()) {
			label = "<terminated> " + label;
		}
		return label;
	}

	public void computeDetail(IValue value, IValueDetailListener listener) {
		String detail = "";
		try {
			detail = value.getValueString();
		} catch (DebugException e) {
		}
		listener.detailComputed(value, detail);
	}

	public IEditorInput getEditorInput(Object element) {
		if (element instanceof IFile) {
			return new FileEditorInput((IFile) element);
		}
		if (element instanceof ILineBreakpoint) {
			return new FileEditorInput((IFile) ((ILineBreakpoint) element).getMarker().getResource());
		}
		return null;
	}

	public String getEditorId(IEditorInput input, Object element) {
		if (element instanceof IFile || element instanceof ILineBreakpoint) {
			return "dsl.editor";
		}
		return null;
	}
}
