package net.anomalyxii.aoc.utils.ocr;

import net.anomalyxii.aoc.utils.geometry.Grid;

/**
 * A {@link LetterSet} containing {@link Character characters} 'A' - 'Z'.
 */
class StandardLetterSet implements LetterSet {

    /*
     * Each capital letter (A-Z).
     */
    private static final int[][][] LETTERS = new int[][][]{
            new int[][]{ /* A */
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* B */
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 0, 0},
            },
            new int[][]{ /* C */
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 0, 0},
            },
            new int[][]{ /* D */
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 0, 0},
            },
            new int[][]{ /* E */
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 1, 1, 1, 0},
            },
            new int[][]{ /* F */
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
            },
            new int[][]{ /* G */
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 1, 0},
            },
            new int[][]{ /* H */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* I */
                    new int[]{0, 1, 1, 1, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 1, 1, 1, 0},
            },
            new int[][]{ /* J */
                    new int[]{0, 0, 1, 1, 0},
                    new int[]{0, 0, 0, 1, 0},
                    new int[]{0, 0, 0, 1, 0},
                    new int[]{0, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 0, 0},
            },
            new int[][]{ /* K */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 1, 0, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* L */
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 1, 1, 1, 0},
            },
            new int[][]{ /* M - guess? */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* N - guess? */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 0, 1, 0},
                    new int[]{1, 1, 0, 1, 0},
                    new int[]{1, 0, 1, 1, 0},
                    new int[]{1, 0, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* O */
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
            },
            new int[][]{ /* P */
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
            },
            new int[][]{ /* Q - guess */
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 1, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{0, 0, 1, 0, 0},
            },
            new int[][]{ /* R  */
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* S */
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{0, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
            },
            new int[][]{ /* T - guess? */
                    new int[]{0, 1, 1, 1, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
            },
            new int[][]{ /* U - guess? */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 0, 0},
            },
            new int[][]{ /* V - guess? */
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{1, 0, 1, 0, 0},
                    new int[]{0, 1, 0, 0, 0},
            },
            new int[][]{ /* W - guess? */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* X - guess? */
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{0, 1, 1, 0, 0},
                    new int[]{1, 0, 0, 1, 0},
                    new int[]{1, 0, 0, 1, 0},
            },
            new int[][]{ /* Y */
                    new int[]{1, 0, 0, 0, 1},
                    new int[]{1, 0, 0, 0, 1},
                    new int[]{0, 1, 0, 1, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 0, 1, 0, 0},
            },
            new int[][]{ /* Z */
                    new int[]{1, 1, 1, 1, 0},
                    new int[]{0, 0, 0, 1, 0},
                    new int[]{0, 0, 1, 0, 0},
                    new int[]{0, 1, 0, 0, 0},
                    new int[]{1, 0, 0, 0, 0},
                    new int[]{1, 1, 1, 1, 0},
            },
    };

    // ****************************************
    // LetterSet Methods
    // ****************************************

    @Override
    public char match(final Grid grid) {
        for (int l = 'A'; l <= 'Z'; l++)
            if (grid.matches(LETTERS[l - 'A']))
                return (char) l;

        throw new IllegalArgumentException("Failed to match a letter :(");
    }
}
