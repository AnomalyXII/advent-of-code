package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 12.
 */
@Solution(year = 2024, day = 12, title = "Garden Groups")
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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final Grid grid = context.readGrid();

        final Map<Character, Set<Coordinate>> plotsPerPlantType = grid.stream()
                .collect(Collectors.groupingBy(c -> (char) grid.get(c), Collectors.toSet()));

        return plotsPerPlantType.values().stream()
                .mapToInt(plots -> process(grid, plots, Day12::calculateArea, Day12::calculatePerimeter))
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = context.readGrid();

        final Map<Character, Set<Coordinate>> plotsPerPlantType = grid.stream()
                .collect(Collectors.groupingBy(c -> (char) grid.get(c), Collectors.toSet()));

        return plotsPerPlantType.values().stream()
                .mapToInt(plots -> process(grid, plots, Day12::calculateArea, Day12::calculateEdges))
                .sum();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final Grid grid = context.readGrid();

        final Map<Character, Set<Coordinate>> plotsPerPlantType = grid.stream()
                .collect(Collectors.groupingBy(c -> (char) grid.get(c), Collectors.toSet()));

        return plotsPerPlantType.values().stream()
                .reduce(
                        IntTuple.NULL,
                        (result, plots) -> result.add(
                                process(grid, plots, Day12::calculateArea, Day12::calculatePerimeter),
                                process(grid, plots, Day12::calculateArea, Day12::calculateEdges)
                        ),
                        IntTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Process all plots of a specific plant type.
     */
    private static int process(
            final Grid grid,
            final Set<Coordinate> plots,
            final ToIntFunction<Set<Coordinate>> areaFunc,
            final ToIntFunction<Set<Coordinate>> perimeterFunc
    ) {
        final Set<Coordinate> copy = new HashSet<>(plots);

        int total = 0;
        while (!copy.isEmpty()) {
            final Coordinate next = copy.iterator().next();
            final Set<Coordinate> contiguousPlots = extractPlot(grid, next, copy);
            final int area = areaFunc.applyAsInt(contiguousPlots);
            final int perimeter = perimeterFunc.applyAsInt(contiguousPlots);
            total += area * perimeter;
        }

        return total;
    }

    /*
     * Extract all the `Coordinate`s that are in a plot.
     */
    private static Set<Coordinate> extractPlot(
            final Grid grid,
            final Coordinate start,
            final Set<Coordinate> plots
    ) {
        final Set<Coordinate> contiguousPlots = new HashSet<>();

        final SequencedSet<Coordinate> nextPlots = new LinkedHashSet<>();
        nextPlots.add(start);
        while (!nextPlots.isEmpty()) {
            final Coordinate next2 = nextPlots.removeFirst();
            plots.remove(next2);

            grid.forEachAdjacentTo(
                    next2,
                    c -> {
                        if (plots.contains(c))
                            nextPlots.add(c);
                    }
            );
            contiguousPlots.add(next2);
        }

        return contiguousPlots;
    }

    /*
     * Calculate the area of a set of plots.
     */
    private static int calculateArea(final Set<Coordinate> plots) {
        return plots.size();
    }

    /*
     * Calculate the perimeter of a set of plots.
     */
    private static int calculatePerimeter(final Set<Coordinate> plots) {
        int result = 0;
        for (final Coordinate c : plots) {
            for (final Direction d : Direction.values()) {
                if (!plots.contains(c.adjustBy(d))) {
                    ++result;
                }
            }
        }
        return result;
    }

    /*
     * Calculate the number of edges for a set of plots.
     */
    private static int calculateEdges(final Set<Coordinate> plots) {
        int corners = 0;
        for (final Coordinate c : plots) {
            final Coordinate up = c.adjustBy(Direction.UP);
            final Coordinate left = c.adjustBy(Direction.LEFT);
            final Coordinate down = c.adjustBy(Direction.DOWN);
            final Coordinate right = c.adjustBy(Direction.RIGHT);
            final Coordinate upleft = up.adjustBy(Direction.LEFT);
            final Coordinate upright = up.adjustBy(Direction.RIGHT);
            final Coordinate downleft = down.adjustBy(Direction.LEFT);
            final Coordinate downright = down.adjustBy(Direction.RIGHT);

            if (isCorner(up, left, upleft, plots)) ++corners;
            if (isCorner(up, right, upright, plots)) ++corners;
            if (isCorner(down, left, downleft, plots)) ++corners;
            if (isCorner(down, right, downright, plots)) ++corners;
        }
        return corners;
    }

    /*
     * Check if a `Coordinate` is a corner.
     */
    private static boolean isCorner(
            final Coordinate adj1,
            final Coordinate adj2,
            final Coordinate diag,
            final Set<Coordinate> plots
    ) {
        return !plots.contains(adj1) && !plots.contains(adj2)
                || plots.contains(adj1) && plots.contains(adj2) && !plots.contains(diag);
    }

}
