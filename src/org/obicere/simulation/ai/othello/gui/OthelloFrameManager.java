package org.obicere.simulation.ai.othello.gui;

import org.obicere.simulation.ai.othello.game.Game;
import org.obicere.utility.util.ConditionalTimer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.TimerTask;

/**
 * @author Obicere
 * @version 1.0
 */
public class OthelloFrameManager {

    public OthelloFrameManager(final Game game) {
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

}
