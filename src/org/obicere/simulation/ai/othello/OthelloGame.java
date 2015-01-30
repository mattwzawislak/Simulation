package org.obicere.simulation.ai.othello;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.ai.othello.game.Game;
import org.obicere.simulation.ai.othello.gui.OthelloFrameManager;
import org.obicere.simulation.ai.othello.strategy.RandomStrategy;
import org.obicere.simulation.ai.othello.strategy.Strategy;
import org.obicere.simulation.ai.othello.strategy.WeightedStrategy;

import javax.swing.SwingUtilities;
import java.util.Scanner;

/**
 * @author Obicere
 * @version 1.0
 */
public class OthelloGame extends Simulation {

    private static final String NAME = "Othello";

    private static final Strategy[] STRATEGIES = new Strategy[]{
            new RandomStrategy(),
            new WeightedStrategy()
    };

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        final Scanner input = new Scanner(System.in);

        System.out.print("Select white's strategy: ");
        final String whiteStrategyName = input.nextLine().trim();
        System.out.print("Select black's strategy: ");
        final String blackStrategyName = input.nextLine().trim();

        final Game game = new Game(strategyForName(whiteStrategyName), strategyForName(blackStrategyName));
        SwingUtilities.invokeLater(() -> new OthelloFrameManager(game));
    }

    private Strategy strategyForName(final String name) {
        for(final Strategy strategy : STRATEGIES){
            if(strategy.name().equalsIgnoreCase(name)){
                return strategy;
            }
        }
        return null;
    }
}
