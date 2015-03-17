package org.obicere.simulation.algorithm.maze.generation.algorithm;

import org.obicere.simulation.algorithm.maze.generation.util.MazeGraph;

import java.awt.Color;
import java.awt.Graphics;

/**
 */
public class WilsonAlgorithm implements MazeGenerationAlgorithm {

    private static final String NAME = " Wilson's Algorithm";

    private static final Color WORKING_COLOR = new Color(0x3070F0);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void paintHelpers(final Graphics g) {
        g.setColor(WORKING_COLOR);
    }

    @Override
    public boolean generateMaze(MazeGraph graph) {
        return false;
    }

    private class WilsonNode {

        private final int i;
        private final int j;
        private final int from;

        private WilsonNode(final int i, final int j) {
            this.i = i;
            this.j = j;
            this.from = -1;
        }

        private WilsonNode(final int i, final int j, final int direction) {
            this.i = i;
            this.j = j;
            this.from = direction;
        }

    }
}
