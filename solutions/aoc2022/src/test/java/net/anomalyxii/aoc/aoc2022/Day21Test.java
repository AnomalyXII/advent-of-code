package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(21);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 21);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(152L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(22382838633806L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(301L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(3099532691300L);
    }

}

