package net.anomalyxii.aoc.utils.maths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static net.anomalyxii.aoc.utils.maths.Operation.*;
import static org.assertj.core.api.Assertions.assertThat;

class OperationTest {


    // ****************************************
    // Data Provider Methods
    // ****************************************

    /*
     * Some integer mathematics.
     */
    private static Stream<Arguments> intOperations() {
        return Stream.of(
                Arguments.of(1, 2, 3, ADD),
                Arguments.of(2, 1, 3, ADD),
                Arguments.of(1, -2, -1, ADD),
                Arguments.of(-2, 1, -1, ADD),
                Arguments.of(7, 0, 7, ADD),
                Arguments.of(1, Integer.MAX_VALUE, Integer.MIN_VALUE, ADD),
                Arguments.of(3, 2, 1, SUBTRACT),
                Arguments.of(2, 3, -1, SUBTRACT),
                Arguments.of(3, -2, 5, SUBTRACT),
                Arguments.of(-2, 3, -5, SUBTRACT),
                Arguments.of(7, 0, 7, SUBTRACT),
                Arguments.of(Integer.MIN_VALUE, 1, Integer.MAX_VALUE, SUBTRACT),
                Arguments.of(3, 2, 6, MULTIPLY),
                Arguments.of(2, 3, 6, MULTIPLY),
                Arguments.of(3, -2, -6, MULTIPLY),
                Arguments.of(-2, 3, -6, MULTIPLY),
                Arguments.of(1, Integer.MAX_VALUE, Integer.MAX_VALUE, MULTIPLY),
                Arguments.of(4, 2, 2, DIVIDE),
                Arguments.of(-10, 5, -2, DIVIDE),
                Arguments.of(10, -5, -2, DIVIDE),
                Arguments.of(7, 1, 7, DIVIDE)
        );
    }

    /*
     * Some integer mathematics that cannot be reverse engineered due to
     * overflows or precision errors, etc.
     */
    private static Stream<Arguments> intOperationsOneWay() {
        return Stream.of(
                Arguments.of(2, Integer.MAX_VALUE, -2, MULTIPLY),
                Arguments.of(3, 2, 1, DIVIDE),
                Arguments.of(2, 4, 0, DIVIDE),
                Arguments.of(7, 0, 0, MULTIPLY)
        );
    }

    /*
     * Some long mathematics.
     */
    private static Stream<Arguments> longOperations() {
        return Stream.of(
                Arguments.of(1, 2, 3, ADD),
                Arguments.of(2, 1, 3, ADD),
                Arguments.of(1, -2, -1, ADD),
                Arguments.of(-2, 1, -1, ADD),
                Arguments.of(7, 0, 7, ADD),
                Arguments.of(1, Integer.MAX_VALUE, 2147483648L, ADD),
                Arguments.of(1, Long.MAX_VALUE, Long.MIN_VALUE, ADD),
                Arguments.of(3, 2, 1, SUBTRACT),
                Arguments.of(2, 3, -1, SUBTRACT),
                Arguments.of(3, -2, 5, SUBTRACT),
                Arguments.of(-2, 3, -5, SUBTRACT),
                Arguments.of(7, 0, 7, SUBTRACT),
                Arguments.of(Integer.MIN_VALUE, 1, -2147483649L, SUBTRACT),
                Arguments.of(Long.MIN_VALUE, 1, Long.MAX_VALUE, SUBTRACT),
                Arguments.of(3, 2, 6, MULTIPLY),
                Arguments.of(2, 3, 6, MULTIPLY),
                Arguments.of(3, -2, -6, MULTIPLY),
                Arguments.of(-2, 3, -6, MULTIPLY),
                Arguments.of(1, Integer.MAX_VALUE, Integer.MAX_VALUE, MULTIPLY),
                Arguments.of(2, Integer.MAX_VALUE, 4294967294L, MULTIPLY),
                Arguments.of(1, Long.MAX_VALUE, Long.MAX_VALUE, MULTIPLY),
                Arguments.of(-1, Long.MAX_VALUE, -9223372036854775807L, MULTIPLY),
                Arguments.of(1, Long.MIN_VALUE, Long.MIN_VALUE, MULTIPLY),
                Arguments.of(4, 2, 2, DIVIDE),
                Arguments.of(-10, 5, -2, DIVIDE),
                Arguments.of(10, -5, -2, DIVIDE),
                Arguments.of(7, 1, 7, DIVIDE)
        );
    }

    /*
     * Some long mathematics that cannot be reverse engineered due to
     * overflows or precision errors, etc.
     */
    private static Stream<Arguments> longOperationsOneWay() {
        return Stream.of(
                Arguments.of(2, Long.MAX_VALUE, -2, MULTIPLY),
                Arguments.of(-2, Long.MAX_VALUE, 2, MULTIPLY),
                Arguments.of(2, Long.MIN_VALUE, 0L, MULTIPLY), // WAT?
                Arguments.of(-2, Long.MIN_VALUE, 0L, MULTIPLY), // WAT?
                Arguments.of(3, 2, 1, DIVIDE),
                Arguments.of(2, 4, 0, DIVIDE),
                Arguments.of(7, 0, 0, MULTIPLY),
                Arguments.of(-1, Long.MIN_VALUE, -9223372036854775808L, MULTIPLY)
        );
    }

    /*
     * Some double mathematics.
     */
    private static Stream<Arguments> doubleOperations() {
        return Stream.of(
                Arguments.of(1, 2, 3, ADD),
                Arguments.of(2, 1, 3, ADD),
                Arguments.of(1, -2, -1, ADD),
                Arguments.of(-2, 1, -1, ADD),
                Arguments.of(7, 0, 7, ADD),
                Arguments.of(1, Integer.MAX_VALUE, 2147483648L, ADD),
                Arguments.of(3, 2, 1, SUBTRACT),
                Arguments.of(2, 3, -1, SUBTRACT),
                Arguments.of(3, -2, 5, SUBTRACT),
                Arguments.of(-2, 3, -5, SUBTRACT),
                Arguments.of(7, 0, 7, SUBTRACT),
                Arguments.of(Integer.MIN_VALUE, 1, -2147483649L, SUBTRACT),
                Arguments.of(3, 2, 6, MULTIPLY),
                Arguments.of(2, 3, 6, MULTIPLY),
                Arguments.of(3, -2, -6, MULTIPLY),
                Arguments.of(-2, 3, -6, MULTIPLY),
                Arguments.of(1, Integer.MAX_VALUE, Integer.MAX_VALUE, MULTIPLY),
                Arguments.of(2, Integer.MAX_VALUE, 4294967294L, MULTIPLY),
                Arguments.of(1, Double.MAX_VALUE, Double.MAX_VALUE, MULTIPLY),
                Arguments.of(-1, Double.MAX_VALUE, -1.7976931348623157E308, MULTIPLY),
                Arguments.of(1, Double.MIN_VALUE, Double.MIN_VALUE, MULTIPLY),
                Arguments.of(-1, Double.MIN_VALUE, -4.9E-324d, MULTIPLY),
                Arguments.of(2, Double.MIN_VALUE, 1.0E-323d, MULTIPLY), // WAT?
                Arguments.of(-2, Double.MIN_VALUE, -1.0E-323, MULTIPLY), // WAT?
                Arguments.of(4, 2, 2, DIVIDE),
                Arguments.of(3, 2, 1.5d, DIVIDE),
                Arguments.of(2, 4, 0.5d, DIVIDE),
                Arguments.of(-10, 5, -2, DIVIDE),
                Arguments.of(10, -5, -2, DIVIDE),
                Arguments.of(7, 1, 7, DIVIDE),
                Arguments.of(10, 3, 3.333333333333333333333333333d, DIVIDE)
        );
    }

    /*
     * Some double mathematics that cannot be reverse engineered due to
     * overflows or precision errors, etc.
     */
    private static Stream<Arguments> doubleOperationsOneWay() {
        return Stream.of(
                Arguments.of(1, Long.MAX_VALUE, 9223372036854775808d, ADD),
                Arguments.of(1, Double.MAX_VALUE, Double.MAX_VALUE, ADD), // Seems weirdly defined?
                Arguments.of(1, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, ADD),
                Arguments.of(1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, ADD),
                Arguments.of(Double.MIN_VALUE, 1, -1.0d, SUBTRACT), // Seems weirdly defined?
                Arguments.of(1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, SUBTRACT),
                Arguments.of(Double.POSITIVE_INFINITY, 1, Double.POSITIVE_INFINITY, SUBTRACT),
                Arguments.of(1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SUBTRACT),
                Arguments.of(Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY, SUBTRACT),
                Arguments.of(7, 0, 0, MULTIPLY),
                Arguments.of(2, Double.MAX_VALUE, Double.POSITIVE_INFINITY, MULTIPLY),
                Arguments.of(-2, Double.MAX_VALUE, Double.NEGATIVE_INFINITY, MULTIPLY),
                Arguments.of(10, 0, Double.POSITIVE_INFINITY, DIVIDE),
                Arguments.of(10, Double.POSITIVE_INFINITY, 0d, DIVIDE),
                Arguments.of(10, Double.NEGATIVE_INFINITY, -0d, DIVIDE)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // Operation.apply

    @ParameterizedTest(name = "{0} {3} {1} = ? (=> {2})")
    @MethodSource({"intOperations", "intOperationsOneWay"})
    public void apply_int_CorrectAnswer(final int left, final int right, final int expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final int result = op.apply(left, right);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} {3} {1} = ? (=> {2})")
    @MethodSource({"longOperations", "longOperationsOneWay"})
    public void apply_long_CorrectAnswer(final long left, final long right, final long expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final long result = op.apply(left, right);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} {3} {1} = ? (=> {2})")
    @MethodSource({"doubleOperations", "doubleOperationsOneWay"})
    public void apply_double_CorrectAnswer(final double left, final double right, final double expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final double result = op.apply(left, right);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    // Operation.reverseLeft


    @ParameterizedTest(name = "? {3} {1} = {2} (=> {0})")
    @MethodSource("intOperations")
    public void reverseLeft_int_CorrectAnswer(final int left, final int right, final int expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final int result = op.reverseLeft(expected, right);

        // assert
        assertThat(result)
                .isEqualTo(left);
    }

    @ParameterizedTest(name = "? {3} {1} = {2} (=> {0})")
    @MethodSource("longOperations")
    public void reverseLeft_long_CorrectAnswer(final long left, final long right, final long expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final long result = op.reverseLeft(expected, right);

        // assert
        assertThat(result)
                .isEqualTo(left);
    }

    @ParameterizedTest(name = "? {3} {1} = {2} (=> {0})")
    @MethodSource("doubleOperations")
    public void reverseLeft_double_CorrectAnswer(final double left, final double right, final double expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final double result = op.reverseLeft(expected, right);

        // assert
        assertThat(result)
                .isEqualTo(left);
    }

    // Operation.reverseRight

    @ParameterizedTest(name = "{0} {3} ? = {2} (=> {1})")
    @MethodSource("intOperations")
    public void reverseRight_int_CorrectAnswer(final int left, final int right, final int expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final int result = op.reverseRight(expected, left);

        // assert
        assertThat(result)
                .isEqualTo(right);
    }

    @ParameterizedTest(name = "{0} {3} ? = {2} (=> {1})")
    @MethodSource("longOperations")
    public void reverseRight_long_CorrectAnswer(final long left, final long right, final long expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final long result = op.reverseRight(expected, left);

        // assert
        assertThat(result)
                .isEqualTo(right);
    }

    @ParameterizedTest(name = "{0} {3} ? = {2} (=> {1})")
    @MethodSource("doubleOperations")
    public void reverseRight_double_CorrectAnswer(final double left, final double right, final double expected, final Operation op) {
        // arrange
        // Nothing to do...? :)

        // act
        final double result = op.reverseRight(expected, left);

        // assert
        assertThat(result)
                .isEqualTo(right);
    }

}