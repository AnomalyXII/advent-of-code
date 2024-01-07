package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 25);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 25);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart10L

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(54);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(556467);
    }

}

