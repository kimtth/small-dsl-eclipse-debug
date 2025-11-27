package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.AbstractMethodCall;
import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.BrkStr;
import org.xtext.labs.mydsl.DoWhileExpression;
import org.xtext.labs.mydsl.IfExpression;
import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.TerminalOrMethod;
import org.xtext.labs.mydsl.varDeclared;
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.varReturn;
import org.xtext.labs.mydsl.debugger.InterpreterException;

/**
 * Abstract switcher for executing different body statement types.
 * Routes body statements to appropriate handlers based on their type.
 */
public class AbstractBodySwitcher extends AbstractProcessHandler {

	private static AbstractBodySwitcher instance;

	public void executor(BodyStatement v, String funcId) {
		ThreadStateForDebugging(v);
		IBody exe = null;

		try {
			if (v instanceof varExpression) {
				exe = new BodyVarExpression(this, (varExpression) v);
			} else if (v instanceof varReturn) {
				exe = new BodyVarReturn((varReturn) v);
			} else if (v instanceof varDeclared) {
				exe = new BodyVarDeclared((varDeclared) v);
			} else if (v instanceof IfExpression) {
				exe = new BodyIf(this, (IfExpression) v);
			} else if (v instanceof DoWhileExpression) {
				exe = new BodyWhile(this, (DoWhileExpression) v);
			} else if (v instanceof BrkStr) {
				exe = new BodyBrk((BrkStr) v);
			} else if (v instanceof AbstractMethodCall) {
				exe = new BodyMethodCall(this, (AbstractMethodCall) v);
			} else {
				new InterpreterException("unsupported body expression");
			}

			exe.execute(funcId);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Execute a terminal or method call and return the result.
	 * This common method is used by expression evaluators.
	 */
	protected Object executeTerminalOrMethod(TerminalOrMethod t, String funcId, AbstractStackHelper helper) {
		Object rtn = null;

		if (t instanceof Terminal) {
			rtn = helper.DecoupleTerminal((Terminal) t, funcId);
		} else if (t instanceof AbstractMethodCall) {
			executor((AbstractMethodCall) t, funcId);
			rtn = AbstractStackHelper.lastFunctionReturn;
		}

		return rtn;
	}

	@Override
	public void run() {
		super.start();
	}

}
