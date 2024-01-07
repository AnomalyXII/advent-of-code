package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day17Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 17);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 17);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(102);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1256);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(94);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1382);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(102);
        assertThat(answer.answer2())
                .isEqualTo(94);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1256);
        assertThat(answer.answer2())
                .isEqualTo(1382);
    }

}

