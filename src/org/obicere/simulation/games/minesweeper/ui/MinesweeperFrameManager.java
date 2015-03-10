package org.obicere.simulation.games.minesweeper.ui;

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
 */
public class MinesweeperFrameManager {

    public MinesweeperFrameManager() {
        final JFrame frame = new JFrame("Minesweeper");
        final BoardPanel panel = new BoardPanel();

        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        final JLabel widthLabel = new JLabel("Width:");
        final JSpinner width = new JSpinner(new SpinnerNumberModel(30, 1, 120, 1));
        final JLabel heightLabel = new JLabel("Height:");
        final JSpinner height = new JSpinner(new SpinnerNumberModel(20, 1, 80, 1));
        final JLabel bombLabel = new JLabel("Bombs:");
        final JSpinner bombCount = new JSpinner(new SpinnerNumberModel(100, 0, 9599, 1));
        final JButton newGame = new JButton("New Game");

        controls.add(widthLabel);
        controls.add(width);
        controls.add(heightLabel);
        controls.add(height);
        controls.add(bombLabel);
        controls.add(bombCount);
        controls.add(newGame);

        newGame.addActionListener(e -> {
            panel.newGame((int) width.getValue(), (int) height.getValue(), (int) bombCount.getValue());
            frame.pack();
            frame.repaint();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
