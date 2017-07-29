package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.DoWhileExpression;
import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.TerminalOrMethod;
import org.xtext.labs.mydsl.varAssignment;
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.debugger.context.Symbol;

public class BodyWhile extends ILogicalHelper implements IBody {

	DoWhileExpression e;
	IBodySwitcher exe;

	public BodyWhile(IBodySwitcher exe, DoWhileExpression e) {
		this.e = e;
		this.exe = exe;
	}

	public void execute(String funcId) {
		Symbol index = indexSymbol(e.getLoopConditon(), funcId);
		int loopCnt = indexCnt(e.getLoopConditon(), funcId);
		int indexVal = (int) index.getVarVal();
		String op = "";
		if (e instanceof varAssignment) {
			op = ((varAssignment) e).getOp().get(0);
		}


		// while(loopCnt < indexVal)
		while (_checkBool(indexVal, op, loopCnt) && !isBrk) {
			for (BodyStatement tn : e.getBody()) {
				if (isBrk) {
					break;
				} else {
					exe.executor(tn, funcId);
				}
			}

			if (isBrk) {
				break;
			} else {
				Symbol index2 = indexSymbol(e.getLoopConditon(), funcId);
				indexVal = (int) index2.getVarVal();
			}
		}
		
		isBrk = false; //initialize
	}

	private int indexCnt(varExpression e, String funcId) {
		int index = 0;

		if (e instanceof varAssignment) {
			TerminalOrMethod tr = ((varAssignment) e).getRight().get(0);

			if (tr instanceof Terminal) {
				Symbol sb = lookupSymbolByTerminal((Terminal) tr, funcId);
				index = (int) sb.getVarVal();
			}
		}
		return index;
	}

	private Symbol indexSymbol(varExpression e, String funcId) {
		Symbol rtn = null;

		if (e instanceof varAssignment) {
			TerminalOrMethod tl = ((varAssignment) e).getLeft();

			if (tl instanceof Terminal) {
				Symbol sb = lookupSymbolByTerminal((Terminal) tl, funcId);
				rtn = sb;
			}
		}
		return rtn;
	}

}
