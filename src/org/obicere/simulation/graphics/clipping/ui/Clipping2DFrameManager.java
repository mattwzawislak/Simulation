package org.obicere.simulation.graphics.clipping.ui;

import org.obicere.simulation.graphics.clipping.Clipping2D;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Obicere
 */
public class Clipping2DFrameManager {

    private final JFrame frame;

    private final ClippingPanel panel;

    public Clipping2DFrameManager(){

        this.frame = new JFrame(Clipping2D.NAME);

        this.panel = new ClippingPanel();

        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

}
