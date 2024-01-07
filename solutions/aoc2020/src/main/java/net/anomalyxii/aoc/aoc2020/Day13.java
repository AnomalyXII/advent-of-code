package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 13.
 */
@Solution(year = 2020, day = 13, title = "Shuttle Search")
public class Day13 {

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
        final List<String> lines = context.read();

        final long arrivalTime = Long.parseLong(lines.getFirst());
        final int[] busIds = Arrays.stream(lines.get(1).split(","))
                .filter(id -> !"x".equalsIgnoreCase(id))
                .mapToInt(Integer::parseInt)
                .toArray();

        long id = -1;
        long remainingMinutes = Long.MAX_VALUE;

        for (final int busId : busIds) {
            final long lastDepartureTime = (arrivalTime / busId) * busId;
            final long nextDepartureTime = lastDepartureTime + busId;

            final long timeToWait = nextDepartureTime - arrivalTime;
            if (timeToWait < remainingMinutes) {
                id = busId;
                remainingMinutes = timeToWait;
            }
        }

        return id * remainingMinutes;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final int[] busIds = Arrays.stream(context.read().get(1).split(","))
                .mapToInt(id -> "x".equalsIgnoreCase(id)
                        ? -1
                        : Integer.parseInt(id))
                .toArray();

        final int[] offsets = IntStream.range(0, busIds.length)
                .filter(i -> busIds[i] > 0)
                .toArray();

        final int[] filteredBusIds = IntStream.of(offsets)
                .map(i -> busIds[i])
                .toArray();


        long stepSize = filteredBusIds[0];

        outer:
        for (long time = stepSize; time < Long.MAX_VALUE; time += stepSize) {
            for (int i = 0; i < offsets.length; i++) {
                if ((time + offsets[i]) % filteredBusIds[i] != 0) {
                    final long newMultiplier = Arrays.stream(filteredBusIds, 0, i)
                            .reduce(1, (x, y) -> x * y);

                    if (newMultiplier > stepSize) {
                        stepSize = newMultiplier;
                    }

                    continue outer;
                }
            }
            return time;
        }

        throw new IllegalStateException("Did not find a solution :(");
    }

}
