package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public enum Algorithm {

    BOGO_SORT(new BogoSort()),

    BUBBLE_SORT(new BubbleSort()),

    INSERTION_SORT(new InsertionSort()),

    MERGE_SORT(new MergeSort());

    private final AlgorithmImplementation impl;

    private volatile boolean computing = false;

    private Algorithm(final AlgorithmImplementation impl) {
        this.impl = impl;
    }

    public void sort(final SortArray array, final int delay) {
        computing = true;
        impl.sort(array, delay);
        array.sleep(100); // Allow final paints to finish, etc.
        computing = false;
    }

    public boolean isComputing() {
        return computing;
    }

    @Override
    public String toString() {
        return impl.getName();
    }

}
