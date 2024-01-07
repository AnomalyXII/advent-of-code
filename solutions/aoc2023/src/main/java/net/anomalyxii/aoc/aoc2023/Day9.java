package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 9.
 */
@Solution(year = 2023, day = 9, title = "Mirage Maintenance")
public class Day9 {

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
        return context.stream()
                .mapToInt(Day9::calculateNext)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        return context.stream()
                .mapToInt(Day9::calculatePrev)
                .sum();
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
                .map(line -> stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .reduce(
                        IntTuple.NULL,
                        (tup, seq) -> tup.add(
                                calculateNext(seq),
                                calculatePrev(seq)
                        ),
                        IntTuple::add
                );
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /*
     * Parse a line of numbers and extrapolate the next number in the
     * sequence.
     */
    static int calculateNext(final String input) {
        return calculateNext(
                stream(input.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray()
        );
    }

    /*
     * Parse a line of numbers and extrapolate the previous number in the
     * sequence.
     */
    static int calculatePrev(final String input) {
        return calculatePrev(
                stream(input.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray()
        );
    }

    /*
     * Extrapolate the next number in the sequence.
     */
    private static int calculateNext(final int[] input) {
        final int[] deltas = new int[input.length - 1];
        final boolean allZeros = fillInDeltas(input, deltas);

        if (allZeros) return input[0];
        else return input[input.length - 1] + calculateNext(deltas);
    }

    /*
     * Extrapolate the previous number in the sequence.
     */
    private static int calculatePrev(final int[] input) {
        final int[] deltas = new int[input.length - 1];
        final boolean allZeros = fillInDeltas(input, deltas);

        if (allZeros) return input[0];
        else return input[0] - calculatePrev(deltas);
    }

    /*
     * Calculate the differences between each pair of input numbers.
     */
    private static boolean fillInDeltas(final int[] input, final int[] deltas) {
        assert input.length > 1 : "Need at least 2 points to extrapolate from!";

        boolean allZeros = true;
        for (int i = 1; i < input.length; i++)
            allZeros &= (deltas[i - 1] = input[i] - input[i - 1]) == 0;
        return allZeros;
    }

}

