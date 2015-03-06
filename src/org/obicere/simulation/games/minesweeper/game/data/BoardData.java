package org.obicere.simulation.games.minesweeper.game.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 */
public class BoardData {

    private static final int[][] DELTA = new int[][]{
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0},
            {1, 1},
            {0, 1},
            {-1, 1},
            {-1, 0}
    };

    private final long seed;

    private final int bombCount;

    private final int width;
    private final int height;

    private final boolean[][] bombs;

    private final Flag[][] flags;

    private volatile int flagsRemaining;

    public BoardData(final long seed, final int width, final int height, final int bombCount) {
        this.seed = seed;
        this.bombCount = bombCount;
        this.width = width;
        this.height = height;
        this.bombs = new boolean[height][width];
        this.flags = new Flag[height][width];
        this.flagsRemaining = bombCount;

        generateFlags(flags);
    }

    public int getFlagsRemaining() {
        return flagsRemaining;
    }

    public Flag getFlag(final int i, final int j) {
        if (i < 0 || height <= i || j < 0 || width <= j) {
            return null;
        }
        return flags[i][j];
    }

    public void setFlag(final Flag flag, final int i, final int j) {
        flags[i][j] = flag;
    }

    public boolean uncover(final int i, final int j) {
        if (bombs[i][j]) {
            setFlag(Flag.BOMB, i, j);
            return true;
        }
        // We have a NONE here, guaranteed
        flood(i, j);
        return false;
    }


    private boolean isBomb(final int i, final int j) {
        if (i < 0 || height <= i || j < 0 || width <= j) {
            return false;
        }
        return bombs[i][j];
    }

    private void flood(final int i, final int j) {
        if (i < 0 || height <= i || j < 0 || width <= j) {
            return;
        }
        int count = 0;
        for (final int[] delta : DELTA) {
            if (isBomb(i + delta[0], j + delta[1])) {
                count++;
            }
        }
        setFlag(Flag.flagForBombCount(count), i, j);
        if (count == 0) {
            // Yay we can flood the surrounding
            for (int d = 0; d < DELTA.length; d++) {
                final int[] delta = DELTA[d];
                final int di = i + delta[0];
                final int dj = j + delta[1];
                if (getFlag(di, dj) == Flag.NONE) {
                    flood(di, dj);
                }
            }
        }
    }

    public void generateBombs(final int firstI, final int firstJ) {
        final int elements = width * height;
        final int clicked = firstI * width + firstJ;

        if (bombCount >= width * height) {
            throw new IllegalArgumentException("Invalid game state, puzzle must be solvable.");
        }
        final ArrayList<Integer> coordinates = new ArrayList<>(elements);
        for (int i = 0; i < elements; i++) {
            coordinates.add(i);
        }

        final Random random = new Random(seed);
        Collections.shuffle(coordinates, random);

        for (int i = 0; i < bombCount; i++) {
            final int value = coordinates.get(i);
            if (value == clicked) {
                setBomb(coordinates.get(elements - 1));
                continue;
            }
            setBomb(value);
        }
    }

    private void setBomb(final int ijValue) {
        bombs[ijValue / width][ijValue % width] = true;
    }

    private void generateFlags(final Flag[][] flags) {
        for (int i = 0; i < height; i++) {
            Arrays.fill(flags[i], Flag.NONE); // Fill each row with no flag set
        }
    }

}
