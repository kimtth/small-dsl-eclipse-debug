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
package org.xtext.labs.mydsl.debug.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.xtext.labs.mydsl.debug.ui.breakpoints.DSLBreakpointAdapter;

/**
 * Action to toggle a breakpoint
 */
public class ToggleBreakpointAction extends Action {

	private ITextEditor fEditor;
	private IVerticalRulerInfo fRulerInfo;

	/**
	 * Constructs a new action to toggle a PDA breakpoint
	 * 
	 * @param editor
	 *            the editor in which to toggle the breakpoint
	 * @param rulerInfo
	 *            specifies breakpoint location
	 */
	public ToggleBreakpointAction(ITextEditor editor, IVerticalRulerInfo rulerInfo) {
		super("Toggle Line Breakpoint");
		fEditor = editor;
		fRulerInfo = rulerInfo;
	}

	public void run() {
		DSLBreakpointAdapter adapter = new DSLBreakpointAdapter();
		int line = fRulerInfo.getLineOfLastMouseButtonActivity();
		IDocumentProvider provider = fEditor.getDocumentProvider();
		ITextSelection selection = null;
		try {
			provider.connect(this);
			IDocument document = provider.getDocument(fEditor.getEditorInput());
			IRegion region = document.getLineInformation(line);
			selection = new TextSelection(document, region.getOffset(), region.getLength());
		} catch (CoreException e1) {
		} catch (BadLocationException e) {
		} finally {
			//provider.disconnect(this); //kim: remove this line for conflict with xtext editor
		}
		if (selection != null) {
			try {
				if (adapter.canToggleLineBreakpoints(fEditor, selection)) {
					adapter.toggleLineBreakpoints(fEditor, selection);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
