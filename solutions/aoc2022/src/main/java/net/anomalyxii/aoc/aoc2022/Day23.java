package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Area;
import net.anomalyxii.aoc.utils.geometry.Bounds;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 23.
 */
@Solution(year = 2022, day = 23, title = "Unstable Diffusion")
public class Day23 {

    private static final Velocity[] DIRECTIONS = {Velocity.NORTH, Velocity.SOUTH, Velocity.WEST, Velocity.EAST};

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
        final Set<Coordinate> elves = loadStartingPositions(context);
        for (int round = 0; round < 10; round++)
            runSimulation(round, elves);

        return calculateEmptyGroundTiles(elves);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Set<Coordinate> elves = loadStartingPositions(context);

        int round = 0;
        boolean elfMoved;
        do {
            elfMoved = runSimulation(round++, elves);
        } while (elfMoved);

        return round;
    }


    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Load the initial elf positions.
     */
    private static Set<Coordinate> loadStartingPositions(final SolutionContext context) {
        final List<String> input = context.read();
        return IntStream.range(0, input.size())
                .mapToObj(y -> IntStream.range(0, input.get(y).length())
                        .filter(x -> input.get(y).charAt(x) == '#')
                        .mapToObj(x -> new Coordinate(x, y))
                        .collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /*
     * Simulate all the elves moving (or not) on a single round.
     */
    private static boolean runSimulation(final int round, final Set<Coordinate> elves) {
        final Set<Coordinate> collisions = new HashSet<>();
        final Map<Coordinate, Coordinate> moves = new HashMap<>();

        for (final Coordinate elf : elves) {
            final boolean shouldMove = elf.neighbours().anyMatch(elves::contains);
            if (!shouldMove) continue;

            // try to determine a move:
            for (int v = 0; v < 4; v++) {
                final Velocity d = DIRECTIONS[(round + v) % 4];
                final Velocity dx = new Velocity(d.v(), d.h());
                final Velocity dxx = new Velocity(-d.v(), -d.h());

                final Coordinate n = elf.adjustBy(d); // Main direction
                final Coordinate nx = n.adjustBy(dx); // TODO: name better...!
                final Coordinate nxx = n.adjustBy(dxx);
                if (!elves.contains(n) && !elves.contains(nx) && !elves.contains(nxx)) {
                    // Candidate move!
                    if (!collisions.contains(n) && !moves.containsKey(n)) {
                        final Coordinate e = moves.put(n, elf);
                        assert e == null;
                    } else {
                        moves.remove(n);
                        collisions.add(n);
                    }
                    break;
                }
            }
        }

        elves.removeAll(moves.values());
        elves.addAll(moves.keySet());
        return !moves.isEmpty();
    }

    /*
     * Calculate the area of ground covered by the elves and, within that
     * area, how many units of ground are not covered by an elf.
     */
    private static long calculateEmptyGroundTiles(final Set<Coordinate> elves) {
        final Area area = elves.stream()
                .reduce(
                        Area.NULL,
                        (a, elf) -> Area.of(
                                Bounds.of(min(a.w().min(), elf.x()), max(a.w().max(), elf.x())),
                                Bounds.of(min(a.h().min(), elf.y()), max(a.h().max(), elf.y()))
                        ),
                        (a, b) -> {
                            throw new IllegalArgumentException("Shouldn't merge?");
                        }
                );

        return area.area() - elves.size();
    }

}

