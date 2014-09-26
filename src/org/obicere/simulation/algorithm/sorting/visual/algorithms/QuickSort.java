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

    public void quickSort(final SortArray array, final int left, final int right, final int delay) {
        if (left >= right) {
            return;
        }
        int middle = (left + right) / 2;

        int i = left, j = right;
        while (i <= j) {
            while (array.compare(i, middle) < 0) {
                array.sleep(delay);
                i++;
            }

            while (array.compare(j, middle) > 0) {
                array.sleep(delay);
                j--;
            }

            if (i <= j) {
                array.swap(i, j);
                i++;
                j--;
            }
        }

        if (left < j) {
            quickSort(array, left, j, delay);
        }

        if (right > i) {
            quickSort(array, i, right, delay);
        }

    }

}
