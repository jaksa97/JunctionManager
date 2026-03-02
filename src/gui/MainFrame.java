package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainFrame extends JFrame {

	private JPanel canvas;

	public MainFrame() {
		setTitle("Junction Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		canvas = new JPanel();
		
		getContentPane().add(canvas, BorderLayout.CENTER);
		
		new Timer(30, e -> canvas.repaint()).start();
	}

	
	
}
