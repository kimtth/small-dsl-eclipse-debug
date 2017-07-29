package org.xtext.labs.mydsl.debugger.context;

public class CallStackItem {

	String funcId;
	SymbolTable st;
	
	public CallStackItem(String funcId, SymbolTable st){
		this.funcId = funcId;
		this.st = st;
	}

	public SymbolTable getSt() {
		return st;
	}

	public void setSt(SymbolTable st) {
		this.st = st;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	@Override
	public String toString() {
		return "CallStackItem [funcId=" + funcId + ", st=" + st + ", toString()=" + super.toString() + "]";
	}
}
