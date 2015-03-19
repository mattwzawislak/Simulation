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

    public void quickSort(final SortArray array, final int start, final int end, final int delay) {
        if (start >= end) {
            return;
        }

        final int middle = start + (end - start) / 2;
        final int pivot = array.get(middle);

        int i = start;
        int j = end;
        while (i <= j) {
            while (array.compareValues(array.get(i), pivot) < 0) {
                array.getMarker().mark(j);
                array.getMarker().markUnique(middle);
                if (!array.sleep(delay)) {
                    return;
                }
                i++;
            }

            while (array.compareValues(array.get(j), pivot) > 0) {
                array.getMarker().mark(i);
                array.getMarker().markUnique(middle);
                if (!array.sleep(delay)) {
                    return;
                }
                j--;
            }

            array.getMarker().markUnique(middle);
            if (i <= j) {
                array.swap(i, j);
                if (!array.sleep(delay)) {
                    return;
                }
                i++;
                j--;
            }
        }

        if (start < j) {
            quickSort(array, start, j, delay);
        }

        if (end > i) {
            quickSort(array, i, end, delay);
        }
    }
}
