package org.obicere.simulation.algorithm.shuffling.bias.shuffling;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms.FisherYates;
import org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms.MathRandom;
import org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms.MathRandomSquared;
import org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms.RandomComparator;

/**
 * @author Obicere
 */
public enum Algorithm {

    FISHER_YATES(new FisherYates()),
    MATH_RANDOM(new MathRandom()),
    MATH_RANDOM_SQUARED(new MathRandomSquared()),
    RANDOM_COMPARATOR(new RandomComparator());

    private final ShufflingAlgorithm algorithm;

    private Algorithm(final ShufflingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public ShufflingAlgorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return algorithm.getName();
    }

    }
