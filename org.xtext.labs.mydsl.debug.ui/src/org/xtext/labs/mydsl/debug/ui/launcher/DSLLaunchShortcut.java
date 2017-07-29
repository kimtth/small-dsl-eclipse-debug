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
package org.xtext.labs.mydsl.debug.ui.launcher;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;

/**
 * Launches a PDA file
 */
public class DSLLaunchShortcut implements ILaunchShortcut {

	public void launch(ISelection selection, String mode) {
		// must be a structured selection with one file selected
		IFile file = (IFile) ((IStructuredSelection) selection).getFirstElement();

		// check for an existing launch config for the pda file
		String path = file.getFullPath().toString();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager
				.getLaunchConfigurationType(DebugCorePlugin.ID_DSL_LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
			for (int i = 0; i < configurations.length; i++) {
				ILaunchConfiguration configuration = configurations[i];
				String attribute = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, (String) null);
				if (path.equals(attribute)) {
					DebugUITools.launch(configuration, mode);
					return;
				}
			}
		} catch (CoreException e) {
			return;
		}

		try {
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, file.getName());
			workingCopy.setAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, path);
			ILaunchConfiguration configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, mode);
		} catch (CoreException e1) {
		}
	}

	public void launch(IEditorPart editor, String mode) {

		//kim: find launch configuration and new make configuration from@active editor.
		IEditorInput input = editor.getEditorInput();
		if(input instanceof FileEditorInput){
			IFile ipath = ((FileEditorInput) input).getFile();
			String path = ipath.getFullPath().toString();

			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType type = launchManager
					.getLaunchConfigurationType(DebugCorePlugin.ID_DSL_LAUNCH_CONFIGURATION_TYPE);
			try {
				ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
				for (int i = 0; i < configurations.length; i++) {
					ILaunchConfiguration configuration = configurations[i];
					String attribute = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, (String) null);
					if (path.equals(attribute)) {
						DebugUITools.launch(configuration, mode);
						return;
					}
				}
			} catch (CoreException e) {
				return;
			}

			try {
				Path p = Paths.get(path);
				String fileName = p.getFileName().toString();
				
				ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, fileName);
				workingCopy.setAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, path);
				ILaunchConfiguration configuration = workingCopy.doSave();
				DebugUITools.launch(configuration, mode);
			} catch (CoreException e1) {
			}
		}
	}

}
