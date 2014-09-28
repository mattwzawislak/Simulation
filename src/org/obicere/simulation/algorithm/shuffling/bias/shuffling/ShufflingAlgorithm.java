package org.obicere.simulation.algorithm.shuffling.bias.shuffling;

/**
 * @author Obicere
 */
public interface ShufflingAlgorithm {

    public String getName();

    public void shuffle(final int[] nums);

    default public void swap(final int[] nums, final int i, final int j){
        final int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

}
