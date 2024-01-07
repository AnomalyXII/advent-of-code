package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 9);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 9);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> exampleSequences() {
        return Stream.of(
                Arguments.of("0 3 6 9 12 15", -3, 18),
                Arguments.of("1 3 6 10 15 21", 0, 28),
                Arguments.of("10 13 16 21 30 45", 5, 68)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(114);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1974232246);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(928);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final IntTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(114);
        assertThat(answer.answer2())
                .isEqualTo(2);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final IntTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(1974232246);
        assertThat(answer.answer2())
                .isEqualTo(928);
    }

    // calculateNext

    @ParameterizedTest
    @MethodSource("exampleSequences")
    void calculateNext_Examples(final String sequence, final int unused, final int expectedNext) {
        // arrange
        // Nothing to do :)

        // act
        final int next = Day9.calculateNext(sequence);

        // assert
        assertThat(next)
                .isEqualTo(expectedNext);
    }

    // calculatePrev

    @ParameterizedTest
    @MethodSource("exampleSequences")
    void calculatePrev_Examples(final String sequence, final int expectedPrev, final int unused) {
        // arrange
        // Nothing to do :)

        // act
        final int prev = Day9.calculatePrev(sequence);

        // assert
        assertThat(prev)
                .isEqualTo(expectedPrev);
    }

}

