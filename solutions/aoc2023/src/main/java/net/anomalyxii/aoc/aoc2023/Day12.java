package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 12.
 */
@Solution(year = 2023, day = 12, title = "Hot Springs")
public class Day12 {

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
                .map(line -> line.split("\\s+", 2))
                .mapToLong(Day12::process)
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
                .map(line -> line.split("\\s+", 2))
                .map(Day12::unfoldLine)
                .mapToLong(Day12::process)
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
                .map(line -> line.split("\\s+", 2))
                .reduce(
                        LongTuple.NULL,
                        (tup, line) -> tup.add(
                                process(line),
                                process(unfoldLine(line))
                        ),
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Unfold an input line by repeating each side 5 times, joined by a `?`
     * on the left-hand side and a `,` on the right.
     */
    static String[] unfoldLine(final String[] parts) {
        final String unfoldedRecord = "%1$s?%1$s?%1$s?%1$s?%1$s" .formatted(parts[0]);
        final String unfoldedArrangement = "%1$s,%1$s,%1$s,%1$s,%1$s" .formatted(parts[1]);
        return new String[]{unfoldedRecord, unfoldedArrangement};
    }

    /*
     * Process an input line.
     */
    static long process(final String[] parts) {
        final String record = parts[0];
        final int[] arrangement = Arrays.stream(parts[1].split(",\\s*"))
                .mapToInt(Integer::parseInt)
                .toArray();

        final int totalArrangement = IntStream.of(arrangement).sum();
        return process(record, arrangement, 0, totalArrangement, Cache.forArrangements(arrangement));
    }

    /*
     * Calculate the viable interpretations of a given record based on the
     * specified arrangements.
     */
    private static long process(
            final String record,
            final int[] arrangement,
            final int currentArrangement,
            final int totalRemainingArrangement,
            final Cache cache
    ) {
        // Some early exists
        final boolean noMoreArrangements = currentArrangement >= arrangement.length;
        if (record.matches("^[.?]*$") && noMoreArrangements) return 1;
        if (noMoreArrangements) return 0;
        if (totalRemainingArrangement > record.length()) return 0;

        // Cache, because otherwise ugh...
        if (cache.contains(currentArrangement, record)) return cache.get(currentArrangement, record);

        long sum = 0;
        final int nextArrangementLength = arrangement[currentArrangement];
        if (canMakeArrangement(record, nextArrangementLength)) {
            sum += process(
                    popBrokenSprings(record, nextArrangementLength),
                    arrangement,
                    currentArrangement + 1,
                    totalRemainingArrangement - nextArrangementLength,
                    cache
            );
        }

        if (record.charAt(0) != '#')
            sum += process(record.substring(1), arrangement, currentArrangement, totalRemainingArrangement, cache);

        assert sum >= 0;
        cache.set(currentArrangement, record, sum);
        return sum;
    }

    /*
     * Check if the first arrangement can be made from this record.
     */
    private static boolean canMakeArrangement(final String record, final int groupLength) {
        if (record.charAt(0) == '.') return false;
        if (record.length() < groupLength) return false;

        final boolean isGroup = record.substring(0, groupLength).indexOf('.') < 0;
        final boolean isTerminated = record.length() == groupLength || record.charAt(groupLength) != '#';
        return isGroup && isTerminated;
    }

    /*
     * Pop a number of broken springs from the start of a record.
     */
    private static String popBrokenSprings(final String record, final int groupLength) {
        if (record.length() - groupLength == 0) return "";

        final char[] newRecord = new char[record.length() - groupLength];
        System.arraycopy(record.toCharArray(), groupLength, newRecord, 0, newRecord.length);
        assert newRecord[0] != '#';
        newRecord[0] = '.';

        return new String(newRecord);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A somewhat optimised cache for results.
     */
    private record Cache(Map<String, Long>[] caches) {

        // Helper Methods

        /*
         * Check if this cache contains a result.
         */
        boolean contains(final int currentArrangement, final String record) {
            final Map<String, Long> cache = caches[currentArrangement];
            return cache != null && cache.containsKey(record);
        }

        /*
         * Get a result.
         */
        long get(final int currentArrangement, final String record) {
            final Map<String, Long> cache = caches[currentArrangement];
            return cache.get(record);
        }

        /*
         * Set a result.
         */
        void set(final int currentArrangement, final String record, final long result) {
            Map<String, Long> cache = caches[currentArrangement];
            if (cache == null)
                cache = (caches[currentArrangement] = new HashMap<>());
            cache.put(record, result);
        }

        // Static Helper Methods

        @SuppressWarnings("unchecked")
        static Cache forArrangements(final int[] arrangements) {
            return new Cache(new Map[arrangements.length]);
        }
    }

}

