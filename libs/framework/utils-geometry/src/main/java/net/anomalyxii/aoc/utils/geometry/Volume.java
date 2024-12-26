package net.anomalyxii.aoc.utils.geometry;

import java.util.function.Consumer;

/**
 * An area between three {@link Point Points}.
 *
 * @param w the horizontal (width) {@link Bounds}
 * @param h the vertical (height) {@link Bounds}
 * @param d the depth (...depth?) {@link Bounds}
 */
public record Volume(Bounds w, Bounds h, Bounds d) implements ThreeDimensionalSpace {

    /**
     * An infinite(ish) {@link Volume}.
     */
    public static final Volume INFINITY = new Volume(Bounds.INFINITY, Bounds.INFINITY, Bounds.INFINITY);

    /**
     * A null {@link Volume}.
     */
    public static final Volume NULL = new Volume(Bounds.NULL, Bounds.NULL, Bounds.NULL);

    // ****************************************
    // ThreeDimensionalSpace Methods
    // ****************************************

    @Override
    public boolean isNull() {
        return w.isNull() || h.isNull() || d.isNull();
    }

    @Override
    public boolean isInfinity() {
        return w.isInfinity() || h.isInfinity() || d.isInfinity();
    }

    @Override
    public Point min() {
        return new Point(w.min(), h.min(), d.min());
    }

    @Override
    public Point max() {
        return new Point(w.max(), h.max(), d.max());
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
    public int depth() {
        return d.length();
    }

    @Override
    public long volume() {
        return (long) w.length() * (long) h.length() * (long) d.length();
    }

    @Override
    public boolean contains(final Point point) {
        return w.contains(point.x()) && h.contains(point.y()) && d.contains(point.z());
    }

    @Override
    public ThreeDimensionalSpace union(final ThreeDimensionalSpace area) {
        if (this.isInfinity() || area.isInfinity()) return INFINITY;
        if (this.isNull()) return area;
        if (area.isNull()) return this;

        final Bounds xUnion = w.union(area.w());
        final Bounds yUnion = h.union(area.h());
        final Bounds zUnion = d.union(area.d());
        return Volume.of(xUnion, yUnion, zUnion);
    }

    @Override
    public ThreeDimensionalSpace intersect(final ThreeDimensionalSpace area) {
        final Bounds xIntersect = w.intersect(area.w());
        if (xIntersect.isNull()) return Volume.NULL;
        final Bounds yIntersect = h.intersect(area.h());
        if (yIntersect.isNull()) return Volume.NULL;
        final Bounds zIntersect = d.intersect(area.d());
        if (zIntersect.isNull()) return Volume.NULL;
        return Volume.of(xIntersect, yIntersect, zIntersect);
    }

    @Override
    public void forEach(final Consumer<Point> consumer) {
        for (int dz = d.min(); dz <= d.max(); dz++)
            for (int dy = h.min(); dy <= h.max(); dy++)
                for (int dx = w.min(); dx <= w.max(); dx++)
                    consumer.accept(new Point(dx, dy, dz));
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Volume} from the given {@link Bounds}.
     *
     * @param x the x {@link Bounds}
     * @param y the y {@link Bounds}
     * @param z the z {@link Bounds}
     * @return the {@link Volume}
     */
    public static Volume of(final Bounds x, final Bounds y, final Bounds z) {
        return new Volume(x, y, z);
    }

    /**
     * Parse an {@link Volume} from an input of form {@literal x=X1..X2, y=Y1..Y2}.
     *
     * @param input the area specification
     * @return the {@link Volume}
     */
    public static Volume parse(final String input) {
        final String[] parts = input.split("\\s*,\\s*");
        if (!parts[0].startsWith("x="))
            throw new IllegalArgumentException("Invalid area: " + input);
        if (!parts[1].startsWith("y="))
            throw new IllegalArgumentException("Invalid area: " + input);
        if (!parts[2].startsWith("z="))
            throw new IllegalArgumentException("Invalid area: " + input);

        return Volume.of(
                Bounds.parse(parts[0].substring(2)),
                Bounds.parse(parts[1].substring(2)),
                Bounds.parse(parts[2].substring(2))
        );
    }
}
