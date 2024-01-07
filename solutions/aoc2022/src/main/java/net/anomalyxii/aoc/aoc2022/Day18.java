package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Bounds;
import net.anomalyxii.aoc.utils.geometry.Point;
import net.anomalyxii.aoc.utils.geometry.Volume;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 18.
 */
@Solution(year = 2022, day = 18, title = "Boiling Boulders")
public class Day18 {

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
        final LavaDroplet droplet = LavaDroplet.parse(context);
        return droplet.resolveSides().size();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final LavaDroplet droplet = LavaDroplet.parse(context);
        return droplet.resolveExposedSides().size();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A side of a 1x1x1 cube, used to model part of a lava droplet and the
     * air surrounding it.
     */
    private record Side(Point top, Point bottom) implements Comparable<Side> {

        private static final Comparator<Side> COMPARATOR = Comparator.comparing((Side s) -> s.top)
                .thenComparing((Side s) -> s.bottom);

        // Comparable Methods

        @Override
        public int compareTo(final Side o) {
            return COMPARATOR.compare(this, o);
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Side side = (Side) o;
            return top.equals(side.top) && bottom.equals(side.bottom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(top, bottom);
        }

        // Static Helper Methods

        /*
         * Returns a `Set` of `Side`s originating at a given point.
         */
        static Set<Side> sidesOf(final Point point) {
            return new HashSet<>(asList(
                    sideOf(point, 1),
                    sideOf(point, 2),
                    sideOf(point, 3),
                    sideOf(point, 4),
                    sideOf(point, 5),
                    sideOf(point, 6)
            ));
        }

        /*
         * Return a `Side` for a given face (based on a die), starting at the
         * given `Point`.
         *
         * The die orientation assumes:
         *   - 1 is facing "forward",
         *   - 2 is facing "down",
         *   - 3 is facing "right",
         *   - 4 is facing "left",
         *   - 5 is facing "up", and
         *   - 6 is facing "backwards".
         */
        private static Side sideOf(final Point point, final int face) {
            return switch (face) {
                case 1 -> new Side(point, point.adjustBy(1, 1, 0));
                case 2 -> new Side(point.adjustBy(0, 1, 0), point.adjustBy(1, 1, 1));
                case 3 -> new Side(point.adjustBy(1, 0, 0), point.adjustBy(1, 1, 1));
                case 4 -> new Side(point, point.adjustBy(0, 1, 1));
                case 5 -> new Side(point, point.adjustBy(1, 0, 1));
                case 6 -> new Side(point.adjustBy(0, 0, 1), point.adjustBy(1, 1, 1));
                default -> throw new IllegalArgumentException("Invalid side: " + face);
            };
        }

        /*
         * Return a `Point` that would be the origin of a cube that borders
         * the given point on a specific face (based on the die described in
         * `Day18.Side#sideOf(Point, int)`).
         */
        public static Point cubeJoiningOnSide(final Point point, final int face) {
            return switch (face) {
                case 1 -> point.adjustBy(0, 0, -1);
                case 2 -> point.adjustBy(0, 1, 0);
                case 3 -> point.adjustBy(1, 0, 0);
                case 4 -> point.adjustBy(-1, 0, 0);
                case 5 -> point.adjustBy(0, -1, 0);
                case 6 -> point.adjustBy(0, 0, 1);
                default -> throw new IllegalArgumentException("Invalid side: " + face);
            };
        }

    }

    /*
     * A lava droplet.
     */
    private static final class LavaDroplet {

        // Private Members

        private final Set<Point> points;

        // Constructors

        LavaDroplet(final Collection<Point> points) {
            this.points = new TreeSet<>(points);
        }

        // Helper Methods

        /**
         * Resolve all the {@link Side Sides} of this {@link LavaDroplet}.
         *
         * @return the {@link Set} of {@link Side Sides}
         */
        Set<Side> resolveSides() {
            final Set<Side> allSides = new TreeSet<>();
            resolveSides(allSides);
            return allSides;
        }

        /**
         * Resolve all the {@link Side Sides} of this {@link LavaDroplet} that
         * are exposed to air and, thus, will eventually be exposed to water.
         *
         * @return the {@link Set} of {@link Side Sides}
         */
        Set<Side> resolveExposedSides() {
            final Set<Side> allSides = resolveSides();
            final Volume space = resolveTotalVolume();
            final Set<Point> airPockets = new HashSet<>();
            space.forEach(point -> {
                if (!points.contains(point))
                    airPockets.add(point);
            });

            trimAirPockets(airPockets, allSides);

            allSides.removeAll(airPockets.stream().map(Side::sidesOf).flatMap(Collection::stream).collect(Collectors.toSet()));
            return allSides;
        }

        // Private Helper Methods

        /*
         * Resolve all exposed sides of a lava droplet.
         */
        private void resolveSides(final Set<Side> allSides) {
            final Set<Side> dupSides = new TreeSet<>();
            points.forEach(cube -> accumulate(cube, allSides, dupSides));
            allSides.removeAll(dupSides);
        }

        /*
         * Calculate the total cuboid-shaped volume taken up by a lava droplet.
         */
        private Volume resolveTotalVolume() {
            Point top = Point.MAX;
            Point bottom = Point.MIN;
            for (final Point point : points) {
                final Point opposite = point.adjustBy(1, 1, 1); // Opposite corner...

                top = new Point(min(point.x(), top.x()), min(point.y(), top.y()), min(point.z(), top.z()));
                bottom = new Point(max(opposite.x(), bottom.x()), max(opposite.y(), bottom.y()), max(opposite.z(), bottom.z()));
            }

            return new Volume(new Bounds(top.x(), bottom.x()), new Bounds(top.y(), bottom.y()), new Bounds(top.z(), bottom.z()));
        }

        // Static Helper Methods

        /**
         * Parse a {@link LavaDroplet} from the provided {@link SolutionContext}.
         *
         * @param context the {@link SolutionContext} to load from
         * @return the {@link LavaDroplet}
         */
        public static LavaDroplet parse(final SolutionContext context) {
            return new LavaDroplet(context.process(Point::parse));
        }

        // Private Static Helper Methods

        /*
         * Remove cubes of air from around a lava droplet.
         */
        private static void trimAirPockets(final Set<Point> airPockets, final Set<Side> cubeSides) {
            boolean changed;
            do {
                changed = airPockets.removeIf(
                        pocket -> {
                            final boolean[] b = {
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 1, cubeSides, airPockets),
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 2, cubeSides, airPockets),
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 3, cubeSides, airPockets),
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 4, cubeSides, airPockets),
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 5, cubeSides, airPockets),
                                    isSideTouchingCubeOrAnotherAirPocket(pocket, 6, cubeSides, airPockets),
                            };

                            return !(b[0] && b[1] && b[2] && b[3] && b[4] && b[5]);
                        }
                );
            } while (changed);
        }

        /*
         * Check if an air pocket is touching a cube of lava or another air
         * pocket cube.
         */
        private static boolean isSideTouchingCubeOrAnotherAirPocket(final Point pocket, final int id, final Set<Side> cubeSides, final Set<Point> airPockets) {
            return cubeSides.contains(Side.sideOf(pocket, id)) || airPockets.contains(Side.cubeJoiningOnSide(pocket, id));
        }

        /*
         * Check each `Side` of the cube starting at the specified `Point` and
         * accumulate those `Side`s by:
         *   - if this is the first time that `Side` has been seen,
         *        -> add to "all sides";
         *   - if this `Side` has been seen previously,
         *        -> add to "duplicate sides".
         */
        private static void accumulate(final Point point, final Set<Side> allSides, final Set<Side> dupSides) {
            Side.sidesOf(point)
                    .forEach(side -> (allSides.contains(side) ? dupSides : allSides).add(side));
        }

    }

}

