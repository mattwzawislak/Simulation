package org.obicere.simulation.algorithm.maze.generation.util;

import org.obicere.simulation.algorithm.maze.generation.MazeGeneration;
import org.obicere.simulation.algorithm.maze.generation.algorithm.MazeGenerationAlgorithm;

import java.util.Objects;

/**
 */
public class MazeGenerator {

    private volatile MazeGraph workingGraph;

    private volatile MazeGenerationAlgorithm workingAlgorithm;

    public void createNewMaze(final MazeGenerationAlgorithm algorithm, final int rows, final int columns) {
        Objects.requireNonNull(algorithm);
        this.workingAlgorithm = algorithm;
        this.workingGraph = new MazeGraph(rows, columns);
    }

    public MazeGraph getWorkingGraph() {
        return workingGraph;
    }

    public MazeGenerationAlgorithm getWorkingAlgorithm() {
        return workingAlgorithm;
    }

}
