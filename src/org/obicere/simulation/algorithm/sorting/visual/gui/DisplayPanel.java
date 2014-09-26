package org.obicere.simulation.algorithm.sorting.visual.gui;

import org.obicere.simulation.algorithm.sorting.visual.algorithms.Algorithm;
import org.obicere.simulation.algorithm.sorting.visual.array.Counter;
import org.obicere.simulation.algorithm.sorting.visual.array.SortArray;
import org.obicere.utility.util.ConditionalTimer;

import javax.swing.*;
import java.awt.*;

/**
 * @author Obicere
 */
public class DisplayPanel extends JPanel {

    private static final String FORMAT = "%s - %d comparisons - %d array accesses.";

    private SortArray array;
    private int       length;
    private Algorithm algorithm;
    private float xOverflow = 0;
    private float yOverflow = 0;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 500);
    }

    public void startAlgorithm(final Algorithm algorithm, final int size, final int delay) {

        this.array = new SortArray(size);
        this.length = size;
        this.algorithm = algorithm;

        new Thread(() -> algorithm.sort(array, delay)).start();
        new ConditionalTimer(15, e -> repaint(), algorithm::isComputing).start();

    }

    @Override
    public void paintComponent(final Graphics g) {
        if (array != null) {
            super.paintComponent(g);
            xOverflow = 0;
            yOverflow = 0;
            final float width = getWidth();
            final float height = getHeight();

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, (int) width, (int) height);

            final float barWidth = width / length;
            final float barHeight = height / length;

            for (int i = 0; i < length; i++) {
                final int value = array.getSilent(i);
                final float x = barWidth * i;
                final float y = barHeight * value;

                drawBar(g, x, height - y, barWidth, y, array.getMarker().colorFor(i));
            }
            g.setColor(Color.WHITE);
            final Counter counter = array.getCounter();
            final String format = String.format(FORMAT, algorithm.toString(), counter.getCompares(), counter.getAccesses());
            g.drawString(format, 3, 20);
        }
    }

    private void drawBar(final Graphics g, final float x, final float y, final float width, final float height, final Color color) {
        g.setColor(color);
        if(width < 1){
            xOverflow += width;
            xOverflow %= 1;
        }
        if(height < 1){
            yOverflow += height;
            yOverflow %= 1;
        }
        final int newX = (int) x;
        final int newY = (int) y;
        final int newWidth = (int) (width + xOverflow);
        final int newHeight = (int) (height + yOverflow) + 1; // Lets avoid random fragments along the bottom
        g.fillRect(newX, newY, newWidth, newHeight);
        if (width > 2) {
            g.setColor(Color.BLACK);
            g.drawRect(newX, newY, newWidth, newHeight);
        }
    }

}
