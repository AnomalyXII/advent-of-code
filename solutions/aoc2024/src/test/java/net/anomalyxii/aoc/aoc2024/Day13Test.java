package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(13);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 13);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(480L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(29877L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(875318608908L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(99423413811305L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(480L);
        assertThat(answer.answer2())
                .isEqualTo(875318608908L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day13 challenge = new Day13();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(29877L);
        assertThat(answer.answer2())
                .isEqualTo(99423413811305L);
    }

}

