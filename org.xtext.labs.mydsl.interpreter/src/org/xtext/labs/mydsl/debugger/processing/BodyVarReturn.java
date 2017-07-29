package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.varReturn;
import org.xtext.labs.mydsl.debugger.context.CallStack;

public class BodyVarReturn extends IStackHelper implements IBody {

	varReturn e;

	public BodyVarReturn(varReturn v) {
		this.e = v;
	}

	@Override
	public void execute(String funcId) {
		isRtn = true;

		Terminal t = e.getRtn();
		Object val = getReturnValue(t, funcId);
		
		lastFunctionReturn = val; //save return value before remove from call stack.
		CallStack.getStack().removeLast(); // remove last item from call stack.
	}

	private Object getReturnValue(Terminal t, String funcId) {
		Object val = null;
		
		val = DecoupleTerminal(t, funcId);

		return val;
	}

}
