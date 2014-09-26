package org.obicere.simulation.geom.calc.convexhull.awt;

import java.awt.*;

/**
 * @author Obicere
 */
public class Marker {

    private static final Color POINT_COLOR = Color.ORANGE;
    private static final Color LINE_COLOR  = Color.GRAY;

    private static final Color UNIQUE_POINT_COLOR = Color.GREEN;
    private static final Color UNIQUE_LINE_COLOR  = Color.BLACK;

    private final Color[]   pointMarks;
    private final Color[][] lineMarks;

    public Marker(final int length) {
        this.pointMarks = new Color[length];
        this.lineMarks = new Color[length][length];
    }

    public void unmark(final int i) {
        pointMarks[i] = null;
    }

    public void unmark(final int i, final int j) {
        lineMarks[i][j] = null;
    }

    public void mark(final int i) {
        pointMarks[i] = POINT_COLOR;
    }

    public void mark(final int i, final int j) {
        lineMarks[i][j] = LINE_COLOR;
    }

    public void markUnique(final int i) {
        pointMarks[i] = UNIQUE_POINT_COLOR;
    }

    public void markUnique(final int i, final int j) {
        lineMarks[i][j] = UNIQUE_LINE_COLOR;
    }

    public Color getColor(final int i) {
        return pointMarks[i];
    }

    public Color getColor(final int i, final int j) {
        return lineMarks[i][j];
    }
}
