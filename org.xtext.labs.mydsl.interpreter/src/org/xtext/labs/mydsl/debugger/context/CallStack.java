package org.xtext.labs.mydsl.debugger.context;

import java.util.LinkedList;

public class CallStack {

	// kim: for synchronized symbolTable between instance.
	// without synchronized symbolTable would not update properly between
	// instances of classes.
	static LinkedList<CallStackItem> stack = new LinkedList<CallStackItem>();

	public static synchronized LinkedList<CallStackItem> getStack() {
		return stack;
	}

	public static synchronized void setStack(LinkedList<CallStackItem> stack) {
		CallStack.stack = stack;
	}

}
