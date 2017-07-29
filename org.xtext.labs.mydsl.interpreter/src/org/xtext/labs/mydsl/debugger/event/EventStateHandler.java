package org.xtext.labs.mydsl.debugger.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EventStateHandler {

	static Socket event;
	
	public EventStateHandler(Socket event) {
		EventStateHandler.event = event;
	}

	public static void update(String command) {
		if(command.startsWith("state")) {
			int lastSpace = command.lastIndexOf('#');
			if (lastSpace > 0) {
				String responseData = command.substring(lastSpace + 1);
				sendData(EventStateHandler.event, responseData);
			}
		}
	}

	private static void sendData(Socket event, String responseData) {
		try {
			PrintWriter out = new PrintWriter(event.getOutputStream(), true);
			out.println(responseData); //don't use write(), PrintWriter should use print().
			out.flush();
			
			// System.out.println("server send state response >> " + responseData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}