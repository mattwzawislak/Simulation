package org.obicere.simulation.geom.calc.convexhull.gui;

import org.obicere.simulation.geom.calc.convexhull.awt.Marker;
import org.obicere.simulation.geom.calc.convexhull.math.Field;
import org.obicere.utility.util.ConditionalTimer;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Obicere
 */
public class DisplayPanel extends JPanel {

    private final BasicStroke stroke = new BasicStroke(2.5f);

    private final Field pointField = new Field();

    private Polygon polygon;

    public void clearField() {
        pointField.clear();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (pointField.isCalculating()) {
                    return;
                }
                pointField.add(e.getPoint());
                pointField.clearMarker();
                polygon = null;
                repaint();
            }
        });
    }

    public void calculate(final int delay) {
        if (pointField.size() == 0) {
            return;
        }
        polygon = new Polygon();
        new Thread(() -> pointField.getConvexHull(polygon, delay)).start();
        new ConditionalTimer(10, e -> repaint(), pointField::isCalculating).start();
    }

    @Override
    public void paintComponent(final Graphics g1) {
        super.paintComponent(g1);
        final Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(stroke);

        final List<Point> points = pointField.points();
        final int size = pointField.size();
        final Marker marker = pointField.getMarker();
        g.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            final Point point = points.get(i);
            final int x = point.x;
            final int y = point.y;
            g.fillOval(x - 2, y - 2, 5, 5);
        }
        if (marker != null) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    final Color c = marker.getColor(i, j);
                    if (c != null) {
                        g.setColor(c);
                        final Point p1 = points.get(i);
                        final Point p2 = points.get(j);
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
            for (int i = 0; i < size; i++) {
                final Point point = points.get(i);
                final int x = point.x;
                final int y = point.y;
                final Color c = marker.getColor(i);
                if (c == null) {
                    continue;
                }
                g.setColor(c);
                g.fillOval(x - 2, y - 2, 5, 5);
            }
        }
    }
}
