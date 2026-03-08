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
    
    // Vehicle enters junction – only one at a time
    public void enterJunction() throws InterruptedException {
        lock.lock();
        try {
            while (occupied) {
                free.await();
            }
            occupied = true;
        } finally {
            lock.unlock();
        }
    }

    // Vehicle exits junction
    public void exitJunction() {
        lock.lock();
        try {
            occupied = false;
            passedVehicles++;
            free.signal(); // allow one waiting vehicle
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
