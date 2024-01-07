package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 8);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 8);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(21L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1669L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(8L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(331344L);
    }

}
