package org.obicere.simulation.graphics.clipping.ui;

import org.obicere.utility.geom.Line2F;
import org.obicere.utility.geom.Viewport2F;
import org.obicere.utility.math.Point2F;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Obicere
 */
public class ClippingPanel extends JPanel {

    private static final int PREFERRED_WIDTH  = 500;
    private static final int PREFERRED_HEIGHT = 500;

    private static final Color DEFAULT_FOREGROUND = new Color(0xDD333333, true);
    private static final Color DEFAULT_BACKGROUND = new Color(0xDDDDDD);

    private final Viewport2F viewport = new Viewport2F(5, 5, PREFERRED_WIDTH - 5, PREFERRED_HEIGHT - 5);

    private final List<Line2F> lines = new LinkedList<>();

    private final ReentrantLock linesLock = new ReentrantLock();

    private final Random random = new Random();

    private volatile boolean defaultViewport = true;

    public ClippingPanel() {
        populateLines(250, 50);

        setBackground(DEFAULT_BACKGROUND);
        setForeground(DEFAULT_FOREGROUND);
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        addComponentListener(new ViewportComponentListener());

        final ViewportMouseListener mouse = new ViewportMouseListener();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void populateLines(final int radius, final int count) {
        try {

            final Point2F[] points = new Point2F[count];
            for (int i = 0; i < count; i++) {
                final double radians = Math.PI * 2 * random.nextDouble();

                final float x = (float) (random.nextDouble() * radius * Math.cos(radians));
                final float y = (float) (random.nextDouble() * radius * Math.sin(radians));

                points[i] = new Point2F(x, y);
            }

            final Line2F[] newLines = new Line2F[3 * count];

            int i = 0;
            for(final Point2F focus : points){
                final Comparator<Point2F> sorter = (o1, o2) -> {
                    if(focus.equals(o1)){
                        return 1;
                    }
                    if(focus.equals(o2)){
                        return -1;
                    }
                    return Double.compare(focus.distance(o1), focus.distance(o2));
                };

                Arrays.sort(points, sorter);

                newLines[i++] = new Line2F(focus, points[0]);
                newLines[i++] = new Line2F(focus, points[1]);
                newLines[i++] = new Line2F(focus, points[2]);
            }

            linesLock.lock();
            lines.clear();
            Collections.addAll(lines, newLines);
        } finally {
            linesLock.unlock();
        }
    }

    @Override
    protected void paintComponent(final Graphics g){
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

        drawViewport(g2);

        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;

        final Point2F origin = new Point2F(centerX, centerY);

        linesLock.lock();
        for(final Line2F line : lines){
            drawLine(g2, line, origin);
        }
        linesLock.unlock();
    }

    private void drawViewport(final Graphics g){
        final int minX = viewport.getMinX();
        final int minY = viewport.getMinY();
        final int maxX = viewport.getMaxX();
        final int maxY = viewport.getMaxY();

        g.drawLine(minX, minY, minX, maxY);
        g.drawLine(minX, maxY, maxX, maxY);
        g.drawLine(maxX, maxY, maxX, minY);
        g.drawLine(maxX, minY, minX, minY);
    }

    private void drawLine(final Graphics g, final Line2F line, final Point2F origin){
        final Point2F a = new Point2F(line.getA()).add(origin);
        final Point2F b = new Point2F(line.getB()).add(origin);

        if(viewport.clip(a, b)){
            final int ax = (int) a.getX();
            final int ay = (int) a.getY();
            final int bx = (int) b.getX();
            final int by = (int) b.getY();

            g.drawLine(ax, ay, bx, by);
        }
    }

    private class ViewportComponentListener implements ComponentListener {

        @Override
        public void componentResized(final ComponentEvent e) {
            if(defaultViewport){
                viewport.setMaxX(getWidth() - 5);
                viewport.setMaxY(getHeight() - 5);
            }
        }

        @Override
        public void componentMoved(final ComponentEvent e) {

        }

        @Override
        public void componentShown(final ComponentEvent e) {

        }

        @Override
        public void componentHidden(final ComponentEvent e) {

        }
    }

    private class ViewportMouseListener extends MouseAdapter {

        private volatile int pressX;
        private volatile int pressY;

        @Override
        public void mousePressed(final MouseEvent e){
            defaultViewport = false;

            pressX = e.getX();
            pressY = e.getY();

            viewport.setBounds(pressX, pressY, pressX, pressY);
            repaint();
        }

        @Override
        public void mouseDragged(final MouseEvent e){
            final int dragX = e.getX();
            final int dragY = e.getY();

            final int minX = Math.min(pressX, dragX);
            final int minY = Math.min(pressY, dragY);
            final int maxX = Math.max(pressX, dragX);
            final int maxY = Math.max(pressY, dragY);

            viewport.setBounds(minX, minY, maxX, maxY);
            repaint();
        }
    }
}
