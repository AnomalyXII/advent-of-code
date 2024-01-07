package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 25.
 */
@Solution(year = 2020, day = 25, title = "Combo Breaker")
public class Day25 {

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
        final long[] keys = context.stream()
                .mapToLong(Long::parseLong)
                .toArray();

        final long cardPublicKey = keys[0];
        final long doorPublicKey = keys[1];

        final int cardLoopSize = bruteForceLoopSize(cardPublicKey);
        final int doorLoopSize = bruteForceLoopSize(doorPublicKey);

        final long encryptionKey1 = transform(doorPublicKey, cardLoopSize);
        final long encryptionKey2 = transform(cardPublicKey, doorLoopSize);
        assert encryptionKey1 == encryptionKey2;

        return encryptionKey1;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return -
     */
    @Part(part = II)
    public NoChallenge calculateAnswerForPart2(final SolutionContext context) {
        return NoChallenge.NO_CHALLENGE;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Brute force the loop size by transforming the `publicKey` until a
     * match is found.
     */
    private static int bruteForceLoopSize(final long publicKey) {
        long value = 1;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (value == publicKey)
                return i;

            value = transform(7, value);
        }

        throw new IllegalStateException("Failed to brute force the key");
    }

    /*
     * Transform the given `publicKey`.
     */
    private static long transform(final long subject, final int loopSize) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value = transform(subject, value);
        }
        return value;
    }

    /*
     * Transform the given subject/value.
     */
    private static long transform(final long subject, final long value) {
        return (value * subject) % 20201227;
    }

}
