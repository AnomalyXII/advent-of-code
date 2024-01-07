package net.anomalyxii.aoc.utils.maths;

import static java.lang.Math.abs;

/**
 * Some maths utility functions related to common factors.
 */
public class Factors {

    // ****************************************
    // Factors Methods
    // ****************************************

    /**
     * Find the lowest common multiple of two numbers.
     *
     * @param first  the first number
     * @param second the second number
     * @return the lowest common multiple
     */
    public static long lowestCommonMultiple(final long first, final long second) {
        final long result = abs(first * second) / highestCommonFactor(first, second);
        assert result % first == 0 : "Result was not a multiple of " + first + " ... long overflow?";
        assert result % second == 0 : "Result was not a multiple of " + second + " ... long overflow?";
        return result;
    }

    /**
     * Find the highest common factor of two numbers.
     *
     * @param first  the first number
     * @param second the second number
     * @return the highest common factor
     */
    public static int highestCommonFactor(final int first, final int second) {
        int t;
        int a = abs(first);
        int b = abs(second);
        while (b != 0) {
            t = b;
            b = a % b;
            a = t;
        }

        return first < 0 || second < 0 ? -a : a;
    }

    /**
     * Find the highest common factor of two numbers.
     *
     * @param first  the first number
     * @param second the second number
     * @return the highest common factor
     */
    public static long highestCommonFactor(final long first, final long second) {
        long t;
        long a = abs(first);
        long b = abs(second);
        while (b != 0) {
            t = b;
            b = a % b;
            a = t;
        }

        return first < 0 || second < 0 ? -a : a;
    }

}
