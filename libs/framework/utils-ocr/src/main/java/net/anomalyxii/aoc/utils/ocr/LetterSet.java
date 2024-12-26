package net.anomalyxii.aoc.utils.ocr;

import net.anomalyxii.aoc.utils.geometry.Grid;

/**
 * A set of {@link Character characters} that can be recognised by the
 * {@link OCR} "algorithm" (I use that term <i>very</i> loosely).
 */
public interface LetterSet {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Attempt to match the provided {@link Grid} to a known
     * {@link Character}, throwing an {@link IllegalStateException error} if
     * no {@link Character} can be matched.
     *
     * @param grid the {@link Grid} to match
     * @return the {@link Character}
     * @throws IllegalStateException if no {@link Character} is matched
     */
    char match(Grid grid);

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Return a {@link LetterSet} containing the "standard" characters.
     *
     * @return the {@link LetterSet}
     */
    static LetterSet standardLetters() {
        return new StandardLetterSet();
    }

}
