package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 20.
 */
@Solution(year = 2024, day = 20, title = "Race Condition")
public class Day20 {

    // ****************************************
    // Private Members
    // ****************************************

    private final int t1;
    private final int t2;

    // ****************************************
    // Constructors
    // ****************************************

    public Day20() {
        this(100, 100);
    }

    Day20(final int t1, final int t2) {
        this.t1 = t1;
        this.t2 = t2;
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
        final Grid grid = context.readGrid();
        final List<Coordinate> path = walkPath(grid);
        final int length = path.size();
        return IntStream.range(0, length - t1)
                .map(start -> countCheats(path, start, 2, t1))
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
        final List<Coordinate> path = walkPath(grid);
        final int length = path.size();
        return IntStream.range(0, length - t2)
                .map(start -> countCheats(path, start, 20, t2))
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
        final List<Coordinate> path = walkPath(grid);
        final int length = path.size();
        return IntStream.range(0, length - Math.min(t1, t2))
                .mapToObj(start -> new IntTuple(
                        countCheats(path, start, 2, t1),
                        countCheats(path, start, 20, t2)
                ))
                .reduce(IntTuple::add)
                .orElseThrow();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Walk the racecourse and record the path.
     */
    private static List<Coordinate> walkPath(final Grid grid) {
        final Coordinate start = grid.stream()
                .filter(c -> grid.get(c) == 'S')
                .findFirst()
                .orElseThrow();
        final Coordinate end = grid.stream()
                .filter(c -> grid.get(c) == 'E')
                .findFirst()
                .orElseThrow();

        final List<Coordinate> path = new ArrayList<>();
        Coordinate c = start;
        out:
        do {
            final Coordinate prev = !path.isEmpty() ? path.getLast() : null;
            path.add(c);

            for (final Direction d : Direction.values()) {
                final Coordinate next = c.adjustBy(d);
                if (grid.get(next) == '#' || next.equals(prev))
                    continue;

                c = next;
                continue out;
            }

            throw new IllegalStateException("Did not find a valid direction to travel...");
        } while (!end.equals(c));
        path.add(c);
        return path;
    }

    /*
     * Find all the cheats that can work in a given duration from a given
     * starting point.
     */
    private int countCheats(
            final List<Coordinate> path,
            final int startIdx,
            final int cheatLength,
            final int target
    ) {
        final Coordinate start = path.get(startIdx);

        int count = 0;
        for (int i = startIdx + target; i < path.size(); i++) {
            // Calculate the Manhattan distance to the next point
            final Coordinate end = path.get(i);

            final int distance = start.calculateManhattanDistance(end);
            if (distance > cheatLength) continue;

            final int saving = i - startIdx - distance;
            if (saving < target) continue;

            ++count;
        }

        return count;
    }

}

