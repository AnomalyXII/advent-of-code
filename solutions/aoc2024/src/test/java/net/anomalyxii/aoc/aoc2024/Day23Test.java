package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day23Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(23);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 23);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(7);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1200);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("co,de,ka,ta");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("ag,gh,hh,iv,jx,nq,oc,qm,rb,sm,vm,wu,zr");
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final ObjectTuple<Integer, String> answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(7);
        assertThat(answer.answer2())
                .isEqualTo("co,de,ka,ta");
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day23 challenge = new Day23();

        // act
        final ObjectTuple<Integer, String> answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1200);
        assertThat(answer.answer2())
                .isEqualTo("ag,gh,hh,iv,jx,nq,oc,qm,rb,sm,vm,wu,zr");
    }

}

