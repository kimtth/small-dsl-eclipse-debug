package org.xtext.labs.mydsl.debugger.processing;

public abstract class ThreadLauncher implements Runnable {
	
	Thread t;
	protected boolean suspended;

	public void start() {
		if (t == null) {
			t = new Thread(this, "thread-01");
			t.start();
		}
	}

	public synchronized void resume() {
	}

	public synchronized void suspend() {
	}

}
