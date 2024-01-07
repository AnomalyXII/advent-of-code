package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.*;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 7.
 */
@Solution(year = 2021, day = 7, title = "The Treachery of Whales")
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
        final int[] startingPositions = readStartingPositions(context);
        final long midpoint = startingPositions.length % 2 != 0
                ? startingPositions[(startingPositions.length / 2) - 1]
                : round(((double) (startingPositions[(startingPositions.length / 2) - 1] + startingPositions[startingPositions.length / 2])) / 2);
        return IntStream.of(startingPositions)
                .mapToLong(position -> Math.abs(position - midpoint))
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
        final int[] startingPositions = readStartingPositions(context);
        final double average = IntStream.of(readStartingPositions(context))
                .summaryStatistics()
                .getAverage();
        final long midpointA = round(floor(average));
        final long midpointB = round(ceil(average));
        return midpointA != midpointB
                ? min(calculateFuelUse(startingPositions, midpointA), calculateFuelUse(startingPositions, midpointB))
                : calculateFuelUse(startingPositions, midpointA);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Read in all the starting positions to an `int` array.
     */
    private int[] readStartingPositions(final SolutionContext context) {
        final List<String> input = context.read();
        final String line = input.getFirst();

        return Arrays.stream(line.split(",\\s*"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .toArray();
    }

    private long calculateFuelUse(final int[] startingPositions, final long midpoint) {
        return IntStream.of(startingPositions)
                .mapToLong(position -> Math.abs(position - midpoint))
                .map(distance -> (distance * (distance + 1)) / 2)
                .sum();
    }
}
