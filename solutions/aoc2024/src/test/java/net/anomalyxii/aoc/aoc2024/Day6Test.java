package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(6);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 6);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(41);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(4883);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(6);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1655);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(41);
        assertThat(answer.answer2())
                .isEqualTo(6);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day6 challenge = new Day6();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(4883);
        assertThat(answer.answer2())
                .isEqualTo(1655);
    }

}

