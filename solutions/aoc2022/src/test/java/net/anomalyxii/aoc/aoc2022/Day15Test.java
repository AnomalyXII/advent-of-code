package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(15);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 15);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day15 challenge = new Day15(10, 0, 20);

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(26L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(5125700L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day15 challenge = new Day15(10, 0, 20);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(56000011L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(11379394658764L);
    }

}

