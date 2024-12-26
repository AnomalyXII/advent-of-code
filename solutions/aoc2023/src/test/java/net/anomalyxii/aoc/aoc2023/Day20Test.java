package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day20Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(20);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(20, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 20);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(32000000L);
    }

    @Test
    void calculateAnswerForPart1_Example2() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(11687500L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(898557000L);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("No example given :(")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(238420328103151L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(898557000L);
        assertThat(answer.answer2())
                .isEqualTo(238420328103151L);
    }

}

