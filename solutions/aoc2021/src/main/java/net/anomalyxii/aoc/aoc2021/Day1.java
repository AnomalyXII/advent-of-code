package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 1.
 */
@Solution(year = 2021, day = 1, title = "Sonar Sweep")
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
        return countIncreases(context.process(Integer::parseInt), 1);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return countIncreases(context.process(Integer::parseInt), 3);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Count the number of times the sum of a window of inputs increases.
     */
    private long countIncreases(final List<Integer> depths, final int windowSize) {
        int count = 0;

        Integer prevSum = null;
        for (int i = windowSize - 1; i < depths.size(); i++) {
            int currentSum = depths.get(i);
            for (int j = 1; j < windowSize; j++) {
                currentSum += depths.get(i - j);
            }

            if (prevSum != null) {
                if (currentSum > prevSum) ++count;
            }
            prevSum = currentSum;
        }

        return count;
    }

}
