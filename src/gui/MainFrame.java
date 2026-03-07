package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class MainFrame extends JFrame {

	private DrawingPanel canvas;

	public MainFrame() {
		setTitle("Junction Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		setJMenuBar(new JunctionMenuBar(this));
		
		canvas = new DrawingPanel();
		canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.spawnVehicle();
            }
        });
		
		getContentPane().add(canvas, BorderLayout.CENTER);
		
		new Timer(30, e -> canvas.repaint()).start();
	}

}
