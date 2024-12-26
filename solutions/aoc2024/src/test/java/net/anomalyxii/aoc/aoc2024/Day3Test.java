package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day3Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(3);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 3);

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
                .isEqualTo(161);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(188192787L);
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
                .isEqualTo(48L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(113965544L);
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
                .isEqualTo(161L);
        assertThat(answer.answer2())
                .isEqualTo(48L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(188192787L);
        assertThat(answer.answer2())
                .isEqualTo(113965544L);
    }

}

