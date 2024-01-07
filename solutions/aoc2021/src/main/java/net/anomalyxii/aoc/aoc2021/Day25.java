package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Grid.MutableGrid;

import java.util.HashMap;
import java.util.Map;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 25.
 */
@Solution(year = 2021, day = 25, title = "Sea Cucumber")
public class Day25 {

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
        final MutableGrid grid = Grid.parseMutable(context.stream());

        long turn = 0;
        boolean atLeastOneCucumberMoved;
        do {
            final Map<Coordinate, Coordinate> movableEast = new HashMap<>();
            grid.forEachValue((coord, maybeCucumber) -> {
                if (maybeCucumber != '>') return;

                Coordinate destination = coord.adjustBy(1, 0);
                if (!grid.contains(destination))
                    destination = new Coordinate(0, destination.y());

                if (grid.get(destination) == '.')
                    movableEast.put(coord, destination);
            });

            movableEast.forEach((start, destination) -> {
                grid.set(start, '.');
                grid.set(destination, start.y() == destination.y() ? '>' : 'v');
            });

            final Map<Coordinate, Coordinate> movableSouth = new HashMap<>();
            grid.forEachValue((coord, maybeCucumber) -> {
                if (maybeCucumber != 'v') return;

                Coordinate destination = coord.adjustBy(0, 1);
                if (!grid.contains(destination))
                    destination = new Coordinate(destination.x(), 0);

                if (grid.get(destination) == '.')
                    movableSouth.put(coord, destination);
            });

            movableSouth.forEach((start, destination) -> {
                grid.set(start, '.');
                grid.set(destination, start.y() == destination.y() ? '>' : 'v');
            });

            atLeastOneCucumberMoved = !movableEast.isEmpty() || !movableSouth.isEmpty();
            ++turn;
        } while (atLeastOneCucumberMoved);

        return turn;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return -
     */
    @Part(part = II)
    public NoChallenge calculateAnswerForPart2(final SolutionContext context) {
        return NoChallenge.NO_CHALLENGE;
    }

}