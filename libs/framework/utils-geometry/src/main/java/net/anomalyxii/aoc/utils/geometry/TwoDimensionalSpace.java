package net.anomalyxii.aoc.utils.geometry;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a slice of two-dimensional space.
 */
public interface TwoDimensionalSpace extends Iterable<Coordinate> {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Check if this {@link TwoDimensionalSpace} represents a null area.
     *
     * @return {@literal true} if the space has no area; {@literal false} otherwise
     */
    boolean isNull();

    /**
     * Check if this {@link TwoDimensionalSpace} represents an infinite area.
     *
     * @return {@literal true} if the space has no area; {@literal false} otherwise
     */
    boolean isInfinity();

    /**
     * Get the {@link Bounds} of the width of this
     * {@link TwoDimensionalSpace}.
     *
     * @return the {@link Bounds} of the width (x-axis)
     */
    Bounds w();

    /**
     * Get the {@link Bounds} of the height of this
     * {@link TwoDimensionalSpace}.
     *
     * @return the {@link Bounds} of the height (y-axis)
     */
    Bounds h();

    /**
     * Get the minimum {@link Coordinate} in this
     * {@link TwoDimensionalSpace}.
     *
     * @return the minimum {@link Coordinate}
     * @see Bounds#min()
     */
    Coordinate min();

    /**
     * Get the maximum {@link Coordinate} in this
     * {@link TwoDimensionalSpace}.
     *
     * @return the maximum {@link Coordinate}
     * @see Bounds#max()
     */
    Coordinate max();

    /**
     * Get the width of this {@link TwoDimensionalSpace}.
     *
     * @return the width
     */
    int width();

    /**
     * Get the height of this {@link TwoDimensionalSpace}.
     *
     * @return the height
     */
    int height();

    /**
     * Calculate the size of this {@link TwoDimensionalSpace space}.
     *
     * @return the size of the space
     */
    long area();

    /**
     * Check if the given {@link Coordinate} is within this
     * {@link TwoDimensionalSpace space}.
     *
     * @param coordinate the {@link Coordinate} to check
     * @return {@literal true} if the {@link Coordinate} is within this space; {@literal false} otherwise
     */
    boolean contains(Coordinate coordinate);

    /**
     * Calculate a new {@link TwoDimensionalSpace} that is the intersection
     * between this and the given {@link TwoDimensionalSpace}.
     *
     * @param area the other {@link TwoDimensionalSpace}
     * @return the {@link TwoDimensionalSpace} of intersection
     */
    TwoDimensionalSpace union(TwoDimensionalSpace area);

    /**
     * Calculate a new {@link TwoDimensionalSpace} that is the union
     * between this and the given {@link TwoDimensionalSpace}.
     *
     * @param area the other {@link TwoDimensionalSpace}
     * @return the {@link TwoDimensionalSpace} of intersection
     */
    TwoDimensionalSpace intersect(TwoDimensionalSpace area);

    /**
     * Get a {@link Stream} of {@link Coordinate Points} within this space
     * that are adjacent to the given {@link Coordinate}.
     *
     * @param coordinate the original {@link Coordinate}
     * @return a {@link Stream} of {@link Coordinate Points}
     * @see Coordinate#adjacent()
     */
    Stream<Coordinate> adjacentTo(Coordinate coordinate);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space}.
     *
     * @param consumer the action to run for each {@link Coordinate}
     */
    void forEach(Consumer<? super Coordinate> consumer);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space}.
     *
     * @param velocity the velocity with which to increment to the next point
     * @param consumer the action to run for each {@link Coordinate}
     */
    void forEachInterval(Velocity velocity, Consumer<Coordinate> consumer);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space}.
     *
     * @param test     the {@link Predicate test} to match against
     * @param consumer the action to run for each {@link Coordinate}
     */
    void forEachMatching(Predicate<Coordinate> test, Consumer<Coordinate> consumer);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space}.
     *
     * @param velocity the velocity with which to increment to the next point
     * @param test     the {@link Predicate test} to match against
     * @param consumer the action to run for each {@link Coordinate}
     */
    void forEachIntervalMatching(Velocity velocity, Predicate<Coordinate> test, Consumer<Coordinate> consumer);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space} that is adjacent to (i.e. shares one
     * border with) the given {@link Coordinate}.
     * <p>
     * For any point <i>already within the area</i> the action will be
     * run at least twice and at most four times.
     *
     * @param coordinate the origin {@link Coordinate}
     * @param consumer   the {@link Consumer action} to run
     */
    void forEachAdjacentTo(Coordinate coordinate, Consumer<Coordinate> consumer);

    /**
     * Run an action for each {@link Coordinate} in the
     * {@link TwoDimensionalSpace space} that is neighbouring (i.e. is at
     * most 1 unit away in either directions to) the given {@link Coordinate}.
     * <p>
     * For any point <i>already within the area</i> the action will be
     * run at least five and at most eight times.
     *
     * @param coordinate the origin {@link Coordinate}
     * @param consumer   the {@link Consumer action} to run
     */
    void forEachNeighbourOf(Coordinate coordinate, Consumer<Coordinate> consumer);

    // ****************************************
    // Default Methods
    // ****************************************

    /**
     * Stream the {@link Coordinate Coordinates} in the {@link Grid}.
     *
     * @return a {@link Stream} of {@link Coordinate Coordinates}
     */
    default Stream<Coordinate> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    default Iterator<Coordinate> iterator() {
        return new CoordinateIterator(this);
    }

}
