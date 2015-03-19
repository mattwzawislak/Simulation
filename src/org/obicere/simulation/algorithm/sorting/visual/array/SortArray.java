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

    private final int offset;

    private final int length;

    public SortArray(final int length) {
        this.marker = new Marker(length);
        this.counter = new Counter();
        this.array = new int[length];
        this.offset = 0;
        this.length = length;

        fill();
        shuffle(); // Shaken, not stirred
    }

    private SortArray(final int[] array, final Counter counter, final Marker marker, final int offset, final int end) {
        this.marker = marker;
        this.counter = counter;
        this.array = array;
        this.offset = offset;
        this.length = end - offset;
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
        return length;
    }

    public void set(final int i, final int value) {
        check(i);
        marker.mark(i);
        counter.accessed();
        array[i] = value;
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
        marker.markUnique(i);
        return array[i];
    }

    public int getValue(final int i) {
        return array[i];
    }

    public SortArray select(final int i, final int j) {
        check(i);
        check(j - 1);
        marker.markRange(i, j - 1);
        counter.multiAccessed(j - i);
        return new SortArray(array, counter, marker, i, j);
    }

    public SortArray selectUnique(final int i, final int j) {
        check(i);
        check(j - 1);
        marker.markRangeUnique(i, j - 1);
        counter.multiAccessed(j - i);
        return new SortArray(array, counter, marker, i, j);
    }

    public SortArray selectUnmarked(final int i, final int j) {
        check(i);
        check(j - 1);
        counter.multiAccessed(j - i);
        return new SortArray(array, counter, marker, i, j);
    }

    public void swap(final int i, final int j) {
        check(i);
        check(j);
        marker.markSwap(i, j);
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

    public int offset() {
        return offset;
    }

    public int compare(final int i, final int j) {
        check(i);
        check(j);
        counter.compared();
        final int x = get(i);
        final int y = get(j);
        return x - y;
    }

    public int compareValues(final int a, final int b) {
        counter.compared();
        return a - b;
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
        if (offset > i || i >= offset + size()) {
            throw new IndexOutOfBoundsException("Index: " + i + ", is out of bounds for size: " + offset + ", " + (offset + size()));
        }
    }

    public boolean sleep(final int delay) {
        if (Thread.interrupted()) {
            return false;
        }
        try {
            Thread.sleep(delay);
        } catch (final Exception e) {
            return false;
        }
        marker.clear();
        return true;
    }

    public int[] cloneData() {
        final int[] nums = new int[length];
        System.arraycopy(array, offset, nums, 0, length);
        return nums;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

}
