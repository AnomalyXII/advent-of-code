package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid.MutableGrid;

import java.util.concurrent.atomic.AtomicLong;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 11.
 */
@Solution(year = 2021, day = 11, title = "Dumbo Octopus")
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
        final MutableGrid octopi = context.readMutableGrid(chr -> chr - '0');

        final AtomicLong flashes = new AtomicLong(0L);
        for (int i = 0; i < 100; i++) {
            pulse(octopi, flashes::incrementAndGet);
        }
        return flashes.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final MutableGrid octopi = context.readMutableGrid(chr -> chr - '0');

        long turn = 0;
        boolean inSync;
        do {
            final AtomicLong flashes = new AtomicLong(0L);
            pulse(octopi, flashes::incrementAndGet);
            inSync = flashes.intValue() == (octopi.area());

            ++turn;
        } while (!inSync);

        return turn;
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Increase the power level of every octopus by 1. Trigger a flash if the
     * power level of an octopus is over 9.
     */
    private static void pulse(final MutableGrid octopi, final Runnable onFlash) {
        // Increase all power levels by 1
        octopi.forEach(p -> increasePowerLevel(octopi, p));

        // Flash (ah-ha):
        final boolean[] gordonsAlive = new boolean[1];
        do {
            gordonsAlive[0] = false;
            octopi.forEach(p -> {
                if (octopi.get(p) == 10) {
                    octopi.set(p, 11);
                    octopi.forEachNeighbourOf(p, o -> increasePowerLevel(octopi, o));
                    gordonsAlive[0] = true;
                }
            });
        } while (gordonsAlive[0]);

        // Increase all power levels by 1
        octopi.forEach(p -> {
            if (octopi.get(p) >= 10) {
                onFlash.run();
                resetPowerLevel(octopi, p);
            }
        });
    }

    /*
     * Increase the power level of an octopus, assuming it is less than 9.
     */
    private static void increasePowerLevel(final MutableGrid octopi, final Coordinate coordinate) {
        octopi.set(coordinate, octopi.get(coordinate) > 9 ? octopi.get(coordinate) : octopi.get(coordinate) + 1);
    }

    /*
     * Reset the power level of an octopus to 0.
     */
    private static void resetPowerLevel(final MutableGrid octopi, final Coordinate coordinate) {
        octopi.set(coordinate, 0);
    }

}
