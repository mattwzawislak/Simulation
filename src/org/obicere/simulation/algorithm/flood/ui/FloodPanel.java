package org.obicere.simulation.algorithm.flood.ui;

import org.obicere.simulation.algorithm.flood.calc.FloodData;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class FloodPanel extends JPanel {

    private static final Color OUTLINE_COLOR = new Color(0x282828);

    private static final Color BLOCKED_FILL = new Color(0x505050);
    private static final Color FLAGGED_FILL = new Color(0x0A78F0);

    private static final int SQUARE_SIZE = 20;
    private static final int SQUARE_PAD  = 2;
    private static final int SQUARE_FILL = SQUARE_SIZE - 2 * SQUARE_PAD;

    private static final int DEFAULT_ROWS    = 9;
    private static final int DEFAULT_COLUMNS = 16;

    private final FloodData data = new FloodData();

    private final ColorCache cache = new ColorCache();

    private volatile int lastI;
    private volatile int lastJ;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_COLUMNS * SQUARE_SIZE, DEFAULT_ROWS * SQUARE_SIZE);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        final Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int width = getWidth();
                final int height = getHeight();
                final int newRows = height / SQUARE_SIZE;
                final int newColumns = width / SQUARE_SIZE;

                data.resize(newRows, newColumns);
                lastI = lastI < 0 ? newRows - 1 : Math.min(lastI, data.getRows() - 1);
                lastJ = lastJ < 0 ? newColumns - 1 : Math.min(lastJ, data.getColumns() - 1);
                if (lastI < 0 || lastJ < 0) {
                    return;
                }
                data.calculate(lastI, lastJ);
            }
        });
    }

    @Override
    protected void paintComponent(final Graphics g1) {
        super.paintComponent(g1);
        final Graphics2D g = (Graphics2D) g1;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int rows = data.getRows();
        final int columns = data.getColumns();
        final int workingWidth = columns * SQUARE_SIZE;
        final int workingHeight = rows * SQUARE_SIZE;

        final int[][] flags = data.getFlags();

        g.setColor(OUTLINE_COLOR);
        g.drawRect(0, 0, workingWidth - 1, workingHeight - 1);

        int x = 0;
        int y = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int flag = flags[i][j];
                if (flag < 0) {
                    g.setColor(BLOCKED_FILL);
                    g.fillRect(SQUARE_PAD + x, SQUARE_PAD + y, SQUARE_FILL, SQUARE_FILL);
                    g.setColor(OUTLINE_COLOR);
                    g.drawRect(SQUARE_PAD + x, SQUARE_PAD + y, SQUARE_FILL - 1, SQUARE_FILL - 1);
                } else {
                    g.setColor(cache.get(flag));
                    g.fillRect(SQUARE_PAD + x, SQUARE_PAD + y, SQUARE_FILL, SQUARE_FILL);
                }
                x += SQUARE_SIZE;
            }
            y += SQUARE_SIZE;
            x = 0;
        }
    }

    private class Mouse extends MouseAdapter {

        private volatile boolean blocked = false;

        @Override
        public void mouseEntered(final MouseEvent e) {
            move(e);
        }

        @Override
        public void mouseMoved(final MouseEvent e) {
            move(e);
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            click(e);
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            final int x = e.getX();
            final int y = e.getY();
            final int i = y / SQUARE_SIZE;
            final int j = x / SQUARE_SIZE;
            if (i < 0 || j < 0 || i >= data.getRows() || j >= data.getColumns()) {
                return;
            }
            blocked = data.get(i, j) >= 0;
            data.toggled(i, j, blocked);
        }

        private void move(final MouseEvent e) {
            final int x = e.getX();
            final int y = e.getY();
            final int i = y / SQUARE_SIZE;
            final int j = x / SQUARE_SIZE;
            if (i < 0 || j < 0 || i >= data.getRows() || j >= data.getColumns()) {
                return;
            }
            lastI = i;
            lastJ = j;
            data.calculate(i, j);
            repaint();
        }

        private void click(final MouseEvent e) {
            final int x = e.getX();
            final int y = e.getY();
            final int i = y / SQUARE_SIZE;
            final int j = x / SQUARE_SIZE;
            if (i < 0 || j < 0 || i >= data.getRows() || j >= data.getColumns()) {
                return;
            }
            data.toggled(i, j, blocked);
            repaint();
        }

    }

    private class ColorCache {

        private final int max = 256;

        private Color[] cache = new Color[max];

        public ColorCache() {
            final float r = FLAGGED_FILL.getRed() / 255f;
            final float g = FLAGGED_FILL.getGreen() / 255f;
            final float b = FLAGGED_FILL.getBlue() / 255f;
            for (int i = 0; i < max; i++) {
                cache[i] = new Color(r, g, b, 1f / (i + 1));
            }
        }

        public Color get(final int index) {
            if (index < max) {
                return cache[index];
            } else {
                return cache[max - 1];
            }
        }

    }

}
