package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day3Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 3);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 3);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(4361L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(532331L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(467835L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(82301120L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(4361L);
        assertThat(answer.answer2())
                .isEqualTo(467835L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(532331L);
        assertThat(answer.answer2())
                .isEqualTo(82301120L);
    }

}

