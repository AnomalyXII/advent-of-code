package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    private static final SolutionContext EXAMPLE_1 = SolutionContext.example(10);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(10, 2);
    private static final SolutionContext EXAMPLE_3 = SolutionContext.example(10, 3);
    private static final SolutionContext EXAMPLE_4 = SolutionContext.example(10, 4);
    private static final SolutionContext EXAMPLE_5 = SolutionContext.example(10, 5);
    private static final SolutionContext EXAMPLE_6 = SolutionContext.example(10, 6);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 10);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example1() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_1);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart1_Example2() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(8);
    }

    @Test
    void calculateAnswerForPart1_Example3() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_3);

        // assert
        assertThat(answer)
                .isEqualTo(23);
    }

    @Test
    void calculateAnswerForPart1_Example4() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_4);

        // assert
        assertThat(answer)
                .isEqualTo(22);
    }

    @Test
    void calculateAnswerForPart1_Example5() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_5);

        // assert
        assertThat(answer)
                .isEqualTo(70);
    }

    @Test
    void calculateAnswerForPart1_Example6() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE_6);

        // assert
        assertThat(answer)
                .isEqualTo(80);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6875);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example1() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_1);

        // assert
        assertThat(answer)
                .isEqualTo(1);
    }

    @Test
    void calculateAnswerForPart2_Example2() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(1);
    }

    @Test
    void calculateAnswerForPart2_Example3() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_3);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart2_Example4() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_4);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart2_Example5() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_5);

        // assert
        assertThat(answer)
                .isEqualTo(8);
    }

    @Test
    void calculateAnswerForPart2_Example6() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE_6);

        // assert
        assertThat(answer)
                .isEqualTo(10);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(471);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example1() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_1);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(4);
        assertThat(answer.answer2())
                .isEqualTo(1);
    }

    @Test
    void calculateAnswers_Example2() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_2);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(8);
        assertThat(answer.answer2())
                .isEqualTo(1);
    }

    @Test
    void calculateAnswers_Example3() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_3);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(23);
        assertThat(answer.answer2())
                .isEqualTo(4);
    }

    @Test
    void calculateAnswers_Example4() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_4);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(22);
        assertThat(answer.answer2())
                .isEqualTo(4);
    }

    @Test
    void calculateAnswers_Example5() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_5);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(70);
        assertThat(answer.answer2())
                .isEqualTo(8);
    }

    @Test
    void calculateAnswers_Example6() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE_6);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(80);
        assertThat(answer.answer2())
                .isEqualTo(10);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day10 challenge = new Day10();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(6875);
        assertThat(answer.answer2())
                .isEqualTo(471);
    }

}

