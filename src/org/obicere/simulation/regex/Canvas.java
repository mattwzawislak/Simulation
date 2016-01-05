package org.obicere.simulation.regex;

import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
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
        interrupt();

        this.graph = new Graph(size, regex);
        this.worker = new Thread(graph::apply);

        worker.start();

        new ConditionalTimer(50, new TimerTask() {
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

        final int minimum = Math.min(getWidth(), getHeight());

        g.drawImage(graph.getImage(), 0, 0, minimum, minimum, this);
    }
}
