package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.*;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 23.
 */
@Solution(year = 2023, day = 23, title = "A Long Walk")
public class Day23 {

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
        return findLongestPath(context, false);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        return findLongestPath(context, true);
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final Grid map = Grid.parse(context.stream());

        final Coordinate start = IntStream.range(0, map.width())
                .mapToObj(x -> new Coordinate(x, 0))
                .filter(x -> map.get(x) == '.')
                .findFirst()
                .orElseThrow();

        final Coordinate end = IntStream.range(0, map.width())
                .mapToObj(x -> new Coordinate(x, map.height() - 1))
                .filter(x -> map.get(x) == '.')
                .findFirst()
                .orElseThrow();

        return new IntTuple(
                findLongestPath(map, start, end, false),
                findLongestPath(map, start, end, true)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the _longest_ path through the forest.
     */
    private static int findLongestPath(
            final SolutionContext context,
            final boolean ignoreSlopes
    ) {
        final Grid map = Grid.parse(context.stream());

        final Coordinate start = IntStream.range(0, map.width())
                .mapToObj(x -> new Coordinate(x, 0))
                .filter(x -> map.get(x) == '.')
                .findFirst()
                .orElseThrow();

        final Coordinate end = IntStream.range(0, map.width())
                .mapToObj(x -> new Coordinate(x, map.height() - 1))
                .filter(x -> map.get(x) == '.')
                .findFirst()
                .orElseThrow();

        return findLongestPath(map, start, end, ignoreSlopes);
    }

    /*
     * Find the longest path between two points in the forest.
     */
    private static int findLongestPath(
            final Grid map,
            final Coordinate start,
            final Coordinate end,
            final boolean ignoreSlopes
    ) {
        final Map<Coordinate, Junction> junctions = findJunctions(map);
        connectJunctions(map, ignoreSlopes, junctions);

        final Junction startJunc = junctions.get(start);
        final Junction endJunc = junctions.get(end);
        return startJunc.findLongestPathTo(endJunc);
    }

    /*
     * Find all the junctions in the forest.
     */
    private static Map<Coordinate, Junction> findJunctions(final Grid map) {
        final LongSupplier idProvider = new IdSupplier();

        return map.stream()
                .filter(coord -> {
                    if (map.get(coord) == '#') return false;
                    final int exits = map.adjacentTo(coord)
                            .mapToInt(map::get)
                            .filter(type -> type != '#')
                            .map(x -> 1)
                            .sum();

                    // exits == 2 => path
                    // exits == 1 => start / finish

                    return exits != 2;
                })
                .collect(Collectors.toMap(
                        Function.identity(),
                        point -> new Junction(idProvider.getAsLong(), point)
                ));
    }

    /*
     * Traverse the various forest paths to work out which `Junction`s are
     * connected and how many steps it takes to reach them.
     */
    private static void connectJunctions(final Grid map, final boolean ignoreSlopes, final Map<Coordinate, Junction> junctions) {
        junctions.values().forEach(junction -> connectJunction(map, junction, ignoreSlopes, junctions));
    }

    /*
     * Traverse a forest path to work out which `Junction`s are connected to
     * it and how many steps it takes to reach them.
     */
    private static void connectJunction(
            final Grid map,
            final Junction junction,
            final boolean ignoreSlopes,
            final Map<Coordinate, Junction> junctions
    ) {
        Direction.stream()
                .map(startDirection -> walkUntilJunction(map, ignoreSlopes, junctions, junction, startDirection))
                .forEach(path -> {
                    if (path == null) return;

                    final Junction connection = junctions.get(path.removeLast());
                    assert connection != null;
                    junction.addEdge(connection, path.size());
                });
    }

    /*
     * Keep walking along a forest path until reaching a `Junction`.
     */
    private static SequencedSet<Coordinate> walkUntilJunction(
            final Grid map,
            final boolean ignoreSlopes,
            final Map<Coordinate, Junction> junctions,
            final Junction junction,
            final Direction startDirection
    ) {
        final SequencedSet<Coordinate> path = new LinkedHashSet<>();
        path.add(junction.point);

        Coordinate head = junction.point.adjustBy(startDirection);
        if (!map.contains(head) || map.get(head) == '#')
            return null;

        do {
            path.add(head);

            final Direction slop = ignoreSlopes ? null : direction(map.get(head));
            head = walk(map, head, slop, path);

            if (junctions.containsKey(head)) {
                path.add(head);
                return path;
            }
        } while (head != null);

        return null;
    }

    /*
     * Go forward one space.
     */
    private static Coordinate walk(final Grid map, final Coordinate head, final Direction slope, final Set<Coordinate> path) {
        return Direction.stream()
                .filter(direction -> slope == null || slope == direction)
                .map(head::adjustBy)
                .filter(coord -> map.contains(coord) && map.get(coord) != '#' && !path.contains(coord))
                .findFirst()
                .orElse(null);

    }

    /*
     * Map a map symbol to a `Direction`.
     */
    private static Direction direction(final int symbol) {
        return switch (symbol) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '>' -> Direction.RIGHT;
            case '<' -> Direction.LEFT;
            case '.' -> null;
            default -> throw new IllegalStateException("Invalid map symbol: " + symbol);
        };
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represent a crossroads within the forest.
     */
    private static class Junction {

        // Private Members

        private final long id;
        private final Coordinate point;
        private final Map<Junction, Integer> edges = new HashMap<>();

        // Constructors

        Junction(final long id, final Coordinate point) {
            this.id = id;
            this.point = point;
        }

        // Helper Methods

        /*
         * Add a (directed) connection between this and another `Junction`.
         */
        void addEdge(final Junction coordinate, final int length) {
            edges.put(coordinate, length);
        }

        /*
         * Find the longest path from here to the specified target `Junction`.
         */
        int findLongestPathTo(final Junction target) {
            return findLongestPathTo(target, 0);
        }

        // Private Helper Methods

        /*
         * Find the longest path from here to the specified target `Junction`,
         * ensuring that the route does not double back on itself.
         */
        int findLongestPathTo(final Junction endJunc, final long visited) {
            if (this == endJunc) return 0;

            final long newVisited = visited | id;
            return edges.entrySet().stream()
                    .filter(entry -> (newVisited & entry.getKey().id) == 0)
                    .mapToInt(entry -> entry.getKey().findLongestPathTo(endJunc, newVisited) + entry.getValue())
                    .reduce(
                            Integer.MIN_VALUE,
                            Math::max
                    );
        }

    }

    /*
     * Supplier of an ID for a `Junction`.
     */
    private static final class IdSupplier implements LongSupplier {

        // Private Members
        private long next = 1;

        // LongSupplier Methods

        @Override
        public long getAsLong() {
            final long id = next;
            next <<= 1;
            assert next > 0;
            return id;
        }

    }

}

