package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 5.
 */
@Solution(year = 2021, day = 5, title = "Hydrothermal Venture")
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
    public long calculateAnswerForPart1(final SolutionContext context) {
        return countOverlaps(context, r -> !r.isDiagonal());
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return countOverlaps(context, r -> true);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Count the number of times two or more `Range`s intersect.
     */
    private long countOverlaps(final SolutionContext context, final Predicate<Range> filter) {
        return countOverlaps(context.stream(), filter);
    }

    /*
     * Count the number of times two or more `Range`s intersect.
     */
    private long countOverlaps(final Stream<String> stream, final Predicate<Range> filter) {
        return stream.map(Range::parse)
                .filter(filter)
                .flatMap(Range::interpolate)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .filter(value -> value.intValue() > 1)
                .count();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A range of `Point`s, represented by a `start` and an `end`.
     */
    private record Range(Coordinate start, Coordinate end) {

        // Helper Methods

        /**
         * Determine whether this {@link Range} runs diagonally.
         *
         * @return {@literal true} if diagonal; {@literal false} otherwise
         */
        boolean isDiagonal() {
            return start.x() != end.x() && start.y() != end.y();
        }

        /**
         * Interpolate this {@link Range} into a {@link Stream} of all
         * {@link Coordinate Coordinates} that lie between the {@literal start}
         * and {@literal end}.
         *
         * @return a {@link Stream} of {@link Coordinate Points}
         */
        Stream<Coordinate> interpolate() {
            final int dx = Integer.compare(end.x(), start.x());
            final int dy = Integer.compare(end.y(), start.y());

            final Stream.Builder<Coordinate> points = Stream.builder();
            points.add(start);
            for (int cx = start.x() + dx, cy = start.y() + dy; cx != end.x() || cy != end.y(); cx += dx, cy += dy) {
                points.add(new Coordinate(cx, cy));
            }
            points.add(end);

            return points.build();
        }

        /**
         * Create a new {@link Range} by parsing the given (x1,y1) -> (x2,y2) coordinates.
         *
         * @param range the range
         * @return the {@link Range}
         */
        static Range parse(final String range) {
            final String[] parts = range.split("\\s*->\\s*", 2);
            final Coordinate start = Coordinate.parse(parts[0]);
            final Coordinate end = Coordinate.parse(parts[1]);

            return new Range(start, end);
        }

    }

}
