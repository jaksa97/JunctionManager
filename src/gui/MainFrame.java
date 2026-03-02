package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainFrame extends JFrame {

	private DrawingPanel canvas;

	public MainFrame() {
		setTitle("Junction Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		setJMenuBar(new JunctionMenuBar(this));
		
		canvas = new DrawingPanel();
		
		getContentPane().add(canvas, BorderLayout.CENTER);
		
		new Timer(30, e -> canvas.repaint()).start();
	}

}
