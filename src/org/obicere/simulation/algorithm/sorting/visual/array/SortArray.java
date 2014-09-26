package org.obicere.simulation.algorithm.sorting.visual.array;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Obicere
 */
public class SortArray {

    private final Random random = new Random();

    private final Marker  marker;
    private final Counter counter;

    private final int[] array;

    public SortArray(final int length) {
        this.marker = new Marker(length);
        this.counter = new Counter();
        this.array = new int[length];

        fill();
        shuffle(); // Shaken, not stirred
    }

    public SortArray(final int[] array, final Counter counter) {
        this.marker = new Marker(array.length);
        this.counter = counter;
        this.array = array;
    }

    private void fill() {
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
    }

    public void shuffle() {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    public Marker getMarker() {
        return marker;
    }

    public Counter getCounter() {
        return counter;
    }

    public int size() {
        return array.length;
    }

    public int get(final int i) {
        check(i);
        marker.mark(i);
        counter.accessed();
        return array[i];
    }

    public int getUnique(final int i) {
        check(i);
        marker.markUnique(i);
        counter.accessed();
        return array[i];
    }

    public int getSilent(final int i) {
        check(i);
        return array[i];
    }

    public SortArray select(final int i, final int j) {
        check(i);
        check(j);
        marker.markRange(i, j);
        counter.multiAccessed(j - i);
        final int[] copy = Arrays.copyOfRange(array, i, j);
        return new SortArray(copy, counter);
    }

    public SortArray selectUnique(final int i, final int j) {
        check(i);
        check(j);
        marker.markRangeUnique(i, j);
        counter.multiAccessed(j - i);
        final int[] copy = Arrays.copyOfRange(array, i, j);
        return new SortArray(copy, counter);
    }

    public void swap(final int i, final int j) {
        check(i);
        check(j);
        marker.markSwap(i, j);
        counter.multiAccessed(2);
        final int temp = get(i);
        array[i] = get(j);
        array[j] = temp;
    }

    public void swapUnique(final int i, final int j) {
        check(i);
        check(j);
        marker.markSwap(i, j);
        counter.multiAccessed(2);
        final int temp = getUnique(i);
        array[i] = getUnique(j);
        array[j] = temp;
    }

    public void cycleDone() {
        marker.clear();
    }

    public int compare(final int i, final int j) {
        check(i);
        check(j);
        counter.compared();
        final int x = get(i);
        final int y = get(j);
        return x - y;
    }

    public int compareUnique(final int i, final int j) {
        check(i);
        check(j);
        counter.compared();
        final int x = getUnique(i);
        final int y = getUnique(j);
        return x - y;
    }

    private void check(final int i) {
        if (0 > i || i >= array.length) {
            throw new IndexOutOfBoundsException("Index: " + i + ", is out of bounds for size: " + array.length);
        }
    }

    public void sleep(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        marker.clear();
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

}
