package org.obicere.simulation.algorithm.maze.generation.util;

import java.util.NoSuchElementException;

/**
 */
public class MazeGraph {

    public static final int DIRECTION_UP    = 0x01;
    public static final int DIRECTION_RIGHT = 0x02;
    public static final int DIRECTION_DOWN  = 0x04;
    public static final int DIRECTION_LEFT  = 0x08;

    private static final int[] DIRECTIONS = new int[]{
            DIRECTION_UP, DIRECTION_RIGHT, DIRECTION_DOWN, DIRECTION_LEFT
    };

    private final Cell[][] cells;

    private final int rows;
    private final int columns;

    private Object renderLock = new Object();

    public MazeGraph(final int rows, final int columns) {
        if (rows <= 0) {
            throw new IllegalArgumentException("Row count must be positive.");
        }
        if (columns <= 0) {
            throw new IllegalArgumentException("Column count must be positive.");
        }
        this.cells = new Cell[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public static int[] getDirections() {
        return DIRECTIONS;
    }

    public Object getRenderLock() {
        return renderLock;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return columns;
    }

    public void add(final int i, final int j) {
        checkBounds(i, j);
        cells[i][j] = new Cell();
    }

    public void add(final int i, final int j, final int direction) {
        final Cell cell = cellAt(i, j);
        if (cell == null) {
            throw new NoSuchElementException("No cell at given coordinates: " + i + ", " + j);
        }
        final Cell newCell = newCell(i, j, direction);
        switch (direction) {
            case DIRECTION_UP:
                cell.upNode = newCell;
                break;
            case DIRECTION_RIGHT:
                cell.rightNode = newCell;
                break;
            case DIRECTION_DOWN:
                cell.downNode = newCell;
                break;
            case DIRECTION_LEFT:
                cell.leftNode = newCell;
                break;
            default:
                throw new AssertionError("Illegal state. New cell creation failed to detect invalid direction.");
        }
    }

    private Cell newCell(final int i, final int j, final int direction) {
        final int di;
        final int dj;
        switch (direction) {
            case DIRECTION_UP:
                di = -1;
                dj = 0;
                break;
            case DIRECTION_RIGHT:
                di = 0;
                dj = 1;
                break;
            case DIRECTION_DOWN:
                di = 1;
                dj = 0;
                break;
            case DIRECTION_LEFT:
                di = 0;
                dj = -1;
                break;
            default:
                throw new IllegalArgumentException("Illegal direction: " + direction);
        }
        final int newI = i + di;
        final int newJ = j + dj;
        checkBounds(newI, newJ);
        final Cell newCell = new Cell();
        cells[newI][newJ] = newCell;
        return newCell;
    }

    public boolean inBounds(final int i, final int j) {
        return 0 <= i && i < rows && 0 <= j && j < columns;
    }

    private void checkBounds(final int i, final int j) {
        if (!inBounds(i, j)) {
            throw new IndexOutOfBoundsException("Indices out of range: " + i + ", " + j);
        }
    }

    public boolean hasCellAt(final int i, final int j) {
        return cellAt(i, j) != null;
    }

    public boolean hasLink(final int i, final int j, final int direction) {
        final Cell cell = cellAt(i, j);
        return cell != null && cellFor(cell, direction) != null;
    }

    private Cell cellAt(final int i, final int j) {
        checkBounds(i, j);
        return cells[i][j];
    }

    private Cell cellFor(final Cell cell, final int direction) {
        switch (direction) {
            case DIRECTION_UP:
                return cell.upNode;
            case DIRECTION_RIGHT:
                return cell.rightNode;
            case DIRECTION_DOWN:
                return cell.downNode;
            case DIRECTION_LEFT:
                return cell.leftNode;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    private class Cell {

        private Cell upNode;
        private Cell rightNode;
        private Cell downNode;
        private Cell leftNode;

    }

}
