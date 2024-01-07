package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 10.
 */
@Solution(year = 2020, day = 10, title = "Adapter Array")
public class Day10 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        final List<Integer> adaptors = context.stream()
                .map(Integer::parseInt)
                .sorted()
                .toList();

        if (adaptors.isEmpty()) {
            return 0L;
        }

        long oneStep = 0;
        long threeStep = 0;

        int prev = 0;
        for (final int adaptor : adaptors) {
            final int difference = adaptor - prev;

            switch (difference) {
                case 1:
                    ++oneStep;
                    break;
                case 2:
                    // Don't need to care?
                    break;
                case 3:
                    ++threeStep;
                    break;
                default:
                    throw new IllegalStateException("Invalid difference in adaptor joltages: [" + adaptor + ", " + adaptor + " => " + difference + " ]");
            }

            prev = adaptor;
        }

        return oneStep * (threeStep + 1);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Integer> adaptors = context.stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        if (adaptors.isEmpty()) {
            return 0L;
        }

        adaptors.add(0, 0);

        final long[] memo = new long[adaptors.size()];
        Arrays.fill(memo, 0);
        memo[0] = 1;

        for (int i = 0; i < adaptors.size(); i++) {
            final int adaptor = adaptors.get(i);

            if (i < adaptors.size() - 1) {
                final int next = adaptors.get(i + 1);
                final int diff = next - adaptor;
                if (diff <= 3) {
                    memo[i + 1] += memo[i];
                }
            }

            if (i < adaptors.size() - 2) {
                final int next = adaptors.get(i + 2);
                final int diff = next - adaptor;
                if (diff <= 3) {
                    memo[i + 2] += memo[i];
                }
            }

            if (i < adaptors.size() - 3) {
                final int next = adaptors.get(i + 3);
                final int diff = next - adaptor;
                if (diff <= 3) {
                    memo[i + 3] += memo[i];
                }
            }
        }

        return memo[adaptors.size() - 1];
    }

}
