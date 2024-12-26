package net.anomalyxii.aoc.utils.algorithms;

import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.List;

/**
 * An {@link AreaCalculator} that uses the shoelace algorithm.
 */
public class ShoelaceCalculator implements AreaCalculator {

    // ****************************************
    // AreaCalculator Methods
    // ****************************************

    @Override
    public long area(final List<Coordinate> corners) {
        long plus = 0;
        long minus = 0;
        for (int x = 0; x < corners.size(); x++) {
            final int y = x == corners.size() - 1 ? 0 : x + 1;

            plus += (long) corners.get(x).x() * corners.get(y).y();
            minus += (long) corners.get(y).x() * corners.get(x).y();
        }

        return (long) (0.5 * Math.abs(plus - minus));
    }

}
