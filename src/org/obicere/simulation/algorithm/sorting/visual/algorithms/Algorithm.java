package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public enum Algorithm {

    BOGO_SORT(new BogoSort()),

    BUBBLE_SORT(new BubbleSort()),

    INSERTION_SORT(new InsertionSort()),

    MERGE_SORT(new MergeSort()),

    QUICK_SORT(new QuickSort());

    private final AlgorithmImplementation impl;

    private Algorithm(final AlgorithmImplementation impl) {
        this.impl = impl;
    }

    public void sort(final SortArray array, final int delay) {
        impl.sort(array, delay);
    }

    @Override
    public String toString() {
        return impl.getName();
    }

}
