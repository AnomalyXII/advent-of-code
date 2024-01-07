package net.anomalyxii.aoc.utils.geometry;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a point in 3D space.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z coordinate
 */
public record Point(int x, int y, int z) implements Comparable<Point> {

    /**
     * A {@link Point} representing an origin, i.e. {@code (0,0,0)}.
     */
    public static final Point ORIGIN = new Point(0, 0, 0);

    /**
     * A (theoretical) maximum {@link Point}.
     */
    public static final Point MAX = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);


    /**
     * A (theoretical) minimum {@link Point}.
     */
    public static final Point MIN = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    private static final Comparator<Point> COMPARATOR = Comparator
            .comparingInt((Point point) -> point.x)
            .thenComparingInt((Point point) -> point.y)
            .thenComparingInt((Point point) -> point.z);

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Calculate the new {@link Point} reached by travelling one step
     * with the given {@link Velocity}.
     *
     * @param h the horizontal distance to adjust by
     * @param v the vertical distance to adjust by
     * @param d the depth distance to adjust by
     * @return the new {@link Point}
     */
    public Point adjustBy(final int h, final int v, final int d) {
        return new Point(x + h, y + v, z + d);
    }

    // ****************************************
    // Comparable Methods
    // ****************************************

    @Override
    public int compareTo(final Point o) {
        return COMPARATOR.compare(this, o);
    }

    // ****************************************
    // Equals & Hash Code
    // ****************************************

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Point} by parsing the given (x,y) coordinate.
     *
     * @param coordinate the coordinate
     * @return the {@link Point}
     */
    public static Point parse(final String coordinate) {
        final String[] xyz = coordinate.split(",", 3);
        return new Point(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
    }

}
