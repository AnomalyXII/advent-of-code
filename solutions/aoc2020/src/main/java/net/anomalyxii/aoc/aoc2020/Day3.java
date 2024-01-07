package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.concurrent.atomic.AtomicLong;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 3.
 */
@Solution(year = 2020, day = 3, title = "Toboggan Trajectory")
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
        return countTreesOnRoute(Grid.parseHorizontallyInfinite(context.stream()), 3, 1);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid map = Grid.parseHorizontallyInfinite(context.stream());
        return countTreesOnRoute(map, 1, 1)
                * countTreesOnRoute(map, 3, 1)
                * countTreesOnRoute(map, 5, 1)
                * countTreesOnRoute(map, 7, 1)
                * countTreesOnRoute(map, 1, 2);
    }

    // ****************************************
    // Helper Members
    // ****************************************

    /**
     * Count the number of trees that would be encountered using a given
     * route through a map.
     *
     * @param map       the map
     * @param xDistance the horizontal distance to travel
     * @param yDistance the vertical distance to travel
     * @return the count of trees encountered on the route
     */
    static long countTreesOnRoute(final Grid map, final int xDistance, final int yDistance) {
        final AtomicLong count = new AtomicLong(0);
        map.forEachIntervalMatching(
                new Velocity(xDistance, yDistance),
                point -> isTreeAtCoordinate(map, point),
                point -> count.incrementAndGet()
        );
        return count.longValue();
    }

    /**
     * Check if there is a tree at the given coordinate on the map.
     *
     * @param map        the map
     * @param coordinate the {@link Coordinate} to check
     * @return {@literal true} if the coordinate is a tree; {@literal false} otherwise
     */
    static boolean isTreeAtCoordinate(final Grid map, final Coordinate coordinate) {
        return map.get(coordinate) == '#';
    }

}
