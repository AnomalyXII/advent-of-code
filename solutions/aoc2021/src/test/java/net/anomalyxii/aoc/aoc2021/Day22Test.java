package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day22Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2021, 22);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(2021, 22, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 22);

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
                .isEqualTo(590784L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(580098L);
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
                .isEqualTo(2758514936282235L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1134725012490723L);
    }

}
