package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2021, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 2);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(150);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1636725L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(900L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1872757425L);
    }

}
