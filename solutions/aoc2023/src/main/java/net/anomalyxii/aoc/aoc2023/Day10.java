package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.algorithms.AreaCalculator;
import net.anomalyxii.aoc.utils.algorithms.ShoelaceCalculator;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;

import java.util.ArrayList;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 10.
 */
@Solution(year = 2023, day = 10, title = "Pipe Maze")
public class Day10 {

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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final Pipe pipe = Pipe.read(context.read());
        return pipe.length / 2;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Pipe pipe = Pipe.read(context.read());
        return pipe.countInsideTiles();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final Pipe pipe = Pipe.read(context.read());
        final int count = pipe.countInsideTiles();
        return new IntTuple(pipe.length / 2, count);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A closed-loop pipe, made up of segments.
     */
    private record Pipe(int length, List<Coordinate> turns) {

        // Helper Methods

        /*
         * Calculate the number of tiles inside the pipe area.
         */
        int countInsideTiles() {
            // Pick's theorem is used to calculate the area of an irregular shape,
            // based on the perimeter and number of internal points.
            // It is defined as: a = i + 0.5 * (p - 1), where:
            //   a => area
            //   i => internal points
            //   p => perimeter points

            // And we can calculate the area...
            final AreaCalculator calculator = new ShoelaceCalculator();
            final long area = calculator.area(turns);

            // ... and the perimeter...
            final int perimeter = length - 1;

            // ... and thus we can rewrite Pick's theorem as:
            //   a - 0.5 * (p - 1) = i
            // This should allow us to calculate the number of internal points!
            final long internalPoints = (long) (area - (0.5 * perimeter));

            // I'm not _exactly_ sure why we need to add 1 to the result...
            //    ... but hey, it works! :X
            return (int) internalPoints + 1;
        }

        // Static Helper Methods

        /*
         * Extract a `Pipe` from a given `Grid`, starting at the position marked
         * with an `S`.
         */
        public static Pipe read(final List<String> lines) {
            int length = 0;
            final List<Coordinate> turns = new ArrayList<>();

            final Coordinate start = findStartingCoordinate(lines);
            final Direction forwards = startForward(start, lines);
            final Direction backwards = startBackward(start, lines);

            Direction direction = backwards;
            Coordinate current = start;
            char type = resolveSegmentType(forwards, backwards);
            while (current != null) {
                ++length;
                if (type == 'F' || type == 'J' || type == 'L' || type == '7')
                    turns.add(current);

                final Coordinate next = current.adjustBy(direction);
                type = charInGrid(lines, next);
                if (type == 'S') {
                    current = null;
                    direction = null;
                } else {
                    direction = continueThroughPipeSegment(direction, type);
                    current = next;
                }
            }

            return new Pipe(length, turns);
        }

        /*
         * Find the starting `Coordinate`.
         */
        private static Coordinate findStartingCoordinate(final List<String> grid) {
            for (int y = 0; y < grid.size(); y++) {
                final String line = grid.get(y);
                final int idx = line.indexOf('S');
                if (idx >= 0) return new Coordinate(idx, y);
            }
            throw new IllegalStateException("No starting point found!");
        }

        /*
         * Resolve a `Segment` type based on the directions that attach to it.
         */
        private static char resolveSegmentType(final Direction forwards, final Direction backwards) {
            final char type;
            if (forwards == Direction.UP && backwards == Direction.DOWN) type = '|';
            else if (forwards == Direction.RIGHT && backwards == Direction.LEFT) type = '-';
            else if (forwards == Direction.UP && backwards == Direction.LEFT) type = 'J';
            else if (forwards == Direction.UP && backwards == Direction.RIGHT) type = 'L';
            else if (forwards == Direction.LEFT && backwards == Direction.DOWN) type = '7';
            else if (forwards == Direction.RIGHT && backwards == Direction.DOWN) type = 'F';
            else throw new IllegalStateException("Invalid starting combination: [" + forwards + "] [" + backwards + "]");
            return type;
        }

        /*
         * Continue through a pipe `Segment`, resolving the outgoing direction as
         * a `Direction`.
         */
        private static Direction continueThroughPipeSegment(final Direction incoming, final char pipe) {
            if (pipe == '|')
                if (incoming == Direction.UP) return Direction.UP;
                else if (incoming == Direction.DOWN) return Direction.DOWN;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            if (pipe == '-')
                if (incoming == Direction.LEFT) return Direction.LEFT;
                else if (incoming == Direction.RIGHT) return Direction.RIGHT;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            if (pipe == 'F')
                if (incoming == Direction.UP) return Direction.RIGHT;
                else if (incoming == Direction.LEFT) return Direction.DOWN;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            if (pipe == '7')
                if (incoming == Direction.RIGHT) return Direction.DOWN;
                else if (incoming == Direction.UP) return Direction.LEFT;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            if (pipe == 'J')
                if (incoming == Direction.RIGHT) return Direction.UP;
                else if (incoming == Direction.DOWN) return Direction.LEFT;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            if (pipe == 'L')
                if (incoming == Direction.LEFT) return Direction.UP;
                else if (incoming == Direction.DOWN) return Direction.RIGHT;
                else throw invalidPipeAndDirectionCombination(pipe, incoming);

            throw new IllegalStateException("Cannot continue along a pipe of type [" + pipe + "] in coming from direction " + incoming);
        }

        /*
         * Pick an appropriate direction to start travelling "forward" along the
         * pipe.
         */
        private static Direction startForward(final Coordinate coord, final List<String> grid) {
            final Coordinate up = coord.adjustBy(Direction.UP);
            if (isInGrid(grid, up)) {
                final char pipe = charInGrid(grid, up);
                if (pipe == '|' || pipe == 'F' || pipe == '7') return Direction.UP;
            }

            final Coordinate right = coord.adjustBy(Direction.RIGHT);
            if (isInGrid(grid, right)) {
                final char pipe = charInGrid(grid, right);
                if (pipe == '-' || pipe == '7' || pipe == 'J') return Direction.RIGHT;
            }

            final Coordinate left = coord.adjustBy(Direction.LEFT);
            if (isInGrid(grid, left)) {
                final char pipe = charInGrid(grid, left);
                if (pipe == '-' || pipe == 'L' || pipe == 'F') return Direction.LEFT;
            }

            throw new IllegalStateException("Could not work out how to start going clockwise from " + coord);
        }

        /*
         * Pick an appropriate direction to start travelling "backward" along the
         * pipe.
         */
        private static Direction startBackward(final Coordinate coord, final List<String> grid) {
            final Coordinate down = coord.adjustBy(Direction.DOWN);
            if (isInGrid(grid, down)) {
                final char pipe = charInGrid(grid, down);
                if (pipe == '|' || pipe == 'L' || pipe == 'J') return Direction.DOWN;
            }

            final Coordinate left = coord.adjustBy(Direction.LEFT);
            if (isInGrid(grid, left)) {
                final char pipe = charInGrid(grid, left);
                if (pipe == '-' || pipe == 'F' || pipe == 'L') return Direction.LEFT;
            }

            final Coordinate right = coord.adjustBy(Direction.RIGHT);
            if (isInGrid(grid, right)) {
                final char pipe = charInGrid(grid, right);
                if (pipe == '-' || pipe == '7' || pipe == 'F') return Direction.RIGHT;
            }

            throw new IllegalStateException("Could not work out how to start going anticlockwise from " + coord);
        }

        /*
         * Check if a given `Coordinate` is inside the grid.
         */
        private static boolean isInGrid(final List<String> grid, final Coordinate coord) {
            return coord.y() >= 0 && coord.x() >= 0
                    && coord.y() < grid.size() && coord.x() < grid.get(coord.y()).length();
        }

        /*
         * Get the character at a given `Coordinate` in the grid.
         */
        private static char charInGrid(final List<String> grid, final Coordinate coord) {
            return grid.get(coord.y()).charAt(coord.x());
        }

        /*
         * Throw an error if the intended direction is incompatible with the
         * current type of pipe.
         */
        private static IllegalArgumentException invalidPipeAndDirectionCombination(final int pipe, final Direction velocity) {
            throw new IllegalArgumentException("Cannot continue with a " + velocity + " along a pipe of type " + (char) pipe);
        }

    }

}
