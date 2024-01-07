package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2021, 21);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 21);

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
                .isEqualTo(739785L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(916083L);
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
                .isEqualTo(444356092776315L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(49982165861983L);
    }

}
