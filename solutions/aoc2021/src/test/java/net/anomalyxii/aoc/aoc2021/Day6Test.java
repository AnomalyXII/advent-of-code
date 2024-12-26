package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(6);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 6);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(5934L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(396210L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(26984457539L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1770823541496L);
    }

}
