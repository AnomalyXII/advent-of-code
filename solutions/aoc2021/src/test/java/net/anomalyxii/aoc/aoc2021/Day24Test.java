package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day24Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(24);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 24);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    @Disabled("No example given :(")
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(53999995829399L);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("No example given :(")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(11721151118175L);
    }

}
