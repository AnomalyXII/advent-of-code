package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day5Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(5);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 5);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(35);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(510109797);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(46);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(9622622);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(35);
        assertThat(answer.answer2())
                .isEqualTo(46);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(510109797);
        assertThat(answer.answer2())
                .isEqualTo(9622622);
    }

}

