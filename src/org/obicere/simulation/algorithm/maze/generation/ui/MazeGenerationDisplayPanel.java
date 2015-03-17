package org.obicere.simulation.algorithm.maze.generation.ui;

import org.obicere.simulation.algorithm.maze.generation.algorithm.MazeGenerationAlgorithm;
import org.obicere.simulation.algorithm.maze.generation.util.MazeGenerator;
import org.obicere.simulation.algorithm.maze.generation.util.MazeGraph;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 */
public class MazeGenerationDisplayPanel extends JPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(500, 500);

    private static final Color FOREGROUND_COLOR = new Color(0x101010);
    private static final Color BACKGROUND_COLOR = new Color(0xEEEEEE);

    private final MazeGenerator generator;

    public MazeGenerationDisplayPanel(final MazeGenerator generator) {
        this.generator = generator;

        setBackground(BACKGROUND_COLOR);
        setForeground(FOREGROUND_COLOR);
    }

    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final MazeGraph graph = generator.getWorkingGraph();
        if (graph == null) {
            return;
        }
        final int rows = graph.getRowCount();
        final int columns = graph.getColumnCount();
        final float size = getSquareSize(rows, columns);

        g.drawRect(0, 0, (int) (columns * size * 2 - size), (int) (rows * size * 2 - size));

        synchronized (graph.getRenderLock()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (!graph.hasCellAt(i, j)) {
                        continue;
                    }
                    final float x = indexToPixel(size, j);
                    final float y = indexToPixel(size, i);
                    g.fillRect(Math.round(x), Math.round(y), (int) size + 1, (int) size + 1);
                    for (final int direction : MazeGraph.getDirections()) {
                        if (graph.hasLink(i, j, direction)) {
                            drawLink(g, direction, x, y, size);
                        }
                    }
                }
            }
            final MazeGenerationAlgorithm algorithm = generator.getWorkingAlgorithm();
            if (algorithm != null) {
                algorithm.paintHelpers(g, size);
            }
        }
    }

    private void drawLink(final Graphics g, final int direction, final float x, final float y, final float size) {
        final float linkX;
        final float linkY;
        switch (direction) {
            case MazeGraph.DIRECTION_UP:
                linkX = x;
                linkY = y - size;
                break;
            case MazeGraph.DIRECTION_RIGHT:
                linkX = x + size;
                linkY = y;
                break;
            case MazeGraph.DIRECTION_DOWN:
                linkX = x;
                linkY = y + size;
                break;
            case MazeGraph.DIRECTION_LEFT:
                linkX = x - size;
                linkY = y;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction " + direction);
        }
        g.fillRect(Math.round(linkX), Math.round(linkY), (int) size + 1, (int) size + 1);
    }

    private float indexToPixel(final float size, final int index) {
        if (index == 0) {
            return 0;
        }
        return size * (index << 1);
    }

    private float getSquareSize(final int rows, final int columns) {
        synchronized (getTreeLock()) {
            final float c = (columns << 1) - 1;
            final float r = (rows << 1) - 1;
            return Math.min(getWidth() / c, getHeight() / r);
        }
    }

}
