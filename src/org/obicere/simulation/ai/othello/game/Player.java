package org.obicere.simulation.ai.othello.game;

/**
 * @author Obicere
 * @version 1.0
 */
public enum Player {

    BLACK(0),
    WHITE(1);

    private final int id;

    private Player(final int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public Player other() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

}
