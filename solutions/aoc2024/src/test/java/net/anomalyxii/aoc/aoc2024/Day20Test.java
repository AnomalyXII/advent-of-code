package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day20Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(20);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 20);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day20 challenge = new Day20(10, 50);

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(10);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1485);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day20 challenge = new Day20(10, 50);

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(285);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1027501);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day20 challenge = new Day20(10, 50);

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(10);
        assertThat(answer.answer2())
                .isEqualTo(285);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1485);
        assertThat(answer.answer2())
                .isEqualTo(1027501);
    }

}

