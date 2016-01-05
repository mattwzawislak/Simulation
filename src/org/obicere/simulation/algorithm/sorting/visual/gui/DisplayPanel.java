package org.obicere.simulation.algorithm.sorting.visual.gui;

import org.obicere.simulation.algorithm.sorting.visual.algorithms.Algorithm;
import org.obicere.simulation.algorithm.sorting.visual.algorithms.SortingProcess;
import org.obicere.simulation.algorithm.sorting.visual.array.Counter;
import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;
import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.TimerTask;

/**
 * @author Obicere
 */
public class DisplayPanel extends JPanel {

    private static final String FORMAT = "%s - %d comparisons - %d array accesses";

    private int length;

    private SortingProcess process;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 500);
    }

    public void startAlgorithm(final Algorithm algorithm, final int size, final int delay) {

        if (process != null) {
            process.halt();
        }
        this.length = size;

        this.process = new SortingProcess(algorithm, size);
        process.sort(delay);
        new ConditionalTimer(15, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, () -> process.isCalculating() && isVisible());

    }

    public SortingProcess getProcess() {
        return process;
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (process != null) {
            super.paintComponent(g);
            final SortArray array = process.getArray();
            final Algorithm algorithm = process.getAlgorithm();

            final float width = getWidth();
            final float height = getHeight() - 20;

            final float barWidth = width / length;
            final float barHeight = (height - 20) / length;

            for (int i = 0; i < length; i++) {
                final int value = array.getValue(i);
                final float x = barWidth * i;
                final float y = barHeight * value;

                drawBar(g, x, height - y, barWidth, y, array.getMarker().colorFor(i));
            }
            g.setColor(Color.BLACK);
            final Counter counter = array.getCounter();
            final String format = String.format(FORMAT, algorithm.toString(), counter.getCompares(), counter.getAccesses());
            g.drawString(format, 3, 15);
        }
    }

    private void drawBar(final Graphics g, final float x, final float y, final float width, final float height, final Color color) {
        g.setColor(color);
        final int newX = (int) x;
        final int newY = (int) y;
        final int newWidth = (int) Math.ceil(width);
        final int newHeight = (int) Math.ceil(height);
        g.fillRect(newX, newY, newWidth, newHeight);
        if (newWidth >= 2) {
            g.setColor(Color.WHITE);
            g.drawLine(newX, newY, newX, newHeight + newY);
        }
    }

}
