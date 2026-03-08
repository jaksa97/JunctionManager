package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import model.light_state.GreenState;
import model.light_state.LightState;
import model.light_state.RedState;

public class TrafficLight implements Runnable {

    private LightState currentState;
    private volatile boolean isRunning = false;

    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition runningCondition = pauseLock.newCondition();

    public TrafficLight(boolean startsGreen) {
        this.currentState = startsGreen ? new GreenState() : new RedState();
    }

    public void setRunning(boolean isRunning) {
    	pauseLock.lock();
        try {
            this.isRunning = isRunning;
            if (this.isRunning) {
                runningCondition.signal();	// Wake up the thread
            }
        } finally {
        	pauseLock.unlock();
        }
    }

    public boolean isGreen() {
    	pauseLock.lock();
        try {
            return currentState.canPass();
        } finally {
        	pauseLock.unlock();
        }
    }

    public LightState getCurrentState() {
    	pauseLock.lock();
        try {
            return currentState;
        } finally {
        	pauseLock.unlock();
        }
    }

    @Override
    public void run() {
        while (true) {
            
        	// Pause if simulation stopped
            pauseLock.lock();
            try {
                while (!isRunning) {
                    runningCondition.await();	// Wait until simulation resumes
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                pauseLock.unlock();
            }

            try {
                // Divide state duration into small steps
                long duration = currentState.getDuration();
                long elapsed = 0;
                long step = 50;	// 50 ms steps

                while (elapsed < duration) {
                    Thread.sleep(step);
                    elapsed += step;

                    // Check pause during step
                    pauseLock.lock();
                    try {
                        while (!isRunning) {
                            runningCondition.await();	// Pause instantly
                        }
                    } finally {
                        pauseLock.unlock();
                    }
                }

                // After duration, switch state
                pauseLock.lock();
                try {
                    currentState = currentState.nextState();
                } finally {
                    pauseLock.unlock();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
}