package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day7Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(7);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 7);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(3749L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(882304362421L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(11387L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(145149066755184L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(3749L);
        assertThat(answer.answer2())
                .isEqualTo(11387L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(882304362421L);
        assertThat(answer.answer2())
                .isEqualTo(145149066755184L);
    }

}

