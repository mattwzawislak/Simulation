package org.obicere.simulation.regex;

import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TimerTask;

/**
 * @author Obicere
 */
public class Canvas extends JPanel {

    private Graph graph;

    private volatile Thread worker;

    private volatile int i = 0;
    private volatile int j = 0;

    public boolean refresh = false;

    public void applyRegex(final int size, final String regex) {
        Objects.requireNonNull(regex);
        clean();
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

    private void clean() {
        i = 0;
        j = 0;
        graph = null;
        System.gc();
        refresh = true;
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
        if (refresh) {
            super.paintComponent(g);
            refresh = false;
        }
        if (graph == null) {
            return;
        }
        final LinkedList<Boolean> clone;
        synchronized (graph.getRenderLock()) {
            final LinkedList<Boolean> render = graph.getRenderCache();
            clone = new LinkedList<>(render);
            render.clear();
        }
        final int width = getWidth();
        final int height = getHeight();
        final int size = Math.min(width, height);
        final float pixel = size / ((float) graph.getImageSize());

        for (final Boolean next : clone) {
            if (next) {
                final float fi = i * pixel;
                final float fj = j * pixel;
                final int pi = (int) (fi + pixel - (int) fi);
                final int pj = (int) (fj + pixel - (int) fj);
                g.fillRect((int) fi, (int) fj, pi, pj);
            }
            i++;
            if (i == graph.getImageSize()) {
                i = 0;
                j++;
            }
        }

    }

}
