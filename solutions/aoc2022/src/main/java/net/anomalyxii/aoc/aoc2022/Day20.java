package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 20.
 */
@Solution(year = 2022, day = 20, title = "Grove Positioning System")
public class Day20 {

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
        final List<Integer> encoding = context.process(Integer::valueOf);
        final List<Long> mixedItUp = mixItUp(encoding, 1, 1L);
        return decryptUnmixedCoordinates(mixedItUp);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Integer> encoding = context.process(Integer::valueOf);
        final List<Long> mixedItUp = mixItUp(encoding, 10, 811589153L);
        return decryptUnmixedCoordinates(mixedItUp);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Perform the mixing (or is it unmixing?) of the input data.
     */
    private static List<Long> mixItUp(final List<Integer> encoding, final int numPasses, final long encryptionConstant) {
        final List<SirMixalot> mixItUp = IntStream.range(0, encoding.size())
                .mapToObj(i -> SirMixalot.of(i, encoding.get(i) * encryptionConstant))
                .toList();

        final List<SirMixalot> mixedItUp = new ArrayList<>(mixItUp);
        for (int i = 0; i < numPasses; i++) {
            for (final SirMixalot hitMe : mixItUp) {
                final int idx = mixedItUp.indexOf(hitMe);
                long newIdx = idx + hitMe.number;
                newIdx %= encoding.size() - 1; // -1 because we removed an item!
                if (newIdx < 0)
                    newIdx += encoding.size() - 1;


                mixedItUp.remove(idx);
                mixedItUp.add((int) newIdx, hitMe);
            }
        }

        return mixedItUp.stream()
                .map(i -> i.number)
                .toList();
    }

    /*
     * Find the 1000th, 2000th and 3000ths (wrapping as necessary)
     * co-ordinates after 0 return sum.
     */
    private static long decryptUnmixedCoordinates(final List<Long> flat) {
        final int idx0 = flat.indexOf(0L);
        if (idx0 < 0)
            throw new IllegalStateException("Unable to find value 0 - aborting!");

        return flat.get((idx0 + 1000) % flat.size())
                + flat.get((idx0 + 2000) % flat.size())
                + flat.get((idx0 + 3000) % flat.size());
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A number, coupled with the original ID just in case there are
     * duplicates.
     */
    private record SirMixalot(int idx, long number) {

        // To String

        @Override
        public String toString() {
            return "Mix[" + number + "]";
        }

        // Static Helper Methods

        /**
         * Create a new {@link SirMixalot}.
         *
         * @param idx the original index
         * @param num the number
         * @return the {@link SirMixalot}
         */
        static SirMixalot of(final int idx, final long num) {
            return new SirMixalot(idx, num);
        }

    }

}

