package org.obicere.simulation.graphics.wireframe.ui;

import org.obicere.simulation.graphics.wireframe.WireFrame;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Obicere
 */
public class WireFrameFrameManager {

    public WireFrameFrameManager(){
        final JFrame frame = new JFrame(WireFrame.NAME);
        final WireFramePanel panel = new WireFramePanel();

        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

}
