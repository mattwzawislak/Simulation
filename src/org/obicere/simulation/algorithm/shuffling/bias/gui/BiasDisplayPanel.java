package org.obicere.simulation.algorithm.shuffling.bias.gui;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ResultData;
import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ShufflingAlgorithm;
import org.obicere.simulation.algorithm.shuffling.bias.shuffling.ShufflingProcess;
import org.obicere.utility.util.ConditionalTimer;

import javax.swing.*;
import java.awt.*;

/**
 * @author Obicere
 */
public class BiasDisplayPanel extends JPanel {

    private static final int PADDING             = 2;
    private static final int DEFAULT_SQUARE_SIZE = 5;

    private int              size;
    private ShufflingProcess process;

    public void calculate(final ShufflingAlgorithm algorithm, final int size, final int count) {
        this.size = size;
        this.process = new ShufflingProcess(algorithm, size, count);

        new Thread(process::startTesting).start();
        new ConditionalTimer(10, e -> repaint(), process::isCalculating).start();
    }

    private int pixelsFor(final int index) {
        return PADDING * (index + 1) + DEFAULT_SQUARE_SIZE * index;
    }

    private float adjustedPixelsFor(final int index, final int padding, final float squareSize) {
        return padding * (index + 1) + squareSize * index;
    }

    @Override
    public void paintComponent(final Graphics g1) {
        super.paintComponent(g1);
        if (size <= 0 || process == null || process.getTestsDone() == 0) {
            return;
        }
        final Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        final int rawSize = pixelsFor(size);

        final int availableWidth = getWidth();
        final int availableHeight = getHeight();
        final int limitingSize = Math.min(availableWidth, availableHeight);

        final int padding = limitingSize < rawSize ? 0 : PADDING;
        final float squareSize = limitingSize / (float) size - padding;
        final float newSize = adjustedPixelsFor(size, padding, squareSize);
        final int paddingWidth = (int) (availableWidth - newSize) / 2;
        final int paddingHeight = (int) (availableHeight - newSize) / 2;

        final ResultData data = process.getData();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final float x = paddingWidth + adjustedPixelsFor(j, padding, squareSize);
                final float y = paddingHeight + adjustedPixelsFor(i, padding, squareSize);

                final int intX = (int) x;
                final int intY = (int) y;

                g.setColor(data.getColorFor(i, j));
                if (padding > 0) {
                    final int width = (int) (squareSize + x - intX);
                    final int height = (int) (squareSize + y - intY);
                    g.fillRect(intX, intY, width, height);
                    g.setColor(Color.GRAY);
                    g.drawRect(intX, intY, width, height);
                } else {
                    final int width = Math.round(squareSize + x - intX);
                    final int height = Math.round(squareSize + y - intY);
                    g.fillRect(intX, intY, width, height);
                }
            }
        }
    }

}
