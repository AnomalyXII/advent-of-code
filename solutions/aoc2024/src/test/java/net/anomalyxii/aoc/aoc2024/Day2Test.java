package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 2);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(442);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(493);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(2);
        assertThat(answer.answer2())
                .isEqualTo(4);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(442);
        assertThat(answer.answer2())
                .isEqualTo(493);
    }

}

