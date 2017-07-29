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
package org.xtext.labs.mydsl.debug.ui.breakpoints;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;
import org.xtext.labs.mydsl.debug.core.breakpoints.DSLLineBreakpoint;

/**
 * Adapter to create breakpoints in DSL files.
 */
public class DSLBreakpointAdapter implements IToggleBreakpointsTarget {

	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		ITextEditor textEditor = getEditor(part);
		if (textEditor != null) {
			IResource resource = (IResource) textEditor.getEditorInput().getAdapter(IResource.class);
			ITextSelection textSelection = (ITextSelection) selection;
			int lineNumber = textSelection.getStartLine();
			
			IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(DebugCorePlugin.ID_DSL_DEBUG_MODEL);
			for (int i = 0; i < breakpoints.length; i++) {
				IBreakpoint breakpoint = breakpoints[i];
				if (breakpoint instanceof ILineBreakpoint && resource.equals(breakpoint.getMarker().getResource())) {
					if (((ILineBreakpoint)breakpoint).getLineNumber() == (lineNumber + 1)) {
						// remove
						breakpoint.delete();
						return;
					}
				}
			}
			// create line breakpoint (doc line numbers start at 0)
			DSLLineBreakpoint lineBreakpoint = new DSLLineBreakpoint(resource, lineNumber + 1);
			DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(lineBreakpoint);
		}
	}

	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return getEditor(part) != null;
	}
	
	/**
	 * Returns the editor being used to edit a DSL file, associated with the
	 * given part, or <code>null</code> if none.
	 *  
	 * @param part workbench part
	 * @return the editor being used to edit a DSL file, associated with the
	 * given part, or <code>null</code> if none
	 */
	private ITextEditor getEditor(IWorkbenchPart part) {
		if (part instanceof ITextEditor) {
			ITextEditor editorPart = (ITextEditor) part;
			IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
			if (resource != null) {
				String extension = resource.getFileExtension();
				if (extension != null && extension.equals("dsl")) {
					return editorPart;
				}
			}
		}
		return null;		
	}
	
	@Override
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
	}

	@Override
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}
	
	@Override
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
	}

	@Override
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}
	
	/**
	 * Returns the variable and function names at the current line, or <code>null</code> if none.
	 * 
	 * @param part text editor
	 * @param selection text selection
	 * @return the variable and function names at the current line, or <code>null</code> if none.
	 *  The array has two elements, the first is the variable name, the second is the function name.
	 */
	private String[] getVariableAndFunctionName(IWorkbenchPart part, ISelection selection) {
	    ITextEditor editor = getEditor(part);
	    if (editor != null && selection instanceof ITextSelection) {
	        ITextSelection textSelection = (ITextSelection) selection;
	        IDocumentProvider documentProvider = editor.getDocumentProvider();
	        try {
	            documentProvider.connect(this);
	            IDocument document = documentProvider.getDocument(editor.getEditorInput());
	            IRegion region = document.getLineInformationOfOffset(textSelection.getOffset());
	            String string = document.get(region.getOffset(), region.getLength()).trim();
	            if (string.startsWith("var ")) {
	                String varName = string.substring(4).trim(); 
	                String fcnName = getFunctionName(document, varName, document.getLineOfOffset(textSelection.getOffset()));
	                return new String[] {varName, fcnName};
	            }
	        } catch (CoreException e) {
	        } catch (BadLocationException e) {
	        } finally {
	            documentProvider.disconnect(this);
	        }
	    }	    
	    return null;
	}
	
	/**
	 * Returns the name of the function containing the given variable defined at the given
	 * line number in the specified document.
	 * 
	 * @param document DSL source file
	 * @param varName variable name
	 * @param line line numbner at which the variable is defined
	 * @return name of function defining the variable
	 */
	private String getFunctionName(IDocument document, String varName, int line) {
	    // This is a simple guess at the function name - look for the labels preceeding
	    // the variable definition, and then see if there are any 'calls' to that
	    // label. If none, assumet the variable is in the "_main_" function
	    String source = document.get();
	    int lineIndex = line - 1;
	    while (lineIndex >= 0) {
            try {
                IRegion information = document.getLineInformation(lineIndex);
                String lineText = document.get(information.getOffset(), information.getLength());
                if (lineText.startsWith(":")) {
                    String label = lineText.substring(1);
                    if (source.indexOf("call " + label) >= 0) {
                        return label;
                    }
                }
                lineIndex--;
            } catch (BadLocationException e) {
            }
	    }
	    return "_main_";
	}
}
