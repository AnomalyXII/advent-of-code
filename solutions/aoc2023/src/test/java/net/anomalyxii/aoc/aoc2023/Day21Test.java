package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 21);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 21);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day21 challenge = new Day21(6, 5000);

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(16);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(3605L);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("The example doesn't actually follow the rules of the puzzle -_-")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day21 challenge = new Day21(6, 5000);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(16733044L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(596734624269210L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(3605L);
        assertThat(answer.answer2())
                .isEqualTo(596734624269210L);
    }

}

