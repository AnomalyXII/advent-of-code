package net.anomalyxii.aoc.utils.geometry;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Represents a direction on a 2D surface.
 */
public enum Direction {

    /**
     * Primary direction UP or NORTH.
     */
    UP(Velocity.UP),

    /**
     * Primary direction DOWN or SOUTH.
     */
    DOWN(Velocity.DOWN),

    /**
     * Primary direction LEFT or WEST.
     */
    LEFT(Velocity.LEFT),

    /**
     * Primary direction RIGHT or EAST.
     */
    RIGHT(Velocity.RIGHT),

    // End of constants
    ;

    // ****************************************
    // Convenient Constants
    // ****************************************

    /**
     * Primary direction NORTH or UP.
     *
     * @see #UP
     */
    public static final Direction NORTH = UP;

    /**
     * Primary direction SOUTH or DOWN.
     *
     * @see #DOWN
     */
    public static final Direction SOUTH = DOWN;

    /**
     * Primary direction EAST or RIGHT.
     *
     * @see #RIGHT
     */
    public static final Direction EAST = RIGHT;

    /**
     * Primary direction WEST or LEFT.
     *
     * @see #LEFT
     */
    public static final Direction WEST = LEFT;

    /*
     * All directions
     */
    private static final Set<Direction> ALL_DIRECTIONS = Set.of(Direction.values());

    // ****************************************
    // Private Members
    // ****************************************

    private final transient Velocity velocity;

    // ****************************************
    // Constructors
    // ****************************************

    Direction(final Velocity velocity) {
        this.velocity = velocity;
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Convert this {@link Direction} to a {@link Velocity}.
     *
     * @return the {@link Velocity}
     */
    public Velocity asVelocity() {
        return velocity;
    }

    /**
     * Reverse this {@link Direction}.
     *
     * @return the opposite {@link Direction}
     */
    public Direction reverse() {
        return reverse(this);
    }

    /**
     * Rotate this {@link Direction} 90 degrees clockwise.
     *
     * @return the new {@link Direction}
     */
    public Direction rotateClockwise() {
        return rotateClockwise(this);
    }

    /**
     * Rotate this {@link Direction} 90 degrees anti-clockwise.
     *
     * @return the new {@link Direction}
     */
    public Direction rotateAnticlockwise() {
        return rotateAnticlockwise(this);
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Parse a {@link Direction} from a symbol.
     *
     * @param symbol the symbol
     * @return the {@link Direction}
     * @throws IllegalStateException if the symbol given is not a valid direction indicator
     */
    public static Direction fromChar(final int symbol) {
        return switch (symbol) {
            case '^' -> UP;
            case '<' -> LEFT;
            case '>' -> RIGHT;
            case 'v' -> DOWN;
            case '.' -> null;
            default -> throw new IllegalStateException("Invalid instruction: " + symbol);
        };
    }

    /**
     * Create a {@link Stream} of every {@link Direction}.
     *
     * @return the {@link Stream}
     */
    public static Stream<Direction> stream() {
        return ALL_DIRECTIONS.stream();
    }

    /**
     * Run an action on every {@link Direction}.
     *
     * @param action the {@link Consumer action} to run
     */
    public static void forEach(final Consumer<Direction> action) {
        ALL_DIRECTIONS.forEach(action);
    }

    /**
     * Reverse a given {@link Direction}.
     *
     * @param direction the {@link Direction} to reverse
     * @return the opposite {@link Direction}
     */
    public static Direction reverse(final Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    /**
     * Rotate a {@link Direction} 90 degrees clockwise.
     *
     * @param direction the {@link Direction} to rotate
     * @return the new {@link Direction}
     */
    public static Direction rotateClockwise(final Direction direction) {
        return switch (direction) {
            case UP -> RIGHT;
            case DOWN -> LEFT;
            case LEFT -> UP;
            case RIGHT -> DOWN;
        };
    }

    /**
     * Rotate a {@link Direction} 90 degrees anit-clockwise.
     *
     * @param direction the {@link Direction} to rotate
     * @return the new {@link Direction}
     */
    public static Direction rotateAnticlockwise(final Direction direction) {
        return switch (direction) {
            case UP -> LEFT;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
            case RIGHT -> UP;
        };
    }

}
