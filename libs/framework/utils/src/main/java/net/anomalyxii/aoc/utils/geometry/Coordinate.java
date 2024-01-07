package net.anomalyxii.aoc.utils.geometry;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a coordinate in 2D space.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 */
public record Coordinate(int x, int y) implements Comparable<Coordinate> {

    /**
     * A {@link Coordinate} representing an origin, i.e. {@code (0,0)}.
     */
    public static final Coordinate ORIGIN = new Coordinate(0, 0);

    private static final Comparator<Coordinate> COMPARATOR = Comparator
            .comparingInt((Coordinate coordinate) -> coordinate.x)
            .thenComparingInt((Coordinate coordinate) -> coordinate.y);

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Calculate the new {@link Coordinate} reached by travelling one step
     * with the given {@link Velocity}.
     *
     * @param h the horizontal distance to adjust by
     * @param v the vertical distance to adjust by
     */
    public Coordinate adjustBy(final int h, final int v) {
        return new Coordinate(x + h, y + v);
    }

    /**
     * Calculate the new {@link Coordinate} reached by travelling one step
     * with the given {@link Velocity}.
     *
     * @param v the {@link Velocity} to adjust by
     */
    public Coordinate adjustBy(final Velocity v) {
        return adjustBy(v.h(), v.v());
    }

    /**
     * Calculate the new {@link Coordinate} reached by travelling one step
     * in the given {@link Direction}.
     *
     * @param d the {@link Direction} to travel
     */
    public Coordinate adjustBy(final Direction d) {
        return adjustBy(d.asVelocity());
    }

    /**
     * Calculate the new {@link Coordinate} reached by travelling one step
     * in the given {@link Direction}.
     *
     * @param d the {@link Direction} to travel
     */
    public Coordinate adjustBy(final Direction d, final int magnitude) {
        final Velocity velocity = d.asVelocity();
        return adjustBy(velocity.h() * magnitude, velocity.v() * magnitude);
    }

    /**
     * Get a {@link Stream} of adjacent {@link Coordinate Coordinates}.
     *
     * @return a {@link Stream} of adjacent {@link Coordinate Coordinates}
     */
    public Stream<Coordinate> adjacent() {
        return Stream.of(
                new Coordinate(x, y - 1),
                new Coordinate(x - 1, y),
                new Coordinate(x + 1, y),
                new Coordinate(x, y + 1)
        );
    }

    /**
     * Get a {@link Stream} of neighbouring {@link Coordinate Coordinates}.
     *
     * @return a {@link Stream} of neighbouring {@link Coordinate Coordinates}
     */
    public Stream<Coordinate> neighbours() {
        return Stream.of(
                new Coordinate(x - 1, y - 1),
                new Coordinate(x, y - 1),
                new Coordinate(x + 1, y - 1),
                new Coordinate(x - 1, y),
                new Coordinate(x + 1, y),
                new Coordinate(x - 1, y + 1),
                new Coordinate(x, y + 1),
                new Coordinate(x + 1, y + 1)
        );
    }

    /**
     * Get a {@link Stream} of neighbouring {@link Coordinate Coordinates},
     * with this {@link Coordinate} in the centre.
     *
     * @return a {@link Stream} of this and neighbouring {@link Coordinate Coordinates}
     */
    public Stream<Coordinate> neighboursAndSelf() {
        return Stream.of(
                new Coordinate(x - 1, y - 1),
                new Coordinate(x, y - 1),
                new Coordinate(x + 1, y - 1),
                new Coordinate(x - 1, y),
                this,
                new Coordinate(x + 1, y),
                new Coordinate(x - 1, y + 1),
                new Coordinate(x, y + 1),
                new Coordinate(x + 1, y + 1)
        );
    }

    // ****************************************
    // Comparable Methods
    // ****************************************

    @Override
    public int compareTo(final Coordinate o) {
        return COMPARATOR.compare(this, o);
    }

    // ****************************************
    // Equals & Hash Code
    // ****************************************

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Coordinate} by parsing the given {@code (x,y)}
     * {@link String}.
     *
     * @param coordinate the coordinate
     * @return the {@link Coordinate}
     */
    public static Coordinate parse(final String coordinate) {
        final String[] xy = coordinate.split(",", 2);
        return new Coordinate(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

}
