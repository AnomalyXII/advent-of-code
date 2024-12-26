package net.anomalyxii.aoc.utils.algorithms;

import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

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
     * @param grid the {@link Grid} to traverse
     * @param from the {@link Coordinate} to start at
     * @param to   the {@link Coordinate} to traverse to
     * @return the shortest distance between thw two points
     */
    long solve(Grid grid, Coordinate from, Coordinate to);

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
        return solve(grid, grid.min(), grid.max());
    }

}
