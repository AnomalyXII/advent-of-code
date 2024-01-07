package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 6.
 */
@Solution(year = 2023, day = 6, title = "Wait For It")
public class Day6 {

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
        final List<String> lines = context.read();
        final long[] durations = stream(lines.getFirst().substring(5).split("\\s+"))
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray();
        final long[] distances = stream(lines.getLast().substring(9).split("\\s+"))
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray();

        return IntStream.range(0, durations.length)
                .map(r -> calculateNumberOfWaysToWinTheRace(durations[r], distances[r]))
                .reduce((a, b) -> a * b)
                .orElse(0);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final List<String> lines = context.read();
        final long duration = Long.parseLong(lines.getFirst().substring(5).replaceAll("\\s+", ""));
        final long distance = Long.parseLong(lines.getLast().substring(9).replaceAll("\\s+", ""));

        return calculateNumberOfWaysToWinTheRace(duration, distance);
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
        final List<String> lines = context.read();

        final long[] durations = stream(lines.getFirst().substring(5).split("\\s+"))
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray();
        final long[] distances = stream(lines.getLast().substring(9).split("\\s+"))
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray();

        final long longDuration = Long.parseLong(lines.getFirst().substring(5).replaceAll("\\s+", ""));
        final long longDistance = Long.parseLong(lines.getLast().substring(9).replaceAll("\\s+", ""));

        return new IntTuple(
                IntStream.range(0, durations.length)
                        .map(r -> calculateNumberOfWaysToWinTheRace(durations[r], distances[r]))
                        .reduce((a, b) -> a * b)
                        .orElse(0),
                calculateNumberOfWaysToWinTheRace(longDuration, longDistance)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the number of ways there are to win a race.
     */
    private static int calculateNumberOfWaysToWinTheRace(final long duration, final long distance) {
        // y = x * (duration - x)
        // :. y = -1 * x^2 + duration * x
        // :. solve for y > distance
        // :. distance = -1 * x^2 + duration * x
        // :. 0 = -1 * x^2 + duration * x - distance
        // :. x = (-duration +- sqrt(duration^2 - 4*-1*-distance) / (2 * -1)
        final double coefficient = Math.sqrt(duration * duration - 4 * (distance + 0.01)); // We want to be _better_ than the distance, so add a little bit
        final int first = (int) Math.floor(0.5 * (duration - coefficient));
        final int second = (int) Math.floor(0.5 * (duration + coefficient));

        return second - first;
    }

}

