package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(11);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 11);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(55312L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(233875L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(65601038650482L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(277444936413293L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(55312L);
        assertThat(answer.answer2())
                .isEqualTo(65601038650482L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(233875L);
        assertThat(answer.answer2())
                .isEqualTo(277444936413293L);
    }

}

