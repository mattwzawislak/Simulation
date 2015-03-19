package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class InsertionSort implements AlgorithmImplementation {

    private static final String NAME = "Insertion Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) {
        for (int i = 1; i < array.size(); i++) {
            int j = i;
            while (j > 0 && array.compare(j - 1, j) > 0) {
                array.swap(j, j - 1);
                if (!array.sleep(delay)) {
                    return;
                }
                j--;
            }
            if (!array.sleep(delay)) {
                return;
            }
        }
    }
}
