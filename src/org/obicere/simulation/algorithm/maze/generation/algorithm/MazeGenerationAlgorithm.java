package org.obicere.simulation.algorithm.maze.generation.algorithm;

import org.obicere.simulation.algorithm.maze.generation.util.MazeGraph;

import java.awt.Graphics;

/**
 */
public interface MazeGenerationAlgorithm {

    public static final int DIRECTION_UP    = 0x01;
    public static final int DIRECTION_RIGHT = 0x02;
    public static final int DIRECTION_DOWN  = 0x04;
    public static final int DIRECTION_LEFT  = 0x08;

    public String getName();

    public void paintHelpers(final Graphics g, final float squareSize);

    public boolean generateMaze(final MazeGraph graph);

}
