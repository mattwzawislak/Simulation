package org.obicere.simulation.ai.othello.strategy;

import org.obicere.simulation.ai.othello.game.Board;
import org.obicere.simulation.ai.othello.game.Move;
import org.obicere.simulation.ai.othello.game.Player;

import java.util.Random;

/**
 * @author Obicere
 * @version 1.0
 */
public class RandomStrategy implements Strategy {

    private static final String NAME = "Random";

    private final Random random = new Random();

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Move getNextMove(final Board board, final Player player) {
        final Move[] moves = board.getMovesFor(player);
        return moves[random.nextInt(moves.length)];
    }
}
