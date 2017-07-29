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
package org.xtext.labs.mydsl.debug.ui;

import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.internal.ui.sourcelookup.SourceLookupFacility;
import org.eclipse.debug.internal.ui.sourcelookup.SourceLookupResult;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;

import java.net.URL;
import java.util.*;

public class DebugUIPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static DebugUIPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	//kim: add image resource is not work properly.
	//if null value, it will initialize imageregistry again. 
	private ImageRegistry imageRegistry;
	
	private final static String ICONS_PATH = "icons/";//$NON-NLS-1$
    
    /**
     * PDA program image
     */
    public final static String IMG_DSL_VIEW = "IMG_DSL_VIEW";
    	
	/**
	 * The constructor.
	 */
	public DebugUIPlugin() {
		super();
		plugin = this;
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static DebugUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = DebugUIPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("org.xtext.labs.mydsl.debug.ui.DebugUIPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
	
	protected void initializeImageRegistry(ImageRegistry reg) {
		declareImage(IMG_DSL_VIEW, ICONS_PATH + "dsl_view.gif");
	}

	
	/**
	 * kim: retry imageRegistry initializing
	 */
	@Override
	public ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
            initializeImageRegistry(imageRegistry);
        }
        return imageRegistry;
	}
	
    /**
     * Declares a workbench image given the path of the image file (relative to
     * the workbench plug-in). This is a helper method that creates the image
     * descriptor and passes it to the main <code>declareImage</code> method.
     * 
     * @param symbolicName the symbolic name of the image
     * @param path the path of the image file relative to the base of the workbench
     * plug-ins install directory
     * <code>false</code> if this is not a shared image
     */
    private void declareImage(String key, String path) {
        URL url = BundleUtility.find("org.xtext.labs.mydsl.debug.ui", path);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        getImageRegistry().put(key, desc);
    }
    
 }
