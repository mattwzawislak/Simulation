package org.obicere.simulation.algorithm.shuffling.bias.gui;

import org.obicere.simulation.algorithm.shuffling.bias.shuffling.Algorithm;

import javax.swing.*;
import java.awt.*;

/**
 * @author Obicere
 */
public class FrameManager {

    public FrameManager() {

        final JFrame frame = new JFrame("Shuffling Bias");

        final BiasDisplayPanel panel = new BiasDisplayPanel();

        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        final JComboBox<Algorithm> algorithmSelector = new JComboBox<>(Algorithm.values());
        final JLabel countLabel = new JLabel("Samples: ");
        final JSpinner countSelector = new JSpinner(new SpinnerNumberModel(100, 1, 200000, 1));
        final JLabel sizeLabel = new JLabel("Size: ");
        final JSpinner sizeSelector = new JSpinner(new SpinnerNumberModel(25, 1, 500, 1));
        final JButton start = new JButton("Start");

        start.addActionListener(e -> {
            final Algorithm algorithm = (Algorithm) algorithmSelector.getSelectedItem();
            final Integer size = (Integer) sizeSelector.getValue();
            final Integer count = (Integer) countSelector.getValue();
            if (count == null || size == null || algorithm == null) {
                return;
            }
            panel.calculate(algorithm.getAlgorithm(), size, count);
        });

        controls.add(algorithmSelector);
        controls.add(countLabel);
        controls.add(countSelector);
        controls.add(sizeLabel);
        controls.add(sizeSelector);
        controls.add(start);

        frame.add(controls, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
