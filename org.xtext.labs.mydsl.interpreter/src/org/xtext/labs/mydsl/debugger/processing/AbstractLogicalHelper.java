package org.xtext.labs.mydsl.debugger.processing;

/**
 * Abstract helper class for logical and comparison operations.
 * Provides utilities for evaluating boolean expressions and comparisons.
 */
public abstract class AbstractLogicalHelper extends AbstractStackHelper {
	
	protected boolean evaluateLogical(boolean returnVal, String op, boolean rightVal) {
		boolean isTrue = true;
		
		if (op.contains("and")){
			if((returnVal & rightVal) == true){
				isTrue = true;
			}else{
				isTrue = false;
			}
		}else if(op.contains("or")){
			if((returnVal | rightVal) == true){
				isTrue = true;
			}else{
				isTrue = false;
			}
		}
		
		return isTrue;
	}
	
	protected boolean evaluateCondition(Object rtn, String op, Object rtn2) {
		boolean _check = false;

		if (op.equals("==")) {
			_check = rtn == rtn2;
		} else if (op.equals("!=")) {
			_check = rtn != rtn2;
		} else if (op.equals(">")) {
			_check = (int) rtn > (int) rtn2;
		} else if (op.equals(">=")) {
			_check = (int) rtn >= (int) rtn2;
		} else if (op.equals("<")) {
			_check = (int) rtn < (int) rtn2;
		} else if (op.equals("<=")) {
			_check = (int) rtn <= (int) rtn2;
		} else if (op.equals("and") || op.equals("or")) {
			_check = evaluateLogical((boolean) rtn, op, (boolean) rtn2);
		} 
		return _check;
	}
}
