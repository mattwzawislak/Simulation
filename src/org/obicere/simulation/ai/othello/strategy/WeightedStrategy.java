package org.obicere.simulation.ai.othello.strategy;

import org.obicere.simulation.ai.othello.game.Board;
import org.obicere.simulation.ai.othello.game.Move;
import org.obicere.simulation.ai.othello.game.Player;

/**
 * @author Obicere
 * @version 1.0
 */
public class WeightedStrategy implements Strategy {

    private static final double[][] WEIGHTS = new double[][]{
            {4.0, 1.0, 2.7, 2.0, 2.0, 2.7, 1.0, 4.0},
            {1.0, 1.0, 1.7, 1.5, 1.5, 1.7, 1.0, 1.0},
            {2.7, 1.7, 1.3, 1.1, 1.1, 1.3, 1.7, 2.7},
            {2.0, 1.5, 1.1, 1.0, 1.0, 1.1, 1.5, 2.0},
            {2.0, 1.5, 1.1, 1.0, 1.0, 1.1, 1.5, 2.0},
            {2.7, 1.7, 1.3, 1.1, 1.1, 1.3, 1.7, 2.7},
            {1.0, 1.0, 1.7, 1.5, 1.5, 1.7, 1.0, 1.0},
            {4.0, 1.0, 2.7, 2.0, 2.0, 2.0, 1.0, 4.0}
    };

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

    private static final int DEPTH = 6;

    private static final String NAME = "weighted";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Move getNextMove(final Board board, final Player player) {
        return getNextMove(board, player, 0);
    }

    private Move getNextMove(final Board board, final Player player, final int depth) {
        if (depth >= DEPTH) {
            return null;
        }
        final Move[] moves = board.getMovesFor(player);
        final int length = moves.length;

        if (length == 0) {
            return getNextMove(board, player.other(), depth + 1);
        }
        Move bestMove = moves[0];
        double bestPlay = playMove(board, bestMove);
        if (depth == 0) {
            System.out.printf("%s: [%g]%n", bestMove, bestPlay);
        }
        for (int i = 1; i < length; i++) {
            final Move move = moves[i];
            double play = playMove(board, move);

            final Board nextBoard = board.playMove(move);
            final Move nextMove = getNextMove(nextBoard, player.other(), depth + 1);
            if (nextMove != null) {
                play -= playMove(nextBoard, nextMove);
            }
            if (depth == 0) {
                System.out.printf("%s: [%g]%n", move, play);
            }
            if (play > bestPlay) {
                bestMove = moves[i];
                bestPlay = play;
            }
        }
        if(depth == 0) {
            System.out.printf("playing move: %s [%g]%n", bestMove, bestPlay);
        }
        return bestMove;
    }

    public double playMove(final Board board, final Move move) {
        final int i = move.getI();
        final int j = move.getJ();
        final Player player = move.getPlayer();
        int o = move.getOrientation();
        int k = 0;

        double sum = 0;
        while (o != 0) {
            if ((o & 1) != 0) {
                final int di = DELTA[k][0];
                final int dj = DELTA[k][1];
                sum += captureTowards(board, i, j, di, dj, player);
            }
            o >>= 1;
            k++;
        }
        return sum;
    }

    private double captureTowards(final Board board, final int i, final int j, final int di, final int dj, final Player player) {
        int ci = i;
        int cj = j;
        int capture = 0;
        double value = 0;
        while (0 <= ci && ci < 8 && 0 <= cj && cj < 8) {

            // We have to ensure that at least a piece was captured since if there is a capture that
            // is related to this piece already, the initial position will have the player piece
            // already present. To counter this, we assume that there is a capture available.
            final Player piece = board.pieceAt(ci, cj);
            if (piece == player && capture > 0) {
                return value;
            }

            capture++;
            value += WEIGHTS[ci][cj];
            ci += di;
            cj += dj;
        }
        return value;
    }


}
