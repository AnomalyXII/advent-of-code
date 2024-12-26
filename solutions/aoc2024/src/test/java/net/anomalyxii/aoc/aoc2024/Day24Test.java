package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day24Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(24);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 24);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2024L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(46362252142374L);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("Example isn't the same as the live...")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final String answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("z00,z01,z02,z05");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final String answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("cbd,gmh,jmq,qrh,rqf,z06,z13,z38");
    }

}

