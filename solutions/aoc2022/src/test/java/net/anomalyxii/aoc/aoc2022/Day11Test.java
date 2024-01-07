package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 11);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 11);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(10605L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(98280L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2713310158L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(17673687232L);
    }

}

