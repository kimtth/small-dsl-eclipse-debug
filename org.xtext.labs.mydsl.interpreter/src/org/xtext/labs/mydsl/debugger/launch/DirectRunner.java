package org.xtext.labs.mydsl.debugger.launch;

import org.eclipse.emf.common.util.EList;
import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.DSLProgram;
import org.xtext.labs.mydsl.mainDeclared;
import org.xtext.labs.mydsl.varDeclared;
import org.xtext.labs.mydsl.debugger.context.CallStack;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;
import org.xtext.labs.mydsl.debugger.context.SymbolTable;
import org.xtext.labs.mydsl.debugger.processing.IBodySwitcher;

public class DirectRunner extends IBodySwitcher {

	String threadName;
	DSLProgram program;

	public DirectRunner(DSLProgram prog) {
		program = prog;
	}

	@Override
	public void run() {
		if (program.getGlobal() != null) {
			execute(program.getGlobal());
		}

		execute(program.getMain());
		System.exit(0);
	}

	private void execute(mainDeclared mainDeclared) {
		CallStack.getStack().add(new CallStackItem("main", new SymbolTable()));
		
		for(BodyStatement v : mainDeclared.getBody()){
			executor(v, "main");
		}
	}

	private void execute(EList<varDeclared> global) {
		CallStack.getStack().add(new CallStackItem("global", new SymbolTable()));
		
		for(varDeclared v : global){
			executor(v, "global");
		}
	}
}
