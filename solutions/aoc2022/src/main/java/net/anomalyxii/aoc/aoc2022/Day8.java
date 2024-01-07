package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;
import static net.anomalyxii.aoc.utils.geometry.Direction.*;

/**
 * Advent of Code 2022, Day 8.
 */
@Solution(year = 2022, day = 8, title = "Treetop Tree House")
public class Day8 {

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
        final Grid forest = Grid.parse(context.stream(), c -> c - '0');

        return forest.entries()
                .filter(entry -> {
                    final Coordinate coordinate = entry.getKey();
                    final int value = entry.getValue();
                    return isVisibleInDirection(forest, coordinate, value, UP)
                            || isVisibleInDirection(forest, coordinate, value, LEFT)
                            || isVisibleInDirection(forest, coordinate, value, DOWN)
                            || isVisibleInDirection(forest, coordinate, value, RIGHT);
                })
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid forest = Grid.parse(context.stream(), c -> c - '0');
        return forest.entries()
                .mapToLong(entry -> {
                    final Coordinate coordinate = entry.getKey();
                    final int value = entry.getValue();

                    return calculateViewingDistanceInDirection(forest, coordinate, value, UP)
                            * calculateViewingDistanceInDirection(forest, coordinate, value, LEFT)
                            * calculateViewingDistanceInDirection(forest, coordinate, value, DOWN)
                            * calculateViewingDistanceInDirection(forest, coordinate, value, RIGHT);
                })
                .max()
                .orElseThrow(() -> new IllegalStateException("No trees were found!"));
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Check if a tree is visible, i.e. there are no trees of equal or greater
     * height, in a given direction.
     */
    private static boolean isVisibleInDirection(
            final Grid forest,
            final Coordinate coordinate,
            final int value,
            final Direction direction
    ) {

        Coordinate next = coordinate.adjustBy(direction);
        while (forest.contains(next)) {
            if (forest.get(next) >= value)
                return false;
            next = next.adjustBy(direction);
        }

        return true;
    }

    /*
     * Calculate the viewing distance from a given tree.
     *
     * The viewing distance is equal to the number of trees that can be seen
     * in the given direction, until reaching a tree of greater or equal
     * height to the starting tree, or the edge of the forest.
     *
     * Note: if the viewing distance is terminated by reaching a taller tree,
     * this tree _is_ included in the viewing distance.
     */
    private static long calculateViewingDistanceInDirection(
            final Grid forest,
            final Coordinate coordinate,
            final int value,
            final Direction direction
    ) {
        int viewingDistance = 0;
        Coordinate next = coordinate.adjustBy(direction);
        while (forest.contains(next)) {
            viewingDistance += 1;

            if (forest.get(next) >= value) break;
            next = next.adjustBy(direction);
        }

        return viewingDistance;
    }

}
