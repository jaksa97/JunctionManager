package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Vehicle implements Runnable {
	
	public int x, y;
    private int speed;
    private Color color;
    private int entrance;
    private boolean isRunning = true;
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
	                while (!myLight.isGreen()) Thread.sleep(30);

	                // **enter junction** - only 1 vehicle at a time
	                JunctionManager.getInstance().enterJunction();
	                try {
	                    moveThroughIntersection(); // now moves fully across junction
	                } finally {
	                	JunctionManager.getInstance().exitJunction();
	                }
	                continue;
	            }
				switch (entrance) {
					case 0: {
						y += speed;
						break;
					}
					case 1: {
						y -= speed;
						break;
					}
					case 2: {
						x += speed;
						break;
					}
					case 3: {
						x -= speed;
						break;
					}
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
	        case 0: // top → moving down
	            return y >= 190 && y <= 200;
	        case 1: // bottom → moving up
	            return y >= 310 && y <= 320;
	        case 2: // left → moving right
	            return x >= 310 && x <= 320;
	        case 3: // right → moving left
	            return x >= 430 && x <= 440;
	        default:
	            return false;
	    }
	}
	
	private void moveThroughIntersection() throws InterruptedException {
	    int midX = 400;
	    int midY = 300;
	    int roadHalfWidth = 40;

	    while (true) {
	        // Move the vehicle based on entrance
	        switch (entrance) {
	            case 0 -> y += speed; // top → down
	            case 1 -> y -= speed; // bottom → up
	            case 2 -> x += speed; // left → right
	            case 3 -> x -= speed; // right → left
	        }

	        Thread.sleep(30);

	        // Check if vehicle has left the junction
	        if (entrance == 0 && y >= midY + roadHalfWidth) break;
	        if (entrance == 1 && y <= midY - roadHalfWidth) break;
	        if (entrance == 2 && x >= midX + roadHalfWidth) break;
	        if (entrance == 3 && x <= midX - roadHalfWidth) break;
	    }
	}

}
