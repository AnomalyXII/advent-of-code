package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day1Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(1);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(1, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 1);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(142L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(54450L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(281L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(54265L);
    }

}

