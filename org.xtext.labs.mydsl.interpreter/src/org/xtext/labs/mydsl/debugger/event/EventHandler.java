package org.xtext.labs.mydsl.debugger.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import org.xtext.labs.mydsl.debugger.ModelGenerator;
import org.xtext.labs.mydsl.debugger.context.CallStack;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;
import org.xtext.labs.mydsl.debugger.context.StateContext;
import org.xtext.labs.mydsl.debugger.context.Symbol;
import org.xtext.labs.mydsl.debugger.context.SymbolTable;

public class EventHandler {

	public void handle(String command, Socket response) {

		Socket eventsender = response;
		// for debugging : request check
		// System.out.println("command: " + command);

		// command processing
		if (command.equalsIgnoreCase("resume")) {
			StateContext.state = "RESUME";
		} else if (command.equalsIgnoreCase("step")) {
			StateContext.state = "RESUME_STEP";
		} else if (command.equalsIgnoreCase("suspend")) {
			ModelGenerator.getTarget().suspend();
		} else {
			// command for "brk, unbrk, data, stack, exit, p(debugging,
			// abbreviate print)"
			sendData(eventsender, responsehandle(command));
		}

		if (command.equalsIgnoreCase("exit")) {
			System.exit(0);
		}

		if (StateContext.state.equals("RESUME_STEP")) {
			ModelGenerator.getTarget().resume();
		} else if (StateContext.state.equals("RESUME")) {
			checkModelandTryResume();
		}
	}

	// this condition will be skip "start event & set brk event".
	@SuppressWarnings("unused")
	private boolean isInitialized(String command) {
		boolean initialized = false;

		if (StateContext.state.equals("INIT")) {
			if (!command.equals("brk")) {
				initialized = true;
			}
		}

		return initialized;
	}

	// prevent start interpreting before make a model. wait and then try again until end of
	// parsing.
	private void checkModelandTryResume() {
		boolean wait = true;
		while (wait) {
			if (StateContext.modelState.equals("END")) {
				ModelGenerator.getTarget().resume();
				wait = false;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private String responsehandle(String command) {

		String responseData = "";
		StateContext.state = "SETTING";

		// Command Process
		if (command.equalsIgnoreCase("start")) {
			responseData = "started";
		} else if (command.toLowerCase().startsWith("brk")) {
			int lastSpace = command.lastIndexOf('#');
			if (lastSpace > 0) {
				String line = command.substring(lastSpace + 1);
				int breakline = Integer.parseInt(line);
				StateContext.breaklines.add(breakline);

				responseData = "set brk " + breakline;
			}
		} else if (command.toLowerCase().startsWith("unbrk")) {
			int lastSpace = command.lastIndexOf('#');
			if (lastSpace > 0) {
				String line = command.substring(lastSpace + 1);
				int breakline = Integer.parseInt(line);
				StateContext.breaklines.remove(breakline);

				responseData = "unset brk " + breakline;
			}
			// p(print): check present state, this command is not used in
			// eclipse ui only for debugging in console.
		} else if (command.equalsIgnoreCase("p")) {
			responseData = printDebugging(responseData);
		} else if (command.equalsIgnoreCase("data")) {
			// when use in single thread program. not using now.
			responseData = stackMsgString(CallStack.getStack()); 
		} else if (command.equalsIgnoreCase("stack")) {
			// using in multi-thread program.
			responseData = printCallStack(responseData);
		} else if (command.equalsIgnoreCase("exit")) {
			responseData = "terminated";
		} else {
			responseData = "unimplemented instruction >>" + command + "<<";
		}

		return responseData;
	}

	//token splitter: first token # > next token & > next token |
	//frame#frame#frame#frame
	private String printCallStack(String responseData) {
		StringBuffer lines = new StringBuffer();
		
		//print frame info
		for(CallStackItem item : CallStack.getStack()){
			String funcId = item.getFuncId();
			SymbolTable tbl = item.getSt();
			
			lines.append(StateContext.filePath);
			lines.append("&");
			lines.append(StateContext.srcline);
			lines.append("&");
			
			//funcId|varType|varName|varVal|scope&
			//funcId|varType|varName|varVal|scope&
			lines.append(funcId);
			lines.append("&");
			lines = printSymbolTbl(tbl, lines);
			lines.append("#");
			
		}
		
		return lines.toString();
	}

	private StringBuffer printSymbolTbl(SymbolTable tbl, StringBuffer lines) {
		
		for(Symbol sym : tbl.getSymbolTable()){
			String line = sym.varType + "|" + sym.varName + "|" + sym.varVal;
			line = line + "&";
			lines.append(line);
		}
		
		return lines;
	}
	
//	private StringBuffer printSymbolTbl(SymbolTable tbl, StringBuffer lines) {
//
//		for (Symbol sym : tbl.getSymbolTable()) {
//			String line = sym.varType + "|" + sym.varName + "|" ;
//
//			//valueなかに区切り文字があるかどうがを確して、あるなら置き換えする。@ is special char for ignore delimeter.
//			String[] matches = new String[] { "&", "#", "\\|" };
//			Object valObj = sym.varVal;
//			String input = "";
//			if (valObj instanceof String) {
//				input = sym.varVal.toString();
//			}
//			for (String s : matches) {
//				if (input.contains(s)) {
//					if(input.contains("@") == false){
//						sym.setVarVal(input.replaceAll(s, "@" + s));
//					}
//					break;
//				}
//			}
//			line += sym.varVal;
//
//			line = line + "&";
//			lines.append(line);
//		}
//
//		return lines;
//	}

	private String printDebugging(String responseData) {
		responseData = "src: " + StateContext.srcline + "/brks:";
		for (int brk : StateContext.breaklines) {
			responseData += String.valueOf(brk) + ".";
		}
		responseData += "step: " + StateContext.breakpointSuspendedline;
		responseData += "|state: " + StateContext.state;

		return responseData;
	}

	private String stackMsgString(LinkedList<CallStackItem> callstack) {
		StringBuffer lines = new StringBuffer();

		// this line need for sending filepath for source code lookup and
		// DSLStackFrame
		// init constructor.
		lines.append(StateContext.filePath);
		lines.append("#");
		// send suspended line number
		lines.append(StateContext.srcline);
		lines.append("#");

		for (Symbol itm : callstack.get(0).getSt().getSymbolTable()) {
			String line = itm.varType + ": " + itm.varName + ": " + itm.varVal + ": " + itm.scope;
			line += "#";

			lines.append(line);
		}

		return lines.toString();
	}

	protected void sendData(Socket eventsender, String responseData) {
		try {
			PrintWriter out = new PrintWriter(eventsender.getOutputStream(), true);

			// **Important** don't use write(), PrintWriter should use print().
			// if read string by in.readline in client, need to match print also
			// with out.println.
			// https://stackoverflow.com/questions/13057740/printwriter-does-not-send-my-string-tcp-ip
			// System.out.println("server response" + responseData);

			out.println(responseData);
			out.flush();

			// System.out.println("server send event response >> " +
			// responseData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendOkError(Socket request, String okOrError) {
		try {
			PrintWriter out = new PrintWriter(request.getOutputStream(), true);
			out.println(okOrError);
			out.flush();

			// System.out.println("server send request response >> " +
			// okOrError);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
