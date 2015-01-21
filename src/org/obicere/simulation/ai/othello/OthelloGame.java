package org.obicere.simulation.ai.othello;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.ai.othello.game.Game;
import org.obicere.simulation.ai.othello.gui.OthelloFrameManager;
import org.obicere.simulation.ai.othello.strategy.RandomStrategy;

import javax.swing.SwingUtilities;

/**
 * @author Obicere
 * @version 1.0
 */
public class OthelloGame extends Simulation {

    private static final String NAME = "Othello";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        final Game game = new Game(new RandomStrategy(), new RandomStrategy());
        SwingUtilities.invokeLater(() -> new OthelloFrameManager(game));

        final Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();
    }
}
