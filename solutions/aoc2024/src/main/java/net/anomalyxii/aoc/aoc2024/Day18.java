package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import net.anomalyxii.aoc.utils.algorithms.Dijkstra;
import net.anomalyxii.aoc.utils.algorithms.ShortestPath;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 18.
 */
@Solution(year = 2024, day = 18, title = "RAM Run")
public class Day18 {

    private static final ShortestPath SHORTEST_PATH = new Dijkstra();

    // ****************************************
    // Private Members
    // ****************************************

    private final int width;
    private final int height;
    private final int t;

    // ****************************************
    // Constructors
    // ****************************************

    public Day18() {
        this(70, 70, 1024);
    }

    Day18(final int width, final int height, final int t) {
        this.width = width;
        this.height = height;
        this.t = t;
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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final List<Coordinate> corruptions = context.stream()
                .map(Coordinate::parse)
                .toList();

        return solve(corruptions, t);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final List<Coordinate> corruptions = context.stream()
                .map(Coordinate::parse)
                .toList();

        final int idx = findCriticalPoint(corruptions, t + 1, corruptions.size());
        final Coordinate blocker = corruptions.get(idx);
        return blocker.x() + "," + blocker.y();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link ObjectTuple} containing the answers for both parts
     */
    @Optimised
    public ObjectTuple<Integer, String> calculateAnswers(final SolutionContext context) {
        final List<Coordinate> corruptions = context.stream()
                .map(Coordinate::parse)
                .toList();

        final int idx = findCriticalPoint(corruptions, t + 1, corruptions.size());
        final Coordinate blocker = corruptions.get(idx);
        return new ObjectTuple<>(
                solve(corruptions, t),
                blocker.x() + "," + blocker.y()
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the critical point that blocks off all escape routes.
     */
    private int findCriticalPoint(final List<Coordinate> corruptions, final int ts, final int te) {
        if (ts == te) return solve(corruptions, te) < Integer.MAX_VALUE ? te : -1;

        final int diff = te - ts;
        if (diff == 1) return solve(corruptions, te) < Integer.MAX_VALUE ? te : ts;

        final int mid = ts + (diff / 2);
        final int path = solve(corruptions, mid);
        return path == Integer.MAX_VALUE
                ? findCriticalPoint(corruptions, ts, mid)
                : findCriticalPoint(corruptions, mid, te);
    }

    /*
     * Find the shortest path.
     */
    private int solve(final List<Coordinate> corruptions, final int limit) {
        final Set<Coordinate> coordinates = new HashSet<>(corruptions.subList(0, limit));
        final Grid grid = Grid.size(width + 1, height + 1, c -> coordinates.contains(c) ? Integer.MAX_VALUE : 1);
        return (int) SHORTEST_PATH.solve(grid, grid.min(), grid.max());
    }

}

