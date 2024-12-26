package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 25.
 */
@Solution(year = 2024, day = 25, title = "Code Chronicle")
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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final int[][] locksAndKeys = parseSchematics(context);
        final int[] locks = locksAndKeys[0];
        final int[] keys = locksAndKeys[1];

        int matches = 0;
        for (final int lock : locks) {
            for (final int key : keys) {
                if (areCompatible(lock, key))
                    ++matches;
            }
        }

        return matches;
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
     * Process all the schematics.
     */
    private static int[][] parseSchematics(final SolutionContext context) {
        int numLocks = 0;
        int numKeys = 0;
        final int[] locks = new int[512];
        final int[] keys = new int[512];
        final List<List<String>> batches = context.readBatches();
        for (final List<String> batch : batches) {
            if (batch.getFirst().matches("^#+$")) locks[numLocks++] = processLockSchematic(batch);
            else if (batch.getLast().matches("^#+$")) keys[numKeys++] = parseKeySchematic(batch);
            else throw new IllegalStateException("Invalid schematic - was neither a lock nor a key");
        }

        return new int[][]{
                Arrays.copyOfRange(locks, 0, numLocks),
                Arrays.copyOfRange(keys, 0, numKeys)
        };
    }

    /*
     * Parse a lock schematic.
     */
    private static int processLockSchematic(final List<String> batch) {
        int lock = 0;
        for (int i = 1; i < batch.size(); i++) {
            for (int j = 0; j < batch.getFirst().length(); j++) {
                final char p = batch.get(i).charAt(j);
                if (p == '#') lock += (1 << (3 * j));
            }
        }
        return lock;
    }

    /*
     * Parse a key schematic.
     */
    private static int parseKeySchematic(final List<String> batch) {
        int key = 0;
        for (int i = 0; i < batch.size() - 1; i++) {
            for (int j = 0; j < batch.getLast().length(); j++) {
                final char p = batch.get(i).charAt(j);
                if (p == '#') key += (1 << (3 * j));
            }
        }
        return key;
    }

    /*
     * Check if the given lock and key are compatible.
     */
    private static boolean areCompatible(final int lock, final int key) {
        for (int i = 0; i < 5; i++) {
            final int lockPin = 0x07 & (lock >> (3 * i));
            final int keyPin = 0x07 & (key >> (3 * i));
            if (lockPin + keyPin >= 6)
                return false;
        }
        return true;
    }

}

