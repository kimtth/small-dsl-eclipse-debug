package org.xtext.labs.mydsl.debugger.processing;

import java.util.Iterator;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.debugger.context.StateContext;
import org.xtext.labs.mydsl.debugger.event.EventStateHandler;

public abstract class IProcessHandler extends ThreadLauncher {

	protected void ThreadStateForDebugging(BodyStatement v) {
		waitOrResumeBodyExpr(v);

		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			System.out.println("InterruptedException: Thread.sleep(1)");
			e1.printStackTrace();
		}

		synchronized (this) {
			while (suspended) {
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("InterruptedException: " + suspended);
					e.printStackTrace();
				}
			}
		}
		
	}

	protected void waitOrResumeBodyExpr(BodyStatement b) {
		// http://stackoverflow.com/questions/18369695/how-to-get-location-th-line-of-eobject-in-original-document
		INode node = NodeModelUtils.getNode(b);
		StateContext.srcline = node.getStartLine();

		// kim: suspend should be after response. if not, sending will be blocked.
		// the stepping line should be checked before check breakpoint.
		if (isResumeStepping() == false) {
			findBreakLines(StateContext.srcline);
			if (StateContext.srcline == StateContext.breakpointSuspendedline) {
				EventStateHandler.update("state#suspended breakpoint " + StateContext.srcline);
				suspend();
			}
		}
	}

	/*
	 * 01. step can be trigger when suspended -> 02. it resume token processing
	 * -> 03. and then suspend once again
	 */
	private boolean isResumeStepping() {
		boolean isSuspend = false;

		if (StateContext.state.equals("RESUME_STEP")) {
			resume();
		}
		if (StateContext.state.equals("SUSPEND_STEP")) {
			suspendStep();
			isSuspend = true;
		}

		return isSuspend;
	}

	private void findBreakLines(int srcLine) {
		Iterator<Integer> itr = StateContext.breaklines.iterator();

		while (itr.hasNext()) {
			int brkLine = itr.next();
			if (srcLine == brkLine) {
				StateContext.breakpointSuspendedline = brkLine;
				StateContext.state = "SUSPEND";
				break;
			}
		}
	}
	
	public synchronized void suspend() {
		suspended = true;
		StateContext.state = "SUSPEND";
	}

	public synchronized void resume() {
		suspended = false;
		notify();

		if (StateContext.state.equals("RESUME_STEP")) {
			EventStateHandler.update("state#resumed step");
			StateContext.state = "SUSPEND_STEP";
		} else if (StateContext.state.equals("RESUME")) {
			EventStateHandler.update("state#resumed client");
		}
	}

	private void suspendStep() {
		suspended = true;
		EventStateHandler.update("state#suspended step");
	}

}
