package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 23.
 */
@Solution(year = 2024, day = 23, title = "LAN Party")
public class Day23 {

    /*
     * Conservative estimate for maximum range of a single character in a
     * computer hostname (assumes a-z only...).
     */
    private static final int MAX_1 = 0x001F;

    /*
     * Number of bits used to store a single character of a hostname.
     */
    private static final int SIZE_1 = 5;

    /*
     * Conservative estimate for maximum range of all characters in a
     * computer hostname (assumes exactly 2 a-z).
     */
    private static final int MAX_2 = 0x03FF;

    /*
     * Number of bits used to store all characters in a hostname.
     */
    private static final int SIZE_2 = 10;

    /*
     * Arbitrary number for maximum number of links from any one computer.
     *
     * Set to 16 to conserve memory, but could be increased if needed.
     * Actually only supports 15 links, as the first entry is always the
     * actual number of entries in the array.
     */
    private static final int MAX_LINKS = 0x10;

    /*
     * Representation of `t` in a hostname.
     */
    private static final int MAGIC_T = 19;

    /*
     * Computer says "no"...
     */
    private static final int[] NO_COMPUTERS = new int[0];

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
        final int[][] computers = parseComputers(context);
        return countClustersOfThreeContainingAComputerStartingWithT(computers);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final int[][] computers = parseComputers(context);
        return findLanPartyPassword(computers);
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link ObjectTuple} containing the answers for both parts
     */
    @Optimised
    public ObjectTuple<Integer, String> calculateAnswers(final SolutionContext context) {
        final int[][] computers = parseComputers(context);
        return new ObjectTuple<>(
                countClustersOfThreeContainingAComputerStartingWithT(computers),
                findLanPartyPassword(computers)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Parse all the links between computers.
     */
    private static int[][] parseComputers(final SolutionContext context) {
        final int[][] computers = new int[MAX_2][];
        context.stream()
                .flatMapToInt(Day23::parseLink)
                .sorted()
                .forEach(link -> registerLink(link, computers));
        return computers;
    }

    /*
     * Parse a link into an integer representation.
     *
     * Return two copies, representing the bi-directionalness.
     */
    private static IntStream parseLink(final String line) {
        assert line.length() == SIZE_1;

        final int c1 = toInt(line, 0, 1);
        final int c2 = toInt(line, 3, 4);
        return IntStream.of(
                // "Forwards"
                c1 << SIZE_2 | c2,
                // ... and "backwards"
                c2 << SIZE_2 | c1
        );
    }

    /*
     * Register a uni-directional link.
     */
    private static void registerLink(final int link, final int[][] computers) {
        final int c1 = link >> SIZE_2 & MAX_2;
        final int c2 = link & MAX_2;
        assert (c1 << SIZE_2 | c2) == link;

        if (computers[c1] == null) {
            computers[c1] = new int[MAX_LINKS];
            Arrays.fill(computers[c1], -1);
            computers[c1][0] = 0;
        }

        final int c1c = ++computers[c1][0];
        assert c1c < MAX_LINKS;
        computers[c1][c1c] = c2;
    }

    /*
     * Count the number of clusters of 3 computers that contain at least one
     * computer with a hostname starting with `t`.
     */
    private static int countClustersOfThreeContainingAComputerStartingWithT(final int[][] allLinks) {
        final Set<Long> result = new HashSet<>();
        for (int c1 = 0; c1 <= allLinks.length; c1++) {
            assert c1 >= 0;
            if (c1 >> SIZE_1 != MAGIC_T) continue; // Doesn't start with a `t`...

            final int[] links1 = allLinks[c1];
            if (links1 == null) continue; // Not set...

            final int numLinks1 = links1[0];
            for (int i2 = 1; i2 <= numLinks1; i2++) {
                final int c2 = links1[i2];
                assert c2 >= 0;

                final int[] links2 = allLinks[c2];
                assert links2 != null; // Should always be a back reference...

                final int numLinks2 = links2[0];
                for (int i3 = 1; i3 <= numLinks2; i3++) {
                    final int c3 = links2[i3];
                    assert c3 >= 0;

                    final int[] links3 = allLinks[c3];
                    assert links3 != null; // Should always be a back reference...

                    final int idx = Arrays.binarySearch(links1, 1, numLinks1, c3);
                    if (idx < 1) continue; // Not found in links1...

                    final long[] r = new long[]{c1, c2, c3};
                    Arrays.sort(r);
                    if (r[0] == r[1] || r[1] == r[2]) continue;
                    result.add(r[0] << 20 | r[1] << SIZE_2 | r[2]);
                }
            }
        }

        return result.size();
    }

    /*
     * Find the password to the LAN party.
     */
    private static String findLanPartyPassword(final int[][] computers) {
        final int[] allComputers = IntStream.range(0, MAX_2)
                .filter(i -> computers[i] != null)
                //.sorted() // _should_ already be sorted???
                .toArray();

        return IntStream.of(findLanParty(computers, allComputers))
                .mapToObj(Day23::toString)
                .collect(Collectors.joining(","));
    }

    /*
     * Find the LAN party by finding the largest cluster of computers.
     */
    private static int[] findLanParty(final int[][] computers, final int[] allComputers) {
        if (allComputers.length == 0) return NO_COMPUTERS;

        int[] result = NO_COMPUTERS;
        for (final int start : allComputers) {
            if (Arrays.binarySearch(result, start) >= 0) continue;

            final int[] links = computers[start];
            final int[] remaining = Arrays.stream(links, 1, links[0] + 1)
                    .filter(link -> Arrays.binarySearch(allComputers, link) >= 0)
                    //.sorted()
                    .toArray();
            // Not enough left to beat the current size?
            if (remaining.length < result.length) continue;

            final int[] cluster = findLanParty(computers, remaining);
            if (cluster.length < result.length) continue; // Not big enough to matter...

            result = new int[cluster.length + 1];
            result[0] = start;
            System.arraycopy(cluster, 0, result, 1, cluster.length);
            Arrays.sort(result);
        }
        return result;
    }

    /*
     * Convert a `String` representation of a computer into an `int`.
     */
    private static int toInt(final String line, final int upper, final int lower) {
        final int c1a = line.charAt(upper) - 'a';
        assert c1a < MAX_1;
        final int c1b = line.charAt(lower) - 'a';
        assert c1b < MAX_1;
        return c1a << SIZE_1 | c1b;
    }

    /*
     * Convert an `int` representation of a computer into a `String`.
     */
    private static String toString(final int c) {
        return new String(new char[]{(char) ('a' + (c >> SIZE_1 & MAX_1)), (char) ('a' + (c & MAX_1))});
    }

}

