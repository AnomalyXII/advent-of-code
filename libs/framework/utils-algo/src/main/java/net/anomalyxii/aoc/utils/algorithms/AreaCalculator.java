package net.anomalyxii.aoc.utils.algorithms;

import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.List;

/**
 * Calculate the area of an irregular polygon.
 */
public interface AreaCalculator {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Calculate the area of a shape defined by the given
     * {@link Coordinate Coordinates}.
     *
     * @param corners the {@link List} of {@link Coordinate Coordinates} that represent the shape
     * @return the area
     */
    long area(List<Coordinate> corners);

}
