package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(8);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 8);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(14);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(398);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(34);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1333);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(14);
        assertThat(answer.answer2())
                .isEqualTo(34);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(398);
        assertThat(answer.answer2())
                .isEqualTo(1333);
    }

}

