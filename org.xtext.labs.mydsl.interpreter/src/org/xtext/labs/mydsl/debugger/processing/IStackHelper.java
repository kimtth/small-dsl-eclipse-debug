package org.xtext.labs.mydsl.debugger.processing;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xtext.labs.mydsl.FuncParameter;
import org.xtext.labs.mydsl.StringRef;
import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.arrayDimension;
import org.xtext.labs.mydsl.arrayRef;
import org.xtext.labs.mydsl.boolRef;
import org.xtext.labs.mydsl.numRef;
import org.xtext.labs.mydsl.varDeclared;
import org.xtext.labs.mydsl.varRef;
import org.xtext.labs.mydsl.varSymbol;
import org.xtext.labs.mydsl.debugger.context.CallStack;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;
import org.xtext.labs.mydsl.debugger.context.Symbol;
import org.xtext.labs.mydsl.debugger.context.SymbolTable;

public abstract class IStackHelper {

	static boolean isBrk = false;
	static boolean isRtn = false;
	static Object lastFunctionReturn = null;
	
	/**
	 * Manipulate CallStack
	 * 
	 * @param funcId
	 * @return
	 */
	protected CallStackItem lookupStackItem(String funcId) {
		Iterator<CallStackItem> itr = CallStack.getStack().iterator();

		while (itr.hasNext()) {
			CallStackItem item = itr.next();

			if (item.getFuncId() == funcId) {
				return item;
			}
		}
		return null;
	}

	protected Symbol lookupSymbolByTerminal(Terminal terminal, String funcId) {

		// if Terminal is StringRef or numRef, the value is not inside call
		// stack. so make dummy symbol and return it.
		if (isDummy(terminal)) {
			Symbol dummy = dummySymbol(terminal);
			return dummy;
		}

		String name = varRefID_finder(terminal);
		CallStackItem item = lookupStackItem(funcId);
		Iterator<Symbol> itr = item.getSt().getSymbolTable().iterator();
		Symbol sym = null;
		boolean isTryGlobal = false;

		while (itr.hasNext()) {
			sym = itr.next();

			if (sym.getVarName() == name) {
				isTryGlobal = false;
				return sym;
			} else {
				isTryGlobal = true;
			}
		}

		if (isTryGlobal) {
			CallStackItem item2 = lookupStackItem("global");
			Iterator<Symbol> itr2 = item2.getSt().getSymbolTable().iterator();

			while (itr2.hasNext()) {
				sym = itr2.next();

				if (sym.getVarName() == name) {
					return sym;
				}
			}
		}

		return sym;
	}
	
	private Symbol lookupSymbolByTerminal(String varName, String funcId) {
		CallStackItem item = lookupStackItem(funcId);
		Iterator<Symbol> itr = item.getSt().getSymbolTable().iterator();
		Symbol sym = null;
		boolean isTryGlobal = false;

		while (itr.hasNext()) {
			sym = itr.next();

			if (sym.getVarName() == varName) {
				isTryGlobal = false;
				return sym;
			} else {
				isTryGlobal = true;
			}
		}

		if (isTryGlobal) {
			CallStackItem item2 = lookupStackItem("global");
			Iterator<Symbol> itr2 = item2.getSt().getSymbolTable().iterator();

			while (itr2.hasNext()) {
				sym = itr2.next();

				if (sym.getVarName() == varName) {
					return sym;
				}
			}
		}

		return sym;
	}

	private Symbol dummySymbol(Terminal terminal) {
		Symbol dummy = new Symbol();

		if (terminal instanceof StringRef) {
			String varVal = ((StringRef) terminal).getValue();
			dummy.setVarVal(varVal);
		}
		if (terminal instanceof numRef) {
			int varVal = ((numRef) terminal).getValue();
			dummy.setVarVal(varVal);
		}
		
		if (terminal instanceof boolRef) {
			String varVal = ((boolRef) terminal).getVarRef();
			boolean boolVal = false;
			
			if(varVal == "true"){
				boolVal = true;
			}
			
			dummy.setVarVal(boolVal);
		}

		return dummy;
	}

	private boolean isDummy(Terminal terminal) {
		if (terminal instanceof StringRef) {
			return true;
		}
		if (terminal instanceof numRef) {
			return true;
		}
		if (terminal instanceof boolRef) {
			return true;
		}
		return false;
	}

	protected void pushCallStackItem(String funcId) {
		CallStack.getStack().add(new CallStackItem(funcId, new SymbolTable()));
	}

	protected void popCallStackItem(String funcId) {
		CallStackItem item = lookupStackItem(funcId);
		CallStack.getStack().remove(item);
	}

	protected void updateCallStackByTerminal(Terminal t, Object val, String funcId) {
		Symbol sm = lookupSymbolByTerminal(t, funcId);
		sm.setVarVal(val);
	}

	protected void addCallStackBySymbol(Symbol sm, String funcId) {
		CallStackItem item = lookupStackItem(funcId);
		item.getSt().getSymbolTable().add(sm);
	}

	/**
	 * For Decoupling Terminal
	 * 
	 * @param t
	 * @param funcId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	protected Object DecoupleTerminal(Terminal t, String funcId) {

		Object rtn = null;

		if (t instanceof StringRef) {
			rtn = ((StringRef) t).getValue();
		}
		
		if (t instanceof boolRef) {
			String bool = ((boolRef) t).getVarRef();
			if(bool.equals("true")){
				rtn = true;
			}else{
				rtn = false;
			}
		}

		if (t instanceof numRef) {
			rtn = ((numRef) t).getValue();
		}

		if (t instanceof arrayRef) {
			Symbol sym = lookupSymbolByTerminal(t, funcId);
			Object dimMap = sym.getVarVal();

			String dimIndex = arrayKeyGenerator((arrayRef) t, funcId);

			for (Entry<String, Object> bar : ((Map<String, Object>) dimMap).entrySet()) {
				if (bar.getKey().equals(dimIndex)) {
					rtn = bar.getValue();
				}
			}
		}

		if (t instanceof varRef) {
			Symbol sb = lookupSymbolByTerminal(t, funcId);
			rtn = sb.getVarVal();
		}

		return rtn;
	}
	
	@SuppressWarnings("unchecked")
	protected void updateCallStackByArray(arrayRef arrleft, Object returnVal, String callerFuncId) {

		String dimIndex = arrayKeyGenerator(arrleft, callerFuncId);
		
		String target = arrleft.getVarRef().getName();
		CallStackItem item = lookupStackItem(callerFuncId);

		for (Symbol s : item.getSt().getSymbolTable()) {
			if (target.equals(s.varName)) {
				Map<String, Object> map = (Map<String, Object>) s.getVarVal();
				map.put(dimIndex, returnVal);
				s.setVarVal(map);
			}
		}
		
	}
	
	protected String arrayKeyGenerator(arrayRef arrleft, String callerFuncId) {
		String dimIndex = "";
		
		for(arrayDimension dim : arrleft.getDim()){
			int index = 0;
			if (dim.getIndex() != null) {
				varSymbol vs = dim.getIndex();
				String varName = varSymbolID_finder(vs);
				Symbol sym2 = lookupSymbolByTerminal(varName, callerFuncId);
				index = (int) sym2.getVarVal();
			} else {
				index = (int) dim.getSize();
			}
			
			dimIndex += String.valueOf(index);
			
			if (((arrayRef) arrleft).getDim().size() != ((arrayRef) arrleft).getDim().indexOf(dim) + 1) {
				dimIndex += "_";
			}
		}

		return dimIndex;
	}
	
	
	/**
	 * Private Method Area
	 * 
	 * @param terminal
	 * @return
	 */

	private String varRefID_finder(Terminal terminal) {

		String name = "";

		if (terminal instanceof varRef) {
			name = varSymbolID_finder(((varRef) terminal).getVarRef());
		}

		if (terminal instanceof arrayRef) {
			name = varSymbolID_finder(((arrayRef) terminal).getVarRef());
		}

		return name;
	}

	private String varSymbolID_finder(varSymbol varRef) {
		String name = "";
		if (varRef instanceof varDeclared) {
			name = varRef.getName();
		}

		if (varRef instanceof FuncParameter) {
			name = varRef.getName();
		}
		return name;
	}
}
