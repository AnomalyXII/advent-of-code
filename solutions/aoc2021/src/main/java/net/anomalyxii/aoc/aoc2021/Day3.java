package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 3.
 */
@Solution(year = 2021, day = 3, title = "Binary Diagnostic")
public class Day3 {

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
        final List<String> inputs = context.read();

        final int inputLength = inputs.getFirst().length();

        long gammaRate = 0, epsilonRate = 0;
        for (int pos = 0; pos < inputLength; pos++) {
            int msbIs1Counter = 0;
            for (final String input : inputs) {
                final char bit = input.charAt(pos);
                if (bit == '0') --msbIs1Counter;
                else if (bit == '1') ++msbIs1Counter;
                else throw parseError(input, bit, pos);
            }
            gammaRate = (gammaRate << 1) | (msbIs1Counter >= 0 ? 1 : 0);
            epsilonRate = (epsilonRate << 1) | (msbIs1Counter >= 0 ? 0 : 1);
        }

        return gammaRate * epsilonRate;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> inputs = context.read();

        final String oxygenGeneratorRatingStr = filterByMostCommonMsb(inputs, false);
        final String co2ScrubberRatingStr = filterByMostCommonMsb(inputs, true);

        final long oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatingStr, 2);
        final long co2ScrubberRating = Integer.parseInt(co2ScrubberRatingStr, 2);
        return oxygenGeneratorRating * co2ScrubberRating;
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Filter the provided inputs by checking the most-signification bit and
     * retaining only the entries with (or without, in the case of inverted
     * filtering) the corresponding bit. Continue this process moving to the
     * next bit (now considered the most-significant bit).
     */
    private String filterByMostCommonMsb(final List<String> inputs, final boolean invert) {
        List<String> filteredRatings = new ArrayList<>(inputs);
        int pos = 0;
        while (filteredRatings.size() > 1) {
            final List<String> msbIs0 = new ArrayList<>();
            final List<String> msbIs1 = new ArrayList<>();
            for (final String rating : filteredRatings) {
                final char bit = rating.charAt(pos);
                if (bit == '0') msbIs0.add(rating);
                else if (bit == '1') msbIs1.add(rating);
                else throw parseError(rating, bit, pos);
            }
            filteredRatings = (msbIs0.size() > msbIs1.size()) ^ invert ? msbIs0 : msbIs1;
            ++pos;
        }
        return filteredRatings.getFirst();
    }

    /*
     * Return a IllegalArgumentException explaining why the given input was invalid.
     */
    private IllegalArgumentException parseError(final String input, final char bit, final int pos) {
        return new IllegalArgumentException("Invalid bit [" + bit + "] at position " + pos + " in input '" + input + "'");
    }

}
