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

    private volatile Thread worker;

    public void applyRegex(final int size, final String regex) {
        Objects.requireNonNull(regex);
        graph = null;
        System.gc();
        this.graph = new Graph(size, regex);
        this.worker = new Thread(graph::apply);

        worker.start();
        new ConditionalTimer(15, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, () -> graph != null && graph.isCalculating() && isVisible());
    }

    public void interrupt() {
        if (worker != null) {
            worker.interrupt();
        }
        graph = null;
        worker = null;
        System.gc();
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

        if (draw == null) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();

        final int letterBoxSize = Math.min(width, height);
        final int paddingWidth = (width - letterBoxSize) / 2;
        final int paddingHeight = (height - letterBoxSize) / 2;

        final Image scaledDraw = draw.getScaledInstance(letterBoxSize, letterBoxSize, Image.SCALE_FAST);

        g.drawImage(scaledDraw, paddingWidth, paddingHeight, letterBoxSize, letterBoxSize, this);
        g.dispose();
    }

}
