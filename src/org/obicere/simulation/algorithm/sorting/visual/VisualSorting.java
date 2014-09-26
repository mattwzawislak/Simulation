package org.obicere.simulation.algorithm.sorting.visual;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.algorithm.sorting.visual.gui.FrameManager;

import javax.swing.*;

/**
 * @author Obicere
 */
public class VisualSorting extends Simulation {

    private static final String NAME = "Visual Sorting";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(FrameManager::new);
    }
}
