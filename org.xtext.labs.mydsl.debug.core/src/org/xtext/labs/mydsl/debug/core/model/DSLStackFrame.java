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

import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

/**
 * PDA stack frame.
 */
public class DSLStackFrame extends DSLDebugElement implements IStackFrame {

	private DSLThread fThread;
	private String fFuncName;
	private int fLineNumber; // kim: fix fPc -> fLineNumber
	private String fFileName;
	private int fId;

	/**
	 * Constructs a stack frame in the given thread with the given frame data.
	 * 
	 * @param thread
	 * @param data
	 *            frame data
	 * @param id
	 *            stack frame id (0 is the bottom of the stack)
	 * @throws DebugException 
	 */
	public DSLStackFrame(DSLThread thread, String data, int id) throws DebugException {
		super(thread.getDSLDebugTarget());
		fId = id;
		fThread = thread;
		init(data);
	}

	/**
	 * Initializes this frame based on its data 
	 * 
	 * @param data
	 * @throws DebugException 
	 */
	private void init(String data) throws DebugException {
		String[] strings = data.split("&");
		String fileName = strings[0];
		fFileName = Paths.get(fileName).getFileName().toString();
		String pc = strings[1];
		fLineNumber = Integer.parseInt(pc);
		fFuncName = strings[2]; 
		
		IVariable[] vars = getDSLVariables(strings);

		fThread.setVariables(this, vars);
	}
	
	// @Comment: kim
	private IVariable[] getDSLVariables(String[] strings) throws DebugException {

		ArrayList<DSLVariable> varsl = new ArrayList<DSLVariable>();

		for (int i = 3; i < strings.length; i++) {
			if (strings[i].isEmpty() == false) {
				String[] cutoffStr = strings[i].split("\\|", -1);
				String varName = cutoffStr[1].trim();
				String varValue = "";

				// when value is "" empty string.
				if (cutoffStr.length > 2) {
					varValue = cutoffStr[2].trim();
					varValue = cutoffStr[2].trim().equals("null") ? "" : varValue;
					varValue = varValue.replace("@", ""); //temporal solution, need to fix
				}

				// @Comment kim:
				// if variable is not exits, create new.
				// if you create all variable every time after "stack" querying,
				// it takes much time for processing.
				// because fireChangeEvent consume take a heavy load for event
				// job and queuing.
				DSLVariable variable = isContainsDSLVariable(varName, varValue);
				if (variable == null) {
					varsl.add(new DSLVariable(this, varName, varValue, fFuncName));
				} else {
					// if exits, compare value with old value and new value.
					String oldValue = variable.getValue().getValueString();
					if (oldValue.equals(varValue) == false) {
						variable.setValue(varValue);
					}
					varsl.add(variable);
				}
			}
		}
		// kim: safety way to convert list to array
		DSLVariable[] vararr = varsl.toArray(new DSLVariable[varsl.size()]);

		return vararr;
	}

	private DSLVariable isContainsDSLVariable(String varName, String varValue) throws DebugException {

		DSLVariable isExits = null;

		IVariable[] variables = ((DSLThread) getThread()).getVariables(this);
		if (variables != null) {
			for (IVariable variable : variables) {
				if (variable != null) {
					if (variable.getName().equals(varName)) {
						return (DSLVariable) variable;
					}
				}
			}
		}

		return isExits;
	}

	public IThread getThread() {
		return fThread;
	}

	public IVariable[] getVariables() throws DebugException {
		return fThread.getVariables(this);
	}

	public boolean hasVariables() throws DebugException {
		return getVariables().length > 0;
	}

	public int getLineNumber() throws DebugException {
		return fLineNumber;
	}
	
	//kim: add
	public void setLineNumber(int fLineNumber){
		this.fLineNumber = fLineNumber;
	}

	public int getCharStart() throws DebugException {
		return -1;
	}

	public int getCharEnd() throws DebugException {
		return -1;
	}

	public String getName() throws DebugException {
		return fFuncName;
	}

	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return null;
	}

	public boolean hasRegisterGroups() throws DebugException {
		return false;
	}

	public boolean canStepInto() {
		return getThread().canStepInto();
	}

	public boolean canStepOver() {
		return getThread().canStepOver();
	}

	public boolean canStepReturn() {
		return getThread().canStepReturn();
	}

	public boolean isStepping() {
		return getThread().isStepping();
	}

	public void stepInto() throws DebugException {
		getThread().stepInto();
	}

	public void stepOver() throws DebugException {
		getThread().stepOver();
	}

	public void stepReturn() throws DebugException {
		getThread().stepReturn();
	}

	public boolean canResume() {
		return getThread().canResume();
	}

	public boolean canSuspend() {
		return getThread().canSuspend();
	}

	public boolean isSuspended() {
		return getThread().isSuspended();
	}

	public void resume() throws DebugException {
		getThread().resume();
	}

	public void suspend() throws DebugException {
		getThread().suspend();
	}

	public boolean canTerminate() {
		return getThread().canTerminate();
	}

	public boolean isTerminated() {
		return getThread().isTerminated();
	}

	public void terminate() throws DebugException {
		getThread().terminate();
	}

	/**
	 * Returns the name of the source file this stack frame is associated with.
	 * 
	 * @return the name of the source file this stack frame is associated with
	 */
	public String getSourceName() {
		return fFileName;
	}

	public boolean equals(Object obj) {
		if (obj instanceof DSLStackFrame) {
			DSLStackFrame sf = (DSLStackFrame) obj;
			return sf.getThread().equals(getThread()) && sf.getSourceName().equals(getSourceName()) && sf.fId == fId;
		}
		return false;
	}

	public int hashCode() {
		return getSourceName().hashCode() + fId;
	}

	/**
	 * Returns this stack frame's unique identifier within its thread
	 * 
	 * @return this stack frame's unique identifier within its thread
	 */
	protected int getIdentifier() {
		return fId;
	}
}
