package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(19);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 19);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(6);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(324);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(16);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(575227823167869L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(6);
        assertThat(answer.answer2())
                .isEqualTo(16);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(324);
        assertThat(answer.answer2())
                .isEqualTo(575227823167869L);
    }

}

