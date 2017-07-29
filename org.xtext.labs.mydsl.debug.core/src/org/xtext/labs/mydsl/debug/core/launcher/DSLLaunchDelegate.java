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
package org.xtext.labs.mydsl.debug.core.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.xtext.labs.mydsl.debug.core.DebugCorePlugin;
import org.xtext.labs.mydsl.debug.core.model.DSLDebugTarget;

public class DSLLaunchDelegate extends LaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		// java -jar target_path/my-utility.jar -i filepath -a args -m mode
		// kim: executable path is defined in the plugin.xml
		// IValueVariable debugjar =
		// VariablesPlugin.getDefault().getStringVariableManager()
		// .getValueVariable("jarExecutable2");
		// String path = debugjar.getValue();

		// kim: finding debugDSL.jar by relative path.
		File exe = null;
		String path = "";
		URL launchPath = Platform.getInstallLocation().getURL();
		String appendPath = "debug/debugDSL.jar";
		try {
			URL debugJarPath = new URL(launchPath.toExternalForm() + appendPath);
			exe = new File(debugJarPath.toURI());
			path = exe.getAbsolutePath();
		} catch (Exception e1) {
			abort(MessageFormat.format("Specified debugger executable {0} does not exist.", new String[] { path }),
					null);
		}

		// exe = new File(path);
		if (!exe.exists()) {
			abort(MessageFormat.format(
					"Specified debugger executable {0} does not exist. Check value of ${debugjarExecutable}.",
					new String[] { path }), null);
		}

		// program name => dsl path
		// kim: ATTR_DSL_PROGRAM was set in MainTab UI performApply method.
		String program = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_PROGRAM, (String) null);
		if (program == null) {
			abort("DSL program unspecified.", null);
		}

		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(program));
		if (!file.exists()) {
			abort(MessageFormat.format("DSL program {0} does not exist.",
					new String[] { file.getFullPath().toString() }), null);
		}

		String dslpath = file.getLocation().toOSString();

		// arg
		String argValue = configuration.getAttribute(DebugCorePlugin.ATTR_DSL_ARG_VALUE, "7,8,9,10");

		// if in debug mode, add debug arguments - i.e. '-debug requestPort
		// eventPort'
		String debugMode = "run";
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			debugMode = "debug";
		}

		ProcessBuilder pb = new ProcessBuilder("java", "-jar", path, "-i", dslpath, "-a", argValue, "-m", debugMode);
		// kim: for remote debugging
		// ProcessBuilder pb = new ProcessBuilder("java", "-jar", "-Xdebug
		// -Xnoagent -Djava.compiler=NONE
		// -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005", path,
		// "-i", dslpath, "-a", argValue, "-m", debugMode);

		Process process = null;
		try {
			process = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		IProcess p = DebugPlugin.newProcess(launch, process, path);
		// if in debug mode, create a debug target
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			// DSLDebugTarget(launch, p, requestPort, eventPort);
			IDebugTarget target = new DSLDebugTarget(launch, p, 29777, 29888);
			launch.addDebugTarget(target);
		}
	}

	// private String getParentDirPath(String strPath) {
	// if (strPath.lastIndexOf("/") <= 0) {
	// return strPath.substring(0, strPath.lastIndexOf("/") + 1);
	// } else {
	// return strPath.substring(0, strPath.lastIndexOf("/"));
	// }
	// }

	/**
	 * Throws an exception with a new status containing the given message and
	 * optional exception.
	 * 
	 * @param message
	 *            error message
	 * @param e
	 *            underlying exception
	 * @throws CoreException
	 */
	private void abort(String message, Throwable e) throws CoreException {
		// kim: replaced by
		// DebugCorePlugin.getDefault().getBundle().getSymbolicName()
		// this get a plug-in id
		throw new CoreException(
				new Status(IStatus.ERROR, DebugCorePlugin.getDefault().getBundle().getSymbolicName(), 0, message, e));
	}

	// /**
	// * Returns a free port number on localhost, or -1 if unable to find a free
	// * port.
	// *
	// * @return a free port number on localhost, or -1 if unable to find a free
	// * port
	// */
	// public static int findFreePort() {
	// ServerSocket socket = null;
	// try {
	// socket = new ServerSocket(0);
	// return socket.getLocalPort();
	// } catch (IOException e) {
	// } finally {
	// if (socket != null) {
	// try {
	// socket.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	// return -1;
	// }
}
