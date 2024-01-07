package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 14.
 */
@Solution(year = 2021, day = 14, title = "Extended Polymerization")
public class Day14 {

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
        return enhance(context, 10);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return enhance(context, 40);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Enhance the polymer a given number of times.
     */
    private long enhance(final SolutionContext context, final int iterations) {
        final List<List<String>> input = context.readBatches();
        final String template = input.getFirst().getFirst();
        final Map<ElementPair, PairInsertion> rules = input.getLast().stream()
                .map(PairInsertion::parse)
                .collect(Collectors.toMap(PairInsertion::pair, Function.identity()));

        final Map<ElementPair, AtomicLong> pairs = new HashMap<>();
        for (int n = 0; n < template.length() - 1; n++) {
            pairs.computeIfAbsent(new ElementPair(template.charAt(n), template.charAt(n + 1)), k -> new AtomicLong(0))
                    .incrementAndGet();
        }

        for (int iteration = 0; iteration < iterations; iteration++) {
            final Map<ElementPair, AtomicLong> nextPairs = new HashMap<>();
            for (final Map.Entry<ElementPair, AtomicLong> entry : pairs.entrySet()) {
                final ElementPair pair = entry.getKey();
                final PairInsertion expansion = rules.get(pair);
                nextPairs.computeIfAbsent(new ElementPair(pair.left, expansion.insert), k -> new AtomicLong(0))
                        .addAndGet(entry.getValue().longValue());
                nextPairs.computeIfAbsent(new ElementPair(expansion.insert, pair.right), k -> new AtomicLong(0))
                        .addAndGet(entry.getValue().longValue());
            }

            pairs.clear();
            pairs.putAll(nextPairs);
        }


        final long[] counts = new long[26];
        ++counts[template.charAt(template.length() - 1) - 'A'];
        for (final Map.Entry<ElementPair, AtomicLong> entry : pairs.entrySet()) {
            final ElementPair pair = entry.getKey();
            counts[pair.left - 'A'] += entry.getValue().longValue();
        }

        long mostFrequent = Long.MIN_VALUE;
        long leastFrequent = Long.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            final long count = counts[i];
            if (count == 0) continue;

            if (count > mostFrequent)
                mostFrequent = count;
            if (count < leastFrequent)
                leastFrequent = count;
        }

        return mostFrequent - leastFrequent;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A pair of elements.
     */
    private record ElementPair(char left, char right) {

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ElementPair that = (ElementPair) o;
            return left == that.left && right == that.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }

    }

    /*
     * A rule that defines the new element that is inserted within an
     * `ElementPair`.
     */
    private record PairInsertion(ElementPair pair, char insert) {

        // Helper Methods

        static PairInsertion parse(final String rule) {
            final String[] parts = rule.split("\\s*->\\s*");
            return new PairInsertion(new ElementPair(parts[0].charAt(0), parts[0].charAt(1)), parts[1].charAt(0));
        }

    }

}
