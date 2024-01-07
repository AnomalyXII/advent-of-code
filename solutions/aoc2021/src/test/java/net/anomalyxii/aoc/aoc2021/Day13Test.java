package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.ocr.LetterSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    private static final SolutionContext EXAMPLE = SolutionContext.builder()
            .example(2021, 13)
            .withLetterSet(new BoxLetterSet())
            .build();
    private static final SolutionContext LIVE = SolutionContext.live(2021, 13);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(17L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(638L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("□");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("CJCKBAPB");
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A `LetterSet` that matches a 5x5 box (□).
     */
    private static final class BoxLetterSet implements LetterSet {

        /*
         * A BOX... a special 5x5 character specifically for 2021 Day 13.
         */
        private static final int[][] BOX = new int[][]{
                new int[]{1, 1, 1, 1, 1},
                new int[]{1, 0, 0, 0, 1},
                new int[]{1, 0, 0, 0, 1},
                new int[]{1, 0, 0, 0, 1},
                new int[]{1, 1, 1, 1, 1},
        };

        // LetterSet Methods

        @Override
        public char match(final Grid grid) {
            if (grid.matches(BOX))
                return '\u25A1';

            throw new IllegalArgumentException("Failed to match the character: \u25A1.");
        }

    }

}
