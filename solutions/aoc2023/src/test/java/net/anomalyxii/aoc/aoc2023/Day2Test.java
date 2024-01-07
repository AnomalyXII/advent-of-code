package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 2);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(8L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2683L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2286L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(49710L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(8L);
        assertThat(answer.answer2())
                .isEqualTo(2286L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(2683L);
        assertThat(answer.answer2())
                .isEqualTo(49710L);
    }

}

