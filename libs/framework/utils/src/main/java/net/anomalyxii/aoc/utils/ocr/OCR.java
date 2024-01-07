package net.anomalyxii.aoc.utils.ocr;

import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Match letters rendered in a {@link Grid grid} of 5x6 characters.
 */
public class OCR {

    // ****************************************
    // Private Members
    // ****************************************

    private final LetterSet letters;

    // ****************************************
    // Constructors
    // ****************************************

    OCR(final LetterSet letters) {
        this.letters = letters;
    }

    // ****************************************
    // Public Helper Methods
    // ****************************************

    /**
     * Detect a {@link String} of {@link Character characters} from the given
     * {@link Grid}. Each {@link Character character} is expected to be
     * contained within a 5x6 sub-grid.
     *
     * @param grid the {@link Grid}
     * @return a {@link String} containing the recognised characters
     */
    public String recognise(final Grid grid) {
        final Grid[] grids = grid.partition(5, -1);
        return Stream.of(grids)
                .map(letters::match)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    // ****************************************
    // Public Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link OCR} using the given {@link LetterSet}.
     *
     * @param letters the {@link LetterSet}
     * @return the {@link OCR}
     */
    public static OCR ofLetters(final LetterSet letters) {
        return new OCR(letters);
    }

}
