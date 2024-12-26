package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 2.
 */
@Solution(year = 2024, day = 2, title = "Red-Nosed Reports")
public class Day2 {

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
    public int calculateAnswerForPart1(final SolutionContext context) {
        return (int) context.stream()
                .map(this::parseAll)
                .filter(levels -> isSafe(levels, 0))
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        return (int) context.stream()
                .map(this::parseAll)
                .filter(levels -> isSafe(levels, 1))
                .count();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        return context.stream()
                .map(this::parseAll)
                .reduce(
                        IntTuple.NULL,
                        (res, line) -> res.incrementIf(isSafe(line, 0), isSafe(line, 1)),
                        IntTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Parse a line into an array of levels.
     */
    private int[] parseAll(final String line) {
        return parseAll(line.split("\\s+"));
    }

    /*
     * Parse a line into an array of levels.
     */
    private int[] parseAll(final String[] line) {
        final int[] result = new int[line.length];
        for (int i = 0; i < line.length; i++) {
            result[i] = Integer.parseInt(line[i]);
        }
        return result;
    }

    /*
     * Check if a given array of levels is safe.
     */
    private static boolean isSafe(final int[] levels, final int tolerance) {
        if (levels.length < 2) return false;

        final int first = levels[0];
        final int second = levels[1];
        return first < second
                ? isSafelyAscending(levels, tolerance)
                : isSafelyDescending(levels, tolerance);
    }

    /*
     * Check if an array of levels is ascending safely.
     */
    private static boolean isSafelyAscending(final int[] levels, final int tolerance) {
        for (int i = 1; i < levels.length; i++) {
            final int prev = levels[i - 1];
            final int next = levels[i];
            final int delta = next - prev;
            if (delta <= 0 || delta > 3)
                return isSafeAfterDroppingLevel(i, levels, tolerance);
        }
        return true;
    }

    /*
     * Check if an array of levels is descending safely.
     */
    private static boolean isSafelyDescending(final int[] levels, final int tolerance) {
        for (int i = 1; i < levels.length; i++) {
            final int prev = levels[i - 1];
            final int next = levels[i];
            final int delta = next - prev;
            if (delta >= 0 || delta < -3)
                return isSafeAfterDroppingLevel(i, levels, tolerance);
        }
        return true;
    }

    /*
     * Check if an array of levels is safe after dropping a bad level.
     */
    private static boolean isSafeAfterDroppingLevel(final int badLevel, final int[] levels, final int tolerance) {
        if (tolerance == 0) return false;
        final int newTolerance = tolerance - 1;
        return isSafe(dropLevel(levels, badLevel - 1), newTolerance)
                || isSafe(dropLevel(levels, badLevel), newTolerance)
                || (badLevel == 2 && isSafe(dropLevel(levels, 0), newTolerance));
    }

    /*
     * Drop a given level from an array of levels.
     */
    private static int[] dropLevel(final int[] levels, final int badLevel) {
        assert badLevel >= 0;
        final int[] dropped = new int[levels.length - 1];
        System.arraycopy(levels, 0, dropped, 0, badLevel);
        System.arraycopy(levels, badLevel + 1, dropped, badLevel, levels.length - badLevel - 1);
        return dropped;
    }
}

