package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 14);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(2020, 14, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 14);

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
                .isEqualTo(165);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(9296748256641L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(208);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(4877695371685L);
    }

}