package gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.TrafficLight;

public class JunctionMenuBar extends JMenuBar {
	
	private boolean simRunning = false;
	
	public JunctionMenuBar(JFrame parent, TrafficLight horizontalLight, TrafficLight verticalLight) {
		JMenu simulation = new JMenu("Simulation");
		JMenuItem stopStart = new JMenuItem("Start/Stop");
		
		stopStart.addActionListener(event -> startStopSim(horizontalLight, verticalLight));
		
		simulation.add(stopStart);
		add(simulation);
		
		JMenu stats = new JMenu("Stats");
        JMenuItem passed = new JMenuItem("Vehicles Passed");

        passed.addActionListener(event -> {
            int count = 0;
            JOptionPane.showMessageDialog(
                    parent,
                    "Total vehicles passed: " + count,
                    "Statistics",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        stats.add(passed);
        add(stats);
	}
	
	private void startStopSim(TrafficLight horizontalLight, TrafficLight verticalLight) {
		simRunning = !simRunning;
		horizontalLight.setRunning(simRunning);
		verticalLight.setRunning(simRunning);
	}

}
