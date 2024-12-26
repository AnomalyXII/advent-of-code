package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(9);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 9);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(15L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(508L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1134);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1564640L);
    }

}
