package org.obicere.simulation.regex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Obicere
 */
public class Graph {

    private final static char[] DIGITS = {'2', '1', '3', '4'};

    private final BufferedImage image;

    private final Pattern pattern;

    private final int imageSize;

    private final int size;

    private volatile boolean calculating = false;

    public Graph(final int size, final String regex) {
        if(size > 14){
            throw new IllegalArgumentException("Invalid size. Size 15 requires roughly 16GB of RAM available. ");
        }
        this.pattern = Pattern.compile(regex);
        this.size = size;
        this.imageSize = 1 << size;
        this.image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
    }

    public void apply(){
        calculating = true;
        final Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);
        for (int x = 0; x < imageSize; x++) {
            for (int y = 0; y < imageSize; y++) {
                final Matcher matcher = pattern.matcher(getName(x, y));
                if (!matcher.matches()) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        calculating = false;
    }

    private String getName(int x, int y) {
        int charPos = 32;
        final char[] buf = new char[32];
        for (int i = 0; i < size; i++) {
            buf[--charPos] = DIGITS[((x & 1) << 1) | (y & 1)];
            x >>>= 1;
            y >>>= 1;
        }
        return new String(buf, (32 - size), size);
    }

    public Image getImage() {
        return image;
    }

    public boolean isCalculating(){
        return calculating;
    }
}
