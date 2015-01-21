package org.obicere.simulation.ai.othello.game;

/**
 * @author Obicere
 * @version 1.0
 */
public class Move {

    private final int i;
    private final int j;

    private int orientation;

    private final Player player;

    public Move(final int i, final int j, final Player player) {
        this.i = i;
        this.j = j;
        this.player = player;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Player getPlayer() {
        return player;
    }

    public void setOrientation(final int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public Move join(final Move move) {
        if (moveEquals(move)) {
            final Move union = new Move(i, j, player);
            union.setOrientation(orientation | move.getOrientation());
            return union;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Move[" + i + ", " + j + ", " + Integer.toBinaryString(orientation) + "]";
    }

    @Override
    public int hashCode() {
        return (player.getID() << 10) | (i << 4) | (j << 0);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Move && moveEquals((Move) obj);
    }

    private boolean moveEquals(final Move move) {
        return move.i == i && move.j == j && move.player == player;
    }

}
