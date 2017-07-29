package org.xtext.labs.mydsl.debug.core.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

public class DSLArray extends DSLValue {

	public DSLArray(DSLValue value) throws DebugException {
		super(value.getDSLDebugTarget(), value.getValueString());
	}

	public boolean hasVariables() throws DebugException {
		return true;
	}

	public IVariable[] getVariables() throws DebugException {
		String str = getValueString();
		str = str.substring(1, str.length() - 1); //remove first and last char "{" and "}"
		
		String[] words = str.split(",");
		IVariable[] variables = new IVariable[words.length];
		
		for (int i = 0; i < words.length; i++) {
			String wordforSplit = words[i];
			String word = wordValueGen(wordforSplit);
			variables[i] = new DSLArrayEntry(getDSLDebugTarget(), i, new DSLValue(getDSLDebugTarget(), word));
		}
		return variables;
	}

	private String wordValueGen(String wordforSplit) {
		String key = wordforSplit.split("=")[0].trim(); 
		String value = wordforSplit.split("=")[1].trim();
		
		key = "index:" + key.replace("_", ",");
		value = "value:" + value;
		
		return key + " " + value;
	}
}
