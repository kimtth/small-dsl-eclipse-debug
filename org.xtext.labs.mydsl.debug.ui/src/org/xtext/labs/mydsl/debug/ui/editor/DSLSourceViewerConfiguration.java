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

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * Source view configuration for the PDA editor
 */
public class DSLSourceViewerConfiguration extends TextSourceViewerConfiguration {

    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return null;
    }

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return null;
	}
    
    
}
