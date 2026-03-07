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
    
    public Vehicle(int x, int y, int entrance) {
        this.x = x;
        this.y = y;
        this.entrance = entrance;
        
        Random random = new Random();
        this.speed = random.nextInt(3) + 2;
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

	@Override
	public void run() {
		while (x < 800 && y < 600 && x > -50 && y > -50) {
			try {
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

}
