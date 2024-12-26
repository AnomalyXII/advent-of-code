package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Bounds;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 15.
 */
@Solution(year = 2022, day = 15, title = "Beacon Exclusion Zone")
public class Day15 {

    /*
     * Input RegExp for a `Sensor`.
     */
    private static final Pattern INPUT = Pattern.compile("Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)");

    // ****************************************
    // Private Members
    // ****************************************

    private final int row;
    private final int minXY;
    private final int maxXY;

    // ****************************************
    // Constructors
    // ****************************************

    public Day15() {
        this(2000000, 0, 4000000);
    }

    Day15(final int row, final int minXY, final int maxXY) {
        this.row = row;
        this.minXY = minXY;
        this.maxXY = maxXY;
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
        final List<Sensor> sensors = loadSensorData(context);

        final long coverageScore = sensors.stream()
                .map(sensor -> sensor.findCoverageForRow(row))
                .flatMap(Optional::stream)
                .sorted()
                .collect(reduce())
                .mapToLong(Bounds::length)
                .sum();
        final long beaconsOnRow = sensors.stream()
                .map(sensor -> sensor.beacon)
                .filter(beacon -> beacon.y() == row)
                .distinct()
                .count();
        return coverageScore - beaconsOnRow;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Sensor> sensors = loadSensorData(context);

        return IntStream.rangeClosed(minXY, maxXY)
                .mapToObj(y -> findMissingCoordinate(sensors, y))
                .flatMap(Optional::stream)
                .mapToLong(missing -> (missing.x() * 4000000L) + missing.y())
                .findFirst()
                .orElseThrow();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Load the `Sensor` data.
     */
    private static List<Sensor> loadSensorData(final SolutionContext context) {
        return context.process(Sensor::parse);
    }

    /*
     * Determine the coverage of all sensors across a given row and return
     * the missing `Coordinate` if there is a gap.
     *
     * Behaviour is not well-defined if there is more than one missing
     * `Coordinate`!
     */
    private Optional<Coordinate> findMissingCoordinate(final List<Sensor> sensors, final int y) {
        return sensors.stream()
                .map(sensor -> sensor.findCoverageForRow(y))
                .flatMap(Optional::stream)
                .sorted()
                // This could theoretically be necessary, but in practice doesn't seem to be?
                //.filter(bounds -> bounds.max() >= minXY && bounds.min() <= maxXY)
                .collect(reduce())
                .skip(1) // Skip the first (and, normally, only) entry
                .findFirst() // Really "findSecond()"!!
                .map(bounds -> new Coordinate(bounds.min() - 1, y));
    }

    /*
     * Reduce a `Set` of `Bounds` into as few contiguous `Bounds` as
     * possible.
     */
    private Collector<? super Bounds, ?, Stream<Bounds>> reduce() {
        return new Collector<Bounds, Stream.Builder<Bounds>, Stream<Bounds>>() {

            private final BoundsReducer reducer = new BoundsReducer();

            @Override
            public Supplier<Stream.Builder<Bounds>> supplier() {
                return Stream::builder;
            }

            @Override
            public BiConsumer<Stream.Builder<Bounds>, Bounds> accumulator() {
                return reducer::accept;
            }

            @Override
            public BinaryOperator<Stream.Builder<Bounds>> combiner() {
                return reducer::combine;
            }

            @Override
            public Function<Stream.Builder<Bounds>, Stream<Bounds>> finisher() {
                return reducer::build;
            }

            @Override
            public Set<Collector.Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a Sensor and corresponding Beacon.
     */
    private record Sensor(Coordinate location, Coordinate beacon) {

        // Helper Methods

        /*
         * Calculate the Manhattan Distance from this sensor to the detected
         * beacon.
         */
        int calculateManhattanDistance() {
            return location.calculateManhattanDistance(beacon);
        }

        /*
         * Find the sensor coverage for a given row.
         */
        Optional<Bounds> findCoverageForRow(final int y) {
            final int maxDistance = calculateManhattanDistance();

            final Coordinate startingPoint = new Coordinate(location.x(), y);
            final int distanceToStartingPoint = location.calculateManhattanDistance(startingPoint);
            if (distanceToStartingPoint > maxDistance)
                return Optional.empty(); // I think we don't care?

            final int dx = maxDistance - distanceToStartingPoint;
            final int minX = location.x() - dx;
            final int maxX = location.x() + dx;
            return Optional.of(new Bounds(minX, maxX));
        }

        // Static Helper Methods

        /**
         * Parse the {@link Sensor} data from the provided line.
         * <p>
         * The input is expected to be in the form:
         * <code>/Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)/</code>
         *
         * @param line the input line
         * @return the {@link Sensor}
         * @throws IllegalArgumentException if the specified input line is invalid
         */
        static Sensor parse(final String line) {
            final Matcher matcher = INPUT.matcher(line);
            if (!matcher.matches())
                throw new IllegalArgumentException("Invalid line: '" + line + "'");

            final Coordinate sensor = new Coordinate(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            final Coordinate beacon = new Coordinate(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            return new Sensor(sensor, beacon);
        }

    }

    /*
     * Utility class for reducing multiple `Bounds` into a `Stream`.
     */
    private static final class BoundsReducer {

        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;

        /*
         * Accept the next `Bounds` to be reduced.
         */
        private void accept(final Stream.Builder<Bounds> builder, final Bounds bounds) {
            if (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE) {
                min = bounds.min();
                max = bounds.max();
                return;
            }

            if (bounds.min() <= (max + 1)) {
                max = max(max, bounds.max());
                return;
            }

            builder.add(new Bounds(min, max));

            min = bounds.min();
            max = bounds.max();
        }

        /*
         * Build the final `Stream` of reduced `Bounds`.
         */
        private Stream<Bounds> build(final Stream.Builder<Bounds> builder) {
            builder.add(new Bounds(min, max));
            return builder.build();
        }

        /*
         * Combine two partially reduced `Stream`s.
         *
         * Actually: throw because ugh.
         */
        private Stream.Builder<Bounds> combine(final Stream.Builder<Bounds> left, final Stream.Builder<Bounds> right) {
            throw new IllegalArgumentException("Should not need to combine!");
        }
    }


}

