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
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;

/**
 * Common function for DSL debug elemnets.
 */
public class DSLDebugElement extends DebugElement {

	/**
	 * Constructs a new debug element in the given target.
	 * 
	 * @param target debug target
	 */
	public DSLDebugElement(IDebugTarget target) {
		super(target);
	}

	public String getModelIdentifier() {
		return DebugCorePlugin.ID_DSL_DEBUG_MODEL;
	}

	public String sendRequest(String request) throws DebugException {
		return getDSLDebugTarget().sendRequest(request);
	}
	
	/**
	 * Returns the debug target as a PDA target.
	 * 
	 * @return DSL debug target
	 */
	protected DSLDebugTarget getDSLDebugTarget() {
	    return (DSLDebugTarget) getDebugTarget();
	}
	
	/**
	 * Returns the breakpoint manager
	 * 
     * @return the breakpoint manager
     */
    protected IBreakpointManager getBreakpointManager() {
        return DebugPlugin.getDefault().getBreakpointManager();
    }
}
