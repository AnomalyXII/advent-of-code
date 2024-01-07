package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 18);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 18);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> equations() {
        return Stream.of(
                Arguments.of("1 + 2 * 3 + 4 * 5 + 6", 71),
                Arguments.of("1 + (2 * 3) + (4 * (5 + 6))", 51),
                Arguments.of("2 * 3 + (4 * 5)", 26),
                Arguments.of("5 + (8 * 3 + 9 + 3 * 4 * 3)", 437),
                Arguments.of("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 12240),
                Arguments.of("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", 13632)
        );
    }

    private static Stream<Arguments> equationsAdv() {
        return Stream.of(
                Arguments.of("1 + 2 * 3 + 4 * 5 + 6", 231),
                Arguments.of("1 + (2 * 3) + (4 * (5 + 6))", 51),
                Arguments.of("2 * 3 + (4 * 5)", 46),
                Arguments.of("5 + (8 * 3 + 9 + 3 * 4 * 3)", 1445),
                Arguments.of("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 669060),
                Arguments.of("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", 23340)
        );
    }

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
                .isEqualTo(26457);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(654686398176L);
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
                .isEqualTo(694173);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day18 challenge = new Day18();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(8952864356993L);
    }

    // evaluate

    @ParameterizedTest
    @MethodSource("equations")
    public void evaluate_TestEquation(final String equation, final long expected) {
        // arrange
        // Nothing to do?

        // act
        final long answer = Day18.evaluate(equation);

        // assert
        assertThat(answer)
                .withFailMessage("Expected [" + equation + "] to equal " + expected + " but result was " + answer)
                .isEqualTo(expected);
    }

    // evaluate

    @ParameterizedTest
    @MethodSource("equationsAdv")
    public void evaluateAdv_TestEquation(final String equation, final long expected) {
        // arrange
        // Nothing to do?

        // act
        final long answer = Day18.evaluateAdv(equation);

        // assert
        assertThat(answer)
                .withFailMessage("Expected [" + equation + "] to equal " + expected + " but result was " + answer)
                .isEqualTo(expected);
    }

}