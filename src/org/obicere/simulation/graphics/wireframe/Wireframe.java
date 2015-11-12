package org.obicere.simulation.graphics.wireframe;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.graphics.wireframe.ui.WireFrameFrameManager;

import javax.swing.SwingUtilities;

/**
 * @author Obicere
 */
public class WireFrame extends Simulation {

    public static final String NAME = "Wire frame in 3D";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(WireFrameFrameManager::new);
    }
}
