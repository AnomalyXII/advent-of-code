package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Grid.MutableGrid;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 11.
 */
@Solution(year = 2020, day = 11, title = "Seating System")
public class Day11 {

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
        final MutableGrid seatingPlan = Grid.parseMutable(context.stream(), c -> c);

        final AtomicLong occupiedSeats = new AtomicLong();
        whileSeatsAreInFlux(() -> {
            seatingPlan.forEachMatching(
                    point -> !isEmptySpace(seatingPlan, point),
                    point -> occupiedSeats.addAndGet(countOccupiedSeats(seatingPlan, point, 1, 4))
            );

            // Update the placeholder positions
            return finaliseUpdatedSeatingPlan(seatingPlan);
        });

        return occupiedSeats.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final MutableGrid seatingPlan = Grid.parseMutable(context.stream(), c -> c);

        final AtomicLong occupiedSeats = new AtomicLong(0);
        whileSeatsAreInFlux(() -> {
            seatingPlan.forEachMatching(
                    point -> !isEmptySpace(seatingPlan, point),
                    point -> occupiedSeats.addAndGet(countOccupiedSeats(seatingPlan, point, Integer.MAX_VALUE, 5))
            );

            // Update the placeholder positions
            return finaliseUpdatedSeatingPlan(seatingPlan);
        });

        return occupiedSeats.longValue();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Repeat an action until the seat occupation stops changing.
     */
    private static void whileSeatsAreInFlux(final BooleanSupplier action) {
        boolean shouldContinue;
        do {
            // Run...
            shouldContinue = action.getAsBoolean();
        } while (shouldContinue);
    }

    /*
     * Count how many seats are occupied (and mark them as such).
     */
    private int countOccupiedSeats(
            final MutableGrid seatingPlan,
            final Coordinate coordinate,
            final int limit,
            final int antisocialness
    ) {
        final long surroundingSeats = Velocity.directions()
                .filter(direction -> isFirstSeatInDirectionOccupied(seatingPlan, coordinate, direction, limit))
                .count();

        return maybeUpdateCurrentSeat(seatingPlan, coordinate, surroundingSeats, antisocialness);
    }

    /*
     * Update the seating plan if anyone will occupy or leave a seat.
     */
    private static int maybeUpdateCurrentSeat(
            final MutableGrid seatingPlan,
            final Coordinate coordinate,
            final long surroundingSeats,
            final int antisocialness
    ) {
        final boolean currentSeatOccupied = isOccupiedSeat(seatingPlan, coordinate);
        if (!currentSeatOccupied && surroundingSeats == 0) {
            seatingPlan.set(coordinate, '~');
            return 1;
        }

        if (currentSeatOccupied && surroundingSeats >= antisocialness) {
            seatingPlan.set(coordinate, 'l');
            return -1;
        }

        return 0;
    }

    /*
     * Update the seating plan with any changed seats.
     */
    private static boolean finaliseUpdatedSeatingPlan(final MutableGrid seatingPlan) {
        final boolean[] updated = new boolean[1];
        seatingPlan.forEach(point -> {
            if (seatingPlan.get(point) == 'l') {
                seatingPlan.set(point, 'L');
                updated[0] = true;
            } else if (seatingPlan.get(point) == '~') {
                seatingPlan.set(point, '#');
                updated[0] = true;
            }
        });
        return updated[0];
    }

    /*
     * Check if the first seat in a given direction is occupied.
     */
    private static boolean isFirstSeatInDirectionOccupied(
            final Grid seatingPlan,
            final Coordinate coordinate,
            final Velocity dir,
            final int limit
    ) {
        Coordinate nextCoordinate = coordinate.adjustBy(dir);
        for (int l = 0; l < limit && seatingPlan.contains(nextCoordinate); l++, nextCoordinate = nextCoordinate.adjustBy(dir)) {
            if (isEmptySpace(seatingPlan, nextCoordinate)) continue;
            return isOccupiedSeat(seatingPlan, nextCoordinate);
        }

        return false;
    }

    /*
     * Check if the given coordinate is an empty space.
     */
    private static boolean isEmptySpace(final Grid seatingPlan, final Coordinate coordinate) {
        return !seatingPlan.contains(coordinate) || seatingPlan.get(coordinate) == '.';
    }

    /*
     * Check if the given coordinate is an occupied seat.
     */
    private static boolean isOccupiedSeat(final Grid seatingPlan, final Coordinate coordinate) {
        return seatingPlan.contains(coordinate)
                && (seatingPlan.get(coordinate) == '#' || seatingPlan.get(coordinate) == 'l');
    }

}
