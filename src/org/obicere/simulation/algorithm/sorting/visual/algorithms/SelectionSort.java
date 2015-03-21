package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class SelectionSort implements AlgorithmImplementation {

    private static final String NAME = "Selection Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) throws InterruptedException {
        final int length = array.size();
        for(int i = 0 ; i < length - 1; i++){
            final int flag = array.get(i);

            int min = i;
            int minValue = flag;
            for(int j = i + 1; j < length; j++){
                final int next = array.get(j);
                if(array.compareValues(minValue, next) > 0){
                    min = j;
                    minValue = next;
                }
                array.getMarker().markUnique(i);
                array.getMarker().markUnique(min);
                array.sleep(delay);
            }
            if(min != i){
                array.swap(min, i);
            }
            array.sleep(delay);

        }

    }
}
