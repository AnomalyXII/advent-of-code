package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day24Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 24);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 24);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    /*
     * Example intersections
     */
    private static Stream<Arguments> examples() {
        return Stream.of(
                Arguments.of(
                        new Day24.Hailstone(19, 13, 30, -2, 1, -2),
                        new Day24.Hailstone(18, 19, 22, -1, -1, -2),
                        Day24.IntersectionPoint.of(43 / 3d, 46 / 3d, 76 / 3d)
                ),
                Arguments.of(
                        new Day24.Hailstone(19, 13, 30, -2, 1, -2),
                        new Day24.Hailstone(20, 25, 34, -2, -2, -4),
                        Day24.IntersectionPoint.of(35 / 3d, 50 / 3d, 68 / 3d)
                ),
                Arguments.of(
                        new Day24.Hailstone(19, 13, 30, -2, 1, -2),
                        new Day24.Hailstone(12, 31, 28, -1, -2, -1),
                        Day24.IntersectionPoint.of(6.2d, 19.4d, 17.2d)
                ),
                Arguments.of(
                        new Day24.Hailstone(19, 13, 30, -2, 1, -2),
                        new Day24.Hailstone(20, 19, 15, 1, -5, -3),
                        Day24.IntersectionPoint.NULL
                ),
                Arguments.of(
                        new Day24.Hailstone(18, 19, 22, -1, -1, -2),
                        new Day24.Hailstone(20, 25, 34, -2, -2, -4),
                        Day24.IntersectionPoint.NULL
                )
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day24 challenge = new Day24(7, 27);

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(26657);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day24 challenge = new Day24(7, 27);

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(47);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day24 challenge = new Day24();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(828418331313365L);
    }

    // intersectXY

    @ParameterizedTest
    @MethodSource("examples")
    void intersectXY_Examples(final Day24.Hailstone first, final Day24.Hailstone second, final Day24.IntersectionPoint expected) {
        // arrange
        // Nothing to see here! :)

        // act
        final Day24.IntersectionPoint result = first.intersectXY(second);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

}

