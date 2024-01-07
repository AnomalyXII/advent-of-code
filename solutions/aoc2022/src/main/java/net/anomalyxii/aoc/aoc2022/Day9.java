package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 9.
 */
@Solution(year = 2022, day = 9, title = "Rope Bridge")
public class Day9 {

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
        return simulateRopePhysics(context, 2);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return simulateRopePhysics(context, 10);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Simulate the movement of a `Rope` of a particular length, counting the
     * number of unique positions that the final knot in the `Rope` visits.
     */
    private static int simulateRopePhysics(final SolutionContext context, final int length) {
        final Rope rope = Rope.ofLength(length);
        context.consume(instruction -> {
            final char direction = instruction.charAt(0);
            final int distance = Integer.parseInt(instruction.substring(2));
            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 'U' -> rope.moveUp();
                    case 'D' -> rope.moveDown();
                    case 'L' -> rope.moveLeft();
                    case 'R' -> rope.moveRight();
                    default -> throw new IllegalArgumentException("Invalid direction: " + direction);
                }
                rope.verify();
            }
        });
        return rope.longTail().visited.size();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /**
     * Represents a Rope that starts with a knot and a given
     * {@link Coordinate 2-dimensional position} followed by 0-or-more knots.
     */
    static final class Rope {

        // Private Members

        private Coordinate position;
        private final Rope tail;

        private final Set<Coordinate> visited;

        // Constructors

        Rope(final Coordinate position, final Rope tail) {
            this.position = position;
            this.tail = tail;
            this.visited = new HashSet<>(Collections.singleton(position));
        }

        // Getters

        /**
         * Get the {@link Coordinate position} of the first knot in this
         * {@link Rope}.
         *
         * @return the {@link Coordinate} of the first knot
         */
        public Coordinate getPosition() {
            return position;
        }

        /**
         * Get a subsection of {@link Rope} starting from the second knot.
         *
         * @return a section of {@link Rope}, or {@literal null} if there are no more knots in this {@link Rope}
         */
        public Rope getTail() {
            return tail;
        }

        // Helper Methods

        /**
         * Move the head of this {@link Rope} one unit vertically.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveUp() {
            position = position.adjustBy(0, 1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveUp();
            else if (headIsLeftOfTail(position, tail)) tail.moveUpLeft();
            else tail.moveUpRight();
        }

        /**
         * Move the head of this {@link Rope} minus one unit vertically.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveDown() {
            position = position.adjustBy(0, -1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveDown();
            else if (headIsLeftOfTail(position, tail)) tail.moveDownLeft();
            else tail.moveDownRight();
        }

        /**
         * Move the head of this {@link Rope} minus one unit horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveLeft() {
            position = position.adjustBy(-1, 0);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameRow(position, tail)) tail.moveLeft();
            else if (headIsAboveTail(position, tail)) tail.moveUpLeft();
            else tail.moveDownLeft();
        }

        /**
         * Move the head of this {@link Rope} one unit horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveRight() {
            position = position.adjustBy(1, 0);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameRow(position, tail)) tail.moveRight();
            else if (headIsAboveTail(position, tail)) tail.moveUpRight();
            else tail.moveDownRight();
        }

        /**
         * Move the head of this {@link Rope} one unit vertically and minus one
         * unit horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveUpLeft() {
            position = position.adjustBy(-1, 1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveUp();
            else if (headAndTailAreInSameRow(position, tail)) tail.moveLeft();
            else if (headIsLeftOfTail(position, tail)) tail.moveUpLeft();
            else
                throw new IllegalStateException("The knot has violated the laws of physics! Universal destruction imminent.");
        }


        /**
         * Move the head of this {@link Rope} one unit vertically and one unit
         * horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveUpRight() {
            position = position.adjustBy(1, 1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveUp();
            else if (headAndTailAreInSameRow(position, tail)) tail.moveRight();
            else if (!headIsLeftOfTail(position, tail)) tail.moveUpRight();
            else
                throw new IllegalStateException("The knot has violated the laws of physics! Universal destruction imminent.");
        }

        /**
         * Move the head of this {@link Rope} minus one unit vertically and one
         * unit horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveDownLeft() {
            position = position.adjustBy(-1, -1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveDown();
            else if (headAndTailAreInSameRow(position, tail)) tail.moveLeft();
            else if (headIsLeftOfTail(position, tail)) tail.moveDownLeft();
            else
                throw new IllegalStateException("The knot has violated the laws of physics! Universal destruction imminent.");
        }

        /**
         * Move the head of this {@link Rope} minus one unit vertically and minus
         * one unit horizontally.
         * <p>
         * If there are any more knots in this {@link Rope}, they will be
         * adjusted according to the well-defined rope physics that everyone
         * is surely already familiar with.
         */
        public void moveDownRight() {
            position = position.adjustBy(1, -1);

            if (!headAndTailHaveMovedTooFarApart(position, tail)) return;

            if (headAndTailAreInSameColumn(position, tail)) tail.moveDown();
            else if (headAndTailAreInSameRow(position, tail)) tail.moveRight();
            else if (!headIsLeftOfTail(position, tail)) tail.moveDownRight();
            else
                throw new IllegalStateException("The knot has violated the laws of physics! Universal destruction imminent.");
        }

        /**
         * Verify that this {@link Rope} has not violated the laws of rope
         * physics, i.e. each knot in the {@link Rope} is adjacent to the knot
         * immediately preceding it.
         */
        public void verify() {
            if (headAndTailHaveMovedTooFarApart(position, tail))
                throw new IllegalStateException("The knot has violated the laws of physics! Universal destruction imminent.");

            if (tail != null)
                tail.verify();

            visited.add(position);
        }

        /**
         * Retrieve the "long tail" (i.e. the last knot) in this {@link Rope}.
         *
         * @return the final segment of {@link Rope} containing a knot
         */
        Rope longTail() {
            return tail == null ? this : tail.longTail();
        }

        // To String

        @Override
        public String toString() {
            return "Rope[" + toKnotsString() + "]";
        }


        // Private Helper Methods

        /*
         * Represent this `Rope` based on the position of all knots.
         */
        private String toKnotsString() {
            final String headString = position.toString();
            final String tailString = tail != null
                    ? "," + tail.toKnotsString()
                    : "";
            return headString + tailString;
        }

        // Public Static Helper Methods

        /**
         * Create a new {@link Rope} containing a given number of knots.
         * <p>
         * Since every section of {@link Rope} is expected to start with a knot,
         * attempting to create {@literal 0} knots will result in <i>no rope</i>
         * being created.
         *
         * @param numKnots the number of knots in the {@link Rope}
         * @return the new {@link Rope}
         */
        public static Rope ofLength(final int numKnots) {
            Rope rope = null;
            int remainingKnots = numKnots;
            while (remainingKnots-- > 0) {
                rope = new Rope(Coordinate.ORIGIN, rope);
            }
            return rope;
        }

        // Private Static Helper Methods

        /*
         * Check if a section of `Rope` containing _at least_ two knots has
         * stretched in such a way that the first and second knot in the rope
         * are now more than 1 unit apart in both the horizontal and vertical
         * directions.
         */
        private static boolean headAndTailHaveMovedTooFarApart(final Coordinate head, final Rope tail) {
            return tail != null && head.neighboursAndSelf().noneMatch(c -> c.equals(tail.position));
        }

        /*
         * Check if a section of `Rope` containing _at least_ two knots has the
         * first knot above the second knot.
         */
        private static boolean headIsAboveTail(final Coordinate position, final Rope tail) {
            return position.y() > tail.position.y();
        }

        /*
         * Check if a section of `Rope` containing _at least_ two knots has the
         * first knot to the left of the second knot.
         */
        private static boolean headIsLeftOfTail(final Coordinate position, final Rope tail) {
            return position.x() < tail.position.x();
        }

        /*
         * Check if a section of `Rope` containing _at least_ two knots has the
         * first and the second knots on the same horizontal line.
         */
        private static boolean headAndTailAreInSameRow(final Coordinate head, final Rope tail) {
            return head.y() == tail.position.y();
        }

        /*
         * Check if a section of `Rope` containing _at least_ two knots has the
         * first and the second knots on the same vertical line.
         */
        private static boolean headAndTailAreInSameColumn(final Coordinate head, final Rope tail) {
            return head.x() == tail.position.x();
        }

    }

}

