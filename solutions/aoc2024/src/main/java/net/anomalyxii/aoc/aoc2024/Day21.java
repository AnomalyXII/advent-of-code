package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 21.
 */
@Solution(year = 2024, day = 21, title = "Keypad Conundrum")
public class Day21 {

    /*
     * A sensible number of robots to control (i.e. part 1).
     */
    private static final int SENSIBLE_NUMBER_OF_ROBOTS = 3;

    /*
     * A stub number of robots to control (i.e. part 2).
     */
    private static final int STUPID_NUMBER_OF_ROBOTS = 26;

    /*
     * Cache of num key mappings.
     */
    private static final char[][] NUM_KEYS;

    /*
     * Cache of navigation key mappings.
     */
    private static final char[][] DIR_KEYS;

    /*
     * Maximum offset for a cache key.
     */
    private static final int MAX_IDX = 144;

    static {
        NUM_KEYS = new char[MAX_IDX][];
        NUM_KEYS[0] = new char[]{'A'};
        NUM_KEYS[1] = new char[]{'^', '<', 'A'};
        NUM_KEYS[2] = new char[]{'^', 'A'};
        NUM_KEYS[3] = new char[]{'^', '>', 'A'};
        NUM_KEYS[4] = new char[]{'^', '^', '<', 'A'};
        NUM_KEYS[5] = new char[]{'^', '^', 'A'};
        NUM_KEYS[6] = new char[]{'^', '^', '>', 'A'};
        NUM_KEYS[7] = new char[]{'^', '^', '^', '<', 'A'};
        NUM_KEYS[8] = new char[]{'^', '^', '^', 'A'};
        NUM_KEYS[9] = new char[]{'^', '^', '^', '>', 'A'};
        NUM_KEYS[11] = new char[]{'>', 'A'};
        NUM_KEYS[12] = new char[]{'>', 'v', 'A'};
        NUM_KEYS[13] = new char[]{'A'};
        NUM_KEYS[14] = new char[]{'>', 'A'};
        NUM_KEYS[15] = new char[]{'>', '>', 'A'};
        NUM_KEYS[16] = new char[]{'^', 'A'};
        NUM_KEYS[17] = new char[]{'^', '>', 'A'};
        NUM_KEYS[18] = new char[]{'^', '>', '>', 'A'};
        NUM_KEYS[19] = new char[]{'^', '^', 'A'};
        NUM_KEYS[20] = new char[]{'^', '^', '>', 'A'};
        NUM_KEYS[21] = new char[]{'^', '^', '>', '>', 'A'};
        NUM_KEYS[23] = new char[]{'>', '>', 'v', 'A'};
        NUM_KEYS[24] = new char[]{'v', 'A'};
        NUM_KEYS[25] = new char[]{'<', 'A'};
        NUM_KEYS[26] = new char[]{'A'};
        NUM_KEYS[27] = new char[]{'>', 'A'};
        NUM_KEYS[28] = new char[]{'<', '^', 'A'};
        NUM_KEYS[29] = new char[]{'^', 'A'};
        NUM_KEYS[30] = new char[]{'^', '>', 'A'};
        NUM_KEYS[31] = new char[]{'<', '^', '^', 'A'};
        NUM_KEYS[32] = new char[]{'^', '^', 'A'};
        NUM_KEYS[33] = new char[]{'^', '^', '>', 'A'};
        NUM_KEYS[35] = new char[]{'v', '>', 'A'};
        NUM_KEYS[36] = new char[]{'<', 'v', 'A'};
        NUM_KEYS[37] = new char[]{'<', '<', 'A'};
        NUM_KEYS[38] = new char[]{'<', 'A'};
        NUM_KEYS[39] = new char[]{'A'};
        NUM_KEYS[40] = new char[]{'<', '<', '^', 'A'};
        NUM_KEYS[41] = new char[]{'<', '^', 'A'};
        NUM_KEYS[42] = new char[]{'^', 'A'};
        NUM_KEYS[43] = new char[]{'<', '<', '^', '^', 'A'};
        NUM_KEYS[44] = new char[]{'<', '^', '^', 'A'};
        NUM_KEYS[45] = new char[]{'^', '^', 'A'};
        NUM_KEYS[47] = new char[]{'v', 'A'};
        NUM_KEYS[48] = new char[]{'>', 'v', 'v', 'A'};
        NUM_KEYS[49] = new char[]{'v', 'A'};
        NUM_KEYS[50] = new char[]{'v', '>', 'A'};
        NUM_KEYS[51] = new char[]{'v', '>', '>', 'A'};
        NUM_KEYS[52] = new char[]{'A'};
        NUM_KEYS[53] = new char[]{'>', 'A'};
        NUM_KEYS[54] = new char[]{'>', '>', 'A'};
        NUM_KEYS[55] = new char[]{'^', 'A'};
        NUM_KEYS[56] = new char[]{'^', '>', 'A'};
        NUM_KEYS[57] = new char[]{'^', '>', '>', 'A'};
        NUM_KEYS[59] = new char[]{'>', '>', 'v', 'v', 'A'};
        NUM_KEYS[60] = new char[]{'v', 'v', 'A'};
        NUM_KEYS[61] = new char[]{'<', 'v', 'A'};
        NUM_KEYS[62] = new char[]{'v', 'A'};
        NUM_KEYS[63] = new char[]{'v', '>', 'A'};
        NUM_KEYS[64] = new char[]{'<', 'A'};
        NUM_KEYS[65] = new char[]{'A'};
        NUM_KEYS[66] = new char[]{'>', 'A'};
        NUM_KEYS[67] = new char[]{'<', '^', 'A'};
        NUM_KEYS[68] = new char[]{'^', 'A'};
        NUM_KEYS[69] = new char[]{'^', '>', 'A'};
        NUM_KEYS[71] = new char[]{'v', 'v', '>', 'A'};
        NUM_KEYS[72] = new char[]{'<', 'v', 'v', 'A'};
        NUM_KEYS[73] = new char[]{'<', '<', 'v', 'A'};
        NUM_KEYS[74] = new char[]{'<', 'v', 'A'};
        NUM_KEYS[75] = new char[]{'v', 'A'};
        NUM_KEYS[76] = new char[]{'<', '<', 'A'};
        NUM_KEYS[77] = new char[]{'<', 'A'};
        NUM_KEYS[78] = new char[]{'A'};
        NUM_KEYS[79] = new char[]{'<', '<', '^', 'A'};
        NUM_KEYS[80] = new char[]{'<', '^', 'A'};
        NUM_KEYS[81] = new char[]{'^', 'A'};
        NUM_KEYS[83] = new char[]{'v', 'v', 'A'};
        NUM_KEYS[84] = new char[]{'>', 'v', 'v', 'v', 'A'};
        NUM_KEYS[85] = new char[]{'v', 'v', 'A'};
        NUM_KEYS[86] = new char[]{'v', 'v', '>', 'A'};
        NUM_KEYS[87] = new char[]{'v', 'v', '>', '>', 'A'};
        NUM_KEYS[88] = new char[]{'v', 'A'};
        NUM_KEYS[89] = new char[]{'v', '>', 'A'};
        NUM_KEYS[90] = new char[]{'v', '>', '>', 'A'};
        NUM_KEYS[91] = new char[]{'A'};
        NUM_KEYS[92] = new char[]{'>', 'A'};
        NUM_KEYS[93] = new char[]{'>', '>', 'A'};
        NUM_KEYS[95] = new char[]{'>', '>', 'v', 'v', 'v', 'A'};
        NUM_KEYS[96] = new char[]{'v', 'v', 'v', 'A'};
        NUM_KEYS[97] = new char[]{'<', 'v', 'v', 'A'};
        NUM_KEYS[98] = new char[]{'v', 'v', 'A'};
        NUM_KEYS[99] = new char[]{'v', 'v', '>', 'A'};
        NUM_KEYS[100] = new char[]{'<', 'v', 'A'};
        NUM_KEYS[101] = new char[]{'v', 'A'};
        NUM_KEYS[102] = new char[]{'v', '>', 'A'};
        NUM_KEYS[103] = new char[]{'<', 'A'};
        NUM_KEYS[104] = new char[]{'A'};
        NUM_KEYS[105] = new char[]{'>', 'A'};
        NUM_KEYS[107] = new char[]{'v', 'v', 'v', '>', 'A'};
        NUM_KEYS[108] = new char[]{'<', 'v', 'v', 'v', 'A'};
        NUM_KEYS[109] = new char[]{'<', '<', 'v', 'v', 'A'};
        NUM_KEYS[110] = new char[]{'<', 'v', 'v', 'A'};
        NUM_KEYS[111] = new char[]{'v', 'v', 'A'};
        NUM_KEYS[112] = new char[]{'<', '<', 'v', 'A'};
        NUM_KEYS[113] = new char[]{'<', 'v', 'A'};
        NUM_KEYS[114] = new char[]{'v', 'A'};
        NUM_KEYS[115] = new char[]{'<', '<', 'A'};
        NUM_KEYS[116] = new char[]{'<', 'A'};
        NUM_KEYS[117] = new char[]{'A'};
        NUM_KEYS[119] = new char[]{'v', 'v', 'v', 'A'};
        NUM_KEYS[132] = new char[]{'<', 'A'};
        NUM_KEYS[133] = new char[]{'^', '<', '<', 'A'};
        NUM_KEYS[134] = new char[]{'<', '^', 'A'};
        NUM_KEYS[135] = new char[]{'^', 'A'};
        NUM_KEYS[136] = new char[]{'^', '^', '<', '<', 'A'};
        NUM_KEYS[137] = new char[]{'<', '^', '^', 'A'};
        NUM_KEYS[138] = new char[]{'^', '^', 'A'};
        NUM_KEYS[139] = new char[]{'^', '^', '^', '<', '<', 'A'};
        NUM_KEYS[140] = new char[]{'<', '^', '^', '^', 'A'};
        NUM_KEYS[141] = new char[]{'^', '^', '^', 'A'};
        NUM_KEYS[143] = new char[]{'A'};

        DIR_KEYS = new char[MAX_IDX][];
        DIR_KEYS[0] = new char[]{'A'};
        DIR_KEYS[1] = new char[]{'>', '^', 'A'};
        DIR_KEYS[2] = new char[]{'>', 'A'};
        DIR_KEYS[3] = new char[]{'>', '>', 'A'};
        DIR_KEYS[11] = new char[]{'>', '>', '^', 'A'};
        DIR_KEYS[12] = new char[]{'v', '<', 'A'};
        DIR_KEYS[13] = new char[]{'A'};
        DIR_KEYS[14] = new char[]{'v', 'A'};
        DIR_KEYS[15] = new char[]{'v', '>', 'A'};
        DIR_KEYS[23] = new char[]{'>', 'A'};
        DIR_KEYS[24] = new char[]{'<', 'A'};
        DIR_KEYS[25] = new char[]{'^', 'A'};
        DIR_KEYS[26] = new char[]{'A'};
        DIR_KEYS[27] = new char[]{'>', 'A'};
        DIR_KEYS[35] = new char[]{'^', '>', 'A'};
        DIR_KEYS[36] = new char[]{'<', '<', 'A'};
        DIR_KEYS[37] = new char[]{'<', '^', 'A'};
        DIR_KEYS[38] = new char[]{'<', 'A'};
        DIR_KEYS[39] = new char[]{'A'};
        DIR_KEYS[47] = new char[]{'^', 'A'};
        DIR_KEYS[132] = new char[]{'v', '<', '<', 'A'};
        DIR_KEYS[133] = new char[]{'<', 'A'};
        DIR_KEYS[134] = new char[]{'<', 'v', 'A'};
        DIR_KEYS[135] = new char[]{'v', 'A'};
        DIR_KEYS[143] = new char[]{'A'};
    }

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
        final long[][] cache = new long[SENSIBLE_NUMBER_OF_ROBOTS][];
        return context.stream()
                .mapToLong(line -> calculateComplexity(line, SENSIBLE_NUMBER_OF_ROBOTS, cache))
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
        final long[][] cache = new long[STUPID_NUMBER_OF_ROBOTS][];
        return context.stream()
                .mapToLong(line -> calculateComplexity(line, STUPID_NUMBER_OF_ROBOTS, cache))
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
        final long[][] cache1 = new long[SENSIBLE_NUMBER_OF_ROBOTS][];
        final long[][] cache2 = new long[STUPID_NUMBER_OF_ROBOTS][];
        return context.stream()
                .reduce(
                        LongTuple.NULL,
                        (result, line) -> result.add(
                                calculateComplexity(line, SENSIBLE_NUMBER_OF_ROBOTS, cache1),
                                calculateComplexity(line, STUPID_NUMBER_OF_ROBOTS, cache2)
                        ),
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the complexity of a numeric key combination.
     */
    static long calculateComplexity(final String line, final int depth) {
        return calculateComplexity(line, depth, new long[depth][]);
    }

    /*
     * Calculate the complexity of a numeric key combination.
     */
    private static long calculateComplexity(final String line, final int depth, final long[][] cache) {
        final int numeric = Integer.parseInt(line.substring(0, line.length() - 1));
        final long length = calculateInputLength(line.toCharArray(), 0, depth, cache);
        return numeric * length;
    }

    /*
     * Calculate the number of keys needed to generate a button sequence.
     */
    private static long calculateInputLength(
            final char[] line,
            final int depth,
            final int maxDepth,
            final long[][] cache
    ) {
        if (depth == maxDepth) return line.length;

        final long[] depthCache = cache[depth] == null ? (cache[depth] = new long[MAX_IDX]) : cache[depth];

        char current = 'A';
        long length = 0;
        for (final char next : line) {
            final int cc = idx(current, next);
            assert cc < MAX_IDX;

            final long result = depthCache[cc] > 0
                    ? depthCache[cc]
                    : (depthCache[cc] = calculateInputLength(
                            (depth == 0 ? NUM_KEYS : DIR_KEYS)[cc],
                            depth + 1,
                            maxDepth,
                            cache
                    ));

            length += result;
            assert length > 0;
            current = next;
        }

        assert current == 'A';
        return length;
    }

    /*
     * Calculate a (hopefully) unique cache index for a key transition.
     */
    private static int idx(final char current, final char next) {
        final byte upper = offset(current);
        final byte lower = offset(next);
        return upper * 12 + lower;
    }

    /*
     * Calculate a (hopefully) unique cache offset for each key.
     */
    private static byte offset(final char current) {
        return switch (current) {
            case '0', '<' -> 0;
            case '1', '^' -> 1;
            case '2', 'v' -> 2;
            case '3', '>' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'A' -> 11;
            default -> throw new IllegalArgumentException("Invalid index: " + current);
        };
    }

}

