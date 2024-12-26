package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(14);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 14);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day14 challenge = new Day14(11, 7);

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(12);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(228421332);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("No example provided :(")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day14 challenge = new Day14(11, 7);

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(7790);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(228421332);
        assertThat(answer.answer2())
                .isEqualTo(7790);
    }

}

