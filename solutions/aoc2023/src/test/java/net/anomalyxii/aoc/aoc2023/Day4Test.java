package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day4Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(4);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 4);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(13);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(23941);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(30);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(5571760);
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
                .isEqualTo(13);
        assertThat(answer.answer2())
                .isEqualTo(30);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(23941);
        assertThat(answer.answer2())
                .isEqualTo(5571760);
    }

}

