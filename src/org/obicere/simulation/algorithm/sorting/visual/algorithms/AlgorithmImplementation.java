package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public interface AlgorithmImplementation {

    public String getName();

    public void sort(final SortArray array, final int delay) throws InterruptedException;

}
