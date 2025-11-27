package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.IfExpression;
import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.TerminalOrMethod;
import org.xtext.labs.mydsl.varAssignment;
import org.xtext.labs.mydsl.varExpression;

public class BodyIf extends AbstractLogicalHelper implements IBody {

	IfExpression e;
	AbstractBodySwitcher exe;

	public BodyIf(AbstractBodySwitcher exe, IfExpression e) {
		this.e = e;
		this.exe = exe;
	}

	public void execute(String funcId) {
		if (insideIf(e.getIfconditon(), funcId)) {
			for (BodyStatement tn : e.getThen()) {
				if (isBrk) {
					break;
				} else {
					exe.executor(tn, funcId);
				}
			}
		} else {
			for (BodyStatement tn : e.getElse()) {
				if (isBrk) {
					break;
				} else {
					exe.executor(tn, funcId);
				}
			}
		}
	}

	private boolean insideIf(varExpression e, String funcId) {
		boolean isInside = false;

		if (e instanceof varAssignment) {
			Terminal tl = ((varAssignment) e).getLeft();
			TerminalOrMethod tr = ((varAssignment) e).getRight() != null ? ((varAssignment) e).getRight().get(0) : null;

			Object rtn = DecoupleTerminal(tl, funcId);
			Object rtn2 = TerminalOrMethodExecutor(tr, funcId);

			isInside = evaluateCondition(rtn, ((varAssignment) e).getOp().get(0), rtn2);
		}
		return isInside;
	}

	protected Object TerminalOrMethodExecutor(TerminalOrMethod t, String funcId) {
		return exe.executeTerminalOrMethod(t, funcId, this);
	}

}
