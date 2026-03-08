package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

public class Vehicle implements Runnable {
	
	public int x, y;
    private int speed;
    private Color color;
    private int entrance;
    private TrafficLight myLight;
    private List<Vehicle> allVehicles;
    private volatile boolean isRunning = true;
    
    public Vehicle(int x, int y, int entrance, TrafficLight light, List<Vehicle> allVehicles) {
        this.x = x;
        this.y = y;
        this.entrance = entrance;
        this.myLight = light;
        this.allVehicles = allVehicles;
        
        Random random = new Random();
        this.speed = random.nextInt(3) + 2;
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
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
    
    public void setRunning(boolean isRunning) {
    	this.isRunning = isRunning;
    }

	@Override
	public void run() {
		while (true) {
			try {
				
				// Pause simulation
                if (!isRunning) {
                    Thread.sleep(50);
                    continue;
                }
				
				if (isOutOfScreen()) {
	                removeVehicle();
	                break;	// Stop the thread
	            }
				
				if (isAtStopLine()) {
	                // Wait for green
	                while (!myLight.isGreen()) {
	                	Thread.sleep(30);
	                }

	                // Enter junction - only 1 vehicle at a time
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
	
	public void draw(Graphics graphics) {
		graphics.setColor(color);
		if(isVertical(entrance)) {
			graphics.fillRect(x, y, 20, 30);
		} else {
			graphics.fillRect(x, y, 30, 20);
		}
    }
	
	private boolean isVertical(int entrance) {
		return entrance < 2;
	}
	
	private boolean isAtStopLine() {
	    switch (entrance) {
	        case 0 -> {
	            return y >= 190 && y <= 200;
	        }
	        case 1 -> {
	            return y >= 310 && y <= 320;
	        }
	        case 2 -> {
	            return x >= 310 && x <= 320;
	        }
	        case 3 -> {
	            return x >= 430 && x <= 440;
	        }
	        default -> {
	            return false;
	        }
	    }
	}
	
	private void moveThroughIntersection() throws InterruptedException {
	    int midX = 400;
	    int midY = 300;
	    int roadHalfWidth = 40;

	    while (true) {
	        moveOneStep();

	        Thread.sleep(30);

	        // Check if vehicle has left the junction
	        if (entrance == 0 && y >= midY + roadHalfWidth + 5) break;
	        if (entrance == 1 && y <= midY - roadHalfWidth - 5) break;
	        if (entrance == 2 && x >= midX + roadHalfWidth + 5) break;
	        if (entrance == 3 && x <= midX - roadHalfWidth - 5) break;
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

	            int safeDistance = 40;

	            switch (entrance) {

	                // top -> down
	                case 0 -> {
	                    if (other.getY() > y && other.getY() - y < safeDistance) {
	                        return true;
	                    }
	                }

	                // bottom -> up
	                case 1 -> {
	                    if (other.getY() < y && y - other.getY() < safeDistance) {
	                        return true;
	                    }
	                }

	                // left -> right
	                case 2 -> {
	                    if (other.getX() > x && other.getX() - x < safeDistance) {
	                        return true;
	                    }
	                }

	                // right -> left
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
        return x < -50 || x > 850 || y < -50 || y > 650;
    }
    
    private void removeVehicle() {

        isRunning = false;

        synchronized (allVehicles) {
            allVehicles.remove(this);
        }
    }

}
