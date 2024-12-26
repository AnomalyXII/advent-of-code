package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day16Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(16);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(16, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 16);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(71);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(29851);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day16 challenge = new Day16(field -> "class".equalsIgnoreCase(field) || "seat".equalsIgnoreCase(field));

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(156);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(3029180675981L);
    }

}