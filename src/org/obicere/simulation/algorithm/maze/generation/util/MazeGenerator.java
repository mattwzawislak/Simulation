package org.obicere.simulation.algorithm.maze.generation.util;

import org.obicere.simulation.algorithm.maze.generation.algorithm.MazeGenerationAlgorithm;

import java.util.Objects;

/**
 */
public class MazeGenerator {

    private volatile MazeGraph workingGraph;

    private volatile MazeGenerationAlgorithm workingAlgorithm;

    private volatile Thread workingThread;

    public void createNewMaze(final MazeGenerationAlgorithm algorithm, final int rows, final int columns) {
        Objects.requireNonNull(algorithm);
        this.workingAlgorithm = algorithm;
        this.workingGraph = new MazeGraph(rows, columns);
        doWork();
    }

    public MazeGraph getWorkingGraph() {
        return workingGraph;
    }

    public MazeGenerationAlgorithm getWorkingAlgorithm() {
        return workingAlgorithm;
    }

    public void doWork() {
        if (workingThread != null) {
            workingThread.interrupt();
        }
        workingThread = new Thread(() -> workingAlgorithm.generateMaze(workingGraph));
        workingThread.setDaemon(true);
        workingThread.start();
    }

}
