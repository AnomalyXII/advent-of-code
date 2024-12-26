package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day12Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(12);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 12);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(31L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(420L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(29L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(414L);
    }

}

