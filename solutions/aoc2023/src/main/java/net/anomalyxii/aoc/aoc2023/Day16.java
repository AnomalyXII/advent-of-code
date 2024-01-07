package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.*;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 16.
 */
@Solution(year = 2023, day = 16, title = "The Floor Will Be Lava")
public class Day16 {

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
        final Grid grid = Grid.parse(context.stream());
        return countEnergisedCells(grid, new Beam(Coordinate.ORIGIN, Direction.RIGHT));
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = Grid.parse(context.stream());

        return testEdges(grid);
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
        final Grid grid = Grid.parse(context.stream());

        return new IntTuple(
                countEnergisedCells(grid, new Beam(Coordinate.ORIGIN, Direction.RIGHT)),
                testEdges(grid)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Create a `Stream` of `Beam`s that start along every edge.
     */
    private static int testEdges(final Grid grid) {
        int max = 0;
        for (int y = 0; y < grid.height(); y++) {
            final int scoreRight = countEnergisedCells(grid, new Beam(new Coordinate(0, y), Direction.RIGHT));
            if (scoreRight > max) max = scoreRight;

            final int scoreLeft = countEnergisedCells(grid, new Beam(new Coordinate(grid.width() - 1, y), Direction.LEFT));
            if (scoreLeft > max) max = scoreLeft;
        }

        for (int x = 0; x < grid.width(); x++) {
            final int scoreDown = countEnergisedCells(grid, new Beam(new Coordinate(x, 0), Direction.DOWN));
            if (scoreDown > max) max = scoreDown;

            final int scoreUp = countEnergisedCells(grid, new Beam(new Coordinate(x, grid.height() - 1), Direction.UP));
            if (scoreUp > max) max = scoreUp;
        }

        return max;
    }

    /*
     * Count the number of energised cells in the grid assuming a beam of
     * light starts from a specific `Coordinate` travelling in a specific
     * `Direction`.
     */
    private static int countEnergisedCells(final Grid grid, final Beam origin) {
        final SequencedSet<Beam> tips = new LinkedHashSet<>();
        tips.addFirst(origin);

        final byte[][] energisedCells = new byte[grid.height()][];
        for (int y = 0; y < grid.height(); y++) energisedCells[y] = new byte[grid.width()];

        while (!tips.isEmpty()) {
            final Beam beam = tips.removeFirst();

            if (!grid.contains(beam.coord)) continue;

            final byte marker = (byte) (1 << beam.direction.ordinal());
            if ((energisedCells[beam.coord.y()][beam.coord.x()] & marker) != 0) continue;

            final char type = (char) grid.get(beam.coord);
            tips.addAll(beam.progress(type));
            energisedCells[beam.coord.y()][beam.coord.x()] |= marker;
        }

        int sum = 0;
        for (int y = 0; y < grid.height(); y++)
            for (int x = 0; x < grid.width(); x++)
                sum += (energisedCells[y][x] > 0 ? 1 : 0);
        return sum;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a mirror.
     */
    private enum Mirror {

        /*
         * A reflection along a line represented by a `/`.
         */
        NE_SW {
            @Override
            Direction reflect(final Direction direction) {
                return switch (direction) {
                    case UP -> Direction.RIGHT;
                    case RIGHT -> Direction.UP;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                };
            }
        },

        /*
         * A reflection along a line represented by a `\`.
         */
        NW_SE {
            @Override
            Direction reflect(final Direction direction) {
                return switch (direction) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    case DOWN -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                };
            }
        }

        // End of constants
        ;

        // Mirror Methods

        /*
         * Calculate the new `Direction` of a beam of light that hits a reflector
         * in this orientation.
         */
        abstract Direction reflect(Direction direction);

    }

    /*
     * Represents the tip of a beam of light, travelling in a specific
     * `Direction`.
     */
    private record Beam(Coordinate coord, Direction direction) {

        // Beam Methods

        /*
         * Determine the next `Beam` or `Beam`s of light.
         */
        List<Beam> progress(final char type) {
            return switch (type) {
                case '.' -> next();
                case '|' -> isPointyEndOfSplitter(type, direction) ? next() : splitVertical();
                case '-' -> isPointyEndOfSplitter(type, direction) ? next() : splitHorizontal();
                case '/' -> reflect(Mirror.NE_SW);
                case '\\' -> reflect(Mirror.NW_SE);
                default -> throw new IllegalStateException("Could not successfully process a tile of type " + type);
            };
        }

        // Private Helper Methods

        /*
         * Progress this beam of light without interference.
         */
        private List<Beam> next() {
            return Collections.singletonList(new Beam(coord.adjustBy(direction), direction));
        }

        /*
         * Split this beam of light vertically.
         */
        private List<Beam> splitVertical() {
            return List.of(
                    new Beam(coord.adjustBy(Direction.UP), Direction.UP),
                    new Beam(coord.adjustBy(Direction.DOWN), Direction.DOWN)
            );
        }

        /*
         * Split this beam of light horizontally.
         */
        private List<Beam> splitHorizontal() {
            return List.of(
                    new Beam(coord.adjustBy(Direction.LEFT), Direction.LEFT),
                    new Beam(coord.adjustBy(Direction.RIGHT), Direction.RIGHT)
            );
        }

        /*
         * Reflect this beam of light.
         */
        private List<Beam> reflect(final Mirror mirror) {
            final Direction newDirection = mirror.reflect(direction);
            return Collections.singletonList(new Beam(coord.adjustBy(newDirection), newDirection));
        }

        /*
         * Check if this beam of light is entering the pointy end of a splitter.
         */
        private static boolean isPointyEndOfSplitter(final char type, final Direction direction) {
            if (type == '-') return direction == Direction.LEFT || direction == Direction.RIGHT;
            if (type == '|') return direction == Direction.UP || direction == Direction.DOWN;
            return false;
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Beam beam = (Beam) o;
            return Objects.equals(coord, beam.coord) && direction == beam.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, direction);
        }

    }

}

