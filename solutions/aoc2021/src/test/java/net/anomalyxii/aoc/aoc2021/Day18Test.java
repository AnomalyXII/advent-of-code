package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2021, 18);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 18);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(4140L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(4435L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(3993L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(4802L);
    }

}
