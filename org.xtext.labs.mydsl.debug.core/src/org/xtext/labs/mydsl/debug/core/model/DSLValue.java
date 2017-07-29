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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * Value of a PDA variable.
 */
public class DSLValue extends DSLDebugElement implements IValue {
	
	private String fValue;
	
	public DSLValue(DSLDebugTarget target, String value) {
		super(target);
		fValue = value;
	}
	
	public String getReferenceTypeName() throws DebugException {
		try {
			Integer.parseInt(fValue);
		} catch (NumberFormatException e) {
			return "text";
		}
		return "integer";
	}

	public String getValueString() throws DebugException {
		return fValue;
	}

	public boolean isAllocated() throws DebugException {
		return true;
	}

	public IVariable[] getVariables() throws DebugException {
		return new IVariable[0];
	}

	public boolean hasVariables() throws DebugException {
//		\w - Matches any word character.
//		\W - Matches any nonword character.
//		\s - Matches any white-space character.
//		\S - Matches anything but white-space characters.
//		\d - Matches any digit.
//		\D - Matches anything except digits.
		
		return fValue.split(",").length > 1;
	}

    public boolean equals(Object obj) {
        return obj instanceof DSLValue && ((DSLValue)obj).fValue.equals(fValue);
    }

    public int hashCode() {
        return fValue.hashCode();
    }
}
