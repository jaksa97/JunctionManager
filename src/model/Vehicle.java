package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Vehicle implements Runnable {
	
	public int x, y;
    private int speed;
    private Color color;
    private int entrance;
    private TrafficLight myLight;
    
    public Vehicle(int x, int y, int entrance, TrafficLight light) {
        this.x = x;
        this.y = y;
        this.entrance = entrance;
        this.myLight = light;
        
        Random random = new Random();
        this.speed = random.nextInt(3) + 2;
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

	@Override
	public void run() {
		while (x < 800 && y < 600 && x > -50 && y > -50) {
			try {
				if (isAtStopLine()) {
	                // wait for green
	                while (!myLight.isGreen()) {
	                	Thread.sleep(30);
	                }

	                // enter junction - only 1 vehicle at a time
	                JunctionManager.getInstance().enterJunction();
	                
	                try {
	                    moveThroughIntersection();
	                } finally {
	                	JunctionManager.getInstance().exitJunction();
	                }
	                
	                continue;
	            }
				
				moveOneStep();
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
	        if (entrance == 0 && y >= midY + roadHalfWidth) break;
	        if (entrance == 1 && y <= midY - roadHalfWidth) break;
	        if (entrance == 2 && x >= midX + roadHalfWidth) break;
	        if (entrance == 3 && x <= midX - roadHalfWidth) break;
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

}
