package net.anomalyxii.aoc.utils.geometry;

import java.util.function.Consumer;

/**
 * Represents a slice of three-dimensional space.
 */
public interface ThreeDimensionalSpace {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Check if this {@link ThreeDimensionalSpace} represents a null volume.
     *
     * @return {@literal true} if the space has no area; {@literal false} otherwise
     */
    boolean isNull();

    /**
     * Check if this {@link ThreeDimensionalSpace} represents an infinite volume.
     *
     * @return {@literal true} if the space has no volume; {@literal false} otherwise
     */
    boolean isInfinity();

    /**
     * Get the {@link Bounds} of the width of this
     * {@link ThreeDimensionalSpace}.
     *
     * @return the {@link Bounds} of the width (x-axis)
     */
    Bounds w();

    /**
     * Get the {@link Bounds} of the height of this
     * {@link ThreeDimensionalSpace}.
     *
     * @return the {@link Bounds} of the height (y-axis)
     */
    Bounds h();

    /**
     * Get the {@link Bounds} of the depth of this
     * {@link ThreeDimensionalSpace}.
     *
     * @return the {@link Bounds} of the depth (z-axis)
     */
    Bounds d();

    /**
     * Get the minimum {@link Point} in this {@link ThreeDimensionalSpace}.
     *
     * @return the minimum {@link Point}
     * @see Bounds#min()
     */
    Point min();

    /**
     * Get the maximum {@link Point} in this {@link ThreeDimensionalSpace}.
     *
     * @return the maximum {@link Point}
     * @see Bounds#max()
     */
    Point max();

    /**
     * Get the width of this {@link ThreeDimensionalSpace}.
     *
     * @return the width
     */
    int width();

    /**
     * Get the height of this {@link ThreeDimensionalSpace}.
     *
     * @return the height
     */
    int height();

    /**
     * Get the depth of this {@link ThreeDimensionalSpace}.
     *
     * @return the depth
     */
    int depth();

    /**
     * Calculate the size of this {@link ThreeDimensionalSpace space}.
     *
     * @return the size of the space
     */
    long volume();

    /**
     * Check if the given {@link Point} is within this
     * {@link ThreeDimensionalSpace space}.
     *
     * @param point the {@link Point} to check
     * @return {@literal true} if the {@link Point} is within this space; {@literal false} otherwise
     */
    boolean contains(Point point);

    /**
     * Calculate a new {@link ThreeDimensionalSpace} that is the intersection
     * between this and the given {@link ThreeDimensionalSpace}.
     *
     * @param area the other {@link ThreeDimensionalSpace}
     * @return the {@link ThreeDimensionalSpace} of intersection
     */
    ThreeDimensionalSpace union(ThreeDimensionalSpace area);

    /**
     * Calculate a new {@link ThreeDimensionalSpace} that is the union
     * between this and the given {@link ThreeDimensionalSpace}.
     *
     * @param area the other {@link ThreeDimensionalSpace}
     * @return the {@link ThreeDimensionalSpace} of intersection
     */
    ThreeDimensionalSpace intersect(ThreeDimensionalSpace area);

    /**
     * Run an action for each {@link Point} in the
     * {@link ThreeDimensionalSpace space}.
     *
     * @param consumer the action to run for each {@link Point}
     */
    void forEach(Consumer<Point> consumer);

}
