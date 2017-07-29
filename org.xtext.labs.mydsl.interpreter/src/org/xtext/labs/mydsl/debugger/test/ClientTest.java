package org.xtext.labs.mydsl.debugger.test;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.Scanner;

public class ClientTest {
//
//	public static void main(String[] args) throws UnknownHostException, IOException {
//		Socket req = new Socket("localhost", 29777);
//		Socket event = new Socket("localhost", 29888);
//
//		// Using PrinterWriter rather than OutputStreamWriter
//		//BufferedWriter output = new BufferedWriter(new PrintWriter(req.getOutputStream()));
//		PrintWriter output = new PrintWriter(req.getOutputStream(), true);
//		BufferedReader eventInput = new BufferedReader(new InputStreamReader(event.getInputStream()));
//		//DataOutputStream output = new DataOutputStream(req.getOutputStream());
//		//DataInputStream eventInput = new DataInputStream(event.getInputStream());
//		
//		// client: 1.write == request / 2.read == response
//		System.out.println("Client start >> ");
//		String data = null;
//		Scanner scan = new Scanner(System.in);
//		
//		try {
//			String msg = "begin client connection";
//			boolean keepScan = true;
//
//			System.out.println(msg);
//			while (keepScan) {
//				msg = scan.next();
//				System.out.println("client send msg " + msg);
//			
//				if (msg.trim().equalsIgnoreCase("exit")) {
//					System.out.println("exit client");
//					keepScan = false;
//				}
//				
//				output.println(msg);
//				output.flush();
//				
//				if ((data = eventInput.readLine()) != null) {
//					System.out.println("client get response >>" + data);
//				}
//			}
//
//			req.close();
//			event.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}

