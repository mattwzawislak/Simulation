package org.obicere.simulation.geom.calc.convexhull;

import org.obicere.simulation.Simulation;
import org.obicere.simulation.geom.calc.convexhull.gui.FrameManager;

import javax.swing.*;

/**
 * @author Obicere
 */
public class ConvexHull extends Simulation {

    private static final String NAME = "Convex Hull";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void main() {
        SwingUtilities.invokeLater(FrameManager::new);
    }
}
