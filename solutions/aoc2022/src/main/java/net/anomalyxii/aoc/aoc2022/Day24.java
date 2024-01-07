package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;
import static net.anomalyxii.aoc.utils.geometry.Velocity.*;

/**
 * Advent of Code 2022, Day 24.
 */
@Solution(year = 2022, day = 24, title = "Blizzard Basin")
public class Day24 {

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
        final Map result = extractMap(context);

        return result.blizzards().navigate(result.entrance(), result.extraction(), 0);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = Grid.parse(context.stream());

        final Coordinate entrance = new Coordinate(1, 0); // TODO: maybe don't hardcode this...
        assert grid.get(entrance) == '.';

        final Coordinate extraction = new Coordinate(grid.width() - 2, grid.height() - 1); // TODO: maybe don't hardcode this...
        assert grid.get(extraction) == '.';

        final Blizzards blizzards = Blizzards.fromGrid(grid);

        final int there = blizzards.navigate(entrance, extraction, 0);
        final int back = blizzards.navigate(extraction, entrance, there);
        return blizzards.navigate(entrance, extraction, back);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Parse the input to create a `Map` containing the locations of all
     * `Blizzards`, plus the entrance and the exit.
     */
    private static Map extractMap(final SolutionContext context) {
        final Grid grid = Grid.parse(context.stream());

        final Coordinate entrance = new Coordinate(1, 0); // TODO: maybe don't hardcode this...
        assert grid.get(entrance) == '.';

        final Coordinate extraction = new Coordinate(grid.width() - 2, grid.height() - 1); // TODO: maybe don't hardcode this...
        assert grid.get(extraction) == '.';

        final Blizzards blizzards = Blizzards.fromGrid(grid);
        return new Map(entrance, extraction, blizzards);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Shows the locations of the entrance, exit and all blizzard.
     */
    private record Map(Coordinate entrance, Coordinate extraction, Blizzards blizzards) {

    }

    /*
     * A blizzard, localised to a single `Coordinate` and moving with a
     * particular `Velocity`.
     */
    private record Blizzard(Velocity direction, Coordinate position) {

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Blizzard blizzard = (Blizzard) o;
            return Objects.equals(direction, blizzard.direction) && Objects.equals(position, blizzard.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, position);
        }

    }

    /*
     * All the localised `Blizzard`s that need to be avoided.
     */
    private record Blizzards(Grid grid, Area arena, Set<Blizzard> blizzards) {

        private static final int NORTH_WIND = '^';
        private static final int EAST_WIND = '>';
        private static final int SOUTH_WIND = 'v';
        private static final int WEST_WIND = '<';

        // Helper Methods

        /**
         * Navigate from the entrance {@link Coordinate} to the extraction
         * {@link Coordinate}, avoiding every {@link Blizzard} on the way.
         *
         * @param entrance   the entrance {@link Coordinate}
         * @param extraction the extraction {@link Coordinate}
         * @param time       the current time
         * @return the number of steps needed
         */
        int navigate(final Coordinate entrance, final Coordinate extraction, final int time) {
            final Set<Coordinate> toCheck = new HashSet<>();
            toCheck.add(entrance);

            int t = time;
            while (!toCheck.isEmpty()) {

                final Set<Coordinate> impasses = blizzardsGonnaBlowBlowBlow(t);
                final Set<Coordinate> nextStates = new HashSet<>();

                final Iterator<Coordinate> iterator = toCheck.iterator();
                while (iterator.hasNext()) {
                    final Coordinate current = iterator.next();
                    iterator.remove();

                    if (current.equals(extraction)) return t - 1;

                    // Can we stay where we are?
                    if (!impasses.contains(current))
                        nextStates.add(current);

                    current.adjacent()
                            .filter(grid::contains)
                            .filter(next -> grid.get(next) != '#')
                            .filter(next -> !impasses.contains(next))
                            .forEach(nextStates::add);
                }

                ++t;
                toCheck.addAll(nextStates);
            }

            throw new IllegalStateException("Ran out of moves without reaching the extraction point!");
        }

        // Private Helper Methods

        /*
         * Determine the position of every `Blizzard` after a given number of
         * turns.
         */
        private Set<Coordinate> blizzardsGonnaBlowBlowBlow(final int turn) {
            final Set<Coordinate> current = new HashSet<>();
            for (final Blizzard blizzard : this.blizzards) {
                Coordinate next = blizzard.position.adjustBy(new Velocity(turn * blizzard.direction.h(), turn * blizzard.direction.v()));

                // TODO: this can probably be optimised?
                while (!arena.contains(next)) {
                    next = next.adjustBy(new Velocity(arena.width() * -blizzard.direction.h(), arena.height() * -blizzard.direction.v()));
                }

                assert arena.contains(next);
                current.add(next);
            }
            return current;
        }

        // Static Helper Methods

        /**
         * Create a new map of the {@link Blizzards} from the given input.
         *
         * @param grid the {@link Grid}
         * @return the {@link Blizzards}
         */
        public static Blizzards fromGrid(final Grid grid) {
            final Set<Blizzard> blizzards = grid.entries()
                    .filter(e -> e.getValue() == NORTH_WIND || e.getValue() == EAST_WIND || e.getValue() == SOUTH_WIND || e.getValue() == WEST_WIND)
                    .map(e -> switch (e.getValue()) {
                        case NORTH_WIND -> new Blizzard(UP, e.getKey());
                        case EAST_WIND -> new Blizzard(RIGHT, e.getKey());
                        case SOUTH_WIND -> new Blizzard(DOWN, e.getKey());
                        case WEST_WIND -> new Blizzard(LEFT, e.getKey());
                        default -> throw new IllegalArgumentException("Invalid wind direction: " + e.getValue());
                    })
                    .collect(Collectors.toSet());

            final Area arena = Area.of(
                    Bounds.of(grid.min().x() + 1, grid.max().x() - 1),
                    Bounds.of(grid.min().y() + 1, grid.max().y() - 1)
            );

            return new Blizzards(grid, arena, blizzards);
        }

    }

}

