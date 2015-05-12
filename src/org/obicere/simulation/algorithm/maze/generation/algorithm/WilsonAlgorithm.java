package org.obicere.simulation.algorithm.maze.generation.algorithm;

import org.obicere.simulation.algorithm.maze.generation.util.MazeGraph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 */
public class WilsonAlgorithm implements MazeGenerationAlgorithm {

    private static final String NAME = "Wilson's Algorithm";

    private static final Color WORKING_COLOR = new Color(0x3070F0);

    private final ArrayList<WilsonNode> workingNodes = new ArrayList<>();

    private final Random random = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void paintHelpers(final Graphics g, final float squareSize) {
        g.setColor(WORKING_COLOR);
        final int size = workingNodes.size();

        WilsonNode last = null;
        float lastX = 0;
        float lastY = 0;

        for (int i = 0; i < size - 1; i++) {
            final WilsonNode node = workingNodes.get(i);

            if (node == null) {
                // If null node, then error in the calculation cycle.
                // Unknown implications.
                break;
            }

            final float x = indexToPixel(squareSize, node.j);
            final float y = indexToPixel(squareSize, node.i);
            g.fillRect(Math.round(x), Math.round(y), (int) squareSize + 1, (int) squareSize + 1);

            if (last != null) {
                drawLink(g, node.from, lastX, lastY, squareSize);
            }
            last = node;
            lastX = x;
            lastY = y;
        }
    }

    private void drawLink(final Graphics g, final int direction, final float x, final float y, final float size) {
        final float linkX;
        final float linkY;
        switch (direction) {
            case MazeGraph.DIRECTION_UP:
                linkX = x;
                linkY = y - size;
                break;
            case MazeGraph.DIRECTION_RIGHT:
                linkX = x + size;
                linkY = y;
                break;
            case MazeGraph.DIRECTION_DOWN:
                linkX = x;
                linkY = y + size;
                break;
            case MazeGraph.DIRECTION_LEFT:
                linkX = x - size;
                linkY = y;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction " + direction);
        }
        g.fillRect(Math.round(linkX), Math.round(linkY), (int) size + 1, (int) size + 1);
    }

    private float indexToPixel(final float size, final int index) {
        if (index == 0) {
            return 0;
        }
        return size * (index << 1);
    }

    @Override
    public boolean generateMaze(final MazeGraph graph) {
        final int rows = graph.getRowCount();
        final int columns = graph.getColumnCount();
        graph.add(0, 0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!graph.hasCellAt(i, j)) {
                    synchronized (graph.getRenderLock()) {
                        workingNodes.clear();

                        workingNodes.add(new WilsonNode(i, j, 0));
                    }

                    int wi = i;
                    int wj = j;
                    int last = 0;
                    while (!graph.hasCellAt(wi, wj)) {
                        tick();
                        if (Thread.interrupted()) {
                            return false;
                        }
                        final int next = nextDirection(wi, wj, rows - 1, columns - 1, last);
                        last = next;
                        wi += deltaI(next);
                        wj += deltaJ(next);
                        if (graph.hasCellAt(wi, wj)) {
                            break;
                        }
                        final WilsonNode node = new WilsonNode(wi, wj, next);
                        synchronized (graph.getRenderLock()) {
                            final int index = workingNodes.indexOf(node);
                            if (index != -1) {
                                final int remove = workingNodes.size() - index;
                                for (int k = 1; k < remove; k++) {
                                    workingNodes.remove(index + 1);

                                }
                            } else {
                                workingNodes.add(node);
                            }
                        }
                    }
                    synchronized (graph.getRenderLock()) {
                        workingNodes.add(new WilsonNode(wi, wj, last));
                        final int length = workingNodes.size();
                        for (int k = length - 1; k > 0; k--) {
                            final WilsonNode node = workingNodes.get(k);
                            graph.add(node.i, node.j, negate(node.from));
                        }
                    }
                }
            }
        }
        workingNodes.clear();
        return true;
    }

    private int negate(final int direction) {
        switch (direction) {
            case DIRECTION_LEFT:
                return DIRECTION_RIGHT;
            case DIRECTION_RIGHT:
                return DIRECTION_LEFT;
            case DIRECTION_DOWN:
                return DIRECTION_UP;
            case DIRECTION_UP:
                return DIRECTION_DOWN;
            default:
                return 0;
        }
    }

    private int deltaI(final int direction) {
        switch (direction) {
            case DIRECTION_UP:
                return -1;
            case DIRECTION_DOWN:
                return 1;
            default:
                return 0;
        }
    }

    private int deltaJ(final int direction) {
        switch (direction) {
            case DIRECTION_LEFT:
                return -1;
            case DIRECTION_RIGHT:
                return 1;
            default:
                return 0;
        }
    }

    private int nextDirection(final int i, final int j, final int rowLimit, final int columnLimit, final int previous) {
        boolean up = true;
        boolean right = true;
        boolean down = true;
        boolean left = true;
        if (i == 0) {
            up = false;
        } else if (i == rowLimit) {
            down = false;
        }
        if (j == 0) {
            left = false;
        } else if (j == columnLimit) {
            right = false;
        }
        switch (previous) {
            case DIRECTION_UP:
                down = false;
                break;
            case DIRECTION_DOWN:
                up = false;
                break;
            case DIRECTION_RIGHT:
                left = false;
                break;
            case DIRECTION_LEFT:
                right = false;
        }
        final ArrayList<Integer> list = new ArrayList<>(4);
        if (left) {
            list.add(DIRECTION_LEFT);
        }
        if (right) {
            list.add(DIRECTION_RIGHT);
        }
        if (down) {
            list.add(DIRECTION_DOWN);
        }
        if (up) {
            list.add(DIRECTION_UP);
        }
        return list.get(random.nextInt(list.size()));
    }

    private void tick() {
        try {
            Thread.sleep(0);
        } catch (final InterruptedException ignored) {
        }
    }

    private class WilsonNode {

        private final int i;
        private final int j;
        private final int from;

        private WilsonNode(final int i, final int j, final int direction) {
            this.i = i;
            this.j = j;
            this.from = direction;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof WilsonNode)) {
                return false;
            }
            final WilsonNode node = (WilsonNode) obj;
            return node.i == i && node.j == j;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(13);
            builder.append("Node[i=");
            builder.append(i);
            builder.append(",j=");
            builder.append(j);
            builder.append(",d=");
            builder.append(from);
            builder.append(']');
            return builder.toString();
        }

    }
}
