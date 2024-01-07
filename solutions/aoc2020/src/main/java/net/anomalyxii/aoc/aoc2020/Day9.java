package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Bounds;

import java.util.List;
import java.util.Optional;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 9.
 */
@Solution(year = 2020, day = 9, title = "Encoding Error")
public class Day9 {

    private final int preambleLength;

    // ****************************************
    // Constructors
    // ****************************************

    public Day9() {
        this(25);
    }

    Day9(final int preambleLength) {
        this.preambleLength = preambleLength;
    }

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
        final List<Long> numbers = context.process(Long::parseLong);
        return findEncryptionError(numbers, preambleLength);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Long> numbers = context.process(Long::parseLong);
        final long target = findEncryptionError(numbers, preambleLength);
        return findEncryptionWeakness(numbers, target);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Search the `List` of given numbers for the first occurrence that is
     * not the sum of any of the previous X numbers.
     */
    private static long findEncryptionError(final List<Long> numbers, final int preambleLength) {
        return Bounds.of(preambleLength, numbers.size() - 1)
                .minPointSatisfying(i -> {
                    final long next = numbers.get(i);
                    final boolean match = Bounds.of(0, preambleLength - 2)
                            .anyMatch(x -> Bounds.of(x + 1, preambleLength - 1)
                                    .anyMatch(y -> {
                                        final long windowX = numbers.get(i - (preambleLength - x));
                                        final long windowY = numbers.get(i - (preambleLength - y));
                                        return windowX + windowY == next;
                                    }));
                    return match
                            ? Optional.empty()
                            : Optional.of(next);
                })
                .orElseThrow(() -> new IllegalStateException("No encoding error was found"));
    }

    /*
     * Find the sum of (min() + max()) of the first contiguous run of numbers
     * that sum to the target value;
     */
    private static long findEncryptionWeakness(final List<Long> numbers, final long target) {

        long total = 0;
        int lowBound = 0, highBound = 0;
        while (highBound < numbers.size()) {
            final long next = numbers.get(highBound);
            total += next;

            while (total > target) {
                final long prev = numbers.get(lowBound++);
                total -= prev;
            }

            if (total == target) {
                // Todo: is there a way of tracking these as we go?
                final long min = numbers.subList(lowBound, highBound)
                        .stream()
                        .mapToLong(x -> x)
                        .min()
                        .orElseThrow();
                final long max = numbers.subList(lowBound, highBound)
                        .stream()
                        .mapToLong(x -> x)
                        .max()
                        .orElseThrow();

                return min + max;
            }

            highBound++;
        }

        throw new IllegalStateException("No encryption weakness was found");
    }

}
