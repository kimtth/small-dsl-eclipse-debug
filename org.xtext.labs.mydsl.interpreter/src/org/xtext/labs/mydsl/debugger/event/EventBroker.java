package org.xtext.labs.mydsl.debugger.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class EventBroker extends Thread {

	ServerSocket server;
	ServerSocket eventSender;
	Socket request;
	Socket response;
	boolean serverOn;
	boolean isFirst = true;

	public EventBroker(ServerSocket server, ServerSocket eventSender) {
		this.server = server;
		this.eventSender = eventSender;
		this.serverOn = true;
	}

	public enum State {
		INIT, SUSPEND, RESUME, STEP
	};

	@Override
	public void run() {

		String data = null;
		Socket request = null;
		Socket response = null;

		try {
			request = server.accept();
			response = eventSender.accept();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		EventHandler event = new EventHandler();
		new EventStateHandler(response); // initialize EventStateHandler
		event.handle("start", response);

		while (serverOn) {
			try {
				Thread.sleep(1000);
				BufferedReader req = new BufferedReader(new InputStreamReader(request.getInputStream()));

				while ((data = req.readLine()) != null) {
//					System.out.println("server get request >> " + data);

					// when ui request data, must response by request socket.
					if (data.equalsIgnoreCase("stack")) {
						event.handle(data, request);
					} else {
						event.sendOkError(request, "ok");
						event.handle(data, response);
					}

					if (data.equals("exit")) {
						request.close();
						response.close();

						// Server also will be closed.
						server.close();
						System.out.println("exit debugging");
						System.exit(0);
					}
				}

			} catch (Exception e) {
				event.sendOkError(request, "error");
				try {
					request.close();
					response.close();
				} catch (IOException e1) {
				}
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
