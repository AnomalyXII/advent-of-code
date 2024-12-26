package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day17Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(17);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(17, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 17);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final String answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("4,6,3,5,6,3,5,2,1,0");
    }

    @Test
    void calculateAnswerForPart1_Example2() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final String answer = challenge.calculateAnswerForPart1(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo("5,7,3,0");
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final String answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("4,3,2,6,4,5,3,2,4");
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(117440L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(164540892147389L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final ObjectTuple<String, Long> answer = challenge.calculateAnswers(EXAMPLE_2);

        // assert
        assertThat(answer.answer1())
                .isEqualTo("5,7,3,0");
        assertThat(answer.answer2())
                .isEqualTo(117440L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day17 challenge = new Day17();

        // act
        final ObjectTuple<String, Long> answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo("4,3,2,6,4,5,3,2,4");
        assertThat(answer.answer2())
                .isEqualTo(164540892147389L);
    }

}

