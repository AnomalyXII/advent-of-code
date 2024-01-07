package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 6.
 */
@Solution(year = 2022, day = 6, title = "Tuning Trouble")
public class Day6 {

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
        return locateStartOfSignal(context, Marker.START_OF_PACKET);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return locateStartOfSignal(context, Marker.START_OF_MESSAGE);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the earliest occurrence of a specified `Marker`.
     */
    private static int locateStartOfSignal(final SolutionContext context, final Marker marker) {
        final String line = context.readLine();
        for (int i = marker.length; i < line.length(); i++)
            if (marker.isMarker(line, i))
                return i;
        throw new IllegalStateException("No signal found :(");
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a marker within a signal.
     */
    private enum Marker {

        /**
         * Indicates the start of a packet.
         */
        START_OF_PACKET(4),

        /**
         * Indicates the start of a message.
         */
        START_OF_MESSAGE(14),

        // End of constants
        ;

        private final int length;

        // Constructors

        Marker(final int length) {
            this.length = length;
        }

        // Helper Methods

        /**
         * Check if the given line contains this signal marker, starting from a
         * specified position within the signal.
         *
         * @param line  the signal
         * @param start the starting position within the signal
         * @return {@literal true} if the signal contains the given {@link Marker}
         */
        boolean isMarker(final String line, final int start) {
            for (int i = 1; i <= length; i++) {
                final String substring = line.substring(start - (i - 1), start);
                final char ch = line.charAt(start - i);
                if (substring.indexOf(ch) >= 0)
                    return false;
            }

            return true;
        }

    }

}
