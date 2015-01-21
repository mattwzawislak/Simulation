package org.obicere.simulation.ai.othello.gui;

import org.obicere.simulation.ai.othello.game.Board;
import org.obicere.simulation.ai.othello.game.Game;
import org.obicere.simulation.ai.othello.game.Player;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;

/**
 * @author Obicere
 * @version 1.0
 */
public class BoardPanel extends JPanel {

    private final Game game;

    public BoardPanel(final Game game) {
        this.game = game;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        final int size = getWidth(); // equal to getHeight()
        final int squareSize = size / 8;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final Board board = game.getCurrentBoard();

        long played = board.getPlayed();
        long pieces = board.getPieces();

        for (int i = 0; i < 64; i++) {
            if ((played & 1) != 0) {
                drawSquare(g2, i, (int) (pieces & 1), squareSize);
            }
            played >>= 1;
            pieces >>= 1;
        }

        for (int i = 0; i < size; i += squareSize) {
            g2.drawLine(i, 0, i, size);
            g2.drawLine(0, i, size, i);
        }

        if (game.isGameOver()) {
            drawGameOverScreen(g2);
        }
    }

    private void drawSquare(final Graphics2D g, final int index, final int player, final int squareSize) {
        final int i = index / 8;
        final int j = index % 8;

        final int x = j * squareSize;
        final int y = i * squareSize;
        g.setColor(player == 0 ? Color.BLACK : Color.WHITE);
        g.fillOval(x + 5, y + 5, squareSize - 10, squareSize - 10);
        g.setColor(Color.GRAY);
        g.drawOval(x + 5, y + 5, squareSize - 10, squareSize - 10);
    }

    private void drawGameOverScreen(final Graphics2D g) {
        final int width = 200;
        final int height = 120;

        final int bufferX = (getWidth() - width) / 2;
        final int bufferY = (getHeight() - height) / 2;

        final int delta = game.getCurrentBoard().getScoreDifference();

        final String winner;
        if (delta > 0) {
            winner = "Player White wins!";
        } else if (delta < 0) {
            winner = "Player Black wins!";
        } else {
            winner = "Draw";
        }

        final FontMetrics context = g.getFontMetrics();
        final int stringWidth = context.stringWidth(winner);

        g.setColor(Color.GRAY);
        g.fillRect(bufferX, bufferY, width, height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(bufferX, bufferY, width, height);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(winner, (getWidth() - stringWidth) / 2, getHeight() / 2);
    }

}
