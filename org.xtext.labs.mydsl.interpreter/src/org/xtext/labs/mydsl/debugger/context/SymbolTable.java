package org.xtext.labs.mydsl.debugger.context;

import java.util.ArrayList;

public class SymbolTable {
	
	ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();

	public ArrayList<Symbol> getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(ArrayList<Symbol> symbolTable) {
		this.symbolTable = symbolTable;
	}

}
