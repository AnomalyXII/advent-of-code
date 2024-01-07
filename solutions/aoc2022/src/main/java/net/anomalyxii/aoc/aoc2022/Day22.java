package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;
import static net.anomalyxii.aoc.utils.geometry.Direction.*;

/**
 * Advent of Code 2022, Day 22.
 */
@Solution(year = 2022, day = 22, title = "Monkey Map")
public class Day22 {

    private static final Pattern NUM = Pattern.compile("[0-9]+");
    private static final Pattern ROT = Pattern.compile("[LR]");

    private static final int SPACE = ' ';
    private static final int FLOOR = '.';
    private static final int WALL = '#';

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
        final List<String> lines = new ArrayList<>(context.read());
        final String directions = lines.removeLast();
        lines.removeLast(); // Remove the trailing blank line, just for good measure

        final MonkeyMap map = MonkeyMap.parseFlat(lines);

        return map.move(directions);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> lines = new ArrayList<>(context.read());
        final String directions = lines.removeLast();
        lines.removeLast(); // Remove the trailing blank line, just for good measure

        final MonkeyMap map = MonkeyMap.parseCube(lines);

        return map.move(directions);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The current state of navigation through a `MonkeyMap`.
     *
     * The state consists of a `Coordinate` ("position") and a `Direction`
     * ("direction").
     */
    private record State(Coordinate position, Direction direction) {

        // Helper Methods

        /**
         * Calculate the "score" of this {@link State}.
         * <p>
         * The "score" is the sum of the "score" of the final position, and the
         * "score" of the direction currently being faced, where:
         * <ul>
         * <li>the score of a position is calculated as: {@code (1000 * y) + (4 * x)}; <i>and</i></li>
         * <li>the score of a direction is: 0 = right, 1 = down, 2 = left, 3 = up.</li>
         * </ul>
         *
         * @return the "score"
         */
        public long calculateScore() {
            return score(position) + score(direction);
        }


        // Private Static Helper Methods

        /*
         * Compute the "score" of a pair of `Coordinate`s.
         */
        private static long score(final Coordinate coordinate) {
            return 1000L * (coordinate.y() + 1) + 4L * (coordinate.x() + 1);
        }

        /*
         * Compute the "score" of a `Direction`.
         */
        private static long score(final Direction direction) {
            return MonkeyMap.mapDirection(direction, v -> 0L, v -> 1L, v -> 2L, v -> 3L);
        }

    }

    /*
     * A function to wrap a `State` if it moves off the edge of the
     * `MonkeyMap`.
     */
    @FunctionalInterface
    private interface WrapFunction {

        /**
         * Perform a movement of one unit in starting at a specific
         * {@link Coordinate position} and moving in a specific
         * {@link Direction direction}.
         *
         * @param grid the {@link Grid} representing the full {@link MonkeyMap}
         * @param pos  the starting {@link Coordinate}
         * @param dir  the starting {@link Direction}
         * @return the next {@link State}
         */
        State wrap(Grid grid, Coordinate pos, Direction dir);

    }

    /*
     * A map, as drawn by one or more monkeys.
     *
     * Key:
     *   - ' ' => empty space;
     *   - '.' => floor;
     *   - '#' => impassable wall.
     */
    private static final class MonkeyMap {

        // Private Members

        private final Grid grid;
        private final WrapFunction wrapper;

        private State state;

        // Constructors

        MonkeyMap(final Grid grid, final State state, final WrapFunction wrapper) {
            this.grid = grid;
            this.state = state;
            this.wrapper = wrapper;
        }

        // "Public" Helper Methods

        /**
         * Move, according to the provided directions, through the area described
         * by this {@link MonkeyMap} and compute the
         * "{@link State#calculateScore() score}" of this path.
         *
         * @param directions the directions
         * @return the "{@link State#calculateScore() score}" of the path
         */
        public long move(final String directions) {
            final Matcher moveMatcher = NUM.matcher(directions);
            final Matcher rotateMatcher = ROT.matcher(directions);
            while (moveMatcher.find()) {
                final String move = moveMatcher.group();
                state = move(state, Integer.parseInt(move));
                if (rotateMatcher.find()) {
                    final String rotate = rotateMatcher.group();
                    state = rotate(state, rotate.charAt(0));
                }
            }

            return state.calculateScore();
        }

        // Private Helper Methods

        /*
         * Move a specified units from the current position, in the direction
         * currently being faced.
         */
        private State move(final State state, final int distance) {
            int remaining = distance;
            State current = state;
            do {
                assert grid.get(current.position) == FLOOR;

                final Coordinate nextPos = current.position.adjustBy(current.direction);
                final State next = grid.contains(nextPos) && grid.get(nextPos) != SPACE
                        ? new State(nextPos, current.direction)
                        : wrapper.wrap(grid, current.position, current.direction);

                final int target = grid.get(next.position);
                assert target == FLOOR || target == WALL; // Shouldn't have moved into empty space...

                if (target == WALL) break;
                current = next;
            } while (--remaining > 0);

            return current;
        }

        /*
         * Rotate on the spot 90 degrees either clockwise ('R') or
         * anti-clockwise ('L').
         */
        private State rotate(final State state, final char direction) {
            final Direction rotated = switch (direction) {
                case 'L' -> state.direction.rotateAnticlockwise();
                case 'R' -> state.direction.rotateClockwise();
                default -> throw new IllegalArgumentException("Invalid rotation: " + direction);
            };

            return new State(state.position, rotated);
        }

        // Static Helper Methods

        /**
         * Create a new {@link MonkeyMap} from the given {@link List} of
         * input lines, corresponding to tiles in a {@link Grid}.
         *
         * @param lines the {@link List} of input lines
         * @return the new {@link MonkeyMap}
         */
        static MonkeyMap parseFlat(final List<String> lines) {
            final int width = lines.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(0);

            final int height = lines.size();
            final Grid grid = Grid.size(width, height, c -> charFor(lines, c));
            final Coordinate position = determineStartingPosition(grid);
            return new MonkeyMap(grid, new State(position, RIGHT), new FlatWrapFunction());
        }

        /**
         * Create a new {@link MonkeyMap} from the given {@link List} of
         * input lines, corresponding to tiles in a {@link Grid}.
         *
         * @param lines the {@link List} of input lines
         * @return the new {@link MonkeyMap}
         */
        static MonkeyMap parseCube(final List<String> lines) {
            final int width = lines.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(0);

            final int height = lines.size();
            final Grid grid = Grid.size(width, height, c -> charFor(lines, c));
            final Coordinate position = determineStartingPosition(grid);
            final WrapFunction wrapper = height == 200 // Ew!!!
                    ? new LiveCubeWrapFunction()
                    : new ExampleCubeWrapFunction();
            return new MonkeyMap(grid, new State(position, RIGHT), wrapper);
        }

        // Private Static Helper Methods

        /*
         * Determine the starting position, i.e. the top-left most `Coordinate`
         * that maps to a "floor" tile on the map.
         */
        private static Coordinate determineStartingPosition(final Grid grid) {
            for (int x = 0; x < grid.width(); x++) {
                final Coordinate coordinate = new Coordinate(x, 0);
                if (grid.get(coordinate) == FLOOR)
                    return coordinate;
            }

            throw new IllegalStateException("Starting position not found...");
        }

        /*
         * Determine the appropriate representation for a tile at the given
         * `Coordinate`s.
         */
        private static int charFor(final List<String> lines, final Coordinate c) {
            final String line = lines.get(c.y());
            return c.x() < line.length() ? line.charAt(c.x()) : ' ';
        }

        /*
         * Perform an operation on the given `Direction`.
         */
        private static <T> T mapDirection(
                final Direction direction,
                final Function<Direction, T> onRight,
                final Function<Direction, T> onDown,
                final Function<Direction, T> onLeft,
                final Function<Direction, T> onUp
        ) {
            if (direction == RIGHT) return onRight.apply(direction);
            if (direction == DOWN) return onDown.apply(direction);
            if (direction == LEFT) return onLeft.apply(direction);
            if (direction == UP) return onUp.apply(direction);

            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

    }

    /*
     * A `WrapFunction`, where edges wrap round (presumably via the use of
     * advanced Elf technology creating a wormhole from one side to the
     * other) to the opposite edge.
     */
    private static final class FlatWrapFunction implements WrapFunction {

        // WrapFunction Methods

        @Override
        public State wrap(final Grid grid, final Coordinate pos, final Direction dir) {
            final Direction reverseGear = dir.reverse();
            Coordinate back = pos;
            Coordinate nextBack = pos.adjustBy(reverseGear);
            while (grid.contains(nextBack) && grid.get(nextBack) != SPACE) {
                back = nextBack;
                nextBack = back.adjustBy(reverseGear);
            }
            return new State(back, dir);
        }

    }

    /*
     * A `WrapFunction` that works on the "example" cube.
     *
     * The example cube looks like this:
     *
     *            A22B
     *            2222
     *            2222
     *            D22C
     *  B66A A33D D11C
     *  6666 3333 1111
     *  6666 3333 1111
     *  H66G G33F F11E
     *            F55E E44C
     *            5555 4444
     *            5555 4444
     *            G55H H44B
     */
    private static final class ExampleCubeWrapFunction implements WrapFunction {

        // WrapFunction Methods

        @Override
        public State wrap(final Grid grid, final Coordinate pos, final Direction dir) {
            // Face "2"
            if (pos.y() >= 0 && pos.y() <= 3) {
                if (pos.x() >= 8 && pos.x() <= 11)
                    return adaptFace2(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Faces "6", "3" and "1"
            if (pos.y() >= 4 && pos.y() <= 7) {
                if (pos.x() >= 0 && pos.x() <= 3)
                    return adaptFace6(pos, dir);

                if (pos.x() >= 4 && pos.x() <= 7)
                    return adaptFace3(pos, dir);

                if (pos.x() >= 8 && pos.x() <= 11)
                    return adaptFace1(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Faces "5" and "4"
            if (pos.y() >= 8 && pos.y() <= 11) {
                if (pos.x() >= 7 && pos.x() <= 11)
                    return adaptFace5(pos, dir);

                if (pos.x() >= 12 && pos.x() <= 15)
                    return adaptFace4(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Errrrrror :(
            throw new IllegalArgumentException("Invalid y position: " + pos);
        }

        // Private Helper Methods

        /*
         * Adapt a coordinate off the edge of Face 1:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace1(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go right? => Face 4, continue down
            if (direction == RIGHT) {
                assert x == 11;

                final Coordinate next = new Coordinate(12 + 7 - y, 8);
                return new State(next, DOWN);
            }
            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 2:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace2(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go left? => Face 3, continue down
            if (direction == LEFT) {
                assert x == 8;

                final Coordinate next = new Coordinate(4 + y, 4);
                return new State(next, DOWN);
            }

            // We go right? => Face 4, continue left
            if (direction == RIGHT) {
                assert x == 11;

                final Coordinate next = new Coordinate(15, 8 + y);
                return new State(next, LEFT);
            }

            // We go up? => Face 6, continue down
            if (direction == UP) {
                assert y == 0;

                final Coordinate next = new Coordinate(y - 8, 4);
                return new State(next, DOWN);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 3:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace3(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 2, continue right
            if (direction == UP) {
                assert y == 4;

                final Coordinate next = new Coordinate(8, x - 4);
                return new State(next, RIGHT);
            }

            // We go down? => Face 5, continue right
            if (direction == DOWN) {
                assert y == 7;

                final Coordinate next = new Coordinate(8, 8 + 7 - x);
                return new State(next, RIGHT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 4:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace4(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 1, continue left
            if (direction == UP) {
                assert y == 8;

                final Coordinate next = new Coordinate(11, 4 + 15 - x);
                return new State(next, LEFT);
            }

            // We go left? => Face 2, continue left
            if (direction == LEFT) {
                assert x == 15;

                final Coordinate next = new Coordinate(8, 11 - y);
                return new State(next, LEFT);
            }

            // We go down? => Face 6, continue right
            if (direction == DOWN) {
                assert y == 11;

                final Coordinate next = new Coordinate(0, 4 + 15 - x);
                return new State(next, RIGHT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 5:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace5(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go left? => Face 3, continue up
            if (direction == LEFT) {
                assert x == 8;

                final Coordinate next = new Coordinate(4 + 11 - y, 7);
                return new State(next, LEFT);
            }

            // We go down? => Face 6, continue up
            if (direction == DOWN) {
                assert y == 11;

                final Coordinate next = new Coordinate(11 - x, 7);
                return new State(next, UP);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 6:
         *
         * D11C  A22B  A33D  E44C  F55E  B66A
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  D22C  G33F  H44B  G55H  H66G
         */
        private State adaptFace6(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 2, continue down
            if (direction == UP) {
                assert y == 8;

                final Coordinate next = new Coordinate(8 + 3 - x, 0);
                return new State(next, DOWN);
            }

            // We go down? => Face 5, continue up
            if (direction == DOWN) {
                assert y == 7;

                final Coordinate next = new Coordinate(8 + 3 - x, 11);
                return new State(next, UP);
            }

            // We go left? => Face 4, continue up
            if (direction == LEFT) {
                assert x == 0;

                final Coordinate next = new Coordinate(8 + 7 - y, 11);
                return new State(next, UP);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }
    }

    /*
     * A `WrapFunction` that works on the "live" cube.
     *
     * The live cube looks like this:
     *
     *       A55B B44G
     *       5555 4444
     *       5555 4444
     *       D55C C44E
     *       D11C
     *       1111
     *       1111
     *       F11E
     *  D33F F22E
     *  3333 2222
     *  3333 2222
     *  A33H H22G
     *  A66H
     *  6666
     *  6666
     *  B66G
     */
    private static final class LiveCubeWrapFunction implements WrapFunction {

        // WrapFunction Methods

        @Override
        public State wrap(final Grid grid, final Coordinate pos, final Direction dir) {
            // Faces "5" and "4"
            if (pos.y() >= 0 && pos.y() <= 49) {
                if (pos.x() >= 50 && pos.x() <= 99)
                    return adaptFace5(pos, dir);
                if (pos.x() >= 100 && pos.x() <= 149)
                    return adaptFace4(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Face "1"
            if (pos.y() >= 50 && pos.y() <= 99) {
                if (pos.x() >= 50 && pos.x() <= 99)
                    return adaptFace1(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Faces "3" and "2"
            if (pos.y() >= 100 && pos.y() <= 149) {
                if (pos.x() >= 0 && pos.x() <= 49)
                    return adaptFace3(pos, dir);

                if (pos.x() >= 50 && pos.x() <= 99)
                    return adaptFace2(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Faces "6"
            if (pos.y() >= 150 && pos.y() <= 199) {
                if (pos.x() >= 0 && pos.x() <= 49)
                    return adaptFace6(pos, dir);

                throw new IllegalArgumentException("Invalid x position: " + pos);
            }

            // Errrrrror :(
            throw new IllegalArgumentException("Invalid y position: " + pos);
        }

        // Private Helper Methods

        /*
         * Adapt a coordinate off the edge of Face 1:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace1(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go left? => Face 3, continue down
            if (direction == LEFT) {
                assert x == 50;

                final Coordinate next = new Coordinate(y - 50, 100);
                return new State(next, DOWN);
            }

            // We go right? => Face 4, continue up
            if (direction == RIGHT) {
                assert x == 99;

                final Coordinate next = new Coordinate(y + 50, 49);
                return new State(next, UP);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 2:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace2(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go right? => Face 4, continue left
            if (direction == RIGHT) {
                assert x == 99;

                final Coordinate next = new Coordinate(149, 149 - y);
                return new State(next, LEFT);
            }

            // We go down? => Face 6, continue left
            if (direction == DOWN) {
                assert y == 149;

                final Coordinate next = new Coordinate(49, 100 + x);
                return new State(next, LEFT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 3:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace3(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 1, continue right
            if (direction == UP) {
                assert y == 100;

                final Coordinate next = new Coordinate(50, 50 + x);
                return new State(next, RIGHT);
            }

            // We go left? => Face 5, continue right
            if (direction == LEFT) {
                assert x == 0;

                final Coordinate next = new Coordinate(50, 149 - y);
                return new State(next, RIGHT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 4:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace4(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 6, continue up
            if (direction == UP) {
                assert y == 0;

                final Coordinate next = new Coordinate(x - 100, 199);
                return new State(next, UP);
            }

            // We go right? => Face 2, continue left
            if (direction == RIGHT) {
                assert x == 149;

                final Coordinate next = new Coordinate(99, 149 - y);
                return new State(next, LEFT);
            }

            // We go down? => Face 1, continue left
            if (direction == DOWN) {
                assert y == 49;

                final Coordinate next = new Coordinate(99, x - 50);
                return new State(next, LEFT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 5:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace5(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go up? => Face 6, continue right
            if (direction == UP) {
                assert y == 0;

                final Coordinate next = new Coordinate(0, 100 + x);
                return new State(next, RIGHT);
            }

            // We go left? => Face 3, continue right
            if (direction == LEFT) {
                assert x == 50;

                final Coordinate next = new Coordinate(0, 149 - y);
                return new State(next, RIGHT);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }

        /*
         * Adapt a coordinate off the edge of Face 6:
         *
         * D11C  F22E  D33F  B44G  A55B  A66H
         * 1111  2222  3333  4444  5555  6666
         * 1111  2222  3333  4444  5555  6666
         * F11E  H22G  A33H  C44E  D55C  B66G
         */
        private State adaptFace6(final Coordinate position, final Direction direction) {
            final int x = position.x();
            final int y = position.y();

            // We go left? => Face 5, continue down
            if (direction == LEFT) {
                assert x == 0;

                final Coordinate next = new Coordinate(y - 100, 0);
                return new State(next, DOWN);
            }

            // We go down? => Face 4, continue down
            if (direction == DOWN) {
                assert y == 199;

                final Coordinate next = new Coordinate(100 + x, 0);
                return new State(next, DOWN);
            }

            // We go right? => Face 2, continue up
            if (direction == RIGHT) {
                assert x == 49;

                final Coordinate next = new Coordinate(y - 100, 149);
                return new State(next, UP);
            }

            throw new AssertionError("Invalid move: " + position + ", " + direction);
        }
    }

}

