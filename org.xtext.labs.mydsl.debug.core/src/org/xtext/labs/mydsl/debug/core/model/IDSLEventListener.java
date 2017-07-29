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

public interface IDSLEventListener {
	
	/**
	 * Notification the given event occurred in the target program
	 * being interpretted.
	 * 
	 * @param event the event
	 */
	public void handleEvent(String event);

}
