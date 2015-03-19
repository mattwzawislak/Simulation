package org.obicere.simulation.algorithm.sorting.visual.gui;

import org.obicere.simulation.algorithm.sorting.visual.algorithms.Algorithm;
import org.obicere.simulation.algorithm.sorting.visual.algorithms.SortingProcess;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        final JSpinner length = new JSpinner(new SpinnerNumberModel(128, 1, 10000, 1));
        final JButton start = new JButton("Start");

        start.addActionListener(e -> {
            final SortingProcess process = panel.getProcess();
            if (process != null) {
                process.halt();
            }

            final Algorithm algorithm = (Algorithm) algorithms.getSelectedItem();
            final Integer delayAmount = (Integer) delay.getValue();
            final Integer lengthAmount = (Integer) length.getValue();
            if (algorithm == null || delayAmount == null || lengthAmount == null) {
                return;
            }
            panel.startAlgorithm(algorithm, lengthAmount, delayAmount);
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                final SortingProcess process = panel.getProcess();
                if (process != null) {
                    process.halt();
                }
            }
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
