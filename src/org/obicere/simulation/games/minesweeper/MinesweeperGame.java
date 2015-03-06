package org.obicere.simulation.games.minesweeper;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.games.minesweeper.ui.MinesweeperFrameManager;

import javax.swing.SwingUtilities;

/**
 */
public class MinesweeperGame extends Simulation {

    private static final String NAME = "Minesweeper";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(MinesweeperFrameManager::new);
    }
}
