package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 14.
 */
@Solution(year = 2022, day = 14, title = "Regolith Reservoir")
public class Day14 {

    /*
     * The entry point of the sand: (500,0).
     */
    private static final Coordinate START = new Coordinate(500, 0);

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
        final Set<Coordinate> walls = context.stream()
                .flatMap(Day14::buildTheWall)
                .collect(Collectors.toSet());

        final int lowestWall = walls.stream()
                .mapToInt(Coordinate::y)
                .max()
                .orElseThrow();

        int count = 0;
        toInfinityAndBeyond:
        while (true) {
            Coordinate grain = START;

            Coordinate next;
            while ((next = fall(grain, n -> isOpenSpace(n, walls))) != null) {
                if (grain.y() >= lowestWall)
                    break toInfinityAndBeyond;

                grain = next;
            }

            // Can't move any more...
            ++count;
            walls.add(grain);
        }

        return count;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Set<Coordinate> walls = context.stream()
                .flatMap(Day14::buildTheWall)
                .collect(Collectors.toSet());

        final int groundLevel = walls.stream()
                .mapToInt(Coordinate::y)
                .max()
                .orElseThrow() + 2;

        int count = 0;
        while (!walls.contains(START)) {
            Coordinate grain = START;
            Coordinate next;
            while ((next = fall(grain, n -> isOpenSpace(n, walls, groundLevel))) != null)
                grain = next;

            // Can't move any more...
            ++count;
            walls.add(grain);
        }

        return count;
    }


    // ****************************************
    // Test Methods
    // ****************************************

    /*
     * Build a wall from a chain of `Coordinate`s.
     */
    private static Stream<Coordinate> buildTheWall(final String line) {
        final List<Coordinate> coords = stream(line.split("\\s*->\\s*"))
                .map(Coordinate::parse)
                .toList();
        final Stream.Builder<Coordinate> builder = Stream.builder();
        for (int i = 1; i < coords.size(); i++) {
            Coordinate prev = coords.get(i - 1);
            final Coordinate next = coords.get(i);

            final int dx = prev.y() == next.y() ? prev.x() < next.x() ? 1 : -1 : 0;
            final int dy = prev.x() == next.x() ? prev.y() < next.y() ? 1 : -1 : 0;
            if (dx == dy)
                throw new IllegalStateException("Cannot model a diagonal wall!");

            while (!prev.equals(next)) {
                builder.add(prev);
                prev = prev.adjustBy(dx, dy);
            }
        }
        builder.add(coords.get(coords.size() - 1));
        return builder.build();
    }

    private Coordinate fall(final Coordinate grain, final Predicate<Coordinate> isOpenSpace) {
        Coordinate next;

        // Directly down...
        next = grain.adjustBy(0, 1);
        if (isOpenSpace.test(next))
            return next;

        // Down and left...
        next = grain.adjustBy(-1, 1);
        if (isOpenSpace.test(next))
            return next;

        // Down and right...
        next = grain.adjustBy(1, 1);
        if (isOpenSpace.test(next))
            return next;

        return null;
    }

    /*
     * Check if the target `Coordinate` is not blocked by a wall.
     */
    private static boolean isOpenSpace(final Coordinate target, final Set<Coordinate> walls) {
        return !walls.contains(target);
    }

    /*
     * Check if the target `Coordinate` is not blocked by a wall or the floor.
     */
    private static boolean isOpenSpace(final Coordinate down, final Set<Coordinate> walls, final int groundLevel) {
        return !walls.contains(down) && down.y() != groundLevel;
    }

}

