package org.obicere.simulation;

import org.obicere.utility.Reflection;
import org.obicere.utility.swing.VerticalFlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

/**
 * @author Obicere
 */
public class SimulationSelector {

    private static final String SEARCH = "Search...";

    private final Font blankFont;
    private final Font presetFont;

    public SimulationSelector(final Set<Class<Simulation>> simulations) {
        final JFrame frame = new JFrame("Simulation Selection");

        final LinkedList<SimulationPanel> panels = buildPanels(simulations);
        Collections.sort(panels);

        final JPanel searchPanel = new JPanel();
        final JPanel content = new JPanel();
        final JScrollPane scrollPane = new JScrollPane(content);

        final JTextField search = new JTextField();
        final JCheckBox box = new JCheckBox("Search packages.");

        final VerticalFlowLayout layout = new VerticalFlowLayout(VerticalFlowLayout.LEADING, 5, 5);

        this.blankFont = search.getFont().deriveFont(Font.ITALIC);
        this.presetFont = search.getFont();

        final Runnable update = new Runnable() {

            @Override
            public void run() {
                final String phrase = search.getText();
                if (phrase == null || phrase.equals(SEARCH)) {
                    return;
                }
                filter(phrase, content, panels, box.isSelected());
            }
        };

        box.addActionListener(e -> update.run());

        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                if (search.getText().equals(SEARCH)) {
                    search.setFont(presetFont);
                    search.setForeground(Color.BLACK);
                    search.setText("");
                }
            }

            @Override
            public void focusLost(final FocusEvent e) {
                final String text = search.getText();
                if (text == null || text.isEmpty()) {
                    search.setFont(blankFont);
                    search.setForeground(Color.GRAY);
                    search.setText(SEARCH);
                }
            }
        });


        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update.run();
            }

        });

        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.add(search);
        searchPanel.add(Box.createHorizontalGlue());
        searchPanel.add(box);

        layout.setMaximizeOtherDimension(true);
        content.setLayout(layout);
        panels.forEach(content::add);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private LinkedList<SimulationPanel> buildPanels(final Set<Class<Simulation>> classes) {
        final LinkedList<SimulationPanel> panels = new LinkedList<>();
        classes.forEach(e -> {
            try {
                final Simulation simulation = Reflection.newInstance(e);
                panels.add(new SimulationPanel(simulation));
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        });
        return panels;
    }

    private void filter(final String search, final JPanel container, final LinkedList<SimulationPanel> panels, final boolean searchPackage) {
        Objects.requireNonNull(search);
        Objects.requireNonNull(container);
        Objects.requireNonNull(panels);

        container.removeAll();
        container.revalidate();
        final String lowerSearch = search.toLowerCase();
        for (final SimulationPanel panel : panels) {
            final Simulation simulation = panel.getSimulation();
            final String name = simulation.getName().toLowerCase();
            final String packageName = simulation.getClass().getPackage().getName().toLowerCase();
            if (name.contains(lowerSearch) || searchPackage && packageName.contains(lowerSearch)) {
                container.add(panel);
            }
        }
        container.revalidate();
        container.updateUI();
    }

}
