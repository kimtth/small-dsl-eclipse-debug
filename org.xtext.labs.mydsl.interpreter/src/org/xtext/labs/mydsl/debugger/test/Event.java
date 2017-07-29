package org.xtext.labs.mydsl.debugger.test;

import java.util.Observable;
import java.util.Scanner;

/**
 * Sometime Observable is called Subject class in Example.
 * https://stackoverflow.com/questions/6883502/how-can-i-implement-the-observer-design-pattern-streaming-data-in-c
 * 
 * In short: no, you can't implement the observer pattern on a client/server application. 
 * There is no (easy) way for a server to invoke a notify method on your client app 
 * and if your client gets disconnected, then it won't unregister from the observable.
 * 
 * Observer Pattern
 * these files in *.event.local package use when need to execute without socket connection.
 * only for test purpose. 
 * 
 */
public class Event extends Observable implements Runnable {
	public void run() {
		while (true) {
			String response = new Scanner(System.in).next();
			setChanged();
			notifyObservers(response);
		}
	}
}

