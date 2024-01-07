package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 20.
 */
@Solution(year = 2020, day = 20, title = "Jurassic Jigsaw")
public class Day20 {

    private static final char NOISE_CHAR = '#';
    private static final char MONSTER_CHAR = 'O';

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
        final Map<String, TileContainer> tiles = readTiles(context);

        final List<List<Tile>> image = scanImage(tiles);

        final List<Tile> firstRow = image.getFirst();
        final List<Tile> lastRow = image.getLast();

        return firstRow.getFirst().id
                * lastRow.getFirst().id
                * firstRow.getLast().id
                * lastRow.getLast().id;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Map<String, TileContainer> tiles = readTiles(context);

        // Start with a corner tile and rotate it so that it has
        //   tiles bordering it to the left and south:

        final List<List<Tile>> image = scanImage(tiles);

        final int imgWidth = image.getFirst().size();
        final int imgHeight = image.size();

        final int tileWidth = image.getFirst().getFirst().width - 2;
        final int tileHeight = image.getFirst().getFirst().height - 2;

        char[][] rasterisedImage = new char[imgHeight * tileHeight][];
        for (int iy = 0; iy < imgHeight; iy++) {
            final List<Tile> iRow = image.get(iy);
            for (int ty = 0; ty < tileHeight; ty++) {
                final int y = iy * tileHeight + ty;
                rasterisedImage[y] = new char[imgWidth * tileWidth];
                for (int ix = 0; ix < imgWidth; ix++) {
                    for (int tx = 0; tx < tileWidth; tx++) {
                        final int x = ix * tileWidth + tx;
                        rasterisedImage[y][x] = iRow.get(ix).tile[ty + 1][tx + 1];
                    }
                }
            }
        }

        // Now we can _finally_ look for the sea monster...
        // ----------------------
        // |                  # |
        // |#    ##    ##    ###|
        // | #  #  #  #  #  #   |
        // ----------------------

        for (int r = 0; r < 4; r++) {
            boolean found = findAndEnhanceSeaMonster(rasterisedImage);
            if (found) break;

            // flip the image and look some more
            rasterisedImage = flip(rasterisedImage, true, false);
            found = findAndEnhanceSeaMonster(rasterisedImage);
            if (found) break;
            // flip a different way the image and look some more
            rasterisedImage = flip(rasterisedImage, false, true);
            found = findAndEnhanceSeaMonster(rasterisedImage);
            if (found) break;
            // flip the image another way and look even more
            rasterisedImage = flip(rasterisedImage, true, false);
            found = findAndEnhanceSeaMonster(rasterisedImage);
            if (found) break;

            // Rotate the image and go around again...
            rasterisedImage = flip(rasterisedImage, false, true); // restore first...
            rasterisedImage = rotate(rasterisedImage, 90);
        }

        return countNoiseInImage(rasterisedImage);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Find the {@link Set} of {@link Edge Borders} that match between the
     * two given {@link Tile Tiles}.
     *
     * @param tile  the {@link Tile} to check
     * @param other the {@link Tile} to compare to
     * @return a {@link Set} of {@link Edge Borders} that match
     */
    static Set<Edge> findMatchingBorders(final Tile tile, final Tile other) {
        final Set<Edge> borders = EnumSet.noneOf(Edge.class);
        if (canBorderMatchWithTile(tile.top, other) != EdgeMatchType.NO_MATCH) {
            borders.add(Edge.TOP);
        }
        if (canBorderMatchWithTile(tile.bottom, other) != EdgeMatchType.NO_MATCH) {
            borders.add(Edge.BOTTOM);
        }
        if (canBorderMatchWithTile(tile.left, other) != EdgeMatchType.NO_MATCH) {
            borders.add(Edge.LEFT);
        }
        if (canBorderMatchWithTile(tile.right, other) != EdgeMatchType.NO_MATCH) {
            borders.add(Edge.RIGHT);
        }
        return borders;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Read in the tiles.
     */
    private Map<String, TileContainer> readTiles(final SolutionContext context) {
        final String[] lastTileId = new String[1];
        final Map<String, List<String>> rawTiles = new HashMap<>();
        // Reset
        context.consume(line -> {
            if (line.isBlank()) {
                // Reset
                lastTileId[0] = null;
                return;
            }

            if (line.startsWith("Tile ")) {
                lastTileId[0] = line.substring(0, line.length() - 1);
                return;
            }

            if (lastTileId[0] != null) {
                rawTiles.computeIfAbsent(lastTileId[0], x -> new ArrayList<>()).add(line);
            }
        });

        final Set<Tile> tiles = rawTiles.entrySet().stream()
                .map(entry -> Tile.createTile(entry.getKey(), entry.getValue().stream().map(String::toCharArray).toArray(char[][]::new)))
                .collect(Collectors.toSet());


        // Lazily populate the adjoining containers otherwise things just get complicated
        final Map<String, TileContainer> containers = tiles.stream()
                .map(tile -> new TileContainer(tile, new EnumMap<>(Edge.class)))
                .collect(Collectors.toMap(tile -> tile.tile.name, Function.identity()));

        tiles.forEach(tile -> {
            final TileContainer container = containers.get(tile.name);
            tiles.stream()
                    .filter(other -> tile != other) // Ignore ourselves!
                    .forEach(other -> {
                        final TileContainer otherContainer = containers.get(other.name);
                        final Set<Edge> borders = findMatchingBorders(tile, other);
                        borders.forEach(border -> {
                            final TileContainer replaced = container.borderingTiles.put(border, otherContainer);
                            if (replaced != null) {
                                throw new AssertionError("Replaced a bordering tile: this should not happen!");
                            }
                        });
                    });
        });

        return containers;
    }

    /*
     * Scan all the tiles to determine the correct layout of all the tiles.
     */
    private List<List<Tile>> scanImage(final Map<String, TileContainer> tiles) {
        final TileContainer corner = tiles.values().stream()
                .filter(TileContainer::isCornerTile)
                .findAny()
                .orElseThrow(AssertionError::new);

        final boolean flipX = !corner.borderingTiles.containsKey(Edge.RIGHT);
        final boolean flipY = !corner.borderingTiles.containsKey(Edge.BOTTOM);

        // Create a copy of the corner tile and remove the original...
        final char[][] copy = flip(corner.tile.tile, flipX, flipY);
        final Tile copyTile = Tile.createTile(corner.tile.name, copy);
        tiles.remove(copyTile.name);

        return scanImageFromCornerTile(tiles, copyTile);
    }

    /*
     * Scan all the tiles to create an image that starts with the specified tile.
     */
    private List<List<Tile>> scanImageFromCornerTile(final Map<String, TileContainer> tiles, final Tile start) {
        // Represent the image as a 2D List of tiles
        final List<List<Tile>> image = new ArrayList<>();

        Tile previous = start;
        do {
            final List<Tile> row = scanRowFromBorderTile(tiles, previous);
            image.add(row);

            // Reached the end of the row...
            //   if there are still tiles remaining, we'll need to
            //   work out what the start of the next row should be...
            final EdgeMatchResult result = findMatchForEdge(tiles, previous.bottom);
            if (result == null) {
                break;
            }

            final char[][] copy2 = rotateToMatchBottomEdge(result);
            final Tile copyTile2 = Tile.createTile(result.match.tile.name, copy2);
            if (!doBordersMatch(previous.bottom, copyTile2.top)) {
                throw new AssertionError("Got our alignment wrong for match type " + result.matchType);
            }

            tiles.remove(result.match.tile.name);

            previous = copyTile2;
        } while (!tiles.isEmpty());
        return image;
    }

    /*
     * Scan all the tiles to create a row that starts with the specified tile.
     */
    private List<Tile> scanRowFromBorderTile(final Map<String, TileContainer> tiles, final Tile start) {
        final List<Tile> row = new ArrayList<>();
        row.add(start);

        Tile previous = start;
        do {
            // Get the right-hand border of the previous tile
            final EdgeMatchResult result = findMatchForEdge(tiles, previous.right);

            // If there are no tiles, then we've reached the end of this row!
            if (result == null) {
                break;
            }

            final char[][] copy = rotateToMatchRightEdge(result);
            final Tile copyTile = Tile.createTile(result.match.tile.name, copy);
            if (!doBordersMatch(previous.right, copyTile.left)) {
                throw new AssertionError("Got our alignment wrong for match type " + result.matchType);
            }

            tiles.remove(result.match.tile.name);
            row.add(copyTile);

            previous = copyTile;
        } while (!tiles.isEmpty());

        return row;
    }

    /*
     * Try and find the sea monster, and make it stand out.
     */
    private static boolean findAndEnhanceSeaMonster(final char[][] rasterisedImage) {
        boolean anyFound = false;

        // We know that the monster can't be in the bottom two rows of the image...
        for (int y = 0; y < rasterisedImage.length - 2; y++) {
            // We know that the monster can't be under 19 "pixels" from the edge...
            for (int x = 19; x < rasterisedImage[y].length - 1; x++) {
                final char c = rasterisedImage[y][x];

                final boolean foundHead = c == NOISE_CHAR
                        && rasterisedImage[y + 1][x - 1] == NOISE_CHAR
                        && rasterisedImage[y + 1][x] == NOISE_CHAR
                        && rasterisedImage[y + 1][x + 1] == NOISE_CHAR;

                final boolean foundBody = rasterisedImage[y + 1][x - 18] == NOISE_CHAR
                        && rasterisedImage[y + 1][x - 13] == NOISE_CHAR
                        && rasterisedImage[y + 1][x - 12] == NOISE_CHAR
                        && rasterisedImage[y + 1][x - 7] == NOISE_CHAR
                        && rasterisedImage[y + 1][x - 6] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 17] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 14] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 11] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 8] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 5] == NOISE_CHAR
                        && rasterisedImage[y + 2][x - 2] == NOISE_CHAR;

                if (foundHead && foundBody) {
                    anyFound = true;

                    // Head
                    rasterisedImage[y][x] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x - 1] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x + 1] = MONSTER_CHAR;

                    // Body
                    rasterisedImage[y + 1][x - 18] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x - 13] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x - 12] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x - 7] = MONSTER_CHAR;
                    rasterisedImage[y + 1][x - 6] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 17] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 14] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 11] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 8] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 5] = MONSTER_CHAR;
                    rasterisedImage[y + 2][x - 2] = MONSTER_CHAR;
                }
            }
        }

        return anyFound;
    }

    /*
     * Rotate the match result so that the tile will match against the
     * right-hand edge.
     */
    private char[][] rotateToMatchRightEdge(final EdgeMatchResult result) {
        return switch (result.matchType) {
            case TOP ->
                // Make it match on the left by rotating -90 degrees and flipping
                    flip(rotate(result.match.tile.tile, 270), false, true);
            case TOP_FLIPPED ->
                // Make it match on the left by rotating -90 degrees
                    rotate(result.match.tile.tile, 270);
            case LEFT ->
                // Perfect match!
                    result.match.tile.tile;
            case LEFT_FLIPPED ->
                // Almost a perfect match, make it match by flipping
                    flip(result.match.tile.tile, false, true);
            case BOTTOM ->
                // Make it match on the left by rotating 90 degrees
                    rotate(result.match.tile.tile, 90);
            case BOTTOM_FLIPPED ->
                // Make it match on the left by rotating 90 degrees and flipping
                    flip(rotate(result.match.tile.tile, 90), false, true);
            case RIGHT ->
                // Make it match on the left by flipping
                    flip(result.match.tile.tile, true, false);
            case RIGHT_FLIPPED ->
                // Make it match on the left by rotating 180 degrees
                    rotate(result.match.tile.tile, 180);
            default -> throw new AssertionError("Can not happen!");
        };
    }

    /*
     * Rotate the match result so that the tile will match against the
     * bottom edge.
     */
    private char[][] rotateToMatchBottomEdge(final EdgeMatchResult result) {
        return switch (result.matchType) {
            case TOP ->
                // Perfect match!
                    result.match.tile.tile;
            case TOP_FLIPPED ->
                // Almost a perfect match, make it match by flipping
                    flip(result.match.tile.tile, true, false);
            case LEFT ->
                // Make it match on the top by rotating 90 degrees and flipping
                    flip(rotate(result.match.tile.tile, 90), true, false);
            case LEFT_FLIPPED ->
                // Make it match on the top by rotating 90 degrees
                    rotate(result.match.tile.tile, 90);
            case BOTTOM ->
                // Make it match on the top by flipping
                    flip(result.match.tile.tile, false, true);
            case BOTTOM_FLIPPED ->
                // Make it match on the top by rotating 180 degrees
                    rotate(result.match.tile.tile, 180);
            case RIGHT ->
                // Make it match on the top by rotating -90 degrees
                    rotate(result.match.tile.tile, 270);
            case RIGHT_FLIPPED ->
                // Make it match on the left by rotating -90 degrees and flipping
                    flip(rotate(result.match.tile.tile, 270), true, false);
            default -> throw new AssertionError("Can not happen!");
        };
    }

    /*
     * Find a tile that matches the specified edge.
     */
    private EdgeMatchResult findMatchForEdge(final Map<String, TileContainer> tiles, final char[] edgeToMatch) {
        // Find a Tile that matches that edge
        EdgeMatchResult result = null;
        for (final TileContainer candidate : tiles.values()) {
            final EdgeMatchType match = canBorderMatchWithTile(edgeToMatch, candidate.tile);
            if (match != EdgeMatchType.NO_MATCH) {
                result = new EdgeMatchResult(candidate, match);
                break;
            }
        }
        return result;
    }

    /*
     * Check if the given `Tile` can match against the specified edge.
     */
    private static EdgeMatchType canBorderMatchWithTile(final char[] edge, final Tile other) {
        if (edge.length == other.top.length) {
            if (doBordersMatch(edge, other.top)) {
                return EdgeMatchType.TOP;
            }
            if (doBordersMatch(edge, flipEdge(other.top))) {
                return EdgeMatchType.TOP_FLIPPED;
            }
        }

        if (edge.length == other.left.length) {
            if (doBordersMatch(edge, other.left)) {
                return EdgeMatchType.LEFT;
            }
            if (doBordersMatch(edge, flipEdge(other.left))) {
                return EdgeMatchType.LEFT_FLIPPED;
            }
        }

        if (edge.length == other.bottom.length) {
            if (doBordersMatch(edge, other.bottom)) {
                return EdgeMatchType.BOTTOM;
            }
            if (doBordersMatch(edge, flipEdge(other.bottom))) {
                return EdgeMatchType.BOTTOM_FLIPPED;
            }
        }

        if (edge.length == other.right.length) {
            if (doBordersMatch(edge, other.right)) {
                return EdgeMatchType.RIGHT;
            }
            if (doBordersMatch(edge, flipEdge(other.right))) {
                return EdgeMatchType.RIGHT_FLIPPED;
            }
        }

        return EdgeMatchType.NO_MATCH;
    }

    /*
     * Flip a tile, either on the x-axis, y-axis or both.
     */
    private static char[][] flip(final char[][] tile, final boolean flipX, final boolean flipY) {
        final char[][] flipped = new char[tile.length][];
        for (int y = 0; y < flipped.length; y++) {
            final char[] row = tile[flipY ? (tile.length - 1) - y : y];

            flipped[y] = new char[row.length];
            for (int x = 0; x < row.length; x++) {
                flipped[y][x] = row[flipX ? (row.length - 1) - x : x];
            }
        }

        return flipped;
    }

    /*
     * Rotate a tile by 0, 90, 180 or 270 degrees.
     */
    private static char[][] rotate(final char[][] tile, final int degree) {
        if (degree == 0) return flip(tile, false, false);
        if (degree == 180) return flip(tile, true, true);
        if (degree == 270) return rotate(flip(tile, true, true), 90);

        if (degree == 90) {
            final int width = tile[0].length;
            final int height = tile.length;

            // Pre-populate the rows/columns 'cause ugh
            final char[][] rotated = new char[width][];
            for (int y = 0; y < width; y++) {
                rotated[y] = new char[height];
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotated[x][(width - 1) - y] = tile[y][x];
                }
            }
            return rotated;
        }

        throw new IllegalArgumentException("Invalid rotation angle: '" + degree + "'");
    }

    /*
     * Flip a border.
     */
    private static char[] flipEdge(final char[] border) {
        final char[] flipped = new char[border.length];
        IntStream.range(0, border.length)
                .forEach(i -> flipped[(border.length - 1) - i] = border[i]);
        return flipped;
    }

    /*
     * Test if two borders match.
     */
    private static boolean doBordersMatch(final char[] border, final char[] other) {
        if (border.length != other.length) {
            return false;
        }

        for (int i = 0; i < border.length; i++) {
            if (border[i] != other[i]) {
                return false;
            }
        }

        return true;
    }

    /*
     * Scan a rasterised image, counting the amount of noise in it.
     */
    private static long countNoiseInImage(final char[][] rasterisedImage) {
        long noise = 0;
        for (final char[] chars : rasterisedImage) {
            for (final char c : chars) {
                if (c == NOISE_CHAR) {
                    noise++;
                }
            }
        }
        return noise;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /**
     * The side of a tile on which an edge matched.
     */
    enum Edge {

        TOP,
        LEFT,
        BOTTOM,
        RIGHT,

        // End of constants
        ;

    }

    /**
     * Represents a Tile.
     */
    static final class Tile {

        // Private Members

        private final long id;
        private final String name;
        private final char[][] tile;
        private final int width;
        private final int height;

        private final char[] top;
        private final char[] left;
        private final char[] bottom;
        private final char[] right;

        // Constructors

        private Tile(
                final String name,
                final int width,
                final int height,
                final char[][] tile,
                final char[] top,
                final char[] bottom,
                final char[] left,
                final char[] right
        ) {
            this.id = Integer.parseInt(name.substring(5));

            this.name = name;
            this.tile = tile;
            this.width = width;
            this.height = height;

            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return name;
        }

        static Tile createTile(final String name, final char[][] tile) {
            final int width = tile[0].length;
            final int height = tile.length;

            final char[] top = new char[width];
            copy(top, IntStream.range(0, width).mapToObj(i -> tile[0][i]));

            final char[] bottom = new char[width];
            copy(bottom, IntStream.range(0, width).mapToObj(i -> tile[height - 1][i]));

            final char[] left = new char[height];
            copy(left, IntStream.range(0, height).mapToObj(i -> tile[i][0]));

            final char[] right = new char[height];
            copy(right, IntStream.range(0, height).mapToObj(i -> tile[i][width - 1]));

            return new Tile(name, width, height, tile, top, bottom, left, right);
        }

        static void copy(final char[] copy, final Stream<Character> border) {
            final int[] i = {0};
            border.forEach(c -> copy[i[0]++] = c);
        }

    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Describes how an `Edge` matched.
     */
    private enum EdgeMatchType {

        TOP,
        TOP_FLIPPED,
        LEFT,
        LEFT_FLIPPED,
        BOTTOM,
        BOTTOM_FLIPPED,
        RIGHT,
        RIGHT_FLIPPED,

        NO_MATCH,

        // End of constants
        ;

    }

    /*
     * The result of matching a `Tile` against a given `Edge`.
     */
    private record EdgeMatchResult(TileContainer match, EdgeMatchType matchType) {
    }

    /*
     * Holds a `Tile` and some extra meta-information.
     */
    private record TileContainer(Tile tile, Map<Edge, TileContainer> borderingTiles) {

        // Helper Methods

        public boolean isCornerTile() {
            return borderingTiles.size() < 3;
        }

        // To String

        @Override
        public String toString() {
            return "[" + tile.name + "]";
        }

    }

}
