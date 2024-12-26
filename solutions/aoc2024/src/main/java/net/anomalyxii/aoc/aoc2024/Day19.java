package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 19.
 */
@Solution(year = 2024, day = 19, title = "Linen Layout")
public class Day19 {

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
        final List<List<String>> batches = context.readBatches();
        final Map<Character, List<char[]>> towels = stream(batches.getFirst().getFirst().split(",\\s*"))
                .map(String::toCharArray)
                .collect(Collectors.groupingBy(c -> c[0]));
        final List<char[]> patterns = batches.getLast().stream()
                .map(String::toCharArray)
                .toList();

        return patterns.stream()
                .mapToLong(pattern -> tallyPossibleArrangements(pattern, towels))
                .mapToInt(count -> count > 0 ? 1 : 0)
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
        final List<List<String>> batches = context.readBatches();
        final Map<Character, List<char[]>> towels = stream(batches.getFirst().getFirst().split(",\\s*"))
                .map(String::toCharArray)
                .collect(Collectors.groupingBy(c -> c[0]));
        final List<char[]> patterns = batches.getLast().stream()
                .map(String::toCharArray)
                .toList();

        return patterns.stream()
                .mapToLong(pattern -> tallyPossibleArrangements(pattern, towels))
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
        final List<List<String>> batches = context.readBatches();
        final Map<Character, List<char[]>> towels = stream(batches.getFirst().getFirst().split(",\\s*"))
                .map(String::toCharArray)
                .collect(Collectors.groupingBy(c -> c[0]));
        final List<char[]> patterns = batches.getLast().stream()
                .map(String::toCharArray)
                .toList();

        return patterns.stream()
                .reduce(
                        LongTuple.NULL,
                        (result, pattern) -> {
                            final long tally = tallyPossibleArrangements(pattern, towels);
                            return result.add(
                                    tally > 0 ? 1 : 0,
                                    tally
                            );
                        },
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Tally all the possible arrangements of towels to make a pattern.
     */
    private long tallyPossibleArrangements(final char[] pattern, final Map<Character, List<char[]>> allTowels) {
        final long[] counts = new long[pattern.length + 1];
        counts[0] = 1;

        for (int start = 0; start < pattern.length; start++) {
            if (counts[start] == 0) continue;
            final List<char[]> towels = allTowels.get(pattern[start]);
            if (towels == null) continue;

            towels:
            for (final char[] towel : towels) {
                final int end = start + towel.length;
                if (end > pattern.length) continue;
                for (int i = 1; i < towel.length; i++)
                    if (pattern[start + i] != towel[i])
                        continue towels;

                counts[end] += counts[start];
            }
        }

        return counts[pattern.length];
    }

}

