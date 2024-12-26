package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 11.
 */
@Solution(year = 2023, day = 11, title = "Cosmic Expansion")
public class Day11 {

    // ****************************************
    // Private Members
    // ****************************************

    private final int scaleFactor;

    // ****************************************
    // Constructors
    // ****************************************

    public Day11() {
        this(1000000);
    }

    Day11(final int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

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
        return solve(context, 1);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return solve(context, scaleFactor - 1);
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
    public LongTuple calculateAnswers(final SolutionContext context) {
        final Set<Coordinate> galaxies = parseGalaxies(context);

        final int[] rowsWithGalaxies = galaxies.stream()
                .mapToInt(Coordinate::y)
                .sorted()
                .distinct()
                .toArray();

        final int[] columnsWithGalaxies = galaxies.stream()
                .mapToInt(Coordinate::x)
                .sorted()
                .distinct()
                .toArray();

        final List<Coordinate> resolvedGalaxies1 = new ArrayList<>();
        final List<Coordinate> resolvedGalaxies2 = new ArrayList<>();
        for (final Coordinate coordinate : galaxies) {
            int ry = 0;
            IntTuple newY = new IntTuple(coordinate.y(), coordinate.y());
            for (int y = 0; y < coordinate.y(); y++) {
                if (ry < rowsWithGalaxies.length && y < rowsWithGalaxies[ry])
                    newY = newY.add(1, scaleFactor - 1);
                else
                    ++ry;
            }

            int rx = 0;
            IntTuple newX = new IntTuple(coordinate.x(), coordinate.x());
            for (int x = 0; x < coordinate.x(); x++) {
                if (rx < columnsWithGalaxies.length && x < columnsWithGalaxies[rx])
                    newX = newX.add(1, scaleFactor - 1);
                else
                    ++rx;
            }

            resolvedGalaxies1.add(new Coordinate(newX.answer1(), newY.answer1()));
            resolvedGalaxies2.add(new Coordinate(newX.answer2(), newY.answer2()));
        }

        long part1 = 0;
        long part2 = 0;
        for (int f = 0; f < galaxies.size(); f++) {
            for (int s = f + 1; s < galaxies.size(); s++) {
                part1 += distance(resolvedGalaxies1.get(f), resolvedGalaxies1.get(s));
                part2 += distance(resolvedGalaxies2.get(f), resolvedGalaxies2.get(s));
            }
        }

        return new LongTuple(part1, part2);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Expand the given input universe and calculate the sum of the distance
     * between every galaxy.
     */
    private long solve(final SolutionContext context, final int scaleFactor) {
        final Set<Coordinate> galaxies = parseGalaxies(context);

        final int[] rowsWithGalaxies = galaxies.stream()
                .mapToInt(Coordinate::y)
                .sorted()
                .distinct()
                .toArray();

        final int[] columnsWithGalaxies = galaxies.stream()
                .mapToInt(Coordinate::x)
                .sorted()
                .distinct()
                .toArray();

        final List<Coordinate> resolvedGalaxies = galaxies.stream()
                .map(galaxy -> {
                    int ry = 0;
                    int newY = galaxy.y();
                    for (int y = 0; y < galaxy.y(); y++) {
                        if (ry < rowsWithGalaxies.length && y < rowsWithGalaxies[ry])
                            newY += scaleFactor;
                        else
                            ++ry;
                    }

                    int rx = 0;
                    int newX = galaxy.x();
                    for (int x = 0; x < galaxy.x(); x++) {
                        if (rx < columnsWithGalaxies.length && x < columnsWithGalaxies[rx])
                            newX += scaleFactor;
                        else
                            ++rx;
                    }

                    return new Coordinate(newX, newY);
                })
                .toList();

        long sum = 0;
        for (int f = 0; f < galaxies.size(); f++)
            for (int s = f + 1; s < galaxies.size(); s++)
                sum += distance(resolvedGalaxies.get(f), resolvedGalaxies.get(s));

        return sum;
    }

    /*
     * Parse the `Coordinate`s of each galaxy.
     */
    private static Set<Coordinate> parseGalaxies(final SolutionContext context) {
        final Grid galaxyMap = context.readGrid();
        final Set<Coordinate> galaxies = new HashSet<>();
        galaxyMap.forEach(coordinate -> {
            final int reading = galaxyMap.get(coordinate);
            if (reading == '#') galaxies.add(coordinate);
        });
        return galaxies;
    }

    /*
     * Calculate the shortest distance between two galaxies.
     */
    private static int distance(final Coordinate start, final Coordinate end) {
        return Math.abs(start.x() - end.x()) + Math.abs(start.y() - end.y());
    }

}

