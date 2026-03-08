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
				// Check if vehicle is at stop line
	            if (isAtStopLine()) {
	                // Wait until the light turns GREEN
	                while (!myLight.isGreen()) {
	                    Thread.sleep(30);
	                }

	                // Move through intersection safely
	                moveThroughIntersection();
	                continue; // skip normal movement this loop
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
	        switch (entrance) {
	            case 0: // top → moving down
	                y += speed;
	                break;
	            case 1: // bottom → moving up
	                y -= speed;
	                break;
	            case 2: // left → moving right
	                x += speed;
	                break;
	            case 3: // right → moving left
	                x -= speed;
	                break;
	        }
	        Thread.sleep(30);
	}

}
