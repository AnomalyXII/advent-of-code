package net.anomalyxii.aoc.utils.algorithms;

import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.Deque;

/**
 * A shortest path algorithm.
 */
public interface ShortestPath {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Find the shortest path between two {@link Coordinate Coordinates} in a
     * {@link Grid}.
     *
     * @param grid     the {@link Grid} to traverse
     * @param from     the {@link Coordinate} to start at
     * @param to       the {@link Coordinate} to traverse to
     * @param listener the {@link Listener} that should receive progress updates
     * @return the shortest distance between thw two points
     */
    long solve(Grid grid, Coordinate from, Coordinate to, Listener listener);

    // ****************************************
    // Default Methods
    // ****************************************

    /**
     * Find the shortest path between the top-left and bottom-right
     * {@link Coordinate Coordinates} in a {@link Grid}.
     *
     * @param grid the {@link Grid} to traverse
     * @return the shortest distance between thw two points
     */
    default long solve(final Grid grid) {
        return solve(grid, grid.min(), grid.max(), Listener.NO_OP);
    }

    /**
     * Find the shortest path between two {@link Coordinate Coordinates} in a
     * {@link Grid}.
     * <p>
     * A modification of Dijkstra's Algorithm as found here:
     * https://cs.stackexchange.com/questions/118388/dijkstra-without-decrease-key
     *
     * @param grid the {@link Grid} to traverse
     * @param from the {@link Coordinate} to start at
     * @param to   the {@link Coordinate} to traverse to
     * @return the shortest distance between thw two points
     */
    default long solve(final Grid grid, final Coordinate from, final Coordinate to) {
        return solve(grid, from, to, Listener.NO_OP);
    }

    /**
     * Find the shortest path between the top-left and bottom-right
     * {@link Coordinate Points} in a {@link Grid}.
     *
     * @param grid     the {@link Grid} to traverse
     * @param listener the {@link Listener} that should receive progress updates
     * @return the shortest distance between thw two points
     */
    default long solve(final Grid grid, final Listener listener) {
        return solve(grid, grid.min(), grid.max(), listener);
    }

    // ****************************************
    // Listeners
    // ****************************************

    /**
     * A listener that will receive callbacks on the progress of the
     * shortest path algorithm.
     */
    interface Listener {

        /**
         * A no-op {@link Listener}.
         */
        Listener NO_OP = points -> {};

        // Interface Methods

        /**
         * Receive a notification when a new, partial shortest path is found.
         *
         * @param shortestPath the new partial shortest path
         */
        void onNewPartialShortestPath(Deque<Coordinate> shortestPath);

        // Default Methods

        /**
         * Receive a notification at the start of processing.
         */
        default void onStarted() {
            // Default: no-op?
        }

        /**
         * Receive a notification at the end of processing.
         */
        default void onCompleted() {
            // Default: no-op
        }

    }

}
