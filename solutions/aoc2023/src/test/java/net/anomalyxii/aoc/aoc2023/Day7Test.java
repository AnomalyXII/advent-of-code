package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day7Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(7);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 7);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(6440);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(249483956);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(5905);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(252137472);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(6440);
        assertThat(answer.answer2())
                .isEqualTo(5905);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(249483956);
        assertThat(answer.answer2())
                .isEqualTo(252137472);
    }

}

