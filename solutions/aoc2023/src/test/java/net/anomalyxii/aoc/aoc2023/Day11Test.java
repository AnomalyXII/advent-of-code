package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(11);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 11);

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
                .isEqualTo(374L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(9681886L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example1() {
        // arrange
        final Day11 challenge = new Day11(10);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1030L);
    }

    @Test
    void calculateAnswerForPart2_Example2() {
        // arrange
        final Day11 challenge = new Day11(100);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(8410L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(791134099634L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example1() {
        // arrange
        final Day11 challenge = new Day11(10);

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(374L);
        assertThat(answer.answer2())
                .isEqualTo(1030L);
    }

    @Test
    void calculateAnswers_Example2() {
        // arrange
        final Day11 challenge = new Day11(100);

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(374L);
        assertThat(answer.answer2())
                .isEqualTo(8410L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day11 challenge = new Day11();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(9681886L);
        assertThat(answer.answer2())
                .isEqualTo(791134099634L);
    }

}

