package org.obicere.simulation.algorithm.sorting.visual.gui;

import org.obicere.simulation.algorithm.sorting.visual.algorithms.Algorithm;

import javax.swing.*;
import java.awt.*;

/**
 * @author Obicere
 */
public class FrameManager {

    public FrameManager() {
        final JFrame frame = new JFrame("Visual Sorting");

        final DisplayPanel panel = new DisplayPanel();

        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        final JComboBox<Algorithm> algorithms = new JComboBox<>(Algorithm.values());
        final JLabel delayLabel = new JLabel("Delay: ");
        final JSpinner delay = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
        final JLabel lengthLabel = new JLabel("Length: ");
        final JSpinner length = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        final JButton start = new JButton("Start");

        start.addActionListener(e -> {
            final Algorithm algorithm = (Algorithm) algorithms.getSelectedItem();
            final Integer delayAmount = (Integer) delay.getValue();
            final Integer lengthAmount = (Integer) length.getValue();
            if (algorithm == null || delayAmount == null || lengthAmount == null) {
                return;
            }
            panel.startAlgorithm(algorithm, lengthAmount, delayAmount);
        });

        controls.add(algorithms);
        controls.add(delayLabel);
        controls.add(delay);
        controls.add(lengthLabel);
        controls.add(length);
        controls.add(start);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
