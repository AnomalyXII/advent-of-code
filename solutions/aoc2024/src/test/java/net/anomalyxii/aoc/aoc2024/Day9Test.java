package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    private static final int SMALL_DISK_SIZE = 64;

    private static final SolutionContext EXAMPLE = SolutionContext.example(9);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 9);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day9 challenge = new Day9(SMALL_DISK_SIZE);

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1928L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6421128769094L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day9 challenge = new Day9(SMALL_DISK_SIZE);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2858L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6448168620520L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day9 challenge = new Day9(SMALL_DISK_SIZE);

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1928L);
        assertThat(answer.answer2())
                .isEqualTo(2858L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(6421128769094L);
        assertThat(answer.answer2())
                .isEqualTo(6448168620520L);
    }

}

