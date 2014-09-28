package org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ShufflingAlgorithm;

/**
 * @author Obicere
 */
public class MathRandom implements ShufflingAlgorithm {

    private static final String NAME = "Math Random";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void shuffle(int[] nums) {
        for (int i = nums.length - 1; i > 0; i--){
            final int index = (int) ((i + 1) * Math.random());
            swap(nums, index, i);
        }
    }
}
