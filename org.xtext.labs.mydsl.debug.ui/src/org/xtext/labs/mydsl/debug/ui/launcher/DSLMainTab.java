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


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.IInternalDebugUIConstants;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;

/**
 * Tab to specify the PDA program to run/debug.
 */
public class DSLMainTab extends AbstractLaunchConfigurationTab {
	
	private Text fProgramText;
	private Button fProgramButton;
	private Text fArgsText;
	
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.verticalSpacing = 0;
		topLayout.numColumns = 3;
		comp.setLayout(topLayout);
		comp.setFont(font);
		
		createVerticalSpacer(comp, 3);
		
		Label programLabel = new Label(comp, SWT.NONE);
		programLabel.setText("&Program:");
		GridData gd = new GridData(GridData.BEGINNING);
		programLabel.setLayoutData(gd);
		programLabel.setFont(font);
		
		fProgramText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fProgramText.setLayoutData(gd);
		fProgramText.setFont(font);
		fProgramText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		
		fProgramButton = createPushButton(comp, "&Browse...", null); //$NON-NLS-1$
		fProgramButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseDSLFiles();
			}
		});
		
		//kim: add arguments area
		Label programLabel2 = new Label(comp, SWT.NONE);
		programLabel2.setText("&Arguments:");
		GridData gd2 = new GridData(GridData.BEGINNING);		
		gd2.horizontalSpan = 3;
		programLabel2.setLayoutData(gd2);
		programLabel2.setFont(font);

		fArgsText = new Text(comp, SWT.BORDER | SWT.MULTI);
		gd2 = new GridData();
		gd2.horizontalSpan = 3;
		gd2.horizontalAlignment = SWT.FILL;
		gd2.grabExcessHorizontalSpace = true;
		gd2.verticalAlignment = SWT.FILL;
		gd2.grabExcessVerticalSpace = true;
		fArgsText.setLayoutData(gd2);
		
		fArgsText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
	}
	
	/**
	 * Open a resource chooser to select a PDA program 
	 */
	protected void browseDSLFiles() {
		ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
		dialog.setTitle("DSL Program");
		dialog.setMessage("Select DSL Program");
		
		//kim: when want to appear file list in Dialog UI. input * or ? => "Select a resource to open"
		if (dialog.open() == Window.OK) {
			Object[] files = dialog.getResult();
			IFile file = (IFile) files[0];
			fProgramText.setText(file.getFullPath().toString());
		}
		
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String program = null;
			program = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, (String)null);
			if (program != null) {
				fProgramText.setText(program);
			}
			
			String argvalue = null;
			argvalue = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_ARG_VALUE, (String)null);
			if (argvalue != null) {
				fArgsText.setText(argvalue);
			}
		} catch (CoreException e) {
			setErrorMessage(e.getMessage());
		}
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String program = fProgramText.getText().trim();
		if (program.length() == 0) {
			program = null;
		}
		configuration.setAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, program);
		
		String argvalue = fArgsText.getText().trim();
		if (argvalue.length() == 0) {
			argvalue = null;
		}
		configuration.setAttribute(DebugCorePlugin.ATTR_DSL_ARG_VALUE, argvalue);
	}
	
	public String getName() {
		return "Main";
	}

	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		String text = fProgramText.getText();
		if (text.length() > 0) {
			IPath path = new Path(text);
			if (ResourcesPlugin.getWorkspace().getRoot().findMember(path) == null) {
				setErrorMessage("Specified program does not exist");
				return false;
			}
		} else {
			setMessage("Specify a program");
		}
		return true;
	}

	//kim: remove for null exception
	//kim: fix img to eclipse common ui img.
	public Image getImage() {
		//return DebugUIPlugin.getDefault().getImageRegistry().get(DebugUIPlugin.IMG_DSL_VIEW);
		Image img = DebugUITools.getImage(IInternalDebugUIConstants.IMG_ELCL_STANDARD_OUT);
		return img;
	}
}
