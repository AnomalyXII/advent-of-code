package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 18);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 18);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(62);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(48503L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(952408144115L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(148442153147147L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(62);
        assertThat(answer.answer2())
                .isEqualTo(952408144115L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(48503L);
        assertThat(answer.answer2())
                .isEqualTo(148442153147147L);
    }

}

