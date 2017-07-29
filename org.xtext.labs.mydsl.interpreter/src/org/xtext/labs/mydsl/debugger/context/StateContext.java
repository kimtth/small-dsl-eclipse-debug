package org.xtext.labs.mydsl.debugger.context;

import java.util.LinkedHashSet;

public class StateContext {

	public static String state = ""; //INIT SUSPEND GO RESUME_STEP SUSPEND_STEP SETTING
	public static String modelState = "NOT_END"; //NOT_END END 
	
	//Set is not duplicate value, and LinkedHash is ordered list 
	public static LinkedHashSet<Integer> breaklines = new LinkedHashSet<Integer>();
	public static int srcline = 0;
	public static int breakpointSuspendedline = 0;
	
	public static String filePath = "";
}
