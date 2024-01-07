package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 1.
 */
@Solution(year = 2020, day = 1, title = "Report Repair")
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
        return findProductOf2020Pairs(context.process(Integer::parseInt));
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return findProductOf2020Triples(context.process(Integer::parseInt));
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Find a pair of numbers in the given {@link List} that sum to
     * {@literal 2020} and return the product of these numbers.
     *
     * @param numbers the numbers
     * @return the product of any two numbers from the specified {@link List} that sum to 2020
     */
    static long findProductOf2020Pairs(final int... numbers) {
        return findProductOf2020Pairs(IntStream.of(numbers).boxed().collect(Collectors.toList()));
    }

    /**
     * Find a pair of numbers in the given {@link List} that sum to
     * {@literal 2020} and return the product of these numbers.
     *
     * @param numbers the {@link List} of numbers
     * @return the product of any two numbers from the specified {@link List} that sum to 2020
     */
    static long findProductOf2020Pairs(final List<Integer> numbers) {
        final List<Integer> sorted = new ArrayList<>(numbers);
        sorted.sort(Integer::compareTo);

        for (int i = 0; i < sorted.size(); i++) {
            final int x = sorted.get(i);
            final int y = 2020 - x;

            if (sorted.contains(y)) {
                return (long) x * y;
            }
        }

        throw new IllegalStateException("No valid 2020 pairs were found!");
    }

    /**
     * Find a triple of numbers in the given {@link List} that sum to
     * {@literal 2020} and return the product of these numbers.
     *
     * @param numbers the numbers
     * @return the product of any three numbers from the specified {@link List} that sum to 2020
     */
    static long findProductOf2020Triples(final int... numbers) {
        return findProductOf2020Triples(IntStream.of(numbers).boxed().collect(Collectors.toList()));
    }

    /**
     * Find a triple of numbers in the given {@link List} that sum to
     * {@literal 2020} and return the product of these numbers.
     *
     * @param numbers the {@link List} of numbers
     * @return the product of any three numbers from the specified {@link List} that sum to 2020
     */
    static long findProductOf2020Triples(final List<Integer> numbers) {
        final List<Integer> sorted = new ArrayList<>(numbers);
        sorted.sort(Integer::compareTo);

        for (int i = 0; i < sorted.size(); i++) {
            final int x = sorted.get(i);

            for (int j = i + 1; j < sorted.size(); j++) {
                final int y = sorted.get(j);

                final int z = 2020 - (x + y);
                if (sorted.contains(z)) {
                    return (long) x * y * z;
                }
            }
        }

        throw new IllegalStateException("No valid 2020 triples were found!");
    }

}
