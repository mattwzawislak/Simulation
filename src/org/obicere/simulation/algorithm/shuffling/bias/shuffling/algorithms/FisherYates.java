package org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ShufflingAlgorithm;

import java.util.Random;

/**
 * @author Obicere
 */
public class FisherYates implements ShufflingAlgorithm {

    private static final String NAME = "Fisher-Yates";

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public void shuffle(int[] nums) {
        final Random random = new Random();
        for (int i = nums.length - 1; i > 0; i--){
            final int index = random.nextInt(i + 1);
            swap(nums, index, i);
        }
    }
}
