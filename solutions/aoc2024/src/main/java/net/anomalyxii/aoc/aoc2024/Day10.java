package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.*;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 10.
 */
@Solution(year = 2024, day = 10, title = "Hoof It")
public class Day10 {

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
        final Grid grid = context.readGrid(c -> c - '0');
        return grid.stream()
                .mapToInt(c -> calculateTrailScore(grid, c))
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
        final Grid grid = context.readGrid(c -> c - '0');
        return grid.stream()
                .mapToInt(c -> calculateTrailRating(grid, c))
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
        final Grid grid = context.readGrid(c -> c - '0');
        return grid.stream()
                .reduce(
                        IntTuple.NULL,
                        (r, c) -> {
                            final int value = grid.get(c);
                            if (value != 0) return r;

                            return r.add(
                                    calculateTrailScore(grid, c),
                                    calculateTrailRating(grid, c)
                            );
                        },
                        IntTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the trail score for a given `Coordinate`.
     */
    private int calculateTrailScore(final Grid grid, final Coordinate c) {
        if (grid.get(c) != 0) return 0;

        final Deque<Coordinate> current = new ArrayDeque<>();
        current.addLast(c);
        for (int i = 1; i <= 9 && !current.isEmpty(); i++) {
            final Set<Coordinate> nexts = new HashSet<>();
            while (!current.isEmpty()) {
                final int finalI = i;
                final Coordinate next = current.pollFirst();
                grid.forEachAdjacentTo(
                        next,
                        c2 -> {
                            final int value = grid.get(c2);
                            if (value != finalI) return;

                            nexts.add(c2);
                        }
                );
            }
            current.addAll(nexts);
        }
        return current.size();
    }

    /*
     * Calculate the trail rating for a given `Coordinate`.
     */
    private int calculateTrailRating(final Grid grid, final Coordinate c) {
        if (grid.get(c) != 0) return 0;

        final Deque<Coordinate> current = new ArrayDeque<>();
        final Map<Coordinate, Integer> ratings = new HashMap<>();
        current.addLast(c);
        ratings.put(c, 1);

        for (int i = 1; i <= 9 && !current.isEmpty(); i++) {
            final Set<Coordinate> nexts = new HashSet<>();
            while (!current.isEmpty()) {
                final int finalI = i;
                final Coordinate next = current.pollFirst();
                grid.forEachAdjacentTo(
                        next,
                        c2 -> {
                            final int value = grid.get(c2);
                            if (value != finalI) return;

                            nexts.add(c2);
                            final int rating = ratings.get(next);
                            ratings.compute(c2, (k, v) -> v == null ? rating : v + rating);
                        }
                );
            }
            current.addAll(nexts);
        }
        return current.stream()
                .mapToInt(ratings::get)
                .sum();
    }

}

