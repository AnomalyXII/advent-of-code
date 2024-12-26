package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 7.
 */
@Solution(year = 2024, day = 7, title = "Bridge Repair")
public class Day7 {

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
    public long calculateAnswerForPart1(final SolutionContext context) {
        return context.stream()
                .map(Calibration::parse)
                .filter(Calibration::isSolvable)
                .mapToLong(Calibration::total)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return context.stream()
                .map(Calibration::parse)
                .filter(Calibration::isSolvableWithConcatenation)
                .mapToLong(Calibration::total)
                .sum();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return a {@link LongTuple} containing the answers for both parts
     */
    @Optimised
    public LongTuple calculateAnswers(final SolutionContext context) {
        return context.stream()
                .map(Calibration::parse)
                .reduce(
                        LongTuple.NULL,
                        (result, calibration) -> {
                            if (calibration.isSolvable())
                                return result.add(calibration.total, calibration.total);
                            else if (calibration.isSolvableWithConcatenation())
                                return result.add(0, calibration.total);
                            return result;
                        },
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A calibration, with all operators removed.
     */
    private record Calibration(long total, int[] parts) {

        // Helper Methods

        /*
         * Check if this calibration is solvable.
         */
        boolean isSolvable() {
            return isSolvable(parts, parts[0], 1, total);
        }

        /*
         * Check if this calibration is solvable when using the concatenation
         * operator.
         */
        boolean isSolvableWithConcatenation() {
            return isSolvableWithConcatenation(parts, parts[0], 1, total);
        }

        // Static Helper Methods

        /*
         * Parse the `Calibration`.
         */
        public static Calibration parse(final String line) {
            final String[] totalAndParts = line.split(":\\s*", 2);

            final long total = Long.parseLong(totalAndParts[0]);
            final String[] parts = totalAndParts[1].split("\\s+");
            return new Calibration(total, stream(parts).mapToInt(Integer::parseInt).toArray());
        }

        /*
         * Check if a given calibration is solvable.
         */
        private static boolean isSolvable(final int[] parts, final long lhs, final int idx, final long total) {
            if (idx == parts.length)
                return lhs == total;

            return isSolvable(parts, lhs + parts[idx], idx + 1, total)
                    || isSolvable(parts, lhs * parts[idx], idx + 1, total);
        }

        /*
         * Check if a given calibration is solvable when using the concatenation
         * operator.
         */
        private static boolean isSolvableWithConcatenation(final int[] parts, final long lhs, final int idx, final long total) {
            if (idx == parts.length)
                return lhs == total;

            return isSolvableWithConcatenation(parts, lhs + parts[idx], idx + 1, total)
                    || isSolvableWithConcatenation(parts, lhs * parts[idx], idx + 1, total)
                    || isSolvableWithConcatenation(parts, concatenate(lhs, parts[idx]), idx + 1, total);
        }

        /*
         * Concatenate two numbers.
         */
        private static long concatenate(final long lhs, final int rhs) {
            final int log = (int) Math.log10(rhs);
            final int power = (int) Math.pow(10, log + 1);
            return lhs * power + rhs;
        }
    }
}

