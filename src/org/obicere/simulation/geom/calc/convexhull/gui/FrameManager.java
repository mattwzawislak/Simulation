package org.obicere.simulation.geom.calc.convexhull.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Obicere
 */
public class FrameManager {

    public FrameManager() {

        final JFrame frame = new JFrame("Convex Hull");

        final DisplayPanel panel = new DisplayPanel();

        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        final JLabel delayLabel = new JLabel("Delay: ");
        final JSpinner delayValue = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
        final JButton clear = new JButton("Clear");
        final JButton start = new JButton("Start");

        start.addActionListener(e -> {
            final Integer delay = (Integer) delayValue.getValue();
            if (delay == null) {
                return;
            }
            panel.calculate(delay);
        });

        clear.addActionListener(e -> panel.clearField());

        controls.add(delayLabel);
        controls.add(delayValue);
        controls.add(clear);
        controls.add(start);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
