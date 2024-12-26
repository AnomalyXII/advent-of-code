package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 14.
 */
@Solution(year = 2024, day = 14, title = "Restroom Redoubt")
public class Day14 {

    // ****************************************
    // Private Members
    // ****************************************

    private final int width;
    private final int height;

    // ****************************************
    // Constructors
    // ****************************************

    public Day14() {
        this(101, 103);
    }

    Day14(final int width, final int height) {
        this.width = width;
        this.height = height;
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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final List<Robot> robots = context.stream()
                .map(Robot::parse)
                .toList();

        final List<Coordinate> finalPositions = robots.stream()
                .map(robot -> robot.calculatePositionAfter(100, width, height))
                .toList();

        return calculateSafetyFactor(finalPositions);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final List<Robot> robots = context.stream()
                .map(Robot::parse)
                .toList();

        return findEasterEgg(robots);
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
        final List<Robot> robots = context.stream()
                .map(Robot::parse)
                .toList();

        final List<Coordinate> nextPositions = robots.stream()
                .map(robot -> robot.calculatePositionAfter(100, width, height))
                .toList();

        final int safetyFactor = calculateSafetyFactor(nextPositions);

        return new IntTuple(
                safetyFactor,
                findEasterEgg(robots)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the safety factor for the current position of robots.
     */
    private int calculateSafetyFactor(final Collection<Coordinate> nextPositions) {
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;

        final int midx = width / 2;
        final int midy = height / 2;

        for (final Coordinate c : nextPositions) {
            if (c.x() < midx && c.y() < midy) ++q1;
            else if (c.x() > midx && c.y() < midy) ++q2;
            else if (c.x() < midx && c.y() > midy) ++q3;
            else if (c.x() > midx && c.y() > midy) ++q4;
        }

        return q1 * q2 * q3 * q4;
    }

    /*
     * Attempt to find the number of iterations before the robots form into
     * the Easter Egg configuration.
     */
    private int findEasterEgg(final List<Robot> robots) {
        for (int i = 101; i < 10_000; i++) {
            final int pos = i;
            final Set<Coordinate> newPositions = robots.stream()
                    .map(robot -> robot.calculatePositionAfter(pos, width, height))
                    .collect(Collectors.toSet());

            if (isReallyLazyAttemptToFindEasterEgg(newPositions))
                return i;
        }

        throw new IllegalStateException("Did not find a Christmas Tree after 10,000 iterations");
    }

    /*
     * Make a wild guess at whether the robots formed a tree by looking at
     * whether any of them assemble into a 5x5 corner...
     */
    private boolean isReallyLazyAttemptToFindEasterEgg(final Set<Coordinate> coordinates) {
        // Assume a high safety factor means that the robots are not clustered enough to make a sensible image
        if (calculateSafetyFactor(coordinates) > 150_000_000) return false;

        // For potential candidates, check if we can find a border.
        // If so, it's _probably_ the correct thing?
        return coordinates.stream()
                .anyMatch(c -> {
                    for (int x = 0; x < 5; x++)
                        if (!coordinates.contains(c.adjustBy(x, 0)))
                            return false;

                    for (int y = 0; y < 5; y++)
                        if (!coordinates.contains(c.adjustBy(0, y)))
                            return false;

                    return true;
                });
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A `Robot`, with a starting position and a velocity.
     */
    private record Robot(Coordinate start, Velocity velocity) {

        // Helper Methods

        /*
         * Calculate the position of this robot after a certain amount of time
         * has elapsed, given an area of a certain size.
         */
        Coordinate calculatePositionAfter(
                final int time,
                final int width,
                final int height
        ) {
            final Coordinate end = start.adjustBy(
                    velocity.h() * time,
                    velocity.v() * time
            );

            final int endX = end.x() % width;
            final int endY = end.y() % height;
            return new Coordinate(
                    endX >= 0 ? endX : endX + width,
                    endY >= 0 ? endY : endY + height
            );
        }

        // Static Helper Methods

        /*
         * Parse the `Robot` from a line of input.
         */
        static Robot parse(final String line) {
            final String[] parts = line.split("\\s+");

            return new Robot(
                    Coordinate.parse(parts[0].split("=")[1]),
                    Velocity.parse(parts[1].split("=")[1])
            );
        }

    }

}

