package org.obicere.simulation.graphics.clipping.ui;

import org.obicere.simulation.graphics.clipping.Clipping2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * @author Obicere
 */
public class Clipping2DFrameManager {

    private final ClippingPanel panel;

    public Clipping2DFrameManager(){

        final JFrame frame = new JFrame(Clipping2D.NAME);

        this.panel = new ClippingPanel();

        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        final JLabel pointCountLabel = new JLabel("Point Count:");
        final JSpinner pointCountSpinner = new JSpinner(new SpinnerNumberModel(50, 5, 10000, 1));
        final JLabel radiusLabel = new JLabel("Radius:");
        final JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(250, 10, 1000, 1));
        final JButton populateButton = new JButton("Populate");

        populateButton.addActionListener(e -> {
            final int pointCount = (int) pointCountSpinner.getValue();
            final int radius = (int) radiusSpinner.getValue();

            panel.populateLines(radius, pointCount);
            frame.repaint();
        });

        controls.add(pointCountLabel);
        controls.add(pointCountSpinner);
        controls.add(radiusLabel);
        controls.add(radiusSpinner);
        controls.add(populateButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

}
