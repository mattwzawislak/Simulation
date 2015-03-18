package org.obicere.simulation.regex;

import org.obicere.simulation.Simulation;

import javax.swing.*;

/**
 * @author Obicere
 */
public class RegexFractals extends Simulation {

    private static final String NAME = "Regex Fractals";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new RegexFractalFrameManager();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }
}
