package org.obicere.simulation;

import org.obicere.utility.Reflection;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.Set;

/**
 * @author Obicere
 */
public class Loader {

    public static void main(final String[] args) {
        final Set<Class<Simulation>> classes = Reflection.subclassOf(Simulation.class);
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new SimulationSelector(classes);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

}
