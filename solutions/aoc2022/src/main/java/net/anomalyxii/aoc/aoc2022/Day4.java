package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Bounds;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 4.
 */
@Solution(year = 2022, day = 4, title = "Camp Cleanup")
public class Day4 {


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
                .filter(line -> {
                    final String[] pairs = line.split(",", 2);
                    final Bounds a1 = Bounds.parse(pairs[0], "-");
                    final Bounds a2 = Bounds.parse(pairs[1], "-");
                    return a1.min() <= a2.min() && a1.max() >= a2.max()
                            || a2.min() <= a1.min() && a2.max() >= a1.max();
                })
                .count();
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
                .filter(line -> {
                    final String[] pairs = line.split(",", 2);
                    final Bounds a1 = Bounds.parse(pairs[0], "-");
                    final Bounds a2 = Bounds.parse(pairs[1], "-");
                    return a1.min() <= a2.min() && a1.max() >= a2.min()
                            || a1.min() >= a2.min() && a1.min() <= a2.max();
                })
                .count();
    }

}
