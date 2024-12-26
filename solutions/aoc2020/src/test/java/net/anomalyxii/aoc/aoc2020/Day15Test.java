package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(15);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 15);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> exampleGames() {
        return Stream.of(
                Arguments.of(new int[]{1, 3, 2}, 2020, 1),
                Arguments.of(new int[]{2, 1, 3}, 2020, 10),
                Arguments.of(new int[]{1, 2, 3}, 2020, 27),
                Arguments.of(new int[]{2, 3, 1}, 2020, 78),
                Arguments.of(new int[]{3, 2, 1}, 2020, 438),
                Arguments.of(new int[]{3, 1, 2}, 2020, 1836),
                Arguments.of(new int[]{1, 3, 2}, 30000000, 2578),
                Arguments.of(new int[]{2, 1, 3}, 30000000, 3544142),
                Arguments.of(new int[]{1, 2, 3}, 30000000, 261214),
                Arguments.of(new int[]{2, 3, 1}, 30000000, 6895259),
                Arguments.of(new int[]{3, 2, 1}, 30000000, 18),
                Arguments.of(new int[]{3, 1, 2}, 30000000, 362)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(436);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1665);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(175594);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day15 challenge = new Day15();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(16439);
    }

    // playGame

    @ParameterizedTest
    @MethodSource("exampleGames")
    public void playGame_Example(final int[] startingNumbers, final int rounds, final long expected) {
        // arrange
        // Nothing to do? :)

        // act
        final long answer = Day15.playGame(rounds, startingNumbers);

        // assert
        assertThat(answer)
                .isEqualTo(expected);
    }

}