package org.xtext.labs.mydsl.debugger;

import com.google.inject.Injector;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xbase.lib.Exceptions;

import org.xtext.labs.MydslStandaloneSetup;
import org.xtext.labs.mydsl.DSLProgram;
import org.xtext.labs.mydsl.debugger.context.StateContext;
import org.xtext.labs.mydsl.debugger.event.EventBroker;
import org.xtext.labs.mydsl.debugger.launch.Debuggable;
import org.xtext.labs.mydsl.debugger.launch.DirectRunner;
import org.xtext.labs.mydsl.debugger.processing.ThreadLauncher;

public class ModelGenerator {

	public static String[] args;
	private static ThreadLauncher launch;

	static String inputFilePath;
	static String argsValue;
	static String mode;

	/*
	 * $> java -jar target_path/my-utility.jar -i filepath -a args -m mode
	 */
	public static void main(final String[] args) {

		ServerSocket requestlistener = null;
		ServerSocket eventSender = null;

		try {
			// #command parsing
			commandParser(args);

			// For Debugging
			// inputFilePath =
			// "D:\\Workspace_labs\\org.xtext.labs.mydsl.product\\src\\return.dsl";
			// inputFilePath =
			// "D:\\Workspace_labs\\org.xtext.labs.mydsl.product\\src\\model.dsl";
			// inputFilePath =
			// "D:\\Workspace_labs\\org.xtext.labs.mydsl.product\\src\\return2.dsl";
			// argsValue = "10,20";
			// mode = "run";

			// 1.event controller
			// create request and event socket separately for asynchronous
			// interaction with interpreter.
			// and session connecting before parsing source.
			requestlistener = new ServerSocket(29777);
			eventSender = new ServerSocket(29888);
			EventBroker broker = new EventBroker(requestlistener, eventSender);
			broker.start();

			// #parsing dsl
			ModelGenerator.init(argsValue);
			final DSLProgram model = ModelGenerator.readFile(inputFilePath);
			if (model != null) {
				StateContext.modelState = "END";
				StateContext.filePath = inputFilePath;
			}

			System.out.println("Start Debugging >>");

			// 2.run model interpreter
			if (mode.equals("debug")) {
				setTarget(new Debuggable(model, null));
				getTarget().start();
			} else if (mode.equals("run")) {
				setTarget(new DirectRunner(model));
				getTarget().start();
			} else {
				System.out.println("the mode is not compatitable.");
			}

		} catch (BindException e) {
			try {
				Process proc = Runtime.getRuntime().exec("netstat -ona | findstr 0.0:29777");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				// Runtime.getRuntime().exec("taskkill /pid 21424 /F");
			} catch (IOException e1) {
				throw Exceptions.sneakyThrow(e1);
			}
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	private static void init(String argsValue) {
		String[] args = argsValue.split(",");

		ModelGenerator.args = new String[args.length];
		int i = 0;
		for (final String arg : args) {
			ModelGenerator.args[i] = arg;
			++i;
		}
	}

	private static void commandParser(String[] args) {
		// Arguments parsing
		Options options = CLIoptionSetting();

		CommandLineParser parser = new GnuParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
			return;
		}

		inputFilePath = cmd.getOptionValue("input");
		argsValue = cmd.getOptionValue("args");
		mode = cmd.getOptionValue("mode");

		/*
		 * //TODO port scanning String port = cmd.getOptionValue("port");
		 * String[] ports = port.split(" "); if (port != null && ports.length ==
		 * 2) { String requestPort = ports[0]; String eventPort = ports[0];
		 * }else{ System.out.println("invalid port value"); }
		 */

		System.out.println("input:" + inputFilePath);
		System.out.println("args:" + argsValue);
		System.out.println("mode:" + mode);
	}

	private static Options CLIoptionSetting() {
		Options options = new Options();

		Option input = new Option("i", "input", true, "file path");
		input.setRequired(true);
		options.addOption(input);

		Option output = new Option("a", "args", true, "sequence of arg index/arg value ex) 66/1,67/2");
		output.setRequired(true);
		options.addOption(output);

		Option mode = new Option("m", "mode", true, "set mode run or debug ex) -m run");
		mode.setRequired(true);
		options.addOption(mode);

		/*
		 * //TODO port scanning Option port = new Option("p", "port", true,
		 * "request port & event port ex) -p 9090 9091");
		 * port.setRequired(false); options.addOption(port);
		 */

		return options;
	}

	public static DSLProgram readFile(String inputFilePath) {
		try {
			String filePath = "";
			filePath = inputFilePath;
			String textStr = "";
			FileReader _fileReader = new FileReader(filePath);
			final BufferedReader br = new BufferedReader(_fileReader);
			try {
				final StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while ((line != null)) {
					{
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
				}
				textStr = sb.toString();
			} finally {
				br.close();
			}
			final DSLProgram model = ModelGenerator.resourceBuilder(textStr);
			return model;
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	/**
	 * http://stackoverflow.com/questions/11867986/xtext-programmatically-parse-a-dsl-script-into-an-ecore-model
	 */
	public static DSLProgram resourceBuilder(final String textStr) {
		try {
			// new StandaloneSetup().setPlatformUri("../"); kim: need to delete
			// for weird log. StandardSetup Plug-in conflict
			final Injector injector = new MydslStandaloneSetup().createInjectorAndDoEMFRegistration();
			final XtextResourceSet resourceSet = injector.<XtextResourceSet>getInstance(XtextResourceSet.class);
			resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
			final Resource resource = resourceSet.createResource(URI.createURI("dummy:/example.dsl"));
			byte[] _bytes = textStr.getBytes();
			final ByteArrayInputStream in = new ByteArrayInputStream(_bytes);
			resource.load(in, resourceSet.getLoadOptions());
			EObject _get = resource.getContents().get(0);
			final DSLProgram model = ((DSLProgram) _get);
			return model;
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	public static ThreadLauncher getTarget() {
		return ModelGenerator.launch;
	}

	public static void setTarget(Debuggable target) {
		ModelGenerator.launch = target;
	}

	private static void setTarget(DirectRunner target) {
		ModelGenerator.launch = target;
	}

}
