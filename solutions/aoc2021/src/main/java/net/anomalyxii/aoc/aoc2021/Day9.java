package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.LongAccumulator;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 9.
 */
@Solution(year = 2021, day = 9, title = "Smoke Basin")
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
        final Grid heightMap = context.readGrid(chr -> chr - '0');

        final LongAccumulator sum = new LongAccumulator(Long::sum, 0);
        heightMap.forEachMatching(
                point -> isLowPoint(heightMap, point),
                point -> sum.accumulate(heightMap.get(point) + 1)
        );

        return sum.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid heightMap = context.readGrid(chr -> chr - '0');

        final List<Long> basinSizes = new ArrayList<>();
        heightMap.forEachMatching(
                point -> isLowPoint(heightMap, point),
                point -> basinSizes.add(countBasin(heightMap, point))
        );

        basinSizes.sort((a, b) -> Long.compare(b, a));
        return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Determine whether this `Coordinate` is a "low-point".
     */
    private boolean isLowPoint(final Grid heightMap, final Coordinate coordinate) {
        return heightMap.adjacentTo(coordinate)
                .allMatch(neighbour -> heightMap.get(neighbour) > heightMap.get(coordinate));
    }

    /*
     * Count the number of `Coordinate`s in a basin.
     */
    private long countBasin(final Grid heightMap, final Coordinate coordinate) {
        return countBasin(heightMap, coordinate, new HashSet<>());
    }

    /*
     * Count the number of `Coordinate`s in a basin.
     */
    private long countBasin(final Grid heightMap, final Coordinate coordinate, final Set<Coordinate> visited) {
        if (!heightMap.contains(coordinate)) return 0;
        if (heightMap.get(coordinate) == 9) return 0;

        visited.add(coordinate);
        return heightMap.adjacentTo(coordinate)
                .filter(neighbour -> !visited.contains(neighbour))
                .mapToLong(neighbour -> countBasin(heightMap, neighbour, visited))
                .sum() + 1;
    }

}
