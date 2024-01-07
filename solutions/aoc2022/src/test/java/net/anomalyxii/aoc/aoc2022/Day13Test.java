package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 13);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 13);

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
                .isEqualTo(13L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6568L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(140L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(19493L);
    }

}

