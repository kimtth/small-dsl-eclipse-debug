package org.xtext.labs.mydsl.debug.core.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.ILogicalStructureTypeDelegate;
import org.eclipse.debug.core.model.IValue;

/**
 * @comment: kim
 * this function will be activated when you click "Show Logical Structure" button in Variables View in Debug Respective.
 *
 */
public class DSLArrayStructureDelegate implements ILogicalStructureTypeDelegate {

	@Override
	public boolean providesLogicalStructure(IValue value) {
		try {
			String string = value.getValueString();
			String[] words = string.split(",");
			return words.length > 1;
		} catch (DebugException e) {
		}
		return false;
	}

	@Override
	public IValue getLogicalStructure(IValue value) throws CoreException {
		return new DSLArray((DSLValue)value);
	}

}
