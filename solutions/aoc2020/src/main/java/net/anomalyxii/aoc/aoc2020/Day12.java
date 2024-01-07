package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 12.
 */
@Solution(year = 2020, day = 12, title = "Rain Risk")
public class Day12 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        final Ship ship = new Ship();

        context.consume(inst -> {
            final char command = inst.charAt(0);
            final long delta = Long.parseLong(inst.substring(1));

            ship.process(command, delta);
        });

        return ship.info.calculateManhattanDistance();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Waypoint waypoint = new Waypoint();

        context.consume(inst -> {
            final char command = inst.charAt(0);
            final long delta = Long.parseLong(inst.substring(1));

            waypoint.process(command, delta);
        });

        return waypoint.ship.calculateManhattanDistance();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a ship that moves relative to the positional information.
     */
    private static final class Ship {

        // Private Members

        private final PositionalInfo info = new PositionalInfo(0, 0, 90);

        // Helper Methods

        /*
         * Process the command given to the ship's navigation.
         */
        void process(final char command, final long delta) {
            switch (command) {
                case 'L', 'R' -> info.rotate(command, (int) delta);
                case 'N', 'E', 'S', 'W' -> info.move(command, delta);
                case 'F' -> info.move(normaliseForwardDirection(info.angle), delta);
                default -> throw new IllegalArgumentException("Invalid command instruction: '" + command + "'");
            }
        }

        // Private Helper Methods

        /*
         * Normalise a direction based on an angle.
         */
        private static char normaliseForwardDirection(final int angle) {
            if (angle == 0) return 'N';
            if (angle == 90) return 'E';
            if (angle == 180) return 'S';
            if (angle == 270) return 'W';

            throw new IllegalArgumentException("Unable to normalise angle: " + angle);
        }
    }

    /*
     * Represents an anchor point relative to a ship.
     */
    private static final class Waypoint {

        // Private Members

        private final PositionalInfo ship = new PositionalInfo(0, 0, 0);
        private final PositionalInfo info = new PositionalInfo(1, 10, 0);

        // Helper Methods

        /*
         * Process the command given to the ship's navigation.
         */
        void process(final char command, final long delta) {
            switch (command) {
                case 'L', 'R' -> info.pivot(command, (int) delta);
                case 'N', 'E', 'S', 'W' -> info.move(command, delta);
                case 'F' -> {
                    final long distanceNorth = info.latitude;
                    ship.move('N', distanceNorth * delta);
                    final long distanceEast = info.longitude;
                    ship.move('E', distanceEast * delta);
                }
                default -> throw new IllegalArgumentException("Invalid command instruction: '" + command + "'");
            }
        }
    }

    /*
     * Holds the position information of the ship or waypoint.
     */
    private static final class PositionalInfo {

        // Private Members

        private long latitude;
        private long longitude;
        private int angle;

        // Constructors

        PositionalInfo(final long latitude, final long longitude, final int angle) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.angle = angle;
        }

        // Helper Methods

        /*
         * Move the position, relative to the current position.
         */
        void move(final char direction, final long distance) {
            switch (direction) {
                case 'N' -> moveNorth(distance);
                case 'E' -> moveEast(distance);
                case 'S' -> moveSouth(distance);
                case 'W' -> moveWest(distance);
                default -> throw new IllegalArgumentException("Invalid direction to move: '" + direction + "'");
            }
        }

        /*
         * Rotate the angle, relative to the current position.
         */
        void rotate(final char direction, final int delta) {
            switch (direction) {
                case 'L' -> rotateLeft(delta);
                case 'R' -> rotateRight(delta);
                default -> throw new IllegalArgumentException("Invalid rotation direction: '" + direction + "'");
            }
        }

        /*
         * Pivot the position, relative to (0, 0).
         */
        void pivot(final char direction, final int delta) {
            switch (direction) {
                case 'L' -> pivotLeft(delta);
                case 'R' -> pivotRight(delta);
                default -> throw new IllegalArgumentException("Invalid pivot direction: '" + direction + "'");
            }
        }

        /*
         * Move the positional information due north a given distance.
         */
        void moveNorth(final long distance) {
            latitude += distance;
        }

        /*
         * Move the positional information due south a given distance.
         */
        void moveSouth(final long distance) {
            latitude -= distance;
        }

        /*
         * Move the positional information due east a given distance.
         */
        void moveEast(final long distance) {
            longitude += distance;
        }

        /*
         * Move the positional information due west a given distance.
         */
        void moveWest(final long distance) {
            longitude -= distance;
        }

        /*
         * Change the angle of the position.
         */
        void rotateLeft(final int distance) {
            angle = ((angle - distance) + 360) % 360;
        }

        /*
         * Change the angle of the position.
         */
        void rotateRight(final int distance) {
            angle = ((angle + distance) + 360) % 360;
        }

        /*
         * Pivot the position by a given amount.
         */
        void pivotLeft(final int distance) {
            if (distance % 90 != 0) {
                throw new IllegalArgumentException("Cannot pivot by " + distance + " degrees");
            }

            int remaining = distance;
            while (remaining > 0) {
                final long x = latitude;
                latitude = longitude;
                longitude = -x;
                remaining -= 90;
            }
        }

        /*
         * Pivot the position by a given amount.
         */
        void pivotRight(final int distance) {
            if (distance % 90 != 0) {
                throw new IllegalArgumentException("Cannot pivot by " + distance + " degrees");
            }

            int remaining = distance;
            while (remaining > 0) {
                final long x = latitude;
                final long y = longitude;

                latitude = -y;
                longitude = x;

                remaining -= 90;
            }
        }

        /*
         * Calculate the manhattan distance of |lat| + |long|.
         */
        public long calculateManhattanDistance() {
            return Math.abs(latitude) + Math.abs(longitude);
        }
    }

}
