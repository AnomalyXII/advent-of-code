package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.algorithms.Dijkstra;
import net.anomalyxii.aoc.utils.algorithms.ShortestPath;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayList;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 12.
 */
@Solution(year = 2022, day = 12, title = "Hill Climbing Algorithm")
public class Day12 {

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
        final Grid grid = context.readGrid();
        final Coordinate start = findStart(grid);
        final Coordinate end = findEnd(grid);

        final ShortestPath sp = new Dijkstra(Day12::resolve);
        return sp.solve(grid, start, end);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = context.readGrid();

        long shortestPath = Long.MAX_VALUE;
        for (final Coordinate start : findPossibleStarts(grid)) {
            final Coordinate end = findEnd(grid);

            final ShortestPath sp = new Dijkstra(Day12::resolve);
            final long distance = sp.solve(grid, start, end);
            if (distance < shortestPath)
                shortestPath = distance;
        }

        return shortestPath;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the "start" co-ordinate (marked with an 'S').
     */
    private static Coordinate findStart(final Grid grid) {
        for (final Coordinate coord : grid) {
            if (grid.get(coord) == 'S')
                return coord;
        }

        throw new IllegalStateException("Start co-ordinate was not found?");
    }

    /*
     * Find all possible "start" co-ordinates (marked with an 'S' or an 'a').
     */
    private static List<Coordinate> findPossibleStarts(final Grid grid) {
        final List<Coordinate> possibleStarts = new ArrayList<>();
        for (final Coordinate coord : grid) {
            final int height = grid.get(coord);
            if (height == 'S' || height == 'a')
                possibleStarts.add(coord);
        }

        if (possibleStarts.isEmpty())
            throw new IllegalStateException("Start co-ordinate was not found?");

        return possibleStarts;
    }

    /*
     * Find the "end" co-ordinate (marked with an 'E').
     */
    private static Coordinate findEnd(final Grid grid) {
        for (final Coordinate coord : grid) {
            if (grid.get(coord) == 'E')
                return coord;
        }

        throw new IllegalStateException("Start co-ordinate was not found?");
    }

    /*
     * Calculate the cost of moving from one position to another.
     *
     * The cost is 1 if the current position is _at most_ one unit lower than
     * the target position; otherwise the cost is /too damn high/.
     */
    private static long resolve(final long curr, final long next, final long prio) {
        final long current = (curr == 'S' ? 'a' : curr) - 'a';
        final long target = (next == 'E' ? 'z' : next) - 'a';
        return target <= (current + 1) ? prio + 1L : (long) Integer.MAX_VALUE;
    }

}

