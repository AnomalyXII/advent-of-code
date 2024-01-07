package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 25.
 */
@Solution(year = 2022, day = 25, title = "Full of Hot Air")
public class Day25 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     */
    @Part(part = I)
    public String calculateAnswerForPart1(final SolutionContext context) {
        final List<String> snafus = context.read();
        final long result = snafus.stream()
                .mapToLong(Day25::snafu2dec)
                .sum();

        return dec2snafu(result);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return -
     */
    @Part(part = II)
    public NoChallenge calculateAnswerForPart2(final SolutionContext context) {
        return NoChallenge.NO_CHALLENGE;
    }


    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Convert a SNAFU number into a decimal.
     *
     * @param snafu the SNAFU number
     * @return the decimal representation
     */
    static long snafu2dec(final String snafu) {

        long decimal = 0;
        for (int i = 0; i < snafu.length(); i++) {
            final char c = snafu.charAt((snafu.length() - 1) - i);
            decimal += (long) ((Math.pow(5, i)) * convert(c));
        }

        return decimal;

    }

    /**
     * Convert a decimal into a SNAFU number.
     *
     * @param decimal the decimal
     * @return the SNAFU number
     */
    static String dec2snafu(final long decimal) {
        final List<Character> snafu = new ArrayList<>();
        long remaining = decimal;
        while (remaining > 0) {
            final int leftover = (int) (remaining % 5);
            remaining = (remaining + 2) / 5;

            snafu.add(0, switch (leftover) {
                case 0 -> '0';
                case 1 -> '1';
                case 2 -> '2';
                case 3 -> '=';
                case 4 -> '-';
                default -> throw new IllegalArgumentException("Invalid SNAFU digit: " + leftover);
            });
        }

        return snafu.stream().map(c -> Character.toString(c)).collect(Collectors.joining());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Convert a SNAFU character into a corresponding decimal
     */
    private static int convert(final char c) {
        return switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '-' -> -1;
            case '=' -> -2;
            default -> throw new IllegalArgumentException("Invalid SNAFU digit: " + c);
        };
    }

}

