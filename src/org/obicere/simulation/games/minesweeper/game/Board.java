package org.obicere.simulation.games.minesweeper.game;

import org.obicere.simulation.games.minesweeper.game.data.BoardData;
import org.obicere.simulation.games.minesweeper.game.data.Flag;

/**
 */
public class Board {

    private final int width;
    private final int height;

    private final BoardData data;

    private volatile boolean initialized = false;

    private volatile int flagsRemaining;

    public Board(final int width, final int height, final int bombCount) {
        this.width = width;
        this.height = height;

        this.flagsRemaining = bombCount;

        this.data = new BoardData(System.currentTimeMillis(), width, height, bombCount);
    }

    public Board(final long seed, final int width, final int height, final int bombCount) {
        this.width = width;
        this.height = height;

        this.data = new BoardData(seed, width, height, bombCount);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BoardData getData() {
        return data;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public synchronized void initializeBombs(final int i, final int j) {
        if (initialized) {
            return;
        }
        data.generateBombs(i, j);
        this.initialized = true;
    }

    public boolean clicked(final int i, final int j, final boolean left) {
        if (!initialized) {
            initializeBombs(i, j);
        }
        final Flag current = data.getFlag(i, j);
        if (current == null) {
            return true;
        }
        if (left) {
            // If we can uncover the next square, check to see if it is a bomb.
            return current == Flag.NONE && data.uncover(i, j);
        } else {
            // Cycle through the flags [NONE, FLAG, MAYBE]
            final Flag next = nextFlag(current);
            if (next != null) {
                data.setFlag(next, i, j);
            }
            return false;
        }
    }

    private Flag nextFlag(final Flag flag) {
        switch (flag) {
            case NONE:
                flagsRemaining--; // We are about to place a flag
                return Flag.FLAG;
            case FLAG:
                flagsRemaining++; // We are about to place a maybe
                return Flag.MAYBE;
            case MAYBE:
                return Flag.NONE;
            default:
                return null;
        }
    }

}
