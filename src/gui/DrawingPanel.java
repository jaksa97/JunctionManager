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
import model.Vehicle;

public class DrawingPanel extends JPanel {
	
	private List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<>());
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	public DrawingPanel() {
		setBackground(Color.WHITE);
	}
	
	public void spawnVehicle() {
		Random random = new Random();
		int entrance = random.nextInt(4);	// 0: Top, 1: Bottom, 2: Left, 3: Right
		
		int startX = 0;
		int startY = 0;
        int midX = getWidth() / 2;
        int midY = getHeight() / 2;
        boolean isVertical = false;
        
        switch (entrance) {
			case 0: {
				startX = midX - 30; 
				startY = 0;
				break;
			}
			case 1: {
				startX = midX + 10; 
				startY = getHeight();
				break;
			}
			case 2: {
				startX = 0; 
				startY = midY + 10;
				break;
			}
			case 3: {
				startX = getWidth(); 
				startY = midY - 30;
				break;
			}
        }
        
        Vehicle vehicle = new Vehicle(startX, startY, entrance);
        vehicles.add(vehicle);
        executorService.execute(vehicle);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		Graphics2D g2 = (Graphics2D) graphics;
		
		int w = getWidth();
        int h = getHeight();
        
        int centerX = w / 2;
        int centerY = h / 2;

        // Draw roads
        g2.setColor(Color.GRAY);
        g2.fillRect(0, centerY - 40, w, 80);
        g2.fillRect(centerX - 40, 0, 80, h);
        
        // Draw dashed lines
        float[] dashPattern = {20f, 20f};
        BasicStroke dashed = new BasicStroke(
                3,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                dashPattern,
                0
        );

        g2.setStroke(dashed);
        g2.setColor(Color.WHITE);

        // Horizontal road dashed lines (LEFT side)
        g2.drawLine(0, centerY, centerX - 40, centerY);

        // Horizontal road dashed lines (RIGHT side)
        g2.drawLine(centerX + 40, centerY, w, centerY);

        // Vertical road dashed lines (TOP side)
        g2.drawLine(centerX, 0, centerX, centerY - 40);

        // Vertical road dashed lines (BOTTOM side)
        g2.drawLine(centerX, centerY + 40, centerX, h);
        
        synchronized (vehicles) {
            for (Vehicle v : vehicles) {
                v.draw(graphics);
            }
        }
	}
	
}