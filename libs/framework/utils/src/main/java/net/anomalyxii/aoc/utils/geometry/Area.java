package net.anomalyxii.aoc.utils.geometry;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An area between two {@link Coordinate Coordinates}.
 *
 * @param w the horizontal (width) {@link Bounds}
 * @param h the vertical (height) {@link Bounds}
 */
public record Area(Bounds w, Bounds h) implements TwoDimensionalSpace {

    /**
     * An infinite(ish) {@link Area}.
     */
    public static final Area INFINITY = new Area(Bounds.INFINITY, Bounds.INFINITY);

    /**
     * A null {@link Area}.
     */
    public static final Area NULL = new Area(Bounds.NULL, Bounds.NULL);

    // ****************************************
    // TwoDimensionalSpace Methods
    // ****************************************

    @Override
    public boolean isNull() {
        return w.isNull() || h.isNull();
    }

    @Override
    public boolean isInfinity() {
        return w.isInfinity() || h.isInfinity();
    }

    @Override
    public Coordinate min() {
        return new Coordinate(w.min(), h.min());
    }

    @Override
    public Coordinate max() {
        return new Coordinate(w.max(), h.max());
    }

    @Override
    public int width() {
        return w.length();
    }

    @Override
    public int height() {
        return h.length();
    }

    @Override
    public long area() {
        return (long) w.length() * (long) h.length();
    }

    @Override
    public boolean contains(final Coordinate coordinate) {
        return w.contains(coordinate.x()) && h.contains(coordinate.y());
    }

    @Override
    public TwoDimensionalSpace union(final TwoDimensionalSpace other) {
        if (this.isInfinity() || other.isInfinity()) return INFINITY;
        if (this.isNull()) return other;
        if (other.isNull()) return this;

        final Bounds xUnion = w.union(other.w());
        final Bounds yUnion = h.union(other.h());
        return Area.of(xUnion, yUnion);
    }

    @Override
    public TwoDimensionalSpace intersect(final TwoDimensionalSpace other) {
        final Bounds xIntersect = w.intersect(other.w());
        if (xIntersect.isNull()) return Area.NULL;
        final Bounds yIntersect = h.intersect(other.h());
        if (yIntersect.isNull()) return Area.NULL;
        return Area.of(xIntersect, yIntersect);
    }

    @Override
    public Stream<Coordinate> adjacentTo(final Coordinate coordinate) {
        return coordinate.adjacent()
                .filter(this::contains);
    }

    @Override
    public void forEach(final Consumer<? super Coordinate> consumer) {
        for (int dy = h.min(); dy <= h.max(); dy++)
            for (int dx = w.min(); dx <= w.max(); dx++)
                consumer.accept(new Coordinate(dx, dy));
    }

    @Override
    public void forEachInterval(final Velocity velocity, final Consumer<Coordinate> consumer) {
        for (Coordinate coordinate = Coordinate.ORIGIN; contains(coordinate); coordinate = coordinate.adjustBy(velocity))
            consumer.accept(coordinate);
    }

    @Override
    public void forEachMatching(final Predicate<Coordinate> test, final Consumer<Coordinate> consumer) {
        for (int dy = h.min(); dy <= h.max(); dy++)
            for (int dx = w.min(); dx <= w.max(); dx++) {
                final Coordinate coordinate = new Coordinate(dx, dy);
                if (test.test(coordinate))
                    consumer.accept(coordinate);
            }
    }

    @Override
    public void forEachIntervalMatching(final Velocity velocity, final Predicate<Coordinate> test, final Consumer<Coordinate> consumer) {
        for (Coordinate coordinate = Coordinate.ORIGIN; contains(coordinate); coordinate = coordinate.adjustBy(velocity))
            if (test.test(coordinate))
                consumer.accept(coordinate);
    }

    @Override
    public void forEachAdjacentTo(final Coordinate coordinate, final Consumer<Coordinate> consumer) {
        ifInBounds(new Coordinate(coordinate.x(), coordinate.y() - 1), consumer);
        ifInBounds(new Coordinate(coordinate.x() - 1, coordinate.y()), consumer);
        ifInBounds(new Coordinate(coordinate.x() + 1, coordinate.y()), consumer);
        ifInBounds(new Coordinate(coordinate.x(), coordinate.y() + 1), consumer);
    }

    @Override
    public void forEachNeighbourOf(final Coordinate coordinate, final Consumer<Coordinate> consumer) {
        ifInBounds(new Coordinate(coordinate.x() - 1, coordinate.y() - 1), consumer);
        ifInBounds(new Coordinate(coordinate.x(), coordinate.y() - 1), consumer);
        ifInBounds(new Coordinate(coordinate.x() + 1, coordinate.y() - 1), consumer);
        ifInBounds(new Coordinate(coordinate.x() - 1, coordinate.y()), consumer);
        ifInBounds(new Coordinate(coordinate.x() + 1, coordinate.y()), consumer);
        ifInBounds(new Coordinate(coordinate.x() - 1, coordinate.y() + 1), consumer);
        ifInBounds(new Coordinate(coordinate.x(), coordinate.y() + 1), consumer);
        ifInBounds(new Coordinate(coordinate.x() + 1, coordinate.y() + 1), consumer);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Grow the {@link Area} by a given amount in all directions.
     *
     * @param size the amount to grow by
     * @return the new {@link Area}
     */
    public Area grow(final int size) {
        return new Area(w.expand(size), h.expand(size));
    }

    // ****************************************
    // Equals & Hash Code
    // ****************************************

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Area that = (Area) o;
        return Objects.equals(w, that.w) && Objects.equals(h, that.h);
    }

    @Override
    public int hashCode() {
        return Objects.hash(w, h);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Dispatch the given `Coordinate` to the `Consumer` iif the point is
     * contained within the bounds of this area.
     */
    private void ifInBounds(final Coordinate coordinate, final Consumer<Coordinate> consumer) {
        if (contains(coordinate)) consumer.accept(coordinate);
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Area} from the given {@link Bounds}.
     *
     * @param x the x {@link Bounds}
     * @param y the y {@link Bounds}
     * @return the {@link Area}
     */
    public static Area of(final Bounds x, final Bounds y) {
        return new Area(x, y);
    }

    /**
     * Create a new {@link Area} from the given {@link Bounds}, assuming that
     * the minimum value in each is {@literal 0}.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the {@link Area}
     */
    public static Area ofOrigin(final int x, final int y) {
        return new Area(Bounds.of(0, x), Bounds.of(0, y));
    }

    /**
     * Parse an {@link Area} from an input of form {@literal x=X1..X2, y=Y1..Y2}.
     *
     * @param input the area specification
     * @return the {@link Area}
     */
    public static Area parse(final String input) {
        final String[] parts = input.split("\\s*,\\s*");
        if (!parts[0].startsWith("x="))
            throw new IllegalArgumentException("Invalid area: " + input);
        if (!parts[1].startsWith("y="))
            throw new IllegalArgumentException("Invalid area: " + input);

        return Area.of(Bounds.parse(parts[0].substring(2)), Bounds.parse(parts[1].substring(2)));
    }

}
