package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 15);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 15);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1320);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(513172);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final int answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(145);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final int answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(237806);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1320);
        assertThat(answer.answer2())
                .isEqualTo(145);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(513172);
        assertThat(answer.answer2())
                .isEqualTo(237806);
    }

    // hash

    @Test
    void hash_ExampleHash() {
        // arrange

        // act
        final int answer = Day15.hash("HASH");

        // assert
        assertThat(answer)
                .isEqualTo(52);
    }

}

