package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 21);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 21);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(5);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2324);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("mxmxvkd,sqjhc,fvjkl");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("bxjvzk,hqgqj,sp,spl,hsksz,qzzzf,fmpgn,tpnnkc");
    }

}