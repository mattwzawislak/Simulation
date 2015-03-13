package org.obicere.simulation.regex;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Obicere
 */
public class FrameManager {

    private static final Color BAD_PATTERN  = new Color(128, 0, 0);
    private static final Color GOOD_PATTERN = new Color(0, 128, 0);

    public FrameManager() {

        final JFrame frame = new JFrame("Regex Fractals");

        final Canvas canvas = new Canvas();
        final JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        final JTextField regex = new JTextField(20);
        final JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 14, 1));
        final JButton graph = new JButton("Graph");

        regex.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    final Pattern pattern = Pattern.compile(regex.getText());
                    Objects.requireNonNull(pattern);
                    regex.setForeground(GOOD_PATTERN);
                } catch (final Exception e) {
                    regex.setForeground(BAD_PATTERN);
                }
            }

        });

        graph.addActionListener(e -> {
            canvas.interrupt();
            canvas.applyRegex((Integer) sizeSpinner.getValue(), regex.getText());
        });

        controls.add(regex);
        controls.add(sizeSpinner);
        controls.add(graph);

        frame.add(canvas, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                canvas.interrupt();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
