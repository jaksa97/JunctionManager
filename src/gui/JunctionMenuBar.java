package gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JunctionMenuBar extends JMenuBar {
	
	private boolean simRunning = false;
	
	public JunctionMenuBar(JFrame parent) {
		JMenu simulation = new JMenu("Simulation");
		JMenuItem stopStart = new JMenuItem("Start/Stop");
		
		stopStart.addActionListener(event -> startStopSim());
		
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
	
	private void startStopSim() {
		simRunning = !simRunning;
	}

}
