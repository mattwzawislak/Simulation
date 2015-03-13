package org.obicere.simulation.algorithm.sorting.visual.array;

import java.awt.*;

/**
 * @author Obicere
 */
public class Marker {

    private static final Color DEFAULT = Color.DARK_GRAY;

    private static final Color SINGLE_COLOR = Color.RED;

    private static final Color SWAP_COLOR = Color.GREEN;

    private static final Color ARRAY_COLOR = Color.ORANGE;

    private static final Color[] UNIQUE_COLORS = new Color[]{
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.PINK,
            Color.BLUE
    };

    private static final int UNIQUE_LENGTH = UNIQUE_COLORS.length;

    private final Color[] marks;
    private final int     length;

    private int uniqueIndex = 0;

    public Marker(final int length) {
        this.marks = new Color[length];
        this.length = length;
    }

    public void mark(final int i) {
        marks[i] = SINGLE_COLOR;
    }

    public void markUnique(final int i) {
        marks[i] = nextUnique();
    }

    public void markRange(final int i, final int j) {
        for (int k = i; k < j; k++) {
            marks[k] = ARRAY_COLOR;
        }
    }

    public void markRangeUnique(final int i, final int j) {
        final Color unique = nextUnique();
        for (int k = i; k < j; k++) {
            marks[k] = unique;
        }
    }

    public void markSwap(final int i, final int j) {
        marks[i] = SWAP_COLOR;
        marks[j] = SWAP_COLOR;
    }

    public void clear() {
        uniqueIndex = 0;
        for (int i = 0; i < length; i++) {
            marks[i] = DEFAULT;
        }
    }

    private Color nextUnique() {
        uniqueIndex %= UNIQUE_LENGTH;
        return UNIQUE_COLORS[uniqueIndex++];
    }

    public Color colorFor(final int i) {
        return marks[i];
    }

}
