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
            final List<Line2F> newLines = new LinkedList<>();

            // simple triangulation algorithm
            for (int i = 0; i < count; i++) {
                for (int j = i + 1; j < count; j++) {
                    for (int k = j + 1; k < count; k++) {
                        boolean isTriangle = true;
                        for (int a = 0; a < count; a++) {
                            if (a == i || a == j || a == k) {
                                continue;
                            }
                            if (inside(points[a], points[i], points[j], points[k])) {
                                isTriangle = false;
                                break;
                            }
                        }
                        if (isTriangle) {
                            newLines.add(new Line2F(points[i], points[j]));
                            newLines.add(new Line2F(points[j], points[k]));
                            newLines.add(new Line2F(points[k], points[i]));
                        }
                    }
                }
            }

            linesLock.lock();
            lines.clear();
            lines.addAll(newLines);
        } finally {
            linesLock.unlock();
        }
    }

    private float side(final Point2F a, final Point2F b){
        return a.getX() * b.getY() - a.getY() * b.getX();
    }
    
    public float area(final Point2F triangleP1, final Point2F triangleP2, final Point2F triangleP3) {
        final float side12 = side(triangleP1, triangleP2);
        final float side23 = side(triangleP2, triangleP3);
        final float side31 = side(triangleP3, triangleP1);
        
        return 0.5f * (side12 + side23 + side31);
    }

    private boolean inside(final Point2F point, final Point2F triangleP1, final Point2F triangleP2, final Point2F triangleP3) {
        final float area = area(triangleP1, triangleP2, triangleP3);
        if (area > 0) {
            return (in(point, triangleP1, triangleP2, triangleP3) > 0);
        } else if (area < 0) {
            return (in(point, triangleP1, triangleP2, triangleP3) < 0);
        }
        return true;
    }

    private float in(final Point2F point, final Point2F triangleP1, final Point2F triangleP2, final Point2F triangleP3) {
        final float adx = triangleP1.getX() - point.getX();
        final float ady = triangleP1.getY() - point.getY();
        final float bdx = triangleP2.getX() - point.getX();
        final float bdy = triangleP2.getY() - point.getY();
        final float cdx = triangleP3.getX() - point.getX();
        final float cdy = triangleP3.getY() - point.getY();

        final float abdet = adx * bdy - bdx * ady;
        final float bcdet = bdx * cdy - cdx * bdy;
        final float cadet = cdx * ady - adx * cdy;

        final float alift = adx * adx + ady * ady;
        final float blift = bdx * bdx + bdy * bdy;
        final float clift = cdx * cdx + cdy * cdy;

        return alift * bcdet + blift * cadet + clift * abdet;
    }


    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

        drawViewport(g2);

        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;

        final Point2F origin = new Point2F(centerX, centerY);

        linesLock.lock();
        for (final Line2F line : lines) {
            drawLine(g2, line, origin);
        }
        linesLock.unlock();
    }

    private void drawViewport(final Graphics g) {
        final int minX = viewport.getMinX();
        final int minY = viewport.getMinY();
        final int maxX = viewport.getMaxX();
        final int maxY = viewport.getMaxY();

        g.drawLine(minX, minY, minX, maxY);
        g.drawLine(minX, maxY, maxX, maxY);
        g.drawLine(maxX, maxY, maxX, minY);
        g.drawLine(maxX, minY, minX, minY);
    }

    private void drawLine(final Graphics g, final Line2F line, final Point2F origin) {
        final Point2F a = new Point2F(line.getA()).add(origin);
        final Point2F b = new Point2F(line.getB()).add(origin);

        if (viewport.clip(a, b)) {
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
            if (defaultViewport) {
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
        public void mousePressed(final MouseEvent e) {
            defaultViewport = false;

            pressX = e.getX();
            pressY = e.getY();

            viewport.setBounds(pressX, pressY, pressX, pressY);
            repaint();
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
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
