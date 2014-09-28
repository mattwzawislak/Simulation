package org.obicere.simulation.algorithm.shuffling.bias.shuffling;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Obicere
 */
public class ResultData {

    // Smaller -> More sensitivity
    private static final float SLOPE = 10f;

    private static final int     COLOR_COUNT = 256;
    private static final Color[] COLORS      = new Color[COLOR_COUNT];

    static {
        for (int i = 0; i < COLOR_COUNT; i++) {
            COLORS[i] = new Color(i, 255 - i, 0); // Remember to fix this if color count changes.
        }
    }

    private final AtomicInteger[][] data;

    private final AtomicLong totalCount = new AtomicLong();

    private final int length;

    public ResultData(final int length) {
        this.data = new AtomicInteger[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                data[i][j] = new AtomicInteger();
            }
        }
        this.length = length;
    }

    public void register(final int[] nums) {
        if (nums.length != length) {
            throw new IllegalArgumentException("Array length must be " + length);
        }
        for (int i = 0; i < length; i++) {
            data[i][nums[i]].incrementAndGet();
            totalCount.incrementAndGet();
        }
    }

    public Color getColorFor(final int i, final int j) {
        final int count = data[i][j].get();
        final float error = percentError(count);
        final float index = (error / (error + SLOPE));
        return COLORS[(int) (index * (COLOR_COUNT - 1))];
    }

    private float percentError(final int value) {
        final float total = totalCount.get() / (float) length;
        return Math.abs((total - value) / total - 1) * 100;
    }

}
