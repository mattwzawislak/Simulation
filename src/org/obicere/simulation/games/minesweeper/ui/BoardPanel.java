package org.obicere.simulation.games.minesweeper.ui;

import org.obicere.simulation.games.minesweeper.game.Board;
import org.obicere.simulation.games.minesweeper.game.data.Flag;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class BoardPanel extends JPanel {


    private static final int SQUARE_SIZE = 25;

    private static final Color FILL_SELECT_COLOR = new Color(0xD0D0D0);
    private static final Font  FLAG_FONT         = new Font("Segoe UI Symbol", Font.BOLD, SQUARE_SIZE);

    private volatile Board board;

    private volatile boolean gameOver;

    public BoardPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                if (board == null || gameOver) {
                    return;
                }
                final int x = e.getX();
                final int y = e.getY();
                final int i = y / SQUARE_SIZE;
                final int j = x / SQUARE_SIZE;
                if (i < 0 || board.getHeight() <= i || j < 0 || board.getWidth() <= j) {
                    return;
                }

                final boolean left;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    left = true;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    left = false;
                } else {
                    return;
                }
                gameOver = board.clicked(i, j, left);
                repaint();
            }
        });
    }

    public void newGame(final int width, final int height, final int bombCount) {
        board = new Board(width, height, bombCount);
        gameOver = false;
    }

    @Override
    public Dimension getPreferredSize() {
        if (board == null) {
            return new Dimension(0, 0);
        } else {
            return new Dimension(board.getWidth() * SQUARE_SIZE, board.getHeight() * SQUARE_SIZE);
        }
    }

    @Override
    protected void paintComponent(final Graphics g1) {
        if (board == null) {
            return;
        }

        super.paintComponent(g1);
        final Graphics2D g = (Graphics2D) g1;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(FLAG_FONT);

        final FontMetrics metrics = g.getFontMetrics();

        final int width = board.getWidth();
        final int height = board.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                drawSquare(g, i, j, metrics);
            }
        }

        if (gameOver) {
            gameOver(g, metrics);
        }
    }

    private void gameOver(final Graphics g, final FontMetrics metrics) {
        final String message = "Game over";

        final int boxWidth = 150;
        final int boxHeight = 50;

        final int boxX = (getWidth() - boxWidth) / 2;
        final int boxY = (getHeight() - boxWidth) / 2;

        final int width = metrics.stringWidth(message);
        final int height = metrics.getHeight();
        final int padX = (boxWidth - width) / 2;
        final int padY = (boxHeight - height) / 2;

        g.setColor(Color.GRAY);
        g.fillRect(boxX, boxY, boxWidth, boxHeight);
        g.setColor(Color.WHITE);
        g.drawString(message, boxX + padX, boxY + boxHeight - padY * 2);
    }

    private void drawSquare(final Graphics g, final int i, final int j, final FontMetrics metrics) {
        g.setColor(Color.GRAY);
        final Flag flag = board.getData().getFlag(i, j);
        final int x = j * SQUARE_SIZE;
        final int y = i * SQUARE_SIZE;
        g.draw3DRect(x, y, SQUARE_SIZE - 1, SQUARE_SIZE - 1, true);

        if (flag == Flag.NONE) {
            return;
        }
        if (flag == Flag.ZERO) {
            g.setColor(FILL_SELECT_COLOR);
            g.fillRect(x, y, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
            return;
        }
        final char representation = flag.getRepresentation();
        final int width = metrics.charWidth(representation);

        final int padX = (SQUARE_SIZE - width) / 2;

        g.setColor(flag.getColor());
        g.drawString(String.valueOf(representation), x + padX, y + SQUARE_SIZE - 3);

    }

}
