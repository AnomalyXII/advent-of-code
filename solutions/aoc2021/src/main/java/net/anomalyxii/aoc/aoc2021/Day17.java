package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Area;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Physics;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 17.
 */
@Solution(year = 2021, day = 17, title = "Trick Shot")
public class Day17 {

    private static final Physics UNDERWATER_PHYSICS = Physics.simpleUnderwaterPhysics();

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
        return calculateViableTrajectories(resolveTargetArea(context)).stream()
                .map(fr -> fr.highestPoint)
                .mapToLong(Coordinate::y)
                .max()
                .orElseThrow();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return calculateViableTrajectories(resolveTargetArea(context)).size();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Read in the target area.
     */
    private Area resolveTargetArea(final SolutionContext context) {
        final String input = context.readLine();
        if (!input.startsWith("target area: "))
            throw new IllegalArgumentException("Invalid input: " + input);

        return Area.parse(input.substring(13));
    }

    /*
     * Brute force all the possible trajectories that result in the probe
     * landing in the target area.
     */
    private Set<FlightResult> calculateViableTrajectories(final Area targetArea) {
        final Set<FlightResult> successes = new HashSet<>();
        for (int h = 0; h <= targetArea.w().max(); h++) {
            for (int v = targetArea.h().min(); v <= Math.abs(targetArea.h().min()) - 1; v++) {
                traceProbePath(new Velocity(h, v), targetArea)
                        .ifPresent(successes::add);
            }
        }
        return successes;
    }

    /*
     * Simulate the flight of a probe launched with a given `Velocity`.
     */
    private Optional<FlightResult> traceProbePath(final Velocity initialVelocity, final Area targetArea) {
        Coordinate highestCoordinate = new Coordinate(0, 0),
                currentCoordinate = new Coordinate(0, 0);

        Velocity velocity = initialVelocity;
        do {
            currentCoordinate = currentCoordinate.adjustBy(velocity);
            if (velocity.v() == 0) highestCoordinate = currentCoordinate;

            if (targetArea.contains(currentCoordinate))
                return Optional.of(new FlightResult(initialVelocity, highestCoordinate));

            velocity = velocity.adjustForPhysics(UNDERWATER_PHYSICS);
        } while (velocity.h() != 0 || currentCoordinate.y() >= targetArea.h().min());

        return Optional.empty();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * The result of a (successful) test firing of a probe.
     */
    private record FlightResult(Velocity velocity, Coordinate highestPoint) {

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final FlightResult that = (FlightResult) o;
            return Objects.equals(velocity, that.velocity) && Objects.equals(highestPoint, that.highestPoint);
        }

        @Override
        public int hashCode() {
            return Objects.hash(velocity, highestPoint);
        }

    }

}
