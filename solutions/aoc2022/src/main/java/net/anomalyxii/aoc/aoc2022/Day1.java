package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 1.
 */
@Solution(year = 2022, day = 1, title = "Calorie Counting")
public class Day1 {

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
        return context.streamBatches()
                .mapToLong(counts -> counts.stream().mapToLong(Long::parseLong).sum())
                .max()
                .orElseThrow(() -> new IllegalStateException("No result found"));
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
        final long[] calories = context.streamBatches()
                .mapToLong(counts -> counts.stream().mapToLong(Long::parseLong).sum())
                .sorted()
                .toArray();

        if (calories.length < 3)
            throw new IllegalStateException("There are fewer than three elves carrying snacks :(");

        return calories[calories.length - 1] + calories[calories.length - 2] + calories[calories.length - 3];
    }

}
