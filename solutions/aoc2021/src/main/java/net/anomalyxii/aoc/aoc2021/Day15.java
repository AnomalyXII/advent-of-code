package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.utils.algorithms.Dijkstra;
import net.anomalyxii.aoc.utils.algorithms.ShortestPath;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 15.
 */
@Solution(year = 2021, day = 15, title = "Chiton")
public class Day15 {

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
        final Grid chiterns = Grid.parse(context.stream(), chr -> chr - '0');
        return findShortestPath(chiterns);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid chitons = Grid.parse(context.stream(), chr -> chr - '0');
        final Grid extraChitons = Grid.size(
                chitons.width() * 5,
                chitons.height() * 5,
                point -> {
                    final int offsetX = point.x() / chitons.width();
                    final int offsetY = point.y() / chitons.height();
                    final int offset = offsetX + offsetY;

                    final int ox = point.x() % chitons.width();
                    final int oy = point.y() % chitons.height();
                    final int value = chitons.get(new Coordinate(ox, oy));

                    return IntStream.range(0, offset)
                            .reduce(value, (result, iter) -> (result + 1) > 9 ? 1 : result + 1);
                }
        );

        return findShortestPath(extraChitons);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Find the shortest path between the top-left and bottom-right corners.
     */
    private long findShortestPath(final Grid chiterns) {
        final ShortestPath solver = new Dijkstra();
        return solver.solve(chiterns);
    }

}
