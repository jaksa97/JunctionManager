package model.vehicle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import model.JunctionManager;
import model.TrafficLight;

public abstract class Vehicle implements Runnable {
	
	protected int x, y;
    protected int speed;
    protected Color color;
    protected int entrance;
    protected VehicleType vehicleType;
    protected int vehicleSize;
    private TrafficLight myLight;
    private List<Vehicle> allVehicles;
    
    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition runningCondition = pauseLock.newCondition();
    private volatile boolean isRunning = true;
    
    public Vehicle(int x, int y, int entrance, TrafficLight light, List<Vehicle> allVehicles) {
        this.x = x;
        this.y = y;
        this.entrance = entrance;
        this.myLight = light;
        this.allVehicles = allVehicles;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEntrance() {
        return entrance;
    }
    
    private int getSafeDistance() {
    	return vehicleSize + 10;
    }
    
    public void setRunning(boolean isRunning) {
    	pauseLock.lock();
        try {
            this.isRunning = isRunning;
            if (this.isRunning) {
                runningCondition.signalAll();
            }
        } finally {
            pauseLock.unlock();
        }
    }

	@Override
	public void run() {
		while (true) {
			try {
				
				// Pause if simulation stopped
                pauseLock.lock();
                try {
                    while (!isRunning) {
                        runningCondition.await();
                    }
                } finally {
                    pauseLock.unlock();
                }
				
				if (isOutOfScreen()) {
	                removeVehicle();
	                break;	// Stop the thread
	            }
				
				if (isAtStopLine()) {
	                // Wait for green
	                while (!myLight.isGreen()) {
	                	Thread.sleep(30);
	                	
	                	// Check pause while waiting
                        pauseLock.lock();
                        try {
                            while (!isRunning) {
                                runningCondition.await();
                            }
                        } finally {
                            pauseLock.unlock();
                        }
	                }

	                //	Vehicle enters junction – only one at a time
	                JunctionManager.getInstance().enterJunction();
	                
	                try {
	                    moveThroughIntersection();
	                } finally {
	                	JunctionManager.getInstance().exitJunction();
	                }
	                
	                continue;
	            }
				
				if (!checkCollisionAhead()) {
                    moveOneStep();
                }
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	protected boolean isVertical(int entrance) {
		return entrance < 2;
	}
	
	private boolean isAtStopLine() {
	    switch (entrance) {
	        case 0 -> {
	            return y >= 230 - vehicleSize - 10 && y <= 230 - vehicleSize;
	        }
	        case 1 -> {
	            return y >= 310 && y <= 320;
	        }
	        case 2 -> {
	            return x >= 360 - vehicleSize - 10 && x <= 360 - vehicleSize;
	        }
	        case 3 -> {
	            return x >= 440 && x <= 450;
	        }
	        default -> {
	            return false;
	        }
	    }
	}
	
	private void moveThroughIntersection() throws InterruptedException {
		int midX = 400;
	    int midY = 270;
	    int roadHalfWidth = 40;

	    while (true) {
	        moveOneStep();

	        Thread.sleep(30);
	        
	        // Check pause during intersection
            pauseLock.lock();
            try {
                while (!isRunning) {
                    runningCondition.await();
                }
            } finally {
                pauseLock.unlock();
            }

	        // Check if vehicle has left the junction
	        if (entrance == 0 && y >= midY + roadHalfWidth + 5) break;
	        if (entrance == 1 && y <= midY - roadHalfWidth - 5 - vehicleSize) break;
	        if (entrance == 2 && x >= midX + roadHalfWidth + 5) break;
	        if (entrance == 3 && x <= midX - roadHalfWidth - 5 - vehicleSize) break;
	    }
	}
	
	private void moveOneStep() {
        switch (entrance) {
            case 0 -> y += speed;
            case 1 -> y -= speed;
            case 2 -> x += speed;
            case 3 -> x -= speed;
        }
    }
	
	// Stop vehicle if another vehicle is too close ahead
	private boolean checkCollisionAhead() {

	    synchronized (allVehicles) {

	        for (Vehicle other : allVehicles) {

	            if (other == this)
	                continue;

	            if (other.getEntrance() != entrance)
	                continue;

	            int safeDistance = getSafeDistance();

	            switch (entrance) {
	                case 0 -> {
	                    if (other.getY() > y && other.getY() - y < safeDistance) {
	                        return true;
	                    }
	                }
	                case 1 -> {
	                    if (other.getY() < y && y - other.getY() < safeDistance) {
	                        return true;
	                    }
	                }
	                case 2 -> {
	                    if (other.getX() > x && other.getX() - x < safeDistance) {
	                        return true;
	                    }
	                }
	                case 3 -> {
	                    if (other.getX() < x && x - other.getX() < safeDistance) {
	                        return true;
	                    }
	                }
	            }
	        }
	    }

	    return false;
	}
    
    public boolean isOutOfScreen() {
        return x < (0 - vehicleSize - 10) || x > (800 + vehicleSize + 10) || y < (0 - vehicleSize - 10) || y > (540 + vehicleSize + 10);
    }
    
    private void removeVehicle() {

        isRunning = false;

        synchronized (allVehicles) {
            allVehicles.remove(this);
        }
    }
    
    
	public void draw(Graphics graphics) {
		graphics.setColor(color);
		if(isVertical(entrance)) {
			graphics.fillRect(x, y, 20, vehicleSize);
		} else {
			graphics.fillRect(x, y, vehicleSize, 20);
		}
    }

}