package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day5Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(5);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 5);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(143);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(4774);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(123);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6004);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(143);
        assertThat(answer.answer2())
                .isEqualTo(123);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(4774);
        assertThat(answer.answer2())
                .isEqualTo(6004);
    }

}

