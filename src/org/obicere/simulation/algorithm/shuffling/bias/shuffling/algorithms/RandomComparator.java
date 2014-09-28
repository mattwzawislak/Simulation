package org.obicere.simulation.algorithm.shuffling.bias.shuffling.algorithms;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ShufflingAlgorithm;

import java.util.Random;

/**
 * @author Obicere
 */
public class RandomComparator implements ShufflingAlgorithm {

    private static final String NAME = "Random-Comparator Sort";

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public void shuffle(final int[] nums) {
        final Random random = new Random();
        for (int i = 0; i < nums.length; i++) {
            boolean swapped = false;
            for (int j = 1; j < (nums.length - i); j++) {
                if (random.nextBoolean()) {
                    swap(nums, j, j - 1);
                    swapped = true;
                }

            }
            if (!swapped) {
                break;
            }
        }

    }
}
