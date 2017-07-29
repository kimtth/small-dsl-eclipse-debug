//package org.xtext.labs.ui.extend;
//
//import org.eclipse.ui.console.ConsolePlugin;
//import org.eclipse.ui.console.IConsole;
//import org.eclipse.ui.console.IConsoleManager;
//import org.eclipse.ui.console.MessageConsole;
//import org.eclipse.ui.console.MessageConsoleStream;
//
//public class MydslPrintConsole {
//
//	public static void PrintConsole(String msg) {
//		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
//
//		MessageConsole console = findConsole("DSL Console");
//		console.clearConsole(); //initialize
//		
//		consoleManager.showConsoleView(console);
//		MessageConsoleStream consoleStream = console.newMessageStream();
//		consoleStream.setEncoding("UTF-8");
//		consoleStream.println(msg);
//	}
//
//	private static MessageConsole findConsole(String name) {
//		ConsolePlugin plugin = ConsolePlugin.getDefault();
//		IConsoleManager conMan = plugin.getConsoleManager();
//
//		IConsole[] existing = conMan.getConsoles();
//		for (int i = 0; i < existing.length; i++)
//			if (name.equals(existing[i].getName()))
//				return (MessageConsole) existing[i];
//
//		MessageConsole myConsole = new MessageConsole(name, null);
//		conMan.addConsoles(new IConsole[] { myConsole });
//		return myConsole;
//	}
//
//	public static void PrintConsoleLog(String msg) {
//		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
//
//		MessageConsole console = findConsole("DSL Log");
//		
//		MessageConsoleStream consoleStream = console.newMessageStream();
//		consoleStream.setEncoding("UTF-8");
//		consoleStream.println(msg);
//		
//		IConsole[] existing = consoleManager.getConsoles();
//		for (int i = 0; i < existing.length; i++){
//			if ("DSL Console".equals(existing[i].getName())){
//				consoleManager.showConsoleView((MessageConsole) existing[i]);
//				break;
//			}
//		}
//	}
//}
