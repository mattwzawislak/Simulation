package org.obicere.simulation.ai.othello.game;

import org.obicere.simulation.ai.othello.strategy.Strategy;

/**
 * @author Obicere
 * @version 1.0
 */
public class Game implements Runnable {

    private static final long MOVE_DELAY = 0;

    private static final long START_PLAYED = 0b00000000_00000000_00000000_00011000_00011000_00000000_00000000_00000000L;
    private static final long START_PIECES = 0b00000000_00000000_00000000_00010000_00001000_00000000_00000000_00000000L;

    private volatile Board currentBoard;

    private volatile boolean gameOver = true;

    private final Strategy whiteStrategy;
    private final Strategy blackStrategy;

    public Game(final Strategy whiteStrategy, final Strategy blackStrategy) {
        this.whiteStrategy = whiteStrategy;
        this.blackStrategy = blackStrategy;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void start(){
        this.gameOver = false;
        this.currentBoard = new Board(START_PLAYED, START_PIECES);

        final Thread gameThread = new Thread(this);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Override
    public void run() {
        Player current = Player.WHITE;
        boolean noMovesLeft = false;
        while (true) {
            final Move[] moves = currentBoard.getMovesFor(current);
            if (moves == null || moves.length == 0) {
                if (noMovesLeft) {
                    break;
                }
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
