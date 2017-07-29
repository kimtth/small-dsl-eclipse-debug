package org.xtext.labs.mydsl.debug.core.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class DSLArrayEntry extends DSLDebugElement implements IVariable {

	private IValue fValue;
	private int fIndex;
	
	public DSLArrayEntry(IDebugTarget target, int index, IValue value) {
		super(target);
		fValue = value;
		fIndex = index;
	}

	public IValue getValue() throws DebugException {
		return fValue;
	}

	public String getName() throws DebugException {
		return "[" + fIndex + "]";
	}

	public String getReferenceTypeName() throws DebugException {
		return "Array";
	}

	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	public void setValue(String expression) throws DebugException {
	}

	public void setValue(IValue value) throws DebugException {
	}

	public boolean supportsValueModification() {
		return false;
	}

	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

}
