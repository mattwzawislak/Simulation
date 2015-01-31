package org.obicere.simulation.ai.othello;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.ai.othello.gui.OthelloFrameManager;

import javax.swing.SwingUtilities;

/**
 * @author Obicere
 * @version 1.0
 */
public class OthelloGame extends Simulation {

    private static final String NAME = "Othello";
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(() -> new OthelloFrameManager());
    }
}
