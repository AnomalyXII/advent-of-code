package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day16Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(16);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 16);

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
                .isEqualTo(46);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(7788);
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
                .isEqualTo(51);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(7987);
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
                .isEqualTo(46);
        assertThat(answer.answer2())
                .isEqualTo(51);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day16 challenge = new Day16();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(7788);
        assertThat(answer.answer2())
                .isEqualTo(7987);
    }

}

