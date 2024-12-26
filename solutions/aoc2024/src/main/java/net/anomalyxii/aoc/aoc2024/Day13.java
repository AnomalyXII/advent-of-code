package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 13.
 */
@Solution(year = 2024, day = 13, title = "Claw Contraption")
public class Day13 {

    /*
     * Can't win the prize :(
     */
    private static final int NO_PRIZE_FOR_YOU = 0;

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
        return context.streamBatches()
                .map(ButtonsAndPrize::parseInput)
                .mapToLong(bap -> determineMinimumTokens(bap, 0))
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return context.streamBatches()
                .map(ButtonsAndPrize::parseInput)
                .mapToLong(bap -> determineMinimumTokens(bap, 10000000000000L))
                .sum();
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
        return context.streamBatches()
                .map(ButtonsAndPrize::parseInput)
                .reduce(
                        LongTuple.NULL,
                        (res, bap) -> res.add(
                                determineMinimumTokens(bap, 0L),
                                determineMinimumTokens(bap, 10000000000000L)
                        ),
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Determine the minimum number of tokens needed to get the prize, if it
     * is possible to do so.
     */
    private static long determineMinimumTokens(final ButtonsAndPrize r, final long prizeOffset) {
        final long prizex = prizeOffset + r.prizex();
        final long prizey = prizeOffset + r.prizey();

        // (i * ax) + (j * bx) = prizex
        // (i * ay) + (j * by) = prizey

        // Solve simultaneous equation...
        // (i * ax * by) + (j * bx * by) = prizex * by
        // (i * ay * bx) + (j * by * bx) = prizey * bx

        // (i * (ax * by - ay * bx)) = (prizex * by) - (prizey * bx)
        // i = (prizex * by) - (prizey * bx) / (ax * by - ay * bx)

        final long i = ((prizex * r.by()) - (prizey * r.bx()))
                / (r.ax() * r.by() - r.ay() * r.bx());

        // j = (prizex - (i * ax)) / bx
        final long j = (prizex - (i * r.ax())) / r.bx();

        if (i < 0 || j < 0) return NO_PRIZE_FOR_YOU;

        final boolean checkx = (i * r.ax()) + (j * r.bx()) == prizex;
        final boolean checky = (i * r.ay()) + (j * r.by()) == prizey;
        if (!checkx || !checky) return NO_PRIZE_FOR_YOU;

        return 3 * i + j;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Contains the button configurations along with the prize co-ords.
     */
    private record ButtonsAndPrize(
            long ax,
            long ay,
            long bx,
            long by,
            long prizex,
            long prizey
    ) {

        // Static Helper Methods

        /*
         * Parse a `ButtonsAndPrize` from the given batch.
         */
        private static ButtonsAndPrize parseInput(final List<String> batch) {
            final String buttonA = batch.get(0);
            final String buttonB = batch.get(1);
            final String prize = batch.get(2);

            final String[] splitA = buttonA.split("[:,]\\s*");

            final String[] splitB = buttonB.split("[:,]\\s*");

            final String[] splitPrize = prize.split("[:,]\\s*");
            return new ButtonsAndPrize(
                    Integer.parseInt(splitA[1].substring(2)),
                    Integer.parseInt(splitA[2].substring(2)),
                    Integer.parseInt(splitB[1].substring(2)),
                    Integer.parseInt(splitB[2].substring(2)),
                    Integer.parseInt(splitPrize[1].substring(2)),
                    Integer.parseInt(splitPrize[2].substring(2))
            );
        }
    }

}

