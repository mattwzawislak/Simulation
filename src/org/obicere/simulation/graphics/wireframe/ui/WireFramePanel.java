package org.obicere.simulation.graphics.wireframe.ui;

import org.obicere.utility.geom.Line3F;
import org.obicere.utility.geom.Viewport2F;
import org.obicere.utility.math.HomogenizeMatrix;
import org.obicere.utility.math.PerspectiveProjectionMatrix;
import org.obicere.utility.math.Point2F;
import org.obicere.utility.math.Point3F;
import org.obicere.utility.math.Point4F;
import org.obicere.utility.math.RotationMatrix;
import org.obicere.utility.math.ScaleMatrix;
import org.obicere.utility.math.TranslationMatrix;

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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Obicere
 */
public class WireFramePanel extends JPanel {

    private static final int PREFERRED_WIDTH  = 500;
    private static final int PREFERRED_HEIGHT = 500;

    private static final Color DEFAULT_FOREGROUND = new Color(0xDD333333, true);
    private static final Color DEFAULT_BACKGROUND = new Color(0xDDDDDD);

    private final Viewport2F viewport = new Viewport2F(0, 0, PREFERRED_WIDTH, PREFERRED_HEIGHT);

    private final List<Line3F> lines = new LinkedList<>();

    private final TranslationMatrix           translation;
    private final RotationMatrix              rotation;
    private final ScaleMatrix                 scale;
    private final PerspectiveProjectionMatrix projection;
    private final HomogenizeMatrix            homogenize;

    public WireFramePanel() {
        setForeground(DEFAULT_FOREGROUND);
        setBackground(DEFAULT_BACKGROUND);
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        this.translation = new TranslationMatrix(0, 0, -20);
        this.rotation = new RotationMatrix((float) Math.PI / 4, 0, 0);
        this.scale = new ScaleMatrix();
        this.projection = new PerspectiveProjectionMatrix((float) Math.PI / 6, (PREFERRED_WIDTH / (float) PREFERRED_HEIGHT), 0.01F, 50);
        this.homogenize = new HomogenizeMatrix(0, 0, PREFERRED_WIDTH, PREFERRED_HEIGHT);

        loadCube();

        addComponentListener(new ViewportComponentListener());
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                final float scaleAmount = (float) e.getPreciseWheelRotation() / 10f;
                scale.setXScale(scale.getXScale() + scaleAmount);
                scale.setYScale(scale.getYScale() + scaleAmount);
                scale.setZScale(scale.getZScale() + scaleAmount);
                repaint();
            }
        });
        final MouseAdapter mouse = new MouseAdapter() {

            private int pressX;
            private int pressY;

            private int button;

            @Override
            public void mousePressed(final MouseEvent e){
                pressX = e.getX();
                pressY = e.getY();
                button = e.getButton();
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                final int dragX = e.getX();
                final int dragY = e.getY();

                final int dx = dragX - pressX;
                final int dy = dragY - pressY;

                switch (button){
                    // left
                    case MouseEvent.BUTTON1:
                        rotation.setXRotation(rotation.getXRotation() + dx / 50f);
                        rotation.setYRotation(rotation.getYRotation() + dy / 50f);
                        break;

                    // right
                    case MouseEvent.BUTTON3:
                        translation.setXTranslation(translation.getXTranslation() + dx / 50f);
                        translation.setYTranslation(translation.getYTranslation() + dy / 50f);
                        break;
                }
                repaint();
                pressX = dragX;
                pressY = dragY;
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    private void loadCube(){

        final Point3F p0 = new Point3F(-1, -1, -1);
        final Point3F p1 = new Point3F(1, -1, -1);
        final Point3F p2 = new Point3F(-1, 1, -1);
        final Point3F p3 = new Point3F(1, 1, -1);
        final Point3F p4 = new Point3F(-1, -1, 1);
        final Point3F p5 = new Point3F(1, -1, 1);
        final Point3F p6 = new Point3F(-1, 1, 1);
        final Point3F p7 = new Point3F(1, 1, 1);

        lines.add(new Line3F(p0, p1));
        lines.add(new Line3F(p0, p2));
        lines.add(new Line3F(p0, p4));
        lines.add(new Line3F(p1, p3));
        lines.add(new Line3F(p1, p5));
        lines.add(new Line3F(p2, p3));
        lines.add(new Line3F(p2, p6));
        lines.add(new Line3F(p3, p7));
        lines.add(new Line3F(p4, p5));
        lines.add(new Line3F(p4, p6));
        lines.add(new Line3F(p5, p7));
        lines.add(new Line3F(p6, p7));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

        for (final Line3F line : lines) {
            drawLine(g, line);
        }
    }

    private void drawLine(final Graphics g, final Line3F line){
        final Point3F a = line.getA();
        final Point3F b = line.getB();

        final Point2F pA = project(a);
        final Point2F pB = project(b);

        if(viewport.clip(pA, pB)){
            final int aX = (int) pA.getX();
            final int aY = (int) pA.getY();
            final int bX = (int) pB.getX();
            final int bY = (int) pB.getY();

            g.drawLine(aX, aY, bX, bY);
        }
    }

    private Point2F project(final Point3F point){
        Point4F hPoint = new Point4F(point);

        hPoint = rotation.multiply(hPoint);
        hPoint = scale.multiply(hPoint);
        hPoint = translation.multiply(hPoint);
        hPoint = projection.multiply(hPoint);
        return homogenize.normalize(hPoint);
    }

    private class ViewportComponentListener implements ComponentListener {

        @Override
        public void componentResized(final ComponentEvent e) {
            final int width = getWidth();
            final int height = getHeight();

            viewport.setMaxX(width);
            viewport.setMaxY(height);

            homogenize.update(viewport);
            projection.setAspectRatio((width / (float) height));
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

}
