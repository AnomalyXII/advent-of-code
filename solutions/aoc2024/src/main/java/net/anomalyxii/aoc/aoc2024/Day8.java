package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 8.
 */
@Solution(year = 2024, day = 8, title = "Resonant Collinearity")
public class Day8 {

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
        final Grid grid = context.readGrid();
        final Map<Character, List<Coordinate>> antennaeByFrequency = findAntennae(grid);

        return (int) antennaeByFrequency.values().stream()
                .flatMap(antennae -> streamAntinodes(grid, antennae))
                .distinct()
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = context.readGrid();
        final Map<Character, List<Coordinate>> antennaeByFrequency = findAntennae(grid);

        return (int) antennaeByFrequency.values().stream()
                .flatMap(antennae -> streamAllAntinodes(grid, antennae))
                .distinct()
                .count();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return a {@link LongTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final Grid grid = context.readGrid();
        final Map<Character, List<Coordinate>> antennaeByFrequency = findAntennae(grid);

        final Set<Coordinate> part1 = new HashSet<>();
        final Set<Coordinate> part2 = new HashSet<>();
        antennaeByFrequency.values()
                .forEach(antennae -> {
                    part1.addAll(streamAntinodes(grid, antennae).collect(Collectors.toSet()));
                    part2.addAll(streamAllAntinodes(grid, antennae).collect(Collectors.toSet()));
                });
        return new IntTuple(part1.size(), part2.size());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find all the antennae, grouping them by frequency.
     */
    private static Map<Character, List<Coordinate>> findAntennae(final Grid grid) {
        return grid.stream()
                .filter(c -> grid.get(c) != '.')
                .collect(Collectors.groupingBy(c -> (char) grid.get(c)));
    }

    /*
     * Stream the immediate antinodes of a single frequency.
     */
    private static Stream<Coordinate> streamAntinodes(final Grid grid, final List<Coordinate> antennae) {
        final Stream.Builder<Coordinate> antinodes = Stream.builder();
        for (int a1 = 0; a1 < antennae.size() - 1; a1++) {
            final Coordinate antenna1 = antennae.get(a1);
            for (int a2 = a1 + 1; a2 < antennae.size(); a2++) {
                final Coordinate antenna2 = antennae.get(a2);

                final Velocity difference1 = new Velocity(-(antenna2.x() - antenna1.x()), -(antenna2.y() - antenna1.y()));
                final Coordinate anti1 = antenna1.adjustBy(difference1);
                if (grid.contains(anti1)) antinodes.add(anti1);

                final Velocity difference2 = new Velocity(antenna2.x() - antenna1.x(), antenna2.y() - antenna1.y());
                final Coordinate anti2 = antenna2.adjustBy(difference2);
                if (grid.contains(anti2)) antinodes.add(anti2);
            }
        }
        return antinodes.build();
    }

    /*
     * Stream ALL the antinodes of a single frequency.
     */
    private static Stream<Coordinate> streamAllAntinodes(final Grid grid, final List<Coordinate> antennae) {
        final Stream.Builder<Coordinate> antinodes = Stream.builder();
        for (int a1 = 0; a1 < antennae.size() - 1; a1++) {
            final Coordinate antenna1 = antennae.get(a1);
            for (int a2 = a1 + 1; a2 < antennae.size(); a2++) {
                final Coordinate antenna2 = antennae.get(a2);

                final Velocity difference1 = new Velocity(-(antenna2.x() - antenna1.x()), -(antenna2.y() - antenna1.y()));
                Coordinate anti1 = antenna1;
                do {
                    antinodes.add(anti1);
                    anti1 = anti1.adjustBy(difference1);
                } while (grid.contains(anti1));

                final Velocity difference2 = new Velocity(antenna2.x() - antenna1.x(), antenna2.y() - antenna1.y());
                Coordinate anti2 = antenna2;
                do {
                    antinodes.add(anti2);
                    anti2 = anti2.adjustBy(difference2);
                } while (grid.contains(anti2));
            }
        }
        return antinodes.build();
    }

}

