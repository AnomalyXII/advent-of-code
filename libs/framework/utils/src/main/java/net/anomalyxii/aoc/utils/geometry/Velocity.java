package net.anomalyxii.aoc.utils.geometry;

import java.util.stream.Stream;

/**
 * Represents a velocity (speed + direction) in 2D space.
 *
 * @param h the velocity in the horizontal direction
 * @param v the velocity in the vertical direction
 */
public record Velocity(int h, int v) {

    // ****************************************
    // Convenient Constants
    // ****************************************

    /**
     * Represents moving a single unit "up" or "north".
     */
    public static final Velocity UP = new Velocity(0, -1);

    /**
     * Represents moving a single unit "up" or "north".
     */
    public static final Velocity NORTH = UP;

    /**
     * Represents moving a single unit "down" or "south".
     */
    public static final Velocity DOWN = new Velocity(0, 1);

    /**
     * Represents moving a single unit "down" or "south".
     */
    public static final Velocity SOUTH = DOWN;

    /**
     * Represents moving a single unit "left" or "west".
     */
    public static final Velocity LEFT = new Velocity(-1, 0);

    /**
     * Represents moving a single unit "left" or "west".
     */
    public static final Velocity WEST = LEFT;

    /**
     * Represents moving a single unit "right" or "east".
     */
    public static final Velocity RIGHT = new Velocity(1, 0);

    /**
     * Represents moving a single unit "right" or "east".
     */
    public static final Velocity EAST = RIGHT;

    /**
     * Represents moving a single unit "up-and-left" or "north-west".
     */
    public static final Velocity UP_LEFT = new Velocity(-1, -1);

    /**
     * Represents moving a single unit "up-and-left" or "north-west".
     */
    public static final Velocity NORTH_WEST = UP_LEFT;

    /**
     * Represents moving a single unit "up-and-right" or "north-east".
     */
    public static final Velocity UP_RIGHT = new Velocity(-1, 1);

    /**
     * Represents moving a single unit "up-and-right" or "north-east".
     */
    public static final Velocity NORTH_EAST = UP_RIGHT;

    /**
     * Represents moving a single unit "down-and-left" or "south-west".
     */
    public static final Velocity DOWN_LEFT = new Velocity(1, -1);

    /**
     * Represents moving a single unit "down-and-left" or "south-west".
     */
    public static final Velocity SOUTH_WEST = DOWN_LEFT;

    /**
     * Represents moving a single unit "down-and-right" or "south-east".
     */
    public static final Velocity DOWN_RIGHT = new Velocity(1, 1);

    /**
     * Represents moving a single unit "down-and-right" or "south-east".
     */
    public static final Velocity SOUTH_EAST = DOWN_RIGHT;

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Calculate the magnitude of this {@link Velocity}.
     *
     * @return the magnitude (rounded down if fractional)
     */
    public int magnitude() {
        if (h == 0) return Math.abs(v);
        if (v == 0) return Math.abs(h);
        return (int) Math.sqrt(h * h + v * v);
    }

    /**
     * Adjust this {@link Velocity} according to the
     * {@link Physics "laws" of  physics}.
     *
     * @param physics the {@link Physics} to use
     * @return the adjusted {@link Velocity}
     */
    public Velocity adjustForPhysics(final Physics physics) {
        return new Velocity(physics.adjustForResistance(h), physics.adjustForGravity(v));
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Velocity} with a given magnitude in the given
     * {@link Direction}.
     *
     * @param direction the {@link Direction}
     * @param magnitude the magnitude
     * @return the {@link Velocity}
     */
    public static Velocity inDirection(final Direction direction, final int magnitude) {
        final Velocity velocity = direction.asVelocity();
        return new Velocity(velocity.h() * magnitude, velocity.v() * magnitude);
    }

    /**
     * Return a {@link Stream} of {@link Velocity Velocities} representing
     * movement of one unit in each of the cardinal (N, E, S, W) and ordinal
     * (NE, SE, SW, NW) directions.
     *
     * @return the {@link Stream} of {@link Velocity Velocities}
     */
    public static Stream<Velocity> directions() {
        return Stream.of(
                NORTH,
                NORTH_EAST,
                EAST,
                SOUTH_EAST,
                SOUTH,
                SOUTH_WEST,
                WEST,
                NORTH_WEST
        );
    }

}
