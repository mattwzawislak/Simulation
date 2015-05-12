package org.obicere.simulation.algorithm.maze.generation.algorithm;

import java.util.function.Supplier;

/**
 */
public enum MazeGenerationAlgorithmSet {

    WILSON_ALGORITHM(WilsonAlgorithm::new);

    private final Supplier<MazeGenerationAlgorithm> algorithm;

    private MazeGenerationAlgorithmSet(final Supplier<MazeGenerationAlgorithm> algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return getAlgorithm().getName();
    }

    public MazeGenerationAlgorithm getAlgorithm() {
        return algorithm.get();
    }

}
