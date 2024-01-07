package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.concurrent.atomic.AtomicLong;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 2.
 */
@Solution(year = 2021, day = 2, title = "Dive")
public class Day2 {

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
        final AtomicLong position = new AtomicLong(0);
        final AtomicLong depth = new AtomicLong(0);

        context.consume(instruction -> {
            final String[] parts = instruction.split(" ", 2);

            final int delta = Integer.parseInt(parts[1]);
            if ("forward".equals(parts[0]))
                position.addAndGet(delta);
            else if ("down".equals(parts[0]))
                depth.addAndGet(delta);
            else if ("up".equals(parts[0]))
                depth.addAndGet(-delta);
        });

        return position.longValue() * depth.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final AtomicLong aim = new AtomicLong(0);
        final AtomicLong position = new AtomicLong(0);
        final AtomicLong depth = new AtomicLong(0);

        context.consume(instruction -> {
            final String[] parts = instruction.split(" ", 2);
            final int delta = Integer.parseInt(parts[1]);
            if ("forward".equals(parts[0])) {
                position.addAndGet(delta);
                depth.addAndGet(aim.longValue() * delta);
            } else if ("down".equals(parts[0])) {
                aim.addAndGet(delta);
            } else if ("up".equals(parts[0])) {
                aim.addAndGet(-delta);
            }
        });

        return position.longValue() * depth.longValue();
    }

    // ****************************************
    // Helper Methods
    // ****************************************


}
