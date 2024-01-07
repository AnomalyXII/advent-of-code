package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 5.
 */
@Solution(year = 2020, day = 5, title = "Binary Boarding")
public class Day5 {

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
        return context.stream()
                .mapToLong(Day5::calculateSeatId)
                .max()
                .orElse(0);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Long> seats = context.stream()
                .map(Day5::calculateSeatId)
                .sorted()
                .toList();

        long candidateForMySeat = -1;
        for (int i = 1; i < seats.size(); i++) {
            final long prevSeatId = seats.get(i - 1);
            final long thisSeatId = seats.get(i);

            if (prevSeatId != (thisSeatId - 1)) {
                if (candidateForMySeat >= 0) {
                    throw new IllegalStateException("Ambiguous missing seat!");
                }

                candidateForMySeat = thisSeatId - 1;
            }
        }

        return candidateForMySeat;
    }

    // ****************************************
    // Helper Members
    // ****************************************

    /**
     * Calculate the ID of a seat, given the boarding pass coordinates.
     *
     * @param pass the boarding pass
     * @return the seat ID
     */
    static long calculateSeatId(final String pass) {
        if (!pass.matches("[FB]{7}[LR]{3}")) {
            throw new IllegalArgumentException("Invalid boarding pass: " + pass);
        }

        int seat = 0;
        for (final char c : pass.toCharArray()) {
            seat = (seat << 1) + ((c == 'B' || c == 'R') ? 1 : 0);
        }

        return seat;
    }

}
