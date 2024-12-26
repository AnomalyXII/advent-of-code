package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 5.
 */
@Solution(year = 2024, day = 5, title = "Print Queue")
public class Day5 {

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
        final List<List<String>> batches = context.readBatches();

        final Map<Integer, Set<Integer>> orderingRules = batches.getFirst().stream()
                .map(rule -> rule.split("\\s*[|]\\s*"))
                .collect(Collectors.groupingBy(
                        rule -> Integer.valueOf(rule[0]),
                        Collectors.mapping(rule -> Integer.valueOf(rule[1]), Collectors.toSet())
                ));

        return batches.getLast().stream()
                .map(Day5::parsePageUpdates)
                .filter(update -> isInCorrectOrder(update, orderingRules))
                .mapToInt(update -> update[update.length / 2])
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
        final List<List<String>> batches = context.readBatches();

        final Map<Integer, Set<Integer>> orderingRules = batches.getFirst().stream()
                .map(rule -> rule.split("\\s*[|]\\s*"))
                .collect(Collectors.groupingBy(
                        rule -> Integer.parseInt(rule[0]),
                        Collectors.mapping(rule -> Integer.parseInt(rule[1]), Collectors.toSet())
                ));

        return batches.getLast().stream()
                .map(Day5::parsePageUpdates)
                .filter(update -> !isInCorrectOrder(update, orderingRules))
                .map(update -> sort(update, orderingRules))
                .mapToInt(update -> update[update.length / 2])
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
        final List<List<String>> batches = context.readBatches();

        final Map<Integer, Set<Integer>> orderingRules = batches.getFirst().stream()
                .map(rule -> rule.split("\\s*[|]\\s*"))
                .collect(Collectors.groupingBy(
                        rule -> Integer.valueOf(rule[0]),
                        Collectors.mapping(rule -> Integer.valueOf(rule[1]), Collectors.toSet())
                ));

        final AtomicInteger part1 = new AtomicInteger(0);
        final AtomicInteger part2 = new AtomicInteger(0);
        batches.getLast().stream()
                .map(Day5::parsePageUpdates)
                .forEach(update -> {
                    if (isInCorrectOrder(update, orderingRules))
                        part1.addAndGet(update[update.length / 2]);
                    else
                        part2.addAndGet(sort(update, orderingRules)[update.length / 2]);
                });

        return new IntTuple(part1.get(), part2.get());
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /*
     * Parse the number of page updates.
     */
    private static int[] parsePageUpdates(final String line) {
        return Arrays.stream(line.split(",\\s*"))
                .mapToInt(Integer::valueOf)
                .toArray();
    }

    /*
     * Check if the given page updates are in the correct order.
     */
    private static boolean isInCorrectOrder(
            final int[] update,
            final Map<Integer, Set<Integer>> orderingRules
    ) {
        for (int i = 1; i < update.length; i++) {
            final Set<Integer> orderBefore = orderingRules.get(update[i]);
            if (orderBefore == null) continue;

            for (int j = 0; j < i; j++) {
                if (orderBefore.contains(update[j]))
                    return false;
            }
        }

        return true;
    }

    /*
     * Sort an `int` array using the custom `OrderingRule`s.
     */
    private static int[] sort(final int[] update, final Map<Integer, Set<Integer>> orderingRules) {
        return IntStream.of(update)
                .boxed()
                .sorted((a, b) -> {
                    final Set<Integer> aBeforeB = orderingRules.get(a);
                    if (aBeforeB != null && aBeforeB.contains(b)) return -1;

                    final Set<Integer> bBeforeA = orderingRules.get(b);
                    if (bBeforeA != null && bBeforeA.contains(a)) return 1;

                    return 0;
                })
                .mapToInt(i -> i)
                .toArray();
    }

}

