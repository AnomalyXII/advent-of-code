package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.List;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 3.
 */
@Solution(year = 2022, day = 3, title = "Rucksack Reorganization")
public class Day3 {


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
                .mapToLong(line -> IntStream.range(0, line.length() / 2)
                        .map(line::charAt)
                        .filter(chr -> line.indexOf(chr, line.length() / 2) >= 0)
                        .distinct()
                        .map(Day3::toScore)
                        .sum())
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
        final List<String> backpacks = context.read();
        return IntStream.iterate(0, i -> i < backpacks.size(), i -> i + 3)
                .mapToLong(i -> {
                    final String e1 = backpacks.get(i);
                    final String e2 = backpacks.get(i + 1);
                    final String e3 = backpacks.get(i + 2);
                    return e1.chars()
                            .filter(c -> e2.indexOf((char) c) >= 0 && e3.indexOf((char) c) >= 0)
                            .mapToObj(c -> (char) c)
                            .findFirst()
                            .map(Day3::toScore)
                            .orElseThrow(() -> new IllegalStateException("Failed to find a common badge!"));
                })
                .sum();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Convert the item indicator to a score.
     */
    private static int toScore(final int c) {
        if (c >= 'a' && c <= 'z') return 1 + (c - 'a');
        else if (c >= 'A' && c <= 'Z') return 27 + (c - 'A');
        else throw new IllegalStateException("Cannot determine score of item: " + ((char) c));
    }

}
