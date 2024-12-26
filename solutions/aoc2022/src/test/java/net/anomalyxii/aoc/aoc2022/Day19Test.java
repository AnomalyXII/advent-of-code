package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(19);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 19);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(33L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2160L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(3472L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(13340L);
    }

}

