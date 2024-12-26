package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(25);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 25);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final int answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(3);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final int answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(3291);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final NoChallenge answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(NoChallenge.NO_CHALLENGE);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final NoChallenge answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(NoChallenge.NO_CHALLENGE);
    }

}

