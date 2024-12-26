package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 1.
 */
@Solution(year = 2024, day = 1, title = "Historian Hysteria")
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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final List<String> lines = context.read();
        final int[] left = new int[lines.size()];
        final int[] right = new int[lines.size()];
        for (int i = 0, linesSize = lines.size(); i < linesSize; i++) {
            final String line = lines.get(i);
            final String[] split = line.split("\\s+");
            left[i] = Integer.parseInt(split[0]);
            right[i] = Integer.parseInt(split[1]);
        }

        Arrays.sort(left);
        Arrays.sort(right);

        return IntStream.range(0, lines.size())
                .map(i -> Math.abs(right[i] - left[i]))
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
        final Map<Integer, CounterPair> counts = new HashMap<>();
        context.stream()
                .map(line -> line.split("\\s+"))
                .forEach(split -> {
                    counts.computeIfAbsent(Integer.parseInt(split[0]), CounterPair::new).leftCount++;
                    counts.computeIfAbsent(Integer.parseInt(split[1]), CounterPair::new).rightCount++;
                });

        return counts.values().stream()
                .mapToInt(CounterPair::computeSimilarityScore)
                .sum();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Count the number of occurrences of specific given location ID.
     */
    private static final class CounterPair {
        private final int locationId;

        private int leftCount = 0;
        private int rightCount = 0;

        private CounterPair(final int locationId) {
            this.locationId = locationId;
        }

        public int computeSimilarityScore() {
            return locationId * leftCount * rightCount;
        }
    }

}

