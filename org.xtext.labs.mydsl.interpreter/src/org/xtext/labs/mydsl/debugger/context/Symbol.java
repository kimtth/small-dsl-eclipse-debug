package org.xtext.labs.mydsl.debugger.context;

public class Symbol {

	public String varType;
	public String varName;
	public Object varVal;
	public String scope;

	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public Object getVarVal() {
		return varVal;
	}

	public void setVarVal(Object varVal) {
		this.varVal = varVal;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "Symbol [varType=" + varType + ", varName=" + varName + ", varVal=" + varVal + ", scope=" + scope + "]";
	}
}
