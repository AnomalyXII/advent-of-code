package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day22Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(22);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(22, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 22);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(37327623L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(19822877190L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(23L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2277L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(37327623L);
        assertThat(answer.answer2())
                .isEqualTo(23L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(19822877190L);
        assertThat(answer.answer2())
                .isEqualTo(2277L);
    }

}

