package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.function.Function;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 12.
 */
@Solution(year = 2021, day = 12, title = "Passage Pathing")
public class Day12 {

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
        return countPaths(context, false);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return countPaths(context, true);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Count the paths out of the cave system.
     */
    private long countPaths(final SolutionContext context, final boolean canVisitTwice) {
        final Map<String, Cave> caves = parseCaves(context);

        final Cave startCave = caves.get("start");
        final Cave endCave = caves.get("end");

        return countPaths(startCave, endCave, new LinkedList<>(), canVisitTwice);
    }

    /*
     * Count the paths out of the cave system.
     */
    private long countPaths(final Cave start, final Cave end, final Deque<Cave> visited, final boolean canVisitTwice) {
        if (start == end)
            return 1;

        visited.addLast(start);
        long count = 0;
        for (final Cave next : start.exits) {
            final boolean notAlreadyVisited = next.isLargeCave || !visited.contains(next);
            final boolean canActuallyVisitTwice = (canVisitTwice && !"start".equals(next.name) && !"end".equals(next.name));
            if (notAlreadyVisited || canActuallyVisitTwice)
                count += countPaths(next, end, visited, (canVisitTwice && notAlreadyVisited));
        }
        visited.removeLast();

        return count;
    }

    /*
     * Parse the cave system from the input file.
     */
    private Map<String, Cave> parseCaves(final SolutionContext context) {
        final Map<String, Cave> caves = new HashMap<>();
        context.consume(line -> {
            final String[] parts = line.split("\\s*-\\s*", 2);

            final Function<String, Cave> getOrCreateCave = name ->
                    caves.computeIfAbsent(name, k -> new Cave(name, new ArrayList<>(), name.matches("[A-Z]+")));

            final Cave start = getOrCreateCave.apply(parts[0]);
            final Cave end = getOrCreateCave.apply(parts[1]);
            start.exits.add(end);
            end.exits.add(start);
        });

        return caves;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A cave.
     */
    private record Cave(String name, List<Cave> exits, boolean isLargeCave) {
    }

}
