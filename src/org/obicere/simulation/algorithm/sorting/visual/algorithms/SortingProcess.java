package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

import javax.swing.SwingWorker;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Obicere
 */
public class SortingProcess {

    private final Algorithm algorithm;
    private final SortArray array;

    private SwingWorker<Void, Integer> task;

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
        this.task = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                calculating.set(true);
                try {
                    algorithm.sort(array, delay);
                } catch (final InterruptedException e) {
                    return null;
                }
                for (int i = 1; i < array.size(); i++) {
                    if (i != array.getSilent(i - 1)) {
                        System.err.println("Sorting algorithm failed.");
                        break;
                    }
                    array.sleep(delay);
                }
                calculating.set(false);
                return null;
            }
        };

        task.execute();
    }

    public void halt() {
        calculating.set(false);
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }

}
