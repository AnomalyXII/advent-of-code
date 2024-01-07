package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Day5Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 5);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 5);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static List<Arguments> seats() {
        return asList(
                Arguments.of("FFFFFFFLLL", 0),
                Arguments.of("FFFFFFFRRR", 7),
                Arguments.of("FFFFFFFLRL", 2),
                Arguments.of("FFFFFFFRLR", 5),
                Arguments.of("BBBBBBBLLL", 1016),
                Arguments.of("BBBBBBBRRR", 1023),
                Arguments.of("BBBBBBBLRL", 1018),
                Arguments.of("BBBBBBBRLR", 1021),
                Arguments.of("FBFBFBFLLL", 336),
                Arguments.of("FBFBFBFRRR", 343),
                Arguments.of("FBFBFBFLRL", 338),
                Arguments.of("FBFBFBFRLR", 341),
                Arguments.of("BFBFBFBLLL", 680),
                Arguments.of("BFBFBFBRRR", 687),
                Arguments.of("BFBFBFBLRL", 682),
                Arguments.of("BFBFBFBRLR", 685),

                Arguments.of("FBFBBFFRLR", 357),
                Arguments.of("BFFFBBFRRR", 567),
                Arguments.of("FFFBBBFRRR", 119),
                Arguments.of("BBFFBBFRLL", 820)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(820);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(965);
    }

    // calculateAnswerForPart2

    @Test
    @Disabled("there was no example given for this part, so we can't test it :(")
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        fail("No example given...");
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day5 challenge = new Day5();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(524);
    }

    // calculateSeatId

    @ParameterizedTest
    @MethodSource("seats")
    void calculateSeatId_AllFront_AllLeft(final String pass, final long expected) {
        // arrange

        // act
        final long seat = Day5.calculateSeatId(pass);

        // assert
        assertThat(seat)
                .withFailMessage("Incorrect seat ID for boarding pass [" + pass + "] (" + seat + " != " + expected + ")")
                .isEqualTo(expected);
    }

}
