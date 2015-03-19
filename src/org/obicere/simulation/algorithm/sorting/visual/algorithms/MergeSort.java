package org.obicere.simulation.algorithm.sorting.visual.algorithms;

import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;

/**
 * @author Obicere
 */
public class MergeSort implements AlgorithmImplementation {

    private static final String NAME = "Merge Sort";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sort(final SortArray array, final int delay) {
        if (array.size() < 2) {
            return;
        }
        mergeSort(array, 0, array.size(), delay);
    }

    public void mergeSort(final SortArray array, final int start, final int end, final int delay) {
        if (array.size() < 2) {
            return;
        }
        final int mid = start + (end - start) / 2;
        final SortArray a = array.selectUnmarked(start, mid);
        final SortArray b = array.selectUnmarked(mid, end);
        mergeSort(a, start, mid, delay);
        mergeSort(b, mid, end, delay);

        int i = start;
        int j = mid;
        int k = start;

        final int[] ca = a.cloneData();
        final int[] cb = b.cloneData();

        while (i < a.size() + start && j < b.size() + mid) {
            array.compare(i, j); // Fake comparisons. Issue has to do with cloned arrays
            if (ca[i - start] < cb[j - mid]) {
                array.set(k, ca[i - start]);
                if (!array.sleep(delay)) {
                    return;
                }
                i++;
            } else {
                array.set(k, cb[j - mid]);
                if (!array.sleep(delay)) {
                    return;
                }
                j++;
            }
            k++;
        }
        while (i < a.size() + start) {
            array.set(k, ca[i - start]);
            if (!array.sleep(delay)) {
                return;
            }
            i++;
            k++;
        }
        while (j < b.size() + mid) {
            array.set(k, cb[j - mid]);
            if (!array.sleep(delay)) {
                return;
            }
            j++;
            k++;
        }
    }

}
