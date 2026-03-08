package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import model.light_state.GreenState;
import model.light_state.LightState;
import model.light_state.RedState;

public class TrafficLight implements Runnable {

    private LightState currentState;
    private volatile boolean isRunning = false;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition runningCondition = lock.newCondition();

    public TrafficLight(boolean startsGreen) {
        this.currentState = startsGreen ? new GreenState() : new RedState();
    }

    public void setRunning(boolean run) {
        lock.lock();
        try {
            isRunning = run;
            if (isRunning) {
                runningCondition.signal(); // wake up the thread
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isGreen() {
        lock.lock();
        try {
            return currentState.canPass();
        } finally {
            lock.unlock();
        }
    }

    public LightState getCurrentState() {
        lock.lock();
        try {
            return currentState;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                while (!isRunning) {
                    runningCondition.await(); // wait until simulation starts
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // exit thread safely
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(currentState.getDuration()); // stay in current state
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            // change to next state
            lock.lock();
            
            try {
                currentState = currentState.nextState();
            } finally {
                lock.unlock();
            }
        }
    }
}
