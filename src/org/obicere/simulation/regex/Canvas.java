package org.obicere.simulation.regex;

import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Objects;
import java.util.TimerTask;

/**
 * @author Obicere
 */
public class Canvas extends JPanel {

    private Graph graph;

    public void applyRegex(final int size, final String regex) {
        Objects.requireNonNull(regex);
        graph = null;
        System.gc();
        this.graph = new Graph(size, regex);
        new Thread(graph::apply).start();
        new ConditionalTimer(15, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, graph::isCalculating);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (graph == null) {
            return;
        }
        final Image draw = graph.getImage();

        final int width = getWidth();
        final int height = getHeight();

        final int letterBoxSize = Math.min(width, height);
        final int paddingWidth = (width - letterBoxSize) / 2;
        final int paddingHeight = (height - letterBoxSize) / 2;

        final Image scaledDraw = draw.getScaledInstance(letterBoxSize, letterBoxSize, Image.SCALE_FAST);

        g.drawImage(scaledDraw, paddingWidth, paddingHeight, letterBoxSize, letterBoxSize, this);
    }

}
