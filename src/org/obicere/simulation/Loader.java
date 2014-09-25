package org.obicere.simulation;

import org.obicere.utility.Reflection;

import javax.swing.*;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @author Obicere
 */
public class Loader {

    public static void main(final String[] args) {
        final Stream<Class<?>> stream = Reflection.subclassOf(Simulation.class);
        final LinkedList<Class<?>> classes = new LinkedList<>();
        stream.forEach(classes::add);
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
