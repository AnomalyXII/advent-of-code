package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day24Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 24);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 24);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(18L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(299L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(54L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(899L);
    }

}

