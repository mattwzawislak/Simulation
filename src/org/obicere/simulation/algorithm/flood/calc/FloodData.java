package org.obicere.simulation.algorithm.flood.calc;

import java.util.ArrayList;

/**
 */
public class FloodData {

    private static final int[][] DELTA = new int[][]{
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1},
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1}
    };

    private volatile int rows;
    private volatile int columns;

    private volatile int[][] flags = new int[0][0];

    public synchronized int[][] getFlags() {
        return flags;
    }

    public synchronized int getRows() {
        return rows;
    }

    public synchronized int getColumns() {
        return columns;
    }

    public synchronized void resize(final int newRows, final int newColumns) {
        final int[][] newFlags = new int[newRows][newColumns];
        final int minColumns = Math.min(columns, newColumns);
        final int minRows = Math.min(rows, newRows);
        for (int i = 0; i < minRows; i++) {
            System.arraycopy(flags[i], 0, newFlags[i], 0, minColumns);
        }

        this.flags = newFlags;
        this.rows = newRows;
        this.columns = newColumns;
    }

    public synchronized int get(final int i, final int j) {
        checkRange(i, j);
        return flags[i][j];
    }

    public synchronized void toggled(final int i, final int j, final boolean blocked) {
        checkRange(i, j);
        if (blocked == flags[i][j] >= 0) {
            if (blocked) {
                flags[i][j] = -1;
            } else {
                flags[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public synchronized void calculate(final int i, final int j) {
        checkRange(i, j);
        if (flags[i][j] < 0) {
            return;
        }
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (flags[row][column] >= 0) {
                    flags[row][column] = Integer.MAX_VALUE;
                }
            }
        }
        flood(i, j);
    }

    private void flood(final int i, final int j) {
        final boolean[][] calculated = new boolean[rows][columns];
        final ArrayList<Cell> queue = new ArrayList<>();
        queue.add(new Cell(i, j, 0));

        while (!queue.isEmpty()) {
            queue.sort((o1, o2) -> Integer.compare(o1.distance, o2.distance));
            final Cell next = queue.remove(0);
            final int distance = next.distance;

            flags[next.i][next.j] = distance;
            calculated[next.i][next.j] = true;
            for (final int[] delta : DELTA) {
                final int di = next.i + delta[0];
                final int dj = next.j + delta[1];
                if (inRange(di, dj) && !calculated[di][dj] && flags[di][dj] >= 0) {
                    final Cell nextCell = new Cell(di, dj, distance + 1);
                    if (!queue.contains(nextCell)) {
                        queue.add(nextCell);
                    }
                }
            }
        }
    }

    private void checkRange(final int i, final int j) {
        if (i < 0 || rows <= i || j < 0 || columns <= j) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
    }

    private boolean inRange(final int i, final int j) {
        return i >= 0 && rows > i && j >= 0 && columns > j;
    }

    private class Cell {

        private final int i;
        private final int j;
        private final int distance;

        public Cell(final int i, final int j, final int distance) {
            this.i = i;
            this.j = j;
            this.distance = distance;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(9);
            builder.append("Cell[");
            builder.append(i);
            builder.append(',');
            builder.append(j);
            builder.append(']');
            return builder.toString();
        }

        @Override
        public int hashCode() {
            return i * 31 + j * 17;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Cell) {
                return cellEquals((Cell) obj);
            }
            return false;
        }

        private boolean cellEquals(final Cell cell) {
            return i == cell.i && j == cell.j;
        }
    }
}
