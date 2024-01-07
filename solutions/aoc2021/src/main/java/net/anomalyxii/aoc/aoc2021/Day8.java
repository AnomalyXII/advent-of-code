package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 8.
 */
@Solution(year = 2021, day = 8, title = "Seven Segment Search")
public class Day8 {

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
        // Ignore the left hand side for now?
        final List<Long> counts = context.process(line -> {
            final String[] parts = line.split("\\s*[|]\\s*", 2);
            // Ignore the left hand side for now?
            final String[] outputs = parts[1].split("\\s+");
            return Arrays.stream(outputs)
                    .map(String::length)
                    .filter(length -> length == 2 || length == 4 || length == 3 || length == 7)
                    .count();
        });

        return counts.stream()
                .mapToLong(i -> i)
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
        final List<Long> results = context.process(line -> {
            final String[] parts = line.split("\\s*[|]\\s*", 2);

            final String[] inputs = parts[0].split("\\s+");
            final String[] outputs = parts[1].split("\\s+");

            return parse(inputs, outputs);
        });

        return results.stream()
                .mapToLong(i -> i)
                .sum();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Parse the inputs and use this to calculate the output.
     */
    private long parse(final String[] inputs, final String[] outputs) {
        final int[] matches = new int[10];
        final int[] reverseMatches = new int[128]; // Sparse, but meh

        // Match 1, 4, 7 and 8, store 0, 2, 3, 5, 6, 9
        final Deque<Integer> remaining = new ArrayDeque<>();
        for (final String input : inputs) {
            if (input.length() == 2) assign(matches, reverseMatches, 1, toInt(input));
            else if (input.length() == 4) assign(matches, reverseMatches, 4, toInt(input));
            else if (input.length() == 3) assign(matches, reverseMatches, 7, toInt(input));
            else if (input.length() == 7) assign(matches, reverseMatches, 8, toInt(input));
            else remaining.addLast(toInt(input));
        }

        while (!remaining.isEmpty()) {
            final int input = remaining.pollFirst();
            if (filterOverlap(input, matches[4], 2, 0, 3, 2)) assign(matches, reverseMatches, 2, input);
            else if (filterOverlap(input, matches[7], 3, 2, 2, 0)) assign(matches, reverseMatches, 3, input);
            else if (filterOverlap(input, matches[4], 3, 1, 2, 1)) assign(matches, reverseMatches, 5, input);
            else if (filterOverlap(input, matches[7], 2, 0, 4, 1)) assign(matches, reverseMatches, 6, input);
            else if (filterOverlap(input, matches[4], 4, 1, 2, 0)) assign(matches, reverseMatches, 9, input);
            else matches[0] = input;
        }

        return Arrays.stream(outputs)
                .mapToInt(this::toInt)
                .mapToLong(val -> reverseMatches[val])
                .reduce(0, (result, next) -> result * 10 + next);
    }

    /*
     * Cache the match.
     */
    private void assign(final int[] matches, final int[] reverseMatches, final int index, final int value) {
        matches[index] = value;
        reverseMatches[value] = index;
    }

    /*
     * Convert the char codes to an int.
     */
    private int toInt(final String input) {
        int result = 0;
        for (final char chr : input.toCharArray()) {
            result |= (1 << (chr - 'a'));
        }
        return result;
    }

    /*
     * Determine the overlap between the given input and the reference value.
     */
    private boolean filterOverlap(final int input, final int reference, final int tp, final int tn, final int fp, final int fn) {
        int tp$ = 0, tn$ = 0, fp$ = 0, fn$ = 0;

        for (int i = 0; i < 7; i++) {
            final int inputBit = (input >> i) & 0x01;
            final int matchBit = (reference >> i) & 0x01;

            if (matchBit == 1 && inputBit == 1) ++tp$;
            else if (matchBit == 1) ++fn$;
            else if (inputBit == 0) ++tn$;
            else ++fp$;
        }

        return tp$ == tp && tn$ == tn && fp$ == fp && fn$ == fn;
    }

}
