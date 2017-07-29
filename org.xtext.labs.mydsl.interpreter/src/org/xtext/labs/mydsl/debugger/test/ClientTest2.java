package org.xtext.labs.mydsl.debugger.test;

//import java.io.*;
//import java.net.*;
//import java.util.Scanner;

public class ClientTest2 {
//
//	public static void main(String[] args) throws UnknownHostException, IOException {
//
//		Socket req = new Socket("127.0.0.1", 29777);
//		Socket event = new Socket("127.0.0.1", 29888);
//
//		try {
//			// Using PrinterWriter rather than OutputStreamWriter
//			// BufferedWriter output = new BufferedWriter(new
//			// PrintWriter(req.getOutputStream()));
//			PrintWriter output = new PrintWriter(req.getOutputStream(), true);
//			BufferedReader input = new BufferedReader(new InputStreamReader(event.getInputStream()));
//			// DataOutputStream output = new
//			// DataOutputStream(req.getOutputStream());
//			// DataInputStream eventInput = new
//			// DataInputStream(event.getInputStream());
//
//			new Thread(new Sender(input, output)).start();
//
//			// For Debugging
//			System.out.println(">>============================================");
//			System.out.format("Port:                 %s\n", req.getPort());
//			System.out.format("Canonical Host Name:  %s\n", req.getInetAddress().getCanonicalHostName());
//			System.out.format("Host Address:         %s\n\n", req.getInetAddress().getHostAddress());
//			System.out.format("Local Address:        %s\n", req.getLocalAddress());
//			System.out.format("Local Port:           %s\n", req.getLocalPort());
//			System.out.format("Local Socket Address: %s\n\n", req.getLocalSocketAddress());
//			System.out.format("Receive Buffer Size:  %s\n", req.getReceiveBufferSize());
//			System.out.format("Send Buffer Size:     %s\n\n", req.getSendBufferSize());
//			System.out.format("Keep-Alive:           %s\n", req.getKeepAlive());
//			System.out.format("SO Timeout:           %s\n", req.getSoTimeout());
//			System.out.println("============================================");
//			System.out.format("Port:                 %s\n", event.getPort());
//			System.out.format("Canonical Host Name:  %s\n", event.getInetAddress().getCanonicalHostName());
//			System.out.format("Host Address:         %s\n\n", event.getInetAddress().getHostAddress());
//			System.out.format("Local Address:        %s\n", event.getLocalAddress());
//			System.out.format("Local Port:           %s\n", event.getLocalPort());
//			System.out.format("Local Socket Address: %s\n\n", event.getLocalSocketAddress());
//			System.out.format("Receive Buffer Size:  %s\n", event.getReceiveBufferSize());
//			System.out.format("Send Buffer Size:     %s\n\n", event.getSendBufferSize());
//			System.out.format("Keep-Alive:           %s\n", event.getKeepAlive());
//			System.out.format("SO Timeout:           %s\n", event.getSoTimeout());
//			System.out.println("============================================<<");
//
//		} catch (Exception e) {
//			req.close();
//			event.close();
//		}
//	}
//}
//
//class Sender implements Runnable {
//	BufferedReader input;
//	PrintWriter output;
//
//	public Sender(BufferedReader in, PrintWriter out) {
//		this.input = in;
//		this.output = out;
//	}
//
//	public void run() {
//
//		// client: 1.write == request / 2.read == response
//		System.out.println("Client start >> ");
//		String data = null;
//
//		try {
//			Scanner sc = new Scanner(System.in);
//			String msg = "begin client connection";
//			boolean keepScan = true;
//
//			System.out.println(msg);
//			while (keepScan) {
//				msg = sc.next();
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
//				if ((data = input.readLine()) != null) {
//
//					String buff = "";
//					if (data.contains("#")) {
//						String[] strs = data.split("#");
//						for (String str : strs) {
//							str += System.lineSeparator();
//							buff += str;
//						}
//					} else {
//						buff = data;
//					}
//
//					System.out.println("client get response >>" + buff);
//				}
//			}
//
//			input.close();
//			output.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}