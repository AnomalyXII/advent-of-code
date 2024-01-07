package net.anomalyxii.aoc.utils.geometry;

import java.util.Iterator;

/**
 * An {@link Iterator} over a set of {@link Coordinate Coordinates} within a
 * bounded {@link TwoDimensionalSpace two-dimentional space}.
 */
class CoordinateIterator implements Iterator<Coordinate> {

    // ****************************************
    // Private Members
    // ****************************************

    private final int width;
    private final int height;

    private int i = 0;
    private int j = 0;

    // ****************************************
    // Constructors
    // ****************************************

    CoordinateIterator(final TwoDimensionalSpace space) {
        this.width = space.width();
        this.height = space.height();
    }

    // ****************************************
    // Iterator Methods
    // ****************************************

    @Override
    public boolean hasNext() {
        return j < height;
    }

    @Override
    public Coordinate next() {
        final Coordinate coordinate = new Coordinate(i, j);
        ++i;
        if (i >= width) {
            i = 0;
            ++j;
        }
        return coordinate;
    }

}
