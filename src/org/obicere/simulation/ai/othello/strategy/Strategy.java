package org.obicere.simulation.ai.othello.strategy;

import org.obicere.simulation.ai.othello.game.Board;
import org.obicere.simulation.ai.othello.game.Move;
import org.obicere.simulation.ai.othello.game.Player;

/**
 * @author Obicere
 * @version 1.0
 */
public abstract class Strategy {

    public abstract String name();

    public abstract Move getNextMove(final Board board, final Player player);

    @Override
    public String toString(){
        return name();
    }

}
