package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Obicere
 */
public class SortingProcess {

    private final Algorithm algorithm;
    private final SortArray array;

    private Thread thread;

    private final AtomicBoolean calculating = new AtomicBoolean();

    public SortingProcess(final Algorithm algorithm, final int length) {
        this.algorithm = algorithm;
        this.array = new SortArray(length);
    }

    public SortArray getArray() {
        return array;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public boolean isCalculating() {
        return calculating.get();
    }

    public void sort(final int delay) {
        this.thread = new Thread(() -> {
            calculating.set(true);
            algorithm.sort(array, delay);
            for (int i = 1; i < array.size(); i++) {
                if (i != array.getSilent(i - 1)) {
                    System.err.println("Sorting algorithm failed.");
                    return;
                }
                array.sleep(delay);
            }
            calculating.set(false);
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void halt() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

}
