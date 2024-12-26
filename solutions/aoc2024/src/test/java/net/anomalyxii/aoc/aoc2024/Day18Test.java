package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(18);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 18);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day18 challenge = new Day18(6, 6, 12);

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(22);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(324);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day18 challenge = new Day18(6, 6, 12);

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("6,1");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("46,23");
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day18 challenge = new Day18(6, 6, 12);

        // act
        final ObjectTuple<Integer, String> answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(22);
        assertThat(answer.answer2())
                .isEqualTo("6,1");
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final ObjectTuple<Integer, String> answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(324);
        assertThat(answer.answer2())
                .isEqualTo("46,23");
    }

}

