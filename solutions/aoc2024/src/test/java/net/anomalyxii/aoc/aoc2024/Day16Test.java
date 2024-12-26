package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day16Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(16);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 16);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(11048);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(109496);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(64);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(551);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(11048);
        assertThat(answer.answer2())
                .isEqualTo(64);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(109496);
        assertThat(answer.answer2())
                .isEqualTo(551);
    }

}

