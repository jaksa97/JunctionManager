package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	
	public DrawingPanel() {
		setBackground(Color.WHITE);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		int w = getWidth();
        int h = getHeight();

        graphics.setColor(Color.GRAY);
        graphics.fillRect(0, h/2 - 40, w, 80);
        graphics.fillRect(w/2 - 40, 0, 80, h);
	}
	
}
