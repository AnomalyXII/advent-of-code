package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day23Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(23);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 23);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(67384529);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(47382659);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(149245887792L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(42271866720L);
    }

}