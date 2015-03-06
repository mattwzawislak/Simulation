package org.obicere.simulation.games.minesweeper.game.data;

import java.awt.Color;

/**
 */
public enum Flag {

    NONE('\0', null),

    FLAG('\u2691', new Color(0x303030)),
    MAYBE('?', new Color(0xE35000)),

    BOMB('*', new Color(0x303030)),

    ZERO('\0', null),
    ONE('1', new Color(0x004080)),
    TWO('2', new Color(0x008034)),
    THREE('3', new Color(0x800000)),
    FOUR('4', new Color(0x600080)),
    FIVE('5', new Color(0x800040)),
    SIX('6', new Color(0x008080)),
    SEVEN('7', new Color(0x303030)),
    EIGHT('8', new Color(0x808080));

    private final char representation;

    private final Color color;

    private Flag(final char representation, final Color color) {
        this.representation = representation;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public char getRepresentation() {
        return representation;
    }

    public static Flag flagForBombCount(final int count) {
        switch (count) {
            case 0:
                return ZERO;
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            default:
                return null;
        }
    }

}
