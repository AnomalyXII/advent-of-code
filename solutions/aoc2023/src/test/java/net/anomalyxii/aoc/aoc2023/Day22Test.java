package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day22Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(22);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 22);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(5);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(480);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(7);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(84021);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(5);
        assertThat(answer.answer2())
                .isEqualTo(7);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(480);
        assertThat(answer.answer2())
                .isEqualTo(84021);
    }

}

