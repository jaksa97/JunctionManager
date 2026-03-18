package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JPanel;
import model.TrafficLight;
import model.vehicle.Vehicle;
import model.vehicle.VehicleFactory;
import model.vehicle.VehicleType;

public class DrawingPanel extends JPanel {
	
	private List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<>());
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	private TrafficLight horizontalLight;
    private TrafficLight verticalLight;
    
    private volatile boolean isRunning = false;
    
    public DrawingPanel() {
    	setBackground(Color.WHITE);
        this.horizontalLight = new TrafficLight(true);
        this.verticalLight = new TrafficLight(false);
        executorService.execute(horizontalLight);
        executorService.execute(verticalLight);
    }
	
	public void spawnVehicle() {
		Random random = new Random();
		int entrance = random.nextInt(4);	// 0: Top, 1: Bottom, 2: Left, 3: Right
		
		int startX = 0;
		int startY = 0;
        int midX = getWidth() / 2;
        int midY = getHeight() / 2;
        
        switch (entrance) {
			case 0 -> {
				startX = midX - 30; 
				startY = 0;
			}
			case 1 -> {
				startX = midX + 10; 
				startY = getHeight();
			}
			case 2 -> {
				startX = 0; 
				startY = midY + 10;
			}
			case 3 -> {
				startX = getWidth(); 
				startY = midY - 30;
			}
        }
        
        // Prevent spawn
        if (!canSpawnVehicle(startX, startY, entrance) || !isRunning) {
            return;
        }
        
        TrafficLight vehicleTrafficLight = entrance < 2 ? verticalLight : horizontalLight;
        
        Vehicle vehicle = VehicleFactory.createVehicle(VehicleType.getRandomType(), startX, startY, entrance, vehicleTrafficLight, vehicles);
        vehicle.setRunning(isRunning);
        vehicles.add(vehicle);
        executorService.execute(vehicle);
	}
	
	private boolean canSpawnVehicle(int x, int y, int entrance) {
	    synchronized (vehicles) {
	        for (Vehicle v : vehicles) {

	            // Check vehicles from same lane
	            if (v.getEntrance() == entrance) {

	                // Vertical lanes
	                if (entrance < 2) {
	                    if (Math.abs(v.getY() - y) < 50) {
	                        return false;
	                    }
	                }

	                // Horizontal lanes
	                else {
	                    if (Math.abs(v.getX() - x) < 50) {
	                        return false;
	                    }
	                }
	            }
	        }
	    }
	    return true;
	}
	
	// Stop/start entire simulation
    public void setSimulationRunning(boolean isRunning) {
        this.isRunning = isRunning;

        // Update traffic lights
        horizontalLight.setRunning(this.isRunning);
        verticalLight.setRunning(this.isRunning);

        // Update all vehicles
        synchronized (vehicles) {
            for (Vehicle v : vehicles) {
                v.setRunning(this.isRunning);
            }
        }
    }

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		Graphics2D graphics2d = (Graphics2D) graphics;
		
		int w = getWidth();
        int h = getHeight();

        int centerX = w / 2;
        int centerY = h / 2;

        drawRoads(graphics2d, w, h, centerX, centerY);
        
        drawDashedLine(graphics2d, w, h, centerX, centerY);
        
        drawTrafficLight(graphics2d, horizontalLight, w/2 - 75, h/2 - 100);
        drawTrafficLight(graphics2d, verticalLight, w/2 + 50, h/2 + 50);
        
        synchronized (vehicles) {
            vehicles.removeIf(vehicle -> vehicle.isOutOfScreen());
        }
        
        synchronized (vehicles) {
            for (Vehicle vehicle : vehicles) {
            	vehicle.draw(graphics);
            }
        }
	}
	
	private void drawRoads(Graphics2D graphics2d, int width, int height, int centerX, int centerY) {
		graphics2d.setColor(Color.GRAY);
        graphics2d.fillRect(0, centerY - 40, width, 80);
        graphics2d.fillRect(centerX - 40, 0, 80, height);
	}
	
	private void drawDashedLine(Graphics2D graphics2d, int width, int height, int centerX, int centerY) {
        float[] dashPattern = {20f, 20f};
        BasicStroke dashed = new BasicStroke(
                3,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                dashPattern,
                0
        );

        graphics2d.setStroke(dashed);
        graphics2d.setColor(Color.WHITE);

        // Horizontal road dashed lines (LEFT side)
        graphics2d.drawLine(0, centerY, centerX - 40, centerY);

        // Horizontal road dashed lines (RIGHT side)
        graphics2d.drawLine(centerX + 40, centerY, width, centerY);

        // Vertical road dashed lines (TOP side)
        graphics2d.drawLine(centerX, 0, centerX, centerY - 40);

        // Vertical road dashed lines (BOTTOM side)
        graphics2d.drawLine(centerX, centerY + 40, centerX, height);
	}
	
	private void drawTrafficLight(Graphics2D g2d, TrafficLight trafficLight, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, 20, 50);
        g2d.setColor(trafficLight.getCurrentState().getColor());
        g2d.fillOval(x + 2, y + 5, 15, 15);
    }
	
}