package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class JunctionManager {
	private static JunctionManager instance;
	private int passedVehicles = 0;
    private boolean occupied = false;
    
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition free = lock.newCondition(); // for waiting vehicles
    
    private JunctionManager() {}
    
    public static JunctionManager getInstance() {
    	if(instance == null) {
    		instance = new JunctionManager();
    	}
    	
    	return instance;
    }
    
    // Vehicle tries to enter the junction
    public void enterJunction() throws InterruptedException {
        lock.lock();
        try {
            while (occupied) {
                free.await(); // wait until junction is free
            }
            occupied = true;
        } finally {
            lock.unlock();
        }
    }

    // Vehicle leaves the junction
    public void exitJunction() {
        lock.lock();
        try {
            occupied = false;
            passedVehicles++;
            free.signalAll(); // notify waiting vehicles
        } finally {
            lock.unlock();
        }
    }
    
    public int getPassedVehicles() {
        lock.lock();
        try {
            return passedVehicles;
        } finally {
            lock.unlock();
        }
    }
}
