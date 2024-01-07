package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 14);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 14);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(136);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(108759);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(64);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(89089);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(136);
        assertThat(answer.answer2())
                .isEqualTo(64);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day14 challenge = new Day14();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(108759);
        assertThat(answer.answer2())
                .isEqualTo(89089);
    }

}

