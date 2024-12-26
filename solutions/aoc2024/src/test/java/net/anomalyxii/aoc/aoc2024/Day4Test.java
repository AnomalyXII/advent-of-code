package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day4Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(4);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 4);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(18);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2551);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(9);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1985);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(18);
        assertThat(answer.answer2())
                .isEqualTo(9);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(2551);
        assertThat(answer.answer2())
                .isEqualTo(1985);
    }

}

