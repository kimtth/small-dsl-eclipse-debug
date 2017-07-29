/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bjorn Freeman-Benson - initial API and implementation
 *******************************************************************************/
package org.xtext.labs.mydsl.debug.core.model;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * A variable in a PDA stack frame
 */
public class DSLVariable extends DSLDebugElement implements IVariable {

	// name & stack frmae
	private String fName;
	private String fScope;
	private DSLStackFrame fFrame;
	private IValue fValue;

	/**
	 * Constructs a variable contained in the given stack frame with the given
	 * name.
	 * 
	 * @param frame
	 *            owning stack frame
	 * @param name
	 *            variable name
	 * @param scope
	 * @param varValue
	 * @throws DebugException
	 */
	public DSLVariable(DSLStackFrame frame, String varName, String varValue, String scope) {
		super(frame.getDSLDebugTarget());
		fFrame = frame;
		fName = varName;
		fScope = scope;
		setValue(varValue);
	}

	public IValue getValue() throws DebugException {
		return fValue;
	}

	public String getName() throws DebugException {
		// int lastSpace = fScope.lastIndexOf("_");
		//
		// String scopeStr;
		// if(lastSpace == -1){
		// scopeStr = fScope;
		// }else{
		// scopeStr = fScope.substring(0, lastSpace);
		// }
		//
		// return fName + " :: " + scopeStr;
		return fName;
	}

	public String getReferenceTypeName() throws DebugException {
		return "Value";
	}

	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	public void setValue(String expression) {
		fValue = new DSLValue(this.getDSLDebugTarget(), expression);
		fireChangeEvent(DebugEvent.CONTENT);
	}

	public void setValue(IValue value) throws DebugException {
		fValue = value;
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

	/**
	 * Returns the stack frame owning this variable.
	 * 
	 * @return the stack frame owning this variable
	 */
	protected DSLStackFrame getStackFrame() {
		return fFrame;
	}

}
