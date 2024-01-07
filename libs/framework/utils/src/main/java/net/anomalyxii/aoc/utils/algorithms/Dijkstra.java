package net.anomalyxii.aoc.utils.algorithms;

import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A {@link ShortestPath} implementation using Dijkstra's algorithm.
 * <p>
 * Specifically, this implementation is a modification of Dijkstra's
 * Algorithm as found
 * <a href="https://cs.stackexchange.com/questions/118388/dijkstra-without-decrease-key">here</a>
 */
public class Dijkstra implements ShortestPath {

    /*
     * No value.
     */
    private static final int NO_VAL = Integer.MAX_VALUE;

    // ****************************************
    // Private Members
    // ****************************************

    private final PriorityFunction priorityFunction;

    // ****************************************
    // Constructors
    // ****************************************

    public Dijkstra() {
        this(Dijkstra::standardPriority);
    }

    public Dijkstra(final PriorityFunction priorityFunction) {
        this.priorityFunction = priorityFunction;
    }

    // ****************************************
    // ShortestPath Methods
    // ****************************************

    @Override
    public long solve(final Grid grid, final Coordinate from, final Coordinate to, final Listener listener) {
        listener.onStarted();

        final long[][] dist = (long[][]) Array.newInstance(long.class, grid.height(), grid.width());
        final Coordinate[][] prev = (Coordinate[][]) Array.newInstance(Coordinate.class, grid.height(), grid.width());

        grid.forEach(point -> {
            dist[point.y()][point.x()] = from.equals(point) ? 0 : NO_VAL;
            prev[point.y()][point.x()] = null;
        });

        final PriorityQueue<CoordinateWithPriority> queue = new PriorityQueue<>(CoordinateWithPriority.COMPARATOR);
        queue.add(prio(from, 0));

        final Deque<Coordinate> shortestPath = resolveShortestPath(from, to, prev);
        listener.onNewPartialShortestPath(shortestPath);

        CoordinateWithPriority uk;
        while ((uk = queue.poll()) != null) {
            final Coordinate u = uk.coordinate;
            final long priority = dist[u.y()][u.x()];
            if (uk.priority > priority) continue;

            grid.forEachAdjacentTo(u, neighbour -> {
                final long neighbourPriority = dist[neighbour.y()][neighbour.x()];
                final long alt = priorityFunction.resolve(grid.get(u), grid.get(neighbour), priority);
                if (alt < neighbourPriority) {
                    dist[neighbour.y()][neighbour.x()] = alt;
                    prev[neighbour.y()][neighbour.x()] = u;

                    queue.add(prio(neighbour, alt));

                    final Deque<Coordinate> newShortestPath = resolveShortestPath(from, neighbour, prev);
                    if (!newShortestPath.equals(shortestPath)) {
                        shortestPath.clear();
                        shortestPath.addAll(newShortestPath);
                        listener.onNewPartialShortestPath(newShortestPath);
                    }
                }
            });
        }

        listener.onCompleted();
        return dist[to.y()][to.x()];
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Resolve the shortest known path between the two `Coordinate`s.
     */
    private static Deque<Coordinate> resolveShortestPath(
            final Coordinate from,
            final Coordinate to,
            final Coordinate[][] paths
    ) {
        final Deque<Coordinate> shortestPath = new ArrayDeque<>();
        Coordinate current = to;
        do {
            shortestPath.addFirst(current);
            if (current.equals(from))
                break;

            current = paths[current.y()][current.x()];
        } while (current != null);
        return shortestPath;
    }

    /*
     * Wrap the given `Coordinate` and `Priority` into a
     * `CoordinateWithPriority`.
     */
    private static CoordinateWithPriority prio(final Coordinate coordinate, final long priority) {
        return new CoordinateWithPriority(coordinate, priority);
    }

    /*
     * Calculate a standard priority (`priority = current + next`).
     */
    private static long standardPriority(final long current, final long neighbour, final long priority) {
        return priority + neighbour;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /**
     * A function for calculating the cost of moving between two nodes.
     */
    @FunctionalInterface
    public interface PriorityFunction {

        /**
         * Calculate the cost of moving from {@literal point} to
         * {@literal neighbour}, given the {@literal current} path cost.
         *
         * @param point     the value of the current node
         * @param neighbour the value of the target node
         * @param current   the cost of this path, so far
         * @return the resolved score
         */
        long resolve(long point, long neighbour, long current);

    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A combination of a `Coordinate` and the priority.
     */
    private record CoordinateWithPriority(Coordinate coordinate, long priority) {

        static final Comparator<CoordinateWithPriority> COMPARATOR = Comparator.comparingLong(pp -> pp.priority);

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final CoordinateWithPriority that = (CoordinateWithPriority) o;
            return priority == that.priority && Objects.equals(coordinate, that.coordinate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinate, priority);
        }

    }

}
