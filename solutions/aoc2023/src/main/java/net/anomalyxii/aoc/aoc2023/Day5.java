package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.result.ObjectTuple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 5.
 */
@Solution(year = 2023, day = 5, title = "If You Give A Seed A Fertilizer")
public class Day5 {

    /*
     * Nothing to process...
     */
    private static final Stream<Range> EMPTY = Stream.empty();

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
        return processAlmanac(context, Range::extractSingleSeedRanges)
                .mapToLong(Range::start)
                .min()
                .orElseThrow();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return processAlmanac(context, Range::extractSeedRanges)
                .mapToLong(Range::start)
                .min()
                .orElseThrow();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return a {@link LongTuple} containing the answers for both parts
     */
    @Optimised
    public LongTuple calculateAnswers(final SolutionContext context) {
        final ObjectTuple<Stream<Range>, Stream<Range>> results = context.streamBatches()
                .reduce(
                        new ObjectTuple<>(EMPTY, EMPTY),
                        (tup, batch) -> new ObjectTuple<>(
                                processMapping(Range::extractSingleSeedRanges, tup.answer1(), batch),
                                processMapping(Range::extractSeedRanges, tup.answer2(), batch)
                        ),
                        (a, b) -> {
                            throw new IllegalStateException("Cannot merge!");
                        }
                );
        return new LongTuple(
                results.answer1().mapToLong(range -> range.start).min().orElseThrow(),
                results.answer2().mapToLong(range -> range.start).min().orElseThrow()
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Process the entire almanac.
     */
    private Stream<Range> processAlmanac(final SolutionContext context, final Function<String, Stream<Range>> seedExtractor) {
        return context.streamBatches()
                .reduce(
                        EMPTY,
                        (list, batch) -> processMapping(seedExtractor, list, batch),
                        Range::merge
                );
    }

    /*
     * Process a single mapping of almanac values.
     */
    private static Stream<Range> processMapping(
            final Function<String, Stream<Range>> seedExtractor,
            final Stream<Range> list,
            final List<String> batch
    ) {
        final String identifier = batch.getFirst();
        if (identifier.startsWith("seeds: "))
            return seedExtractor.apply(identifier.substring(7));

        final SortedSet<RangeMap> bounds = batch.subList(1, batch.size()).stream()
                .map(RangeMap::parse)
                .collect(Collectors.toCollection(TreeSet::new));

        return list
                .flatMap(sourceRange -> RangeMap.offset(sourceRange, bounds))
                .sorted();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A range of numbers.
     */
    private record Range(long start, long end) implements Comparable<Range> {

        // Comparable Methods

        @Override
        public int compareTo(final Range o) {
            return Long.compare(start, o.start);
        }

        // Static Helper Methods

        /*
         * Create a `Range` that contains only one value.
         */
        static Range singleton(final long start) {
            return new Range(start, start);
        }

        /*
         * Create a `Range` from a given start point with a given length.
         */
        static Range from(final long start, final long length) {
            return new Range(start, start + (length - 1));
        }

        /*
         * Extract a `Set` of `Range`s that represent a single seed.
         */
        static Stream<Range> extractSingleSeedRanges(final String seeds) {
            return Arrays.stream(seeds.split("\\s"))
                    .map(Long::valueOf)
                    .map(Range::singleton)
                    .sorted();
        }

        /*
         * Extract a `Set` of `Range`s that represent multiple seeds.
         */
        static Stream<Range> extractSeedRanges(final String seeds) {
            final List<Long> parts = Arrays.stream(seeds.split("\\s"))
                    .map(Long::valueOf)
                    .toList();

            final Stream.Builder<Range> seedRanges = Stream.builder();
            for (int i = 0; i < parts.size(); i += 2)
                seedRanges.add(Range.from(parts.get(i), parts.get(i + 1)));
            return seedRanges.build();
        }

        /*
         * Merge two `Stream`s of `Range`s.
         */
        static Stream<Range> merge(final Stream<Range> first, final Stream<Range> second) {
            return Stream.concat(first, second);
        }
    }

    /*
     * A mapping between two number ranges.
     */
    private record RangeMap(Range source, Range destination)
            implements Comparable<RangeMap> {

        // Comparable Methods

        @Override
        public int compareTo(final RangeMap o) {
            return Long.compare(source.start, o.source.start);
        }

        // Helper Methods

        /*
         * Map an input based on the relative position in this `RangeMap`.
         */
        long offset(final long input) {
            return (input - source.start) + destination.start;
        }

        // Static Helper Methods

        /*
         * Parse a `RangeMap` from a given line of format:
         *   <destination start> <source start> <length>
         */
        static RangeMap parse(final String line) {
            final String[] parts = line.split("\\s");
            return RangeMap.from(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1]),
                    Long.parseLong(parts[2])
            );
        }

        /*
         * Parse a `RangeMap` from a destination start point, a source start
         * point and a length for the two `Range`s.
         */
        static RangeMap from(final long destination, final long source, final long length) {
            return new RangeMap(
                    Range.from(source, length),
                    Range.from(destination, length)
            );
        }

        /*
         * Transform all numbers in a given `Range` according to the `RangeMap`s
         * provided.
         */
        static Stream<Range> offset(final Range sourceRange, final SortedSet<RangeMap> bounds) {
            final Stream.Builder<Range> result = Stream.builder();
            final Iterator<RangeMap> boundsIterator = bounds.iterator();

            RangeMap currentBounds = null;
            long current = sourceRange.start;
            while (current <= sourceRange.end) {
                if (currentBounds == null) {
                    if (!boundsIterator.hasNext()) {
                        result.add(new Range(current, sourceRange.end));
                        return result.build();
                    }

                    currentBounds = boundsIterator.next();
                }

                if (current < currentBounds.source.start) {
                    final long end = Math.min(sourceRange.end, currentBounds.source.start - 1);
                    if (end >= current)
                        result.add(new Range(current, end));
                    current = end + 1;
                    continue;
                }

                if (current > currentBounds.source.end) {
                    currentBounds = null;
                    continue;
                }

                final long end = Math.min(currentBounds.source.end, sourceRange.end);
                result.add(new Range(currentBounds.offset(current), currentBounds.offset(end)));
                current = end + 1;
            }

            return result.build();
        }
    }

}

