package model;

import model.light_state.GreenState;
import model.light_state.LightState;
import model.light_state.RedState;

public class TrafficLight implements Runnable {

	private final Object lock = new Object();
	
	private LightState currentState;
	private boolean isRunning = false;
	
	public TrafficLight(boolean startsGreen) {
		this.currentState = startsGreen ? new GreenState() : new RedState();
	}
	
	public void setRunning(boolean run) {
        this.isRunning = run;
        if (run) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
	
	public synchronized boolean isGreen() {
        return currentState.canPass();
    }
	
	public synchronized LightState getCurrentState() {
        return currentState;
    }
	
	@Override
    public void run() {
        while (true) {
            try {
                if (!isRunning) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                Thread.sleep(currentState.getDuration());
                synchronized (this) {
                    currentState = currentState.nextState();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
