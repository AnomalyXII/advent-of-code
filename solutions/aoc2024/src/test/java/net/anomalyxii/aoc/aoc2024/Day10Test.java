package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(10);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 10);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(36);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(510);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(81);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1058);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(36);
        assertThat(answer.answer2())
                .isEqualTo(81);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(510);
        assertThat(answer.answer2())
                .isEqualTo(1058);
    }

}

