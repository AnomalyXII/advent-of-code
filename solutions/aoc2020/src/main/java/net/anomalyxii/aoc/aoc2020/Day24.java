package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 24.
 */
@Solution(year = 2020, day = 24, title = "Lobby Layout")
public class Day24 {

    private static final Pattern DIRECTIONS = Pattern.compile("ne|e|se|sw|w|nw");

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
        final Map<Tile, Boolean> tiles = new HashMap<>();
        context.consume(line -> {
            final Tile e = calculateTileCoordinate(line);
            tiles.put(e, !tiles.getOrDefault(e, false));
        });

        return countBlackTiles(tiles);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Map<Tile, Boolean> tiles = new HashMap<>();
        context.consume(line -> {
            final Tile e = calculateTileCoordinate(line);
            tiles.put(e, !tiles.getOrDefault(e, false));
        });

        for (int i = 0; i < 100; i++) {
            // Create a Set of _just_ black tiles:
            final Set<Tile> blackTiles = streamBlackTiles(tiles)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            // Since we don't care about white tiles that don't have a border with
            //   a black tile, we can just ignore anything not immediately
            //   surrounding a black tile?
            final Set<Tile> interestingTiles = blackTiles.stream()
                    .flatMap(tile -> Stream.of(
                            tile, // Centre,
                            tile.calc(Direction.NORTH_EAST),
                            tile.calc(Direction.EAST),
                            tile.calc(Direction.SOUTH_EAST),
                            tile.calc(Direction.SOUTH_WEST),
                            tile.calc(Direction.WEST),
                            tile.calc(Direction.NORTH_WEST)
                    ))
                    .peek(tile -> tiles.computeIfAbsent(tile, x -> false)) // Sneaky sneaky
                    .collect(Collectors.toSet());

            final Map<Tile, Boolean> updatedTiles = new HashMap<>();
            interestingTiles.forEach(tile -> {
                final boolean wasBlack = tiles.get(tile); // Tile should exist by now, so we can NPE if it doesn't!
                final long surroundingBlackTiles = Arrays.stream(Direction.values())
                        .filter(d -> tiles.getOrDefault(tile.calc(d), false))
                        .count();

                final boolean isNowBlack
                        // Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
                        = (wasBlack && !(surroundingBlackTiles == 0 || surroundingBlackTiles > 2))
                        // Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
                        || (!wasBlack && surroundingBlackTiles == 2);

                if (wasBlack != isNowBlack) {
                    updatedTiles.put(tile, isNowBlack);
                }
            });

            tiles.putAll(updatedTiles);
        }

        return countBlackTiles(tiles);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the coordinate of a tile, relative to the centre (0, 0).
     */
    private static Tile calculateTileCoordinate(final String line) {
        final Matcher m = DIRECTIONS.matcher(line);

        int north = 0;
        int east = 0;
        while (m.find()) {
            final Direction d = Direction.fromShortStr(m.group(0));
            north = d.modifyNorth(north);
            east = d.modifyEast(east);
        }

        return new Tile(north, east);
    }

    /*
     * Stream all the black tiles.
     */
    private static Stream<Map.Entry<Tile, Boolean>> streamBlackTiles(final Map<Tile, Boolean> tiles) {
        return tiles.entrySet().stream()
                .filter(Map.Entry::getValue);
    }

    /*
     * Count all the black tiles.
     */
    private static long countBlackTiles(final Map<Tile, Boolean> tiles) {
        return streamBlackTiles(tiles).count();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A direction that a hexagon can border another.
     */
    private enum Direction {

        NORTH_EAST("ne", 1, 1),
        EAST("e", 0, 2),
        SOUTH_EAST("se", -1, 1),
        SOUTH_WEST("sw", -1, -1),
        WEST("w", 0, -2),
        NORTH_WEST("nw", 1, -1),

        // end of constants
        ;

        private final String str;
        private final int northMod;
        private final int eastMod;

        // Constructors

        Direction(final String str, final int northMod, final int eastMod) {
            this.str = str;
            this.northMod = northMod;
            this.eastMod = eastMod;
        }

        // Helper Methods

        public int modifyNorth(final int north) {
            return north + northMod;
        }

        public int modifyEast(final int east) {
            return east + eastMod;
        }

        /*
         * Look up the Direction based on the short string.
         */
        static Direction fromShortStr(final String str) {
            return Stream.of(values())
                    .filter(d -> d.str.equals(str))
                    .findAny()
                    .orElseThrow();
        }
    }

    /*
     * A tile, represented as the coordinate of the tile relative to the
     * centre tile (0,0).
     */
    private record Tile(int north, int east) {

        // Helper Methods

        Tile calc(final Direction direction) {
            return new Tile(direction.modifyNorth(north), direction.modifyEast(east));
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Tile tile = (Tile) o;
            return north == tile.north && east == tile.east;
        }

        @Override
        public int hashCode() {
            return Objects.hash(north, east);
        }

    }

}
