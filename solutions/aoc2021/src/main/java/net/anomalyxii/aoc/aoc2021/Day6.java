package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.stream.LongStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 6.
 */
@Solution(year = 2021, day = 6, title = "Lanturnfish")
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
    public long calculateAnswerForPart1(final SolutionContext context) {
        return simulate(context, 80);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return simulate(context, 256);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Simulate the lifecycle of a school of Lanturnfish.
     */
    private long simulate(final SolutionContext context, final int numberOfDays) {
        final long[] fish = context.stream()
                .flatMap(line -> Arrays.stream(line.split(",\\s*")))
                .map(Integer::parseInt)
                .reduce(
                        new long[9],
                        (result, x) -> {
                            result[x]++;
                            return result;
                        },
                        (a, b) -> a
                );

        for (int day = 0; day < numberOfDays; day++) {
            final long fishAt0Days = fish[0];

            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(fish, 1, fish, 0, 8);

            fish[6] += fishAt0Days;
            fish[8] = fishAt0Days;
        }

        return LongStream.of(fish).sum();
    }

}
