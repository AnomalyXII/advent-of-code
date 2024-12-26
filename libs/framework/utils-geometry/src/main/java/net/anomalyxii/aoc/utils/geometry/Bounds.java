package net.anomalyxii.aoc.utils.geometry;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

/**
 * Two extreme positions along an axis.
 *
 * @param min the lower bound
 * @param max the upper bound
 */
public record Bounds(int min, int max) implements Comparable<Bounds> {


    /**
     * To infinity, and beyond!
     */
    public static final Bounds INFINITY = new Bounds(Integer.MIN_VALUE, Integer.MAX_VALUE);

    /**
     * Nothing really matters... to meeeee!
     */
    public static final Bounds NULL = new Bounds(Integer.MAX_VALUE, Integer.MIN_VALUE);

    /**
     * From zero to h^Hzero?
     */
    public static final Bounds ZERO = new Bounds(0, 0);

    private static final Comparator<Bounds> COMPARATOR = Comparator
            .comparingInt((Bounds coordinate) -> coordinate.min)
            .thenComparingInt((Bounds coordinate) -> coordinate.max);

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Check if these {@link Bounds} represent a null length.
     *
     * @return {@literal true} if the {@link Bounds} have no length; {@literal false} otherwise
     */
    public boolean isNull() {
        return min > max;
    }

    /**
     * Check if these {@link Bounds} represent an infinite length.
     *
     * @return {@literal true} if the {@link Bounds} have an infinte length; {@literal false} otherwise
     */
    public boolean isInfinity() {
        return min == INFINITY.min() && max == INFINITY.max();
    }

    /**
     * Calculate the length of these {@link Bounds}.
     *
     * @return the number of positions contained within these {@link Bounds}
     */
    public int length() {
        return (max - min) + 1;
    }

    /**
     * Check if the given position is within these {@link Bounds}.
     *
     * @param value the value to check
     * @return {@literal true} if the given value is within these {@link Bounds}; {@literal false} otherwise
     */
    public boolean contains(final int value) {
        return value >= min && value <= max;
    }

    /**
     * Grow the {@link Bounds} by the given amount in each direction.
     *
     * @param size the amount to grow by
     * @return the new {@link Bounds}
     */
    public Bounds expand(final int size) {
        return Bounds.of(min - size, max + size);
    }

    /**
     * Return new {@link Bounds} spanning both this and the given
     * {@link Bounds} (and all points in between).
     *
     * @param other the other {@link Bounds}
     * @return the new {@link Bounds}
     */
    public Bounds union(final Bounds other) {
        return Bounds.of(Math.min(min, other.min), Math.max(max, other.max));
    }

    /**
     * Return new {@link Bounds} spanning the common points between this and
     * the given {@link Bounds} (and all points in between).
     *
     * @param other the other {@link Bounds}
     * @return the new {@link Bounds}
     */
    public Bounds intersect(final Bounds other) {
        if (other.min > max || other.max < min) return Bounds.NULL;
        return Bounds.of(Math.max(min, other.min), Math.min(max, other.max));
    }

    /**
     * Perform an operation on every position within these {@link Bounds}.
     *
     * @param consumer the {@link IntConsumer action to perform}
     */
    public void forEach(final IntConsumer consumer) {
        for (int i = min; i <= max; i++)
            consumer.accept(i);
    }

    /**
     * Check if any position within these {@link Bounds} passes the
     * specified {@link IntPredicate test}.
     *
     * @param test the {@link IntPredicate test}
     * @return {@literal true} if at least one position passes; {@literal false} otherwise
     */
    public boolean anyMatch(final IntPredicate test) {
        for (int i = min; i <= max; i++)
            if (test.test(i))
                return true;
        return false;
    }

    /**
     * Find the minimum point that returns a
     * {@link Optional#isPresent() non-empty} result when the provided
     * {@link IntFunction function} is applied.
     *
     * @param test the {@link IntFunction to apply}
     * @param <T>  the type of result
     * @return the first non-empty result, or {@link Optional#empty() empty} if no result was found
     */
    public <T> Optional<T> minPointSatisfying(final IntFunction<Optional<T>> test) {
        for (int i = min; i <= max; i++) {
            final Optional<T> result = test.apply(i);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    // ****************************************
    // Comparable Methods
    // ****************************************

    @Override
    public int compareTo(final Bounds o) {
        return COMPARATOR.compare(this, o);
    }

    // ****************************************
    // Equals & Hash Code
    // ****************************************

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Bounds bounds = (Bounds) o;
        return min == bounds.min && max == bounds.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create new {@link Bounds} from the given extremes.
     *
     * @param first  one extreme
     * @param second the other extreme
     * @return the new {@link Bounds}
     */
    public static Bounds of(final int first, final int second) {
        return first < second
                ? new Bounds(first, second)
                : new Bounds(second, first);
    }

    /**
     * Parse the {@link Bounds} from an input of format: {@code A..B}.
     *
     * @param input the specification of these bounds
     * @return the new {@link Bounds}
     */
    public static Bounds parse(final String input) {
        return parse(input, "\\.\\.");
    }


    /**
     * Parse the {@link Bounds} from an input of format: {@code A[separator]B}.
     *
     * @param input     the specification of these bounds
     * @param separator the separator
     * @return the new {@link Bounds}
     */
    public static Bounds parse(final String input, final String separator) {
        final String[] parts = input.split(separator);
        final int first = Integer.parseInt(parts[0]);
        final int second = Integer.parseInt(parts[1]);
        return Bounds.of(first, second);
    }

}
