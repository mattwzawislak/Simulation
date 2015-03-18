package org.obicere.simulation.regex;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Obicere
 */
public class Graph {

    private final static char[] DIGITS = {'2', '1', '3', '4'};

    private final Object renderLock = new Object();

    private final LinkedList<Boolean> render = new LinkedList<>();

    private final Pattern pattern;

    private final int imageSize;

    private final int size;

    private volatile boolean calculating = false;

    public Graph(final int size, final String regex) {
        this.pattern = Pattern.compile(regex);
        this.size = size;
        this.imageSize = 1 << size;
    }

    public LinkedList<Boolean> getRenderCache() {
        return render;
    }

    public Object getRenderLock() {
        return renderLock;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void apply() {
        calculating = true;
        for (int x = 0; x < imageSize; x++) {
            for (int y = 0; y < imageSize; y++) {
                final Matcher matcher = pattern.matcher(getName(x, y));
                synchronized (renderLock) {
                    render.add(!matcher.matches());
                }
            }
            if (Thread.interrupted()) {
                synchronized (renderLock) {
                    render.clear();
                }
                return;
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

    public boolean isCalculating() {
        return calculating;
    }
}
