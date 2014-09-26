package org.obicere.simulation;

import javax.swing.*;

/**
 * @author Obicere
 */
public class SimulationPanel extends JPanel implements Comparable<SimulationPanel> {

    private final Simulation simulation;

    public SimulationPanel(final Simulation simulation) {
        this.simulation = simulation;

        final BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        final JLabel label = new JLabel(simulation.getName());
        final JButton run = new JButton("Run");

        run.addActionListener(e -> {
            SwingUtilities.invokeLater(simulation::main);
        });

        setLayout(layout);

        add(label);
        add(Box.createHorizontalGlue());
        add(run);
    }

    public Simulation getSimulation() {
        return simulation;
    }

    @Override
    public int compareTo(final SimulationPanel o) {
        return simulation.getName().compareToIgnoreCase(o.getSimulation().getName());
    }
}
