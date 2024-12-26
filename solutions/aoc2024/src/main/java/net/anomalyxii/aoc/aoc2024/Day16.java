package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.min;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 16.
 */
@Solution(year = 2024, day = 16, title = "Reindeer Maze")
public class Day16 {

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
        final Coordinate start = grid.stream()
                .filter(c -> grid.get(c) == 'S')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));
        final Coordinate end = grid.stream()
                .filter(c -> grid.get(c) == 'E')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));

        return findShortestPath(grid, start, end, new HashSet<>());
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
        final Coordinate start = grid.stream()
                .filter(c -> grid.get(c) == 'S')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));
        final Coordinate end = grid.stream()
                .filter(c -> grid.get(c) == 'E')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));

        final Set<Coordinate> onPath = new HashSet<>();
        findShortestPath(grid, start, end, onPath);
        return onPath.size();
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
        final Coordinate start = grid.stream()
                .filter(c -> grid.get(c) == 'S')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));
        final Coordinate end = grid.stream()
                .filter(c -> grid.get(c) == 'E')
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find starting position"));

        final Set<Coordinate> onPath = new HashSet<>();
        final int shortestPath = findShortestPath(grid, start, end, onPath);
        return new IntTuple(shortestPath, onPath.size());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Modification of Dijkstra's shortest path, to deal with directions.
     */
    private int findShortestPath(
            final Grid grid,
            final Coordinate from,
            final Coordinate to,
            final Set<Coordinate> onPath
    ) {
        final int[] dist = (int[]) Array.newInstance(int.class, grid.height() * grid.width() * 4);

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[offset(grid, from, Direction.RIGHT)] = 0;

        final PriorityQueue<TrackedState> queue = new PriorityQueue<>(TrackedState.COMPARATOR);
        queue.add(prio(from, Direction.RIGHT, new ArrayList<>(List.of(from)), 0));

        TrackedState uk;
        int lowestScoreSoFar = Integer.MAX_VALUE;
        while ((uk = queue.poll()) != null) {
            final Coordinate uc = uk.coordinate;
            final Direction ud = uk.direction;

            final int priority = dist[offset(grid, uc, ud)];
            if (uk.priority > priority) continue;

            if (uc.equals(to) && priority <= lowestScoreSoFar) {
                if (priority < lowestScoreSoFar) {
                    lowestScoreSoFar = priority;
                    onPath.clear();
                }

                onPath.addAll(uk.path);
                continue;
            }

            for (final Direction d : new Direction[]{ud, ud.rotateClockwise(), ud.rotateAnticlockwise()}) {
                if (d == ud.reverse()) continue;

                final Coordinate neighbour = uc.adjustBy(d);
                final char sym = (char) grid.get(neighbour);
                if (sym == '#') continue;

                final boolean forward = ud == d;
                final int modifier = forward ? 0 : 1000;
                final int alt = priority + modifier + 1;

                final int idx = offset(grid, neighbour, d);
                final int neighbourPriority = dist[idx];
                final int target = min(neighbourPriority, lowestScoreSoFar);
                if (alt <= target) {
                    dist[idx] = alt;

                    final List<Coordinate> newPath = new ArrayList<>(uk.path);
                    newPath.add(neighbour);
                    queue.add(prio(neighbour, d, newPath, alt));
                }
            }
        }

        return lowestScoreSoFar;
    }

    /*
     * Compute the linear offset of the `Coordinate`/`Direction` pair.
     */
    private int offset(final Grid grid, final Coordinate from, final Direction direction) {
        return (from.y() * grid.width() + from.x()) * 4 + direction.ordinal();
    }

    /*
     * Wrap the given `Coordinate`, `Direction` and `Priority` into a
     * `TrackedState`.
     */
    private static TrackedState prio(
            final Coordinate coordinate,
            final Direction direction,
            final Collection<Coordinate> path,
            final int priority
    ) {
        return new TrackedState(coordinate, direction, path, priority);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A combination of a `CoordinateAndDirection` and the priority.
     */
    private record TrackedState(Coordinate coordinate, Direction direction, Collection<Coordinate> path, int priority) {

        /*
         * A comparator for `TrackedState`.
         */
        static final Comparator<TrackedState> COMPARATOR = Comparator.comparingLong((TrackedState pp) -> pp.priority);

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final TrackedState that = (TrackedState) o;
            return Objects.equals(coordinate, that.coordinate)
                    && direction == that.direction
                    && priority == that.priority;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinate, direction, priority);
        }

    }

}

