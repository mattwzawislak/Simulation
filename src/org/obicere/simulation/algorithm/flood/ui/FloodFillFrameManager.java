package org.obicere.simulation.algorithm.flood.ui;

import org.obicere.simulation.algorithm.flood.FloodFillSimulation;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 */
public class FloodFillFrameManager {

    public FloodFillFrameManager() {

        final JFrame frame = new JFrame(FloodFillSimulation.NAME);
        final FloodPanel panel = new FloodPanel();

        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
