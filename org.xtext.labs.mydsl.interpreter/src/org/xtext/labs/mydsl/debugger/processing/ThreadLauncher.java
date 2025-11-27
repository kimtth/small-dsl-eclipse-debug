package org.xtext.labs.mydsl.debugger.processing;

/**
 * Base class for interpreter thread management.
 * Provides thread lifecycle control (start, suspend, resume).
 */
public abstract class ThreadLauncher implements Runnable {
	
	protected Thread t;
	protected boolean suspended;

	public void start() {
		if (t == null) {
			t = new Thread(this, "interpreter-thread");
			t.start();
		}
	}

	public synchronized void resume() {
	}

	public synchronized void suspend() {
	}

}
