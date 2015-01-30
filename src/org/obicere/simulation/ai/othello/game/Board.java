package org.obicere.simulation.ai.othello.game;

import java.util.ArrayList;

/**
 * @author Obicere
 * @version 1.0
 */
public class Board {

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

    private volatile long played;
    private volatile long pieces;

    public Board(final long played, final long pieces) {
        this.played = played;
        this.pieces = pieces;
    }

    public Board(final Board board) {
        this.played = board.played;
        this.pieces = board.pieces;
    }

    public int getScoreDifference() {
        int delta = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final Player piece = pieceAt(i, j);
                if (piece == Player.BLACK) {
                    delta--;
                } else if (piece == Player.WHITE) {
                    delta++;
                }
            }
        }
        return delta;
    }

    public Board playMove(final Move move) {
        final int i = move.getI();
        final int j = move.getJ();
        final Player player = move.getPlayer();
        int o = move.getOrientation();
        int k = 0;
        Board newBoard = new Board(this);
        while (o != 0) {
            if ((o & 1) != 0) {
                final int di = DELTA[k][0];
                final int dj = DELTA[k][1];
                newBoard = captureTowards(newBoard, i, j, di, dj, player);
            }
            o >>= 1;
            k++;
        }
        return newBoard;
    }

    private Board captureTowards(final Board board, final int i, final int j, final int di, final int dj, final Player player) {
        int ci = i;
        int cj = j;
        int capture = 0;
        while (0 <= ci && ci < 8 && 0 <= cj && cj < 8) {

            // We have to ensure that at least a piece was captured since if there is a capture that
            // is related to this piece already, the initial position will have the player piece
            // already present. To counter this, we assume that there is a capture available.
            final Player piece = pieceAt(ci, cj);
            if (piece == player && capture > 0) {
                return board;
            }

            capture++;
            capture(board, ci, cj, player);
            ci += di;
            cj += dj;
        }
        return board;
    }

    private void capture(final Board board, final int i, final int j, final Player player) {
        final int shift = shiftForCoordinate(i, j);
        board.played = set(board.played, shift);
        if (player == Player.BLACK) {
            board.pieces = clear(board.pieces, shift);
        } else {
            board.pieces = set(board.pieces, shift);
        }
    }

    public Player pieceAt(final int i, final int j) {
        final int shift = shiftForCoordinate(i, j);
        final long flag = get(played, shift);
        if (flag == 0) {
            return null;
        }
        final long value = get(pieces, shift);
        return value == 0 ? Player.BLACK : Player.WHITE;
    }

    private int shiftForCoordinate(final int i, final int j) {
        return (i * 8) + j;
    }

    private long set(final long memory, final int shift) {
        return memory | (1L << shift);
    }

    private long clear(final long memory, final int shift) {
        return memory & ~(1L << shift);
    }

    private long get(final long memory, final int shift) {
        return memory & (1L << shift);
    }

    public long getPlayed() {
        return played;
    }

    public long getPieces() {
        return pieces;
    }

    public Move[] getMovesFor(final Player player) {
        final ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final Player piece = pieceAt(i, j);
                if (piece == null) {
                    continue;
                }
                if (piece == player) {
                    exhaustMoves(moves, i, j, player);
                }
            }
        }
        return moves.toArray(new Move[moves.size()]);
    }

    private void exhaustMoves(final ArrayList<Move> moveSet, final int i, final int j, final Player player) {
        int o = 1 << 4;
        for (final int[] delta : DELTA) {
            final Move move = getMove(i, j, delta[0], delta[1], player);
            if (move != null) {
                move.setOrientation(o);
                final int index = moveSet.indexOf(move);
                if (index >= 0) {
                    final Move other = moveSet.get(index);
                    final Move joint = other.join(move);
                    moveSet.remove(index);
                    moveSet.add(joint);
                } else {
                    moveSet.add(move);
                }
            }
            o <<= 1;
            if (o > (128)) {
                o = 1;
            }
        }
    }

    private Move getMove(final int i, final int j, final int di, final int dj, final Player player) {
        int ci = i + di;
        int cj = j + dj;
        int capture = 0;
        while (0 <= ci && ci < 8 && 0 <= cj && cj < 8) {
            final Player piece = pieceAt(ci, cj);
            if (piece == null) {
                if (capture == 0) {
                    return null;
                }
                return new Move(ci, cj, player);
            }
            if (piece == player) {
                return null;
            }
            capture++;
            ci += di;
            cj += dj;
        }
        return null;
    }

}
