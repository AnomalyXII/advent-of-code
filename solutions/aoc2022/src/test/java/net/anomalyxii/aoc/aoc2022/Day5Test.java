package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day5Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(5);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 5);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final String answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("CMZ");
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final String answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("QNHWJVJZW");
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("MCD");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("BPCZJLFJW");
    }

}
