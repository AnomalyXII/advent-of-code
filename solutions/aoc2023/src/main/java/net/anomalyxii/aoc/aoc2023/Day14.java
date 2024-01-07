package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.HashMap;
import java.util.Map;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 14.
 */
@Solution(year = 2023, day = 14, title = "Parabolic Reflector Dish")
public class Day14 {

    /*
     * Number of iterations of tilting.
     */
    private static final int NUMBER_OF_ITERATIONS = 1000000000;

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
        final Grid.MutableGrid grid = Grid.parseMutable(context.stream());
        tilt(grid, Direction.UP);

        return grid.stream()
                .filter(coord -> grid.get(coord) == 'O')
                .mapToInt(coord -> (grid.height() - coord.y()))
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
        final Grid.MutableGrid grid = Grid.parseMutable(context.stream());

        final Map<Integer, Integer> loopDetection = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            tilt(grid, Direction.NORTH);
            tilt(grid, Direction.WEST);
            tilt(grid, Direction.SOUTH);
            tilt(grid, Direction.EAST);

            final int hash = grid.calculateHash();
            if (loopDetection.containsKey(hash)) {
                final int startOfLoop = loopDetection.get(hash);
                final int lengthOfLoop = i - startOfLoop;
                final int delta = (NUMBER_OF_ITERATIONS - i) % lengthOfLoop;
                i = NUMBER_OF_ITERATIONS - (delta == 0 ? lengthOfLoop : delta);
            } else {
                loopDetection.put(hash, i);
            }
        }

        return grid.stream()
                .filter(coord -> grid.get(coord) == 'O')
                .mapToInt(coord -> (grid.height() - coord.y()))
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
        final Grid.MutableGrid grid = Grid.parseMutable(context.stream());

        tilt(grid, Direction.NORTH);

        final int answer1 = grid.stream()
                .filter(coord -> grid.get(coord) == 'O')
                .mapToInt(coord -> (grid.height() - coord.y()))
                .sum();

        tilt(grid, Direction.WEST);
        tilt(grid, Direction.SOUTH);
        tilt(grid, Direction.EAST);

        final Map<Integer, Integer> loopDetection = new HashMap<>();
        for (int i = 1; i < NUMBER_OF_ITERATIONS; i++) {
            tilt(grid, Direction.NORTH);
            tilt(grid, Direction.WEST);
            tilt(grid, Direction.SOUTH);
            tilt(grid, Direction.EAST);

            final int hash = grid.calculateHash();
            if (loopDetection.containsKey(hash)) {
                final int startOfLoop = loopDetection.get(hash);
                final int lengthOfLoop = i - startOfLoop;
                final int delta = (NUMBER_OF_ITERATIONS - i) % lengthOfLoop;
                i = NUMBER_OF_ITERATIONS - (delta == 0 ? lengthOfLoop : delta);
            } else {
                loopDetection.put(hash, i);
            }
        }

        final int answer2 = grid.stream()
                .filter(coord -> grid.get(coord) == 'O')
                .mapToInt(coord -> (grid.height() - coord.y()))
                .sum();

        return new IntTuple(answer1, answer2);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Tilt the dish in a specific direction.
     */
    private static void tilt(final Grid.MutableGrid grid, final Direction direction) {
        switch (direction) {
            case UP, LEFT -> tiltForwards(grid, direction);
            case DOWN, RIGHT -> tiltBackwards(grid, direction);
            default -> throw new IllegalArgumentException("Invalid direction:: " + direction);
        }

    }

    /*
     * Locate all the rocks on the dish.
     */

    /*
     * Collect the positions of rocks from top-left to bottom-right.
     */
    private static void tiltForwards(final Grid.MutableGrid grid, final Direction direction) {
        for (int y = 0; y < grid.height(); y++) {
            for (int x = 0; x < grid.width(); x++) {
                final Coordinate rock = new Coordinate(x, y);
                if (grid.get(rock) != 'O') continue;

                rollRock(grid, direction, rock);
            }
        }
    }

    /*
     * Collect the positions of rocks from bottom-right to top-left.
     */
    private static void tiltBackwards(final Grid.MutableGrid grid, final Direction direction) {
        for (int y = grid.height() - 1; y >= 0; y--) {
            for (int x = grid.width() - 1; x >= 0; x--) {
                final Coordinate rock = new Coordinate(x, y);
                if (grid.get(rock) != 'O') continue;
                rollRock(grid, direction, rock);
            }
        }
    }

    /*
     * Roll a rock in a given direction.
     */
    private static void rollRock(final Grid.MutableGrid grid, final Direction direction, final Coordinate rock) {
        Coordinate current = rock;
        Coordinate adjusted = current.adjustBy(direction);
        while (grid.contains(adjusted) && grid.get(adjusted) == '.') {
            current = adjusted;
            adjusted = current.adjustBy(direction);
        }

        grid.set(rock, '.');
        grid.set(current, 'O');
    }

}

