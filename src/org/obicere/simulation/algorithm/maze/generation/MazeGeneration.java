package org.obicere.simulation.algorithm.maze.generation;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.algorithm.maze.generation.ui.MazeGenerationFrameManager;

import javax.swing.SwingUtilities;

/**
 */
public class MazeGeneration extends Simulation {

    public static final String NAME = "Maze Generation";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(MazeGenerationFrameManager::new);
    }
}
