package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 17.
 */
@Solution(year = 2023, day = 17, title = "Clumsy Crucible")
public class Day17 {

    /*
     * No value set.
     */
    private static final int NO_VAL = Integer.MAX_VALUE;

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
        final Memo memo = Memo.forCrucible(grid);
        return solve(grid, memo);
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

        final Memo memo = Memo.forUltraCrucible(grid);
        return solve(grid, memo);
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

        final Memo part1 = Memo.forCrucible(grid);
        final Memo part2 = Memo.forUltraCrucible(grid);

        return new IntTuple(solve(grid, part1), solve(grid, part2));
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the path from top-left to bottom-right that results in the
     * least amount of heat loss.
     */
    private static int solve(final Grid grid, final Memo memo) {
        final Coordinate from = grid.min();
        final Coordinate to = grid.max();

        memo.dist[memo.offset(from, Direction.DOWN, 0)] = 0;
        memo.dist[memo.offset(from, Direction.RIGHT, 0)] = 0;

        final PriorityQueue<CoordinateWithPriority> queue = new PriorityQueue<>(CoordinateWithPriority.COMPARATOR);
        queue.add(prio(from, Direction.DOWN, 0, 0));
        queue.add(prio(from, Direction.RIGHT, 0, 0));

        CoordinateWithPriority uk;
        while ((uk = queue.poll()) != null) {
            final Coordinate uc = uk.coordinate;
            final Direction ud = uk.direction;
            final int un = uk.count;
            final int up = uk.priority;

            final int priority = memo.get(uk);
            if (up > priority) continue;

            Direction.forEach(direction -> {
                final CoordinateWithPriority next = memo.next(grid, direction, uc, ud, un, up);
                if (next == null) return;

                final int neighbourPriority = memo.get(next);
                if (next.priority < neighbourPriority) {
                    memo.update(uc, next);
                    queue.add(next);
                }
            });
        }

        return memo.shortestPath(to);
    }

    /*
     * Wrap the given `Coordinate` and `Priority` into a
     * `CoordinateWithPriority`.
     */
    private static CoordinateWithPriority prio(
            final Coordinate coordinate,
            final Direction direction,
            final int count,
            final int priority
    ) {
        return new CoordinateWithPriority(coordinate, direction, count, priority);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A combination of a `Coordinate` and the priority.
     */
    private record CoordinateWithPriority(Coordinate coordinate, Direction direction, int count, int priority) {

        static final Comparator<CoordinateWithPriority> COMPARATOR = Comparator.comparingLong((CoordinateWithPriority pp) -> pp.priority);

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final CoordinateWithPriority that = (CoordinateWithPriority) o;
            return priority == that.priority
                    && direction == that.direction
                    && count == that.count
                    && Objects.equals(coordinate, that.coordinate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinate, direction, count, priority);
        }

    }

    /*
     * Holds memoised data for calculating the shortest path.
     */
    private record Memo(int[] dist, Coordinate[] prev, int width, int min, int max) {

        // Helper Methods

        /*
         * Calculate the next `Coordinate` to visit, and the priority of that
         * visit compared to other `Coordinate`s.
         */
        CoordinateWithPriority next(
                final Grid grid,
                final Direction nextDirection,
                final Coordinate currentCoordinate,
                final Direction currentDirection,
                final int currentDirectionCount,
                final int currentPriority
        ) {
            if (nextDirection == currentDirection.reverse()) return null;
            if (currentDirection == nextDirection) {
                final Coordinate neighbour = currentCoordinate.adjustBy(nextDirection);
                if (!grid.contains(neighbour)) return null;

                final int nextCount = currentDirectionCount + 1;
                if (nextCount == (max - min) + 1) return null;

                final int score = grid.get(neighbour);
                return prio(neighbour, nextDirection, nextCount, score + currentPriority);
            }

            int sum = 0;
            Coordinate next = currentCoordinate;
            for (int i = 0; i < min; i++) {
                next = next.adjustBy(nextDirection);
                if (!grid.contains(next)) return null;

                sum += grid.get(next);
            }

            return prio(next, nextDirection, 0, sum + currentPriority);
        }

        /*
         * Wrap the given `Coordinate` and `Priority` into a
         * `CoordinateWithPriority`.
         */
        int offset(
                final Coordinate coordinate,
                final Direction direction,
                final int count
        ) {
            final int cidx = coordinate.y() * width + coordinate.x();
            final int didx = cidx * 4 + direction.ordinal();
            final int boundary = (max - min) + 1;
            assert count >= 0 && count <= boundary : "Count: 0 <= " + count + " <= " + boundary;
            return (didx * boundary) + count;
        }

        /*
         * Get the currently set priority of a given `Coordinate`.
         */
        int get(final CoordinateWithPriority next) {
            return dist[offset(next.coordinate, next.direction, next.count)];
        }

        /*
         * Update the memo with the result of a given visit.
         */
        void update(final Coordinate current, final CoordinateWithPriority next) {
            dist[offset(next.coordinate, next.direction, next.count)] = next.priority;
            prev[offset(next.coordinate, next.direction, next.count)] = current;
        }

        /*
         * Return the length of the shortest path to the destination cell.
         */
        int shortestPath(final Coordinate to) {
            return Arrays.stream(dist, offset(to, Direction.UP, 0), offset(to, Direction.RIGHT, (max - min) + 1))
                    .min()
                    .orElseThrow();
        }

        // Static Helper Methods

        /*
         * Create a `Memo` for a regular crucible.
         */
        static Memo forCrucible(final Grid grid) {
            return forMinMaxDistance(grid, 1, 3);
        }

        /*
         * Create a `Memo` for an ultra crucible.
         */
        static Memo forUltraCrucible(final Grid grid) {
            return forMinMaxDistance(grid, 4, 10);
        }

        /*
         * Create a `Memo` for a crucible that has both a minimum and maximum
         * travel distance in any direction.
         */
        private static Memo forMinMaxDistance(final Grid grid, final int min, final int max) {
            final int distanceSize = (max - min) + 1;
            final int size = grid.height() * grid.width() * 4 * distanceSize;
            final int[] dist = (int[]) Array.newInstance(int.class, size);
            final Coordinate[] prev = (Coordinate[]) Array.newInstance(Coordinate.class, size);

            Arrays.fill(dist, NO_VAL);
            Arrays.fill(prev, null);
            return new Memo(dist, prev, grid.width(), min, max);
        }
    }

}

