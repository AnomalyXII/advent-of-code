package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(14);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 14);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1588L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2223L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2188189693529L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2566282754493L);
    }

}
