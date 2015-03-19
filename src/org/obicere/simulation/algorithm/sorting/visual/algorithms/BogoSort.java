package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class BogoSort implements AlgorithmImplementation {

    private static final String NAME = "Bogo Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) {
        for (int i = 1; i < array.size(); i++) {
            if (array.compare(i, i - 1) <= 0) {
                array.shuffle();

                if (!array.sleep(delay)) {
                    return;
                }
                i = 0;
            }
        }
    }
}
