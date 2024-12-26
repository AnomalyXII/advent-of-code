package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 21.
 */
@Solution(year = 2023, day = 21, title = "Step Counter")
public class Day21 {

    /*
     * Default step target for an Elf.
     */
    private static final int DEFAULT_STEP_TARGET = 64;
    private static final int DEFAULT_ACTUAL_STEP_TARGET = 26501365;

    // ****************************************
    // Private Members
    // ****************************************

    private final int stepTarget;
    private final int actualStepTarget;

    // ****************************************
    // Constructors
    // ****************************************

    public Day21() {
        this(DEFAULT_STEP_TARGET, DEFAULT_ACTUAL_STEP_TARGET);
    }

    Day21(final int stepTarget, final int actualStepTarget) {
        this.stepTarget = stepTarget;
        this.actualStepTarget = actualStepTarget;
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
        final Grid map = context.readGrid();

        final Coordinate start = map.stream()
                .filter(c -> map.get(c) == 'S')
                .findFirst()
                .orElseThrow();

        final int[] results = walk(map, start, stepTarget);
        return results[stepTarget - 1];
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Grid map = Grid.parseInfinite(context.stream());
        final Coordinate start = map.stream()
                .filter(c -> map.get(c) == 'S')
                .findFirst()
                .orElseThrow();

        final int offset = start.x();
        final int width = map.width();

        final int[] results = walk(map, start, offset + width * 2);
        final int target = (actualStepTarget - offset) / width;
        return mathsH4x(target, results[offset - 1], results[offset + width - 1], results[offset + width + width - 1]);
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
        final Grid map = Grid.parseInfinite(context.stream());
        final Coordinate start = map.stream()
                .filter(c -> map.get(c) == 'S')
                .findFirst()
                .orElseThrow();

        final int offset = start.x();
        final int width = map.width();

        final int[] results = walk(map, start, offset + width * 2);
        final int target = (actualStepTarget - offset) / width;

        final long answer1 = results[stepTarget - 1];
        final long answer2 = mathsH4x(target, results[offset - 1], results[offset + width - 1], results[offset + width + width - 1]);

        return new LongTuple(answer1, answer2);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Walk the garden map and see how many plots can be visited.
     */
    private int[] walk(final Grid map, final Coordinate start, final int steps) {
        final int[] results = new int[steps];

        Set<Coordinate> positions = Set.of(start);
        final Set<Coordinate> visited = new HashSet<>();
        for (int i = 0; i < steps; i++) {
            positions = positions.stream()
                    .flatMap(Coordinate::adjacent)
                    .filter(visited::add)
                    .filter(neighbour -> map.get(neighbour) != '#')
                    .collect(Collectors.toSet());
            results[i] = positions.size() + (i > 1 ? results[i - 2] : 0);
        }

        return results;
    }

    /*
     * Do some crazy maths that I found on Reddit to efficiently solve Pt II.
     *
     * See: https://www.reddit.com/r/adventofcode/comments/18nevo3/comment/keao4q8/.
     */
    private static long mathsH4x(final long target, final int a, final int b, final int c) {
        return a + target * (b - a + (target - 1) * (c - b - b + a) / 2);
    }

}

