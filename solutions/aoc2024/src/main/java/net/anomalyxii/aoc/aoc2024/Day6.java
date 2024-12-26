package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 6.
 */
@Solution(year = 2024, day = 6, title = "Guard Gallivant")
public class Day6 {

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
        return traceGuardRoute(context.readGrid());
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        return calculatePossibleObstacleLocations(context.readGrid())
                .getAnswer2();
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
        return calculatePossibleObstacleLocations(context.readGrid());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Trace the guard's route around the area.
     */
    private static int traceGuardRoute(final Grid grid) {
        final Coordinate start = findStartPosition(grid);

        final Set<Coordinate> visited = new HashSet<>();
        final ExitReason exitReason = traceGuardRouteFrom(grid, start, Direction.UP, null, visited::add);
        if (exitReason == ExitReason.LOOP)
            throw new IllegalStateException("Should not detect a loop when finding guard's path");

        return visited.size();
    }

    /*
     * Trace the guard's route around the area, calculating the possible
     * locations at which an obstacle could be placed to cause the path to
     * form a loop.
     */
    private static IntTuple calculatePossibleObstacleLocations(final Grid grid) {
        Coordinate current = findStartPosition(grid);
        Direction facing = Direction.UP;
        final Set<Coordinate> obstacles = new HashSet<>();
        final Set<Coordinate> notObstacles = new HashSet<>();
        final Set<Coordinate> path = new HashSet<>();
        do {
            path.add(current); // Should never place an obstacle where the guard has already walked

            final Coordinate next = current.adjustBy(facing);
            if (!grid.contains(next)) break;

            final int nextVal = grid.get(next);
            if (nextVal == '#') {
                facing = facing.rotateClockwise();
                continue;
            }

            final boolean tryToBlockPath = !path.contains(next)
                    && !obstacles.contains(next)
                    && !notObstacles.contains(next);

            if (tryToBlockPath) {
                final ExitReason newPathExitReason =
                        traceGuardRouteFrom(grid, current, facing, next, c -> {});

                if (newPathExitReason == ExitReason.LOOP) obstacles.add(next);
                else notObstacles.add(next);
            }

            current = next;
        } while (grid.contains(current));

        return new IntTuple(path.size(), obstacles.size());
    }

    /*
     * Find the starting position.
     */
    private static Coordinate findStartPosition(final Grid grid) {
        return grid.stream()
                .filter(c -> grid.get(c) == '^')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to find the guard's starting position."));
    }

    /*
     * Trace the guard's route around the area, starting from a certain
     * location and heading in a particular direction.
     */
    private static ExitReason traceGuardRouteFrom(
            final Grid grid,
            final Coordinate start,
            final Direction startingDirection,
            final Coordinate extraObstacle,
            final Consumer<Coordinate> onProgress
    ) {
        Coordinate current = start;
        Direction facing = startingDirection;
        final Set<VisitedSquare> visited = new HashSet<>();
        do {
            if (!visited.add(new VisitedSquare(current, facing)))
                return ExitReason.LOOP;

            onProgress.accept(current);

            final Coordinate next = current.adjustBy(facing);
            if (!grid.contains(next)) break;

            final int nextVal = extraObstacle != null && extraObstacle.equals(next)
                    ? '#'
                    : grid.get(next);
            if (nextVal == '#') {
                facing = facing.rotateClockwise();
                continue;
            }

            current = next;
        } while (grid.contains(current));

        return ExitReason.LEFT_AREA;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A record of which squares the guard has visited, along with which
     * direction they were travelling at the time.
     */
    private record VisitedSquare(Coordinate pos, Direction direction) {

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final VisitedSquare that = (VisitedSquare) o;
            return Objects.equals(pos, that.pos) && direction == that.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, direction);
        }

    }

    /*
     * The reason that following the guard's path was terminated.
     */
    private enum ExitReason {

        /*
         * The guard left the grid.
         */
        LEFT_AREA,

        /*
         * The guard entered a loop.
         */
        LOOP,

        // end of constants
        ;
    }

}

