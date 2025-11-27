package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.TerminalOrMethod;
import org.xtext.labs.mydsl.arrayRef;
import org.xtext.labs.mydsl.varAssignment;
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.debugger.context.CallStack;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;

public class BodyVarExpression extends AbstractLogicalHelper implements IBody {

	varExpression e;
	AbstractBodySwitcher exe;

	public BodyVarExpression(AbstractBodySwitcher exe, varExpression e) {
		this.e = e;
		this.exe = exe;
	}

	@Override
	public void execute(String funcId) {
		if (e instanceof varAssignment) {
			Object returnVal = null;
			Object rightVal = null;

			for (String op : ((varAssignment) e).getOp()) {
				int opindex = ((varAssignment) e).getOp().indexOf(op);
				TerminalOrMethod tr = ((varAssignment) e).getRight().get(opindex);

				if (tr instanceof Terminal) {
					rightVal = DecoupleTerminal((Terminal)tr, funcId);
				} else if (tr instanceof AbstractMethodCall) {
					rightVal = TerminalOrMethodExecutor(tr, funcId);
				}

				boolean isCalc = false;
				boolean isAndOr = false;
				if (op.contains("+") || op.contains("-") || op.contains("/") || op.contains("*")) {
					isCalc = true;
				} 
				
				if (op.equals("=")) {
					returnVal = rightVal;
				} else if (isCalc) {
					returnVal = arithmetic((int) returnVal, op, (int) rightVal);
				} 

				Terminal tl = ((varAssignment) e).getLeft();
				if (tl instanceof arrayRef) {
					updateCallStackByArray((arrayRef)tl, returnVal, getCallerFuncId(funcId));
				} else {
					updateCallStackByTerminal(tl, returnVal, getCallerFuncId(funcId));
				}
			}
		}
	}

	private String getCallerFuncId(String funcId) {
		int lastindex = CallStack.getStack().size() - 1;
		CallStackItem itm = CallStack.getStack().get(lastindex);

		return itm.getFuncId();
	}
	
	private int arithmetic(int data1, String sign, int data2) {
		int _rtn = 0;

		if (sign.equals("-")) {
			_rtn = data1 - data2;
		} else if (sign.equals("+")) {
			_rtn = data1 + data2;
		} else if (sign.equals("/")) {
			_rtn = data1 / data2;
		} else if (sign.equals("*")) {
			_rtn = data1 * data2;
		}

		return _rtn;
	}
	
	protected Object TerminalOrMethodExecutor(TerminalOrMethod t, String funcId) {
		return exe.executeTerminalOrMethod(t, funcId, this);
	}
}