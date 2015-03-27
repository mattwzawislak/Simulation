package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class CocktailSort implements AlgorithmImplementation {

    private static final String NAME = "Cocktail sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) throws InterruptedException {
        int begin = -1;
        int end = array.size() - 2;
        boolean swapped;
        do {
            swapped = false;
            begin++;
            for (int i = begin; i <= end; i++) {
                if (array.compare(i, i + 1) > 0) {
                    array.swap(i, i + 1);
                    swapped = true;
                }
                array.sleep(delay);
            }

            if (!swapped) {
                return;
            }
            end--;
            swapped = false;
            for (int i = end; i >= begin; i--) {
                if (array.compare(i, i + 1) > 0) {
                    array.swap(i, i + 1);
                    swapped = true;
                }
                array.sleep(delay);
            }

        } while (swapped);
    }
}
