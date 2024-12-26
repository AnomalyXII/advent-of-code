package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(19);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(19, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 19);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> grammars() {
        return Stream.of(
                Arguments.of("\"a\"", new String[0], "a", true),
                Arguments.of("\"a\"", new String[0], "b", false),
                Arguments.of("\"a\"", new String[0], "a a", false),
                Arguments.of("\"a\"", new String[0], "", false),
                Arguments.of("\"a\" \"a\"", new String[0], "a a", true),
                Arguments.of("\"a\" \"a\"", new String[0], "a b", false),
                Arguments.of("\"a\" \"a\"", new String[0], "a a b", false),
                Arguments.of("\"a\" \"a\"", new String[0], "a", false),
                Arguments.of("\"a\" \"b\" \"a\"", new String[0], "a b a", true),
                Arguments.of("\"a\" \"b\" \"a\"", new String[0], "a b c", false),
                Arguments.of("\"a\" | \"b\"", new String[0], "a", true),
                Arguments.of("\"a\" | \"b\"", new String[0], "b", true),
                Arguments.of("\"a\" | \"b\"", new String[0], "c", false),
                Arguments.of("\"a\" | \"b\"", new String[0], "a a", false),
                Arguments.of("\"a\" | \"b\"", new String[0], "a b", false),
                Arguments.of("\"a\" | \"b\"", new String[0], "b a", false),
                Arguments.of("\"a\" | \"b\"", new String[0], "c a", false),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "a b", true),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "a a", true),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "b a", false),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "a b a", false),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "a a b", false),
                Arguments.of("\"a\" \"a\" | \"a\" \"b\"", new String[0], "b a a", false),
                Arguments.of("0", new String[]{"\"a\""}, "a", true),
                Arguments.of("0", new String[]{"\"a\""}, "b", false),
                Arguments.of("0", new String[]{"\"a\""}, "a a", false),
                Arguments.of("0", new String[]{"\"a\""}, "", false),
                Arguments.of("0 0", new String[]{"\"a\""}, "a a", true),
                Arguments.of("0 0", new String[]{"\"a\""}, "a b", false),
                Arguments.of("0 0", new String[]{"\"a\""}, "a b a", false),
                Arguments.of("0 0", new String[]{"\"a\""}, "a", false),
                Arguments.of("0 \"b\"", new String[]{"\"a\""}, "a b", true),
                Arguments.of("0 \"b\"", new String[]{"\"a\""}, "a a", false),
                Arguments.of("0 \"b\"", new String[]{"\"a\""}, "a a b", false),
                Arguments.of("0 \"b\"", new String[]{"\"a\""}, "a b a", false),
                Arguments.of("0 0 \"b\"", new String[]{"\"a\""}, "a a b", true),
                Arguments.of("0 0 \"b\"", new String[]{"\"a\""}, "a b", false),
                Arguments.of("0 0 \"b\"", new String[]{"\"a\""}, "a a b a", false),
                Arguments.of("0 0 \"b\"", new String[]{"\"a\""}, "a b a a", false),
                Arguments.of("0 \"b\" 0", new String[]{"\"a\""}, "a b a", true),
                Arguments.of("0 \"b\" 0", new String[]{"\"a\""}, "a a b", false),
                Arguments.of("0 \"b\" 0", new String[]{"\"a\""}, "b a a", false),
                Arguments.of("0 \"b\" 0", new String[]{"\"a\""}, "a b a a", false),
                Arguments.of("0 | 1", new String[]{"\"a\"", "\"b\""}, "a", true),
                Arguments.of("0 | 1", new String[]{"\"a\"", "\"b\""}, "b", true),
                Arguments.of("0 | 1", new String[]{"\"a\"", "\"b\""}, "c", false),
                Arguments.of("0 | 1", new String[]{"\"a\"", "\"b\""}, "a b", false),
                Arguments.of("0 | 1", new String[]{"\"a\"", "\"b\""}, "b a", false),
                Arguments.of("0 0 | 1 1", new String[]{"\"a\"", "\"b\""}, "a a", true),
                Arguments.of("0 0 | 1 1", new String[]{"\"a\"", "\"b\""}, "b b", true),
                Arguments.of("0 0 | 1 1", new String[]{"\"a\"", "\"b\""}, "a b", false),
                Arguments.of("0 0 | 1 1", new String[]{"\"a\"", "\"b\""}, "b a", false),
                Arguments.of("0 1 | 1 0", new String[]{"\"a\"", "\"b\""}, "a b", true),
                Arguments.of("0 1 | 1 0", new String[]{"\"a\"", "\"b\""}, "b a", true),
                Arguments.of("0 1 | 1 0", new String[]{"\"a\"", "\"b\""}, "a a", false),
                Arguments.of("0 1 | 1 0", new String[]{"\"a\"", "\"b\""}, "b b", false),
                Arguments.of("0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a", true),
                Arguments.of("0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "b", true),
                Arguments.of("0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "c", false),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a a", true),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a b", true),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "b a", true),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "b b", true),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a a a", false),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "b b b", false),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a b a", false),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "a b b", false),
                Arguments.of("0 0", new String[]{"1 | 2", "\"a\"", "\"b\""}, "c c", false),
                Arguments.of("4 1 5", new String[]{"-", "2 3 | 3 2", "4 4 | 5 5", "4 5 | 5 4", "\"a\"", "\"b\""}, "a b a b b b", true),
                Arguments.of("4 1 5", new String[]{"-", "2 3 | 3 2", "4 4 | 5 5", "4 5 | 5 4", "\"a\"", "\"b\""}, "a b b b a b", true),
                Arguments.of("4 1 5", new String[]{"-", "2 3 | 3 2", "4 4 | 5 5", "4 5 | 5 4", "\"a\"", "\"b\""}, "b a b a b a", false),
                Arguments.of("4 1 5", new String[]{"-", "2 3 | 3 2", "4 4 | 5 5", "4 5 | 5 4", "\"a\"", "\"b\""}, "a a a b b b", false),
                Arguments.of("4 1 5", new String[]{"-", "2 3 | 3 2", "4 4 | 5 5", "4 5 | 5 4", "\"a\"", "\"b\""}, "a a a a b b b", false)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart1_ExamplePart2() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(3);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(104);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(12);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(314);
    }

    // isValid

    @Test
    void isValid_ExampleGrammar() {
        // arrange
        final String[] grammar = {
                "P -> S",
                "S -> S \"+\" M | M ",
                "M -> M \"*\" T | T",
                "T -> \"1\" | \"2\" | \"3\" | \"4\"",
        };

        // act
        final boolean matches = Day19.isValid("2 + 3 * 4", grammar);

        // assert
        assertThat(matches)
                .withFailMessage("Expected '2 + 3 * 4' to match the example grammar")
                .isTrue();
    }

    @ParameterizedTest(name = "{0} == '{1}'")
    @MethodSource("grammars")
    public void isValid_TestVariousGrammars(
            final String rule,
            final String[] others,
            final String input,
            final boolean expectedMatch
    ) {
        // arrange
        // Nothing to do?

        // act
        final boolean matches = Day19.isValid(input, rule, others);

        // assert
        assertThat(matches)
                .withFailMessage("Expected input '" + input + "' to " + (expectedMatch ? "match" : "not match") + " [" + rule + "]")
                .isEqualTo(expectedMatch);
    }

}
