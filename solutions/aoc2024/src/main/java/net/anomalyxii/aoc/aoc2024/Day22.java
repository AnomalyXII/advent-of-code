package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.Arrays;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 22.
 */
@Solution(year = 2024, day = 22, title = "Monkey Market")
public class Day22 {

    /*
     * A magic number.
     *
     * In this case, it's an upper bound for the number of bananas that could
     * ever realistically be acquired (assuming ~2330 monkeys).
     */
    private static final int MAGIC_NUMBER = 0x0000FFFF;

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
                .mapToInt(Integer::parseInt)
                .mapToLong(init -> {
                    long next = init;
                    for (int i = 0; i < 2000; i++) {
                        next = op(next);
                    }
                    return next;
                })
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
        final int[] sequenceToBananaHaul = new int[(MAGIC_NUMBER + 1) >> 1];
        final int[] seenSequences = new int[(MAGIC_NUMBER + 1) >> 5];
        context.stream()
                .mapToInt(Integer::parseInt)
                .forEach(init -> generateSecretNumbers(init, sequenceToBananaHaul, seenSequences));

        return IntStream.of(sequenceToBananaHaul)
                .flatMap(packed -> IntStream.of(packed >> 16 & MAGIC_NUMBER, packed & MAGIC_NUMBER))
                .max()
                .orElseThrow();
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
        final int[] sequenceToBananaHaul = new int[(MAGIC_NUMBER + 1) >> 1];
        final int[] seenSequences = new int[(MAGIC_NUMBER + 1) >> 5];
        final long part1 = context.stream()
                .mapToInt(Integer::parseInt)
                .mapToLong(init -> generateSecretNumbers(init, sequenceToBananaHaul, seenSequences))
                .sum();

        final int part2 = IntStream.of(sequenceToBananaHaul)
                .flatMap(packed -> IntStream.of(packed >> 16 & MAGIC_NUMBER, packed & MAGIC_NUMBER))
                .max()
                .orElseThrow();

        return new LongTuple(part1, part2);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Generate 2000 secret numbers, and update the banana haul.
     */
    private static long generateSecretNumbers(final int init, final int[] sequenceToBananaHaul, final int[] seenSequences) {
        short deltas = 0;
        long next = init;

        Arrays.fill(seenSequences, 0);
        for (int i = 0; i < 2000; i++) {
            final long newNext = op(next);

            final byte qty = (byte) (newNext % 10);
            final byte prevQty = (byte) (next % 10);
            final byte delta = (byte) (qty - prevQty);
            deltas = (short) (deltas << 4 | 0x0F & delta);

            next = newNext;
            final int hash = MAGIC_NUMBER & deltas;
            final int mod = 1 << ((0x00FF & hash) & 0x1F);
            if (i < 3 || (seenSequences[hash >> 5] & mod) != 0) continue;
            seenSequences[hash >> 5] |= mod;

            final int packed = sequenceToBananaHaul[hash >> 1];
            final int unpackedTop = packed >> 16 & MAGIC_NUMBER;
            final int unpackedBottom = packed & MAGIC_NUMBER;

            sequenceToBananaHaul[hash >> 1] = (hash & 0x01) == 1
                    ? unpackedTop + qty << 16 | unpackedBottom
                    : unpackedTop << 16 | unpackedBottom + qty;
        }

        return next;
    }

    /*
     * Transform a secret number.
     */
    private static long op(final long next) {
        final long op1 = (next << 6 ^ next) & 0x0FFFFFF;
        final long op2 = (op1 >> 5 ^ op1) & 0x0FFFFFF;
        return (op2 << 11 ^ op2) & 0x0FFFFFF;
    }

}

