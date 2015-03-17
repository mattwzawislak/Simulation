package org.obicere.simulation.algorithm.maze.generation.algorithm;

/**
 */
public enum MazeGenerationAlgorithmSet {

    WILSON_ALGORITHM(new WilsonAlgorithm());

    private final MazeGenerationAlgorithm algorithm;

    private MazeGenerationAlgorithmSet(final MazeGenerationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return algorithm.getName();
    }

    public MazeGenerationAlgorithm getAlgorithm() {
        return algorithm;
    }

}
