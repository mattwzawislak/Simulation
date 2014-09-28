package org.obicere.simulation.algorithm.shuffling.bias;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.algorithm.shuffling.bias.gui.FrameManager;

import javax.swing.*;

/**
 * @author Obicere
 */
public class ShufflingBias extends Simulation {

    private static final String NAME = "Shuffling Bias";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(FrameManager::new);
    }
}
