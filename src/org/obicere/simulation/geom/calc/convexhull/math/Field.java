package org.obicere.simulation.geom.calc.convexhull.math;

import org.obicere.simulation.geom.calc.convexhull.awt.Marker;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Obicere
 */
public class Field {

    private final ArrayList<Point> pointList = new ArrayList<>();

    private Marker marker;

    private volatile boolean calculating = false;

    public List<Point> points() {
        return pointList;
    }

    public Marker getMarker() {
        return marker;
    }

    public void add(final Point point) {
        pointList.add(point);
    }

    public void clear() {
        pointList.clear();
    }

    public void clearMarker() {
        marker = null;
    }

    private void safeSleep(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return pointList.size();
    }

    public boolean isCalculating() {
        return calculating;
    }

    public void getConvexHull(final Polygon polygon, final int delay) {
        if (pointList.size() < 3) {
            return;
        }
        calculating = true;

        final int size = pointList.size();
        marker = new Marker(size);
        final Point[] array = pointList.toArray(new Point[size]);
        final int leftMost = getLeftMost(array);
        Point pointOnHull = array[leftMost];
        final Point finalEnd = pointOnHull;
        int last = leftMost;
        marker.markUnique(leftMost);
        while (true) {
            polygon.addPoint(pointOnHull.x, pointOnHull.y);
            safeSleep(delay);
            Point end = array[0];
            int j = 0;
            int best = 0;
            while (j + 1 < size) {
                j++;
                if (array[j] == null) {
                    continue;
                }
                if (end == null) {
                    end = array[j];
                    best = j;
                    continue;
                }
                marker.mark(j);
                marker.mark(last, j);
                safeSleep(delay);
                if (end.equals(pointOnHull) || rightOfLine(array[j], pointOnHull, end)) {
                    end = array[j];
                    best = j;
                }
                marker.unmark(j);
                marker.unmark(last, j);
            }
            marker.markUnique(best);
            marker.markUnique(last, best);
            array[best] = null; // Avoid repetition
            last = best;
            pointOnHull = end;
            if (end.equals(finalEnd)) {
                break;
            }
        }
        safeSleep(100); // Allow final repaint to get rid of fragments
        calculating = false;
    }

    private boolean rightOfLine(final Point a, final Point b, final Point c) {
        return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0;
    }

    private int getLeftMost(final Point[] array) {
        int best = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[best].x > array[i].x) {
                best = i;
            }
        }
        return best;
    }
}
