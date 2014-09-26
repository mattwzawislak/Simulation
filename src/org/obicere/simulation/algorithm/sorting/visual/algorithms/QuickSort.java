package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class QuickSort implements AlgorithmImplementation {

    private static final String NAME = "Quick Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) {
        quickSort(array, 0, array.size() - 1, delay);
    }

    private int partition(final SortArray array, int left, int right, final int delay) {
        int i = left;
        int j = right;
        final int pivot = (left + right) / 2;

        while (i <= j) {
            while (array.compare(i, pivot) < 0) {
                i++;
            }
            while (array.compare(j, pivot) > 0) {
                j--;
            }
            if (i <= j) {
                array.swap(i, j);
                i++;
                j--;
            }
        }

        return i;
    }

    private void quickSort(final SortArray array, int left, int right, final int delay) {
        int index = partition(array, left, right, delay);
        if (left < index - 1) {
            quickSort(array, left, index - 1, delay);
        }
        if (index < right) {
            quickSort(array, index, right, delay);
        }
    }

}
