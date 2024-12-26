package net.anomalyxii.aoc.utils.maths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static net.anomalyxii.aoc.utils.maths.Factors.highestCommonFactor;
import static net.anomalyxii.aoc.utils.maths.Factors.lowestCommonMultiple;
import static org.assertj.core.api.Assertions.assertThat;

class FactorsTest {

    // ****************************************
    // Data Provider Methods
    // ****************************************

    /*
     * Example LCMs for `int`s.
     */
    private static Stream<Arguments> lcmInt() {
        return Stream.of(
                Arguments.of(10, 5, 10),
                Arguments.of(7, 3, 21),
                Arguments.of(2147483647, 2, 4294967294L)
        );
    }

    /*
     * Example HCFs for `int`s.
     */
    private static Stream<Arguments> hcfInt() {
        return Stream.of(
                Arguments.of(10, 5, 5),
                Arguments.of(100, 5, 5),
                Arguments.of(100, 50, 50),
                Arguments.of(100, 75, 25),
                Arguments.of(7873, 7877, 1), // two primes
                Arguments.of(2, -4, -2), // negatives
                Arguments.of(-2, -4, -2) // negatives
        );
    }

    /*
     * Example HCFs for `int`s.
     */
    private static Stream<Arguments> hcfLong() {
        return Stream.of(
                Arguments.of(10L, 5L, 5L),
                Arguments.of(100L, 5L, 5L),
                Arguments.of(100L, 50L, 50L),
                Arguments.of(100L, 75L, 25L),
                Arguments.of(7873L, 7877L, 1L), // two primes
                Arguments.of(2L, -4L, -2L), // negatives
                Arguments.of(-2L, -4L, -2L), // negatives
                Arguments.of(12312432523525L, 1477491902823L, 492497300941L)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    @ParameterizedTest
    @MethodSource("lcmInt")
    void lowestCommonMultiple_is_correct(final int first, final int second, final long expected) {
        // arrange
        // Nothing to see here! :)

        // act
        final long lcm = lowestCommonMultiple(first, second);

        // assert
        assertThat(lcm)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("hcfInt")
    void highestCommonFactor_is_correct_for_int_values(final int first, final int second, final int expected) {
        // arrange
        // Nothing to see here! :)

        // act
        final long hcf = highestCommonFactor(first, second);

        // assert
        assertThat(hcf)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("hcfLong")
    void highestCommonFactor_is_correct_for_long_values(final long first, final long second, final long expected) {
        // arrange
        // Nothing to see here! :)

        // act
        final long hcf = highestCommonFactor(first, second);

        // assert
        assertThat(hcf)
                .isEqualTo(expected);
    }
}