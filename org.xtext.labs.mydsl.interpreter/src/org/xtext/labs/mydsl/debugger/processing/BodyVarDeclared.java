package org.xtext.labs.mydsl.debugger.processing;

import java.util.HashMap;
import java.util.Map;

import org.xtext.labs.mydsl.varDeclared;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;
import org.xtext.labs.mydsl.debugger.context.Symbol;
import org.xtext.labs.mydsl.debugger.context.SymbolTable;

public class BodyVarDeclared extends IStackHelper implements IBody {

	varDeclared e;

	public BodyVarDeclared(varDeclared e) {
		this.e = e;
	}
	
	@Override
	public void execute(String funcId) {
		String scope = e.getScope();
		String name = e.getName();
		String type = e.getType();
		
		Symbol sb = new Symbol();
		sb.setScope(scope);
		sb.setVarName(name);
		sb.setVarType(type);
		
		if(e.getDim().size() > 0){
			sb = arrayValueGenetrator(sb); //initialize symbol value when type is array.
		}
		
		addSymbolTable(sb, funcId);
	}
	
	private Symbol arrayValueGenetrator(Symbol va) {

		// array is convert into map internally.
		// num[10][20][30] => Map<"10,20,30", value>
		Map<String, Object> dim = new HashMap<String, Object>();
		va.setVarVal(dim);

		return va;
	}

	private void addSymbolTable(Symbol sb, String funcId) {
		CallStackItem item = lookupStackItem(funcId);
		
		if(item != null){
			SymbolTable tb = item.getSt();
			tb.getSymbolTable().add(sb);
		}
	}

}
