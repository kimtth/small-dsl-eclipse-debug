package org.xtext.labs.mydsl.debugger.test;

import java.util.Observable;
import java.util.Observer;

public class EventObserver implements Observer {
	
	@Override
	public void update(Observable target, Object param) {
//		String command = (String) param;
//		
//		StateContext.state = "INIT";
//		if(StateContext.debugEnd){
//			command = "EXIT";
//		}
//
//		//Command Process
//		if (command.toLowerCase().startsWith("brk")) {
//			StateContext.breakline = Integer.parseInt(command.split("/")[1]);
//		}else if(command.equalsIgnoreCase("go")){
//			StateContext.state = "GO";
//		}else if(command.equalsIgnoreCase("step")){
//			StateContext.breakline = StateContext.breakline + 1;
//			StateContext.state = "RUN";
//		}else if(command.equalsIgnoreCase("ln")){ //ln == line
//			System.out.println("src: "+StateContext.srcline + "/brk:"+StateContext.breakline);
//		}else if(command.equalsIgnoreCase("stk")){ //stk == stack
//			for(Symbol itm : Processing.getList()){
//				System.out.println(itm.varType + ": " + itm.varName + ": " + itm.varVal + ": " + itm.scope);
//			}
//		}else if(command.equalsIgnoreCase("exit")){
//			System.exit(0);
//		}else{
//			System.out.println("Incompatible command: " + command);
//		}
//		
//		//Command Console
//		System.out.print("Command>>");
//		
//		if (StateContext.state.equals("RUN") && StateContext.srcline != StateContext.breakline) {
//			ModelParser.getTarget().resume();
//		}else if(StateContext.state.equals("GO")){
//			ModelParser.getTarget().resume();
//		}else{
//			ModelParser.getTarget().suspend();
//		}
	}

}
