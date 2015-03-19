package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class BubbleSort implements AlgorithmImplementation {

    private static final String NAME = "Bubble Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) {
        boolean swapped = true;
        int j = 0;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < array.size() - j; i++) {
                final int compare = array.compare(i, i + 1);
                if (compare > 0) {
                    array.swap(i, i + 1);
                    swapped = true;
                }
                if (!array.sleep(delay)) {
                    return;
                }
            }
        }
    }
}
