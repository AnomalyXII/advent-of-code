package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 8);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(2023, 8, 2);
    private static final SolutionContext EXAMPLE_3 = SolutionContext.example(2023, 8, 3);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 8);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2L);
    }

    @Test
    void calculateAnswerForPart1_Example2() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(6L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(13939L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_3);

        // assert
        assertThat(answer)
                .isEqualTo(6L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(8906539031197L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE_2);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(6L);
        assertThat(answer.answer2())
                .isEqualTo(6L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day8 challenge = new Day8();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(13939L);
        assertThat(answer.answer2())
                .isEqualTo(8906539031197L);
    }

}

