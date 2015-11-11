package org.obicere.simulation.graphics.clipping;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.graphics.clipping.ui.Clipping2DFrameManager;

import javax.swing.SwingUtilities;

/**
 * @author Obicere
 */
public class Clipping2D extends Simulation{

    public static final String NAME = "Clipping in 2D";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(Clipping2DFrameManager::new);
    }
}
