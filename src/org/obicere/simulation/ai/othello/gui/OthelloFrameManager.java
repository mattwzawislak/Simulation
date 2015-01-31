package org.obicere.simulation.ai.othello.gui;

import org.obicere.simulation.ai.othello.game.Game;
import org.obicere.simulation.ai.othello.strategy.RandomStrategy;
import org.obicere.simulation.ai.othello.strategy.Strategy;
import org.obicere.simulation.ai.othello.strategy.WeightedStrategy;
import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.TimerTask;

/**
 * @author Obicere
 * @version 1.0
 */
public class OthelloFrameManager {

    private static final Strategy[] STRATEGIES = new Strategy[]{
            new RandomStrategy(),
            new WeightedStrategy()
    };

    public OthelloFrameManager() {

        final SpringLayout layout = new SpringLayout();
        final JPanel dialog = new JPanel(layout);
        final JComboBox<Strategy> whiteSelection = new JComboBox<>(STRATEGIES);
        final JComboBox<Strategy> blackSelection = new JComboBox<>(STRATEGIES);

        final JLabel whiteLabel = new JLabel("White strategy: ");
        final JLabel blackLabel = new JLabel("Black strategy: ");

        dialog.add(whiteLabel);
        dialog.add(whiteSelection);
        dialog.add(blackLabel);
        dialog.add(blackSelection);

        makeCompactGrid(dialog, 2, 2, 5, 5, 10, 5);

        final int option = JOptionPane.showConfirmDialog(null, dialog, "Strategy Selection", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        final Game game = new Game(strategyForName(whiteSelection.getSelectedItem().toString()), strategyForName(blackSelection.getSelectedItem().toString()));
        final JFrame frame = new JFrame("Othello");

        final JPanel content = new JPanel(null);

        final BoardPanel panel = new BoardPanel(game);

        content.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int min = Math.min(content.getWidth(), content.getHeight());
                final int paddingWidth = (content.getWidth() - min) / 2;
                final int paddingHeight = (content.getHeight() - min) / 2;
                panel.setBounds(paddingWidth, paddingHeight, min, min); // Oh god this isn't good at all
                content.revalidate();
            }
        });

        content.setPreferredSize(new Dimension(500, 500));
        content.add(panel);

        frame.setContentPane(content);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        new ConditionalTimer(15, new TimerTask() {
            @Override
            public void run() {
                frame.repaint();
            }
        }, frame::isVisible);

    }

    private Strategy strategyForName(final String name) {
        for (final Strategy strategy : STRATEGIES) {
            if (strategy.name().equalsIgnoreCase(name)) {
                return strategy;
            }
        }
        return null;
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component in a column is as wide
     * as the maximum preferred width of the components in that column;
     * height is similarly determined for each row. The parent is made just
     * big enough to fit them all.
     *
     * @param rows     number of rows
     * @param cols     number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad     x padding between cells
     * @param yPad     y padding between cells
     */
    public void makeCompactGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                                                                    getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                                                                     getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    /* Used by makeCompactGrid. */
    private SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

}
