package org.obicere.simulation.algorithm.maze.generation.ui;

import org.obicere.simulation.algorithm.maze.generation.MazeGeneration;
import org.obicere.simulation.algorithm.maze.generation.algorithm.MazeGenerationAlgorithmSet;
import org.obicere.simulation.algorithm.maze.generation.util.MazeGenerator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 */
public class MazeGenerationFrameManager {

    public MazeGenerationFrameManager() {
        final JFrame frame = new JFrame(MazeGeneration.NAME);

        final MazeGenerator generator = new MazeGenerator();
        final JPanel display = new MazeGenerationDisplayPanel(generator);

        final JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        final JComboBox<MazeGenerationAlgorithmSet> algorithmComboBox = new JComboBox<>(MazeGenerationAlgorithmSet.values());
        final JLabel rowLabel = new JLabel("Rows:");
        final JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        final JLabel columnLabel = new JLabel("Columns:");
        final JSpinner columnSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        final JButton calculate = new JButton("Calculate");

        calculate.addActionListener(e -> {
            final int rows = (int) rowSpinner.getValue();
            final int columns = (int) columnSpinner.getValue();

            final MazeGenerationAlgorithmSet algorithm = (MazeGenerationAlgorithmSet) algorithmComboBox.getSelectedItem();
            if (algorithm == null) {
                return;
            }
            generator.createNewMaze(algorithm.getAlgorithm(), rows, columns);
        });

        control.add(algorithmComboBox);
        control.add(rowLabel);
        control.add(rowSpinner);
        control.add(columnLabel);
        control.add(columnSpinner);
        control.add(calculate);

        frame.add(display, BorderLayout.CENTER);
        frame.add(control, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        new Timer(15, e -> {
            frame.repaint();
        }).start();
    }

}
