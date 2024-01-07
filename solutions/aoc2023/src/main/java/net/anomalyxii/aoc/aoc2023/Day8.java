package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.maths.Factors;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 8.
 */
@Solution(year = 2023, day = 8, title = "Haunted Wasteland")
public class Day8 {

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
        final Maps maps = Maps.parse(context);
        return maps.calculateDistanceToExit();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Maps maps = Maps.parse(context);
        return maps.calculateDistanceToAllExits();
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
        final Maps maps = Maps.parse(context);
        return new LongTuple(
                maps.calculateDistanceToExit(),
                maps.calculateDistanceToAllExits()
        );
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A collection of maps and directions for traversing them.
     */
    private record Maps(char[] directions, Map<String, String[]> maps) {

        private static final String GENERIC_ENTRANCE = "AAA";
        private static final String GENERIC_EXIT = "ZZZ";

        // Helper Methods

        /*
         * Calculate the distance from the entrance (AAA) to the exit (ZZZ).
         */
        long calculateDistanceToExit() {
            return calculateDistance(GENERIC_ENTRANCE, GENERIC_EXIT::equals);
        }

        /*
         * Calculate the convergence of every entrance (??A) to every exit (??Z).
         */
        public long calculateDistanceToAllExits() {
            final String[] locations = maps.keySet().stream()
                    .filter(Maps::isEntrance)
                    .toArray(String[]::new);

            final int[] results = stream(locations)
                    .mapToInt(start -> calculateDistance(start, Maps::isExit))
                    .toArray();

            return stream(results)
                    .mapToLong(r -> (long) r)
                    .reduce(Factors::lowestCommonMultiple)
                    .orElse(0L);
        }

        /*
         * Calculate the distance from an arbitrary start point.
         */
        private int calculateDistance(final String start, final Predicate<String> endTest) {
            int count = 0;
            String location = start;
            do {
                location = map(count++, location);
            } while (!endTest.test(location));
            return count;
        }

        /*
         * Map from one location to another, based on the distance travelled.
         */
        private String map(final int distance, final String location) {
            final char side = directions[distance % directions.length];
            final String[] map = maps.get(location);
            return (side == 'L' ? map[0] : map[1]);
        }

        // Static Helper Methods

        /*
         * Parse the maps.
         */
        static Maps parse(final SolutionContext context) {
            final List<List<String>> parts = context.readBatches();
            final char[] directions = parts.getFirst().getFirst().toCharArray();

            final Map<String, String[]> maps = parts.getLast().stream()
                    .map(line -> line.split("\\s*=\\s*"))
                    .collect(Collectors.toMap(
                            map -> map[0].trim(),
                            map -> map[1].substring(1, map[1].length() - 1).split(",\\s*")
                    ));

            return new Maps(directions, maps);
        }

        /*
         * Check if a location is an entrance (??A).
         */
        private static boolean isEntrance(final String key) {
            return key.endsWith("A");
        }

        /*
         * Check if a location is an exit (??Z).
         */
        private static boolean isExit(final String location) {
            return location.endsWith("Z");
        }

    }

}

