package org.obicere.simulation.regex;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Obicere
 */
public class Graph {

    private final static char[] DIGITS = {'2', '1', '3', '4'};

    private final Pattern pattern;

    private final int imageSize;

    private final int size;

    private volatile boolean calculating = false;

    private final BufferedImage image;

    public Graph(final int size, final String regex) {
        this.pattern = Pattern.compile(regex);
        this.size = size;
        this.imageSize = 1 << size;

        this.image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
    }

    public int getImageSize() {
        return imageSize;
    }

    public Image getImage() {
        return image;
    }

    public void apply() {
        calculating = true;
        for (int x = 0; x < imageSize; x++) {
            for (int y = 0; y < imageSize; y++) {
                final Matcher matcher = pattern.matcher(getName(x, y));
                final int color;
                if (matcher.matches()) {
                    color = 0;
                } else {
                    color = 0xFFFFFF;
                }
                image.setRGB(x, y, color);
            }
            if (Thread.interrupted()) {
                image.flush();
                return;
            }
        }
        calculating = false;
        image.flush();
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

    public boolean isCalculating() {
        return calculating;
    }
}
