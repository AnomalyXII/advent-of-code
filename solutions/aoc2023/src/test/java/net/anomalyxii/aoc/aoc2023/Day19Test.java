package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(19);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 19);

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
                .isEqualTo(19114L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(489392L);
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
                .isEqualTo(167409079868000L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(134370637448305L);
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
                .isEqualTo(19114L);
        assertThat(answer.answer2())
                .isEqualTo(167409079868000L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(489392L);
        assertThat(answer.answer2())
                .isEqualTo(134370637448305L);
    }

}

