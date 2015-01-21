package org.obicere.simulation.ai.othello.game;

import org.obicere.simulation.ai.othello.strategy.Strategy;

/**
 * @author Obicere
 * @version 1.0
 */
public class Game implements Runnable {

    private static final long MOVE_DELAY = 5000;

    private static final long START_PLAYED = 0b00000000_00000000_00000000_00011000_00011000_00000000_00000000_00000000L;
    private static final long START_PIECES = 0b00000000_00000000_00000000_00010000_00001000_00000000_00000000_00000000L;

    private volatile Board currentBoard;

    private volatile boolean gameOver;

    private final Strategy whiteStrategy;
    private final Strategy blackStrategy;

    public Game(final Strategy whiteStrategy, final Strategy blackStrategy) {
        this.currentBoard = new Board(START_PLAYED, START_PIECES);
        this.whiteStrategy = whiteStrategy;
        this.blackStrategy = blackStrategy;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    @Override
    public void run() {
        Player current = Player.WHITE;
        boolean noMovesLeft = false;
        while (true) {
            //System.out.printf("Player %s's turn%n", current);
            final Move[] moves = currentBoard.getMovesFor(current);
            if (moves == null || moves.length == 0) {
                if (noMovesLeft) {
                    //System.out.println("No moves remain for either player. Game ending.");
                    break;
                }
                //System.out.println("No moves for this player. Next player's turn.");
                noMovesLeft = true;

                if (current == Player.BLACK) {
                    current = Player.WHITE;
                } else {
                    current = Player.BLACK;
                }
                continue;
            }
            //System.out.println("Calculating move");
            final Move move;
            if (current == Player.BLACK) {
                move = blackStrategy.getNextMove(currentBoard, Player.BLACK);

                current = Player.WHITE;
            } else {
                move = whiteStrategy.getNextMove(currentBoard, Player.WHITE);

                current = Player.BLACK;
            }
            //System.out.printf("Playing move %s%n", move);

            currentBoard = currentBoard.playMove(move);
            try {
                Thread.sleep(MOVE_DELAY);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
