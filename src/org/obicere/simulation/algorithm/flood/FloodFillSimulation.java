package org.obicere.simulation.algorithm.flood;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.algorithm.flood.ui.FloodFillFrameManager;

import javax.swing.SwingUtilities;

/**
 */
public class FloodFillSimulation extends Simulation {

    public static final String NAME = "Flood Fill";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(FloodFillFrameManager::new);
    }
}
