package net.anomalyxii.aoc.utils.geometry;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a 2D grid of {@link Integer} values.
 */
public class Grid implements TwoDimensionalSpace {

    // ****************************************
    // Private Members
    // ****************************************

    private final int[][] grid;
    private final Area area;

    // ****************************************
    // Constructors
    // ****************************************

    private Grid(final int[][] grid, final Area area) {
        this.grid = grid;
        this.area = area;
    }

    // ****************************************
    // TwoDimensionalSpace Methods
    // ****************************************

    @Override
    public boolean isNull() {
        return area.isNull();
    }

    @Override
    public boolean isInfinity() {
        return area.isInfinity();
    }

    @Override
    public Bounds w() {
        return area.w();
    }

    @Override
    public Bounds h() {
        return area.h();
    }

    @Override
    public Coordinate min() {
        return getArea().min();
    }

    @Override
    public Coordinate max() {
        return getArea().max();
    }

    @Override
    public int width() {
        return area.width();
    }

    @Override
    public int height() {
        return area.height();
    }

    @Override
    public long area() {
        return getArea().area();
    }

    @Override
    public boolean contains(final Coordinate coordinate) {
        return getArea().contains(coordinate);
    }

    @Override
    public TwoDimensionalSpace union(final TwoDimensionalSpace area) {
        // TODO: probably need to extend `Grid` to allow areas that aren't anchored on (0,0)
        throw new UnsupportedOperationException("Need to work out the semantics here...");
    }

    @Override
    public TwoDimensionalSpace intersect(final TwoDimensionalSpace area) {
        // TODO: probably need to extend `Grid` to allow areas that aren't anchored on (0,0)
        throw new UnsupportedOperationException("Need to work out the semantics here...");
    }

    @Override
    public Stream<Coordinate> adjacentTo(final Coordinate coordinate) {
        return getArea().adjacentTo(coordinate);
    }

    @Override
    public void forEach(final Consumer<? super Coordinate> consumer) {
        getArea().forEach(consumer);
    }

    @Override
    public void forEachInterval(final Velocity velocity, final Consumer<Coordinate> consumer) {
        getArea().forEachInterval(velocity, consumer);
    }

    @Override
    public void forEachMatching(final Predicate<Coordinate> test, final Consumer<Coordinate> consumer) {
        getArea().forEachMatching(test, consumer);
    }

    @Override
    public void forEachIntervalMatching(final Velocity velocity, final Predicate<Coordinate> test, final Consumer<Coordinate> consumer) {
        getArea().forEachIntervalMatching(velocity, test, consumer);
    }

    @Override
    public void forEachAdjacentTo(final Coordinate coordinate, final Consumer<Coordinate> consumer) {
        getArea().forEachAdjacentTo(coordinate, consumer);
    }

    @Override
    public void forEachNeighbourOf(final Coordinate coordinate, final Consumer<Coordinate> consumer) {
        getArea().forEachNeighbourOf(coordinate, consumer);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Get the value at a given {@link Coordinate}.
     * <p>
     * This method does not do a bounds check on the {@link Grid}, and so can
     * cause an {@link ArrayIndexOutOfBoundsException exception} to be thrown
     * if a value outside the area is requested.
     *
     * @param coordinate the {@link Coordinate}
     * @return the value
     */
    public int get(final Coordinate coordinate) {
        return grid[coordinate.y()][coordinate.x()];
    }

    /**
     * Check if this {@link Grid} matches the values provided.
     *
     * @param other the values to compare
     * @return {@literal true} if all values match; {@literal false} otherwise
     */
    public boolean matches(final int[][] other) {
        return Arrays.compare(grid, other, Arrays::compare) == 0;
    }

    /**
     * Run an action for each {@link Coordinate} and its associated value in
     * the {@link Grid grid}.
     *
     * @param consumer the action to run for each {@link Coordinate} and value
     */
    public void forEachValue(final ObjIntConsumer<Coordinate> consumer) {
        getArea().forEach(coord -> consumer.accept(coord, get(coord)));
    }

    /**
     * Stream all the {@link Coordinate Coordinates} in this {@link Grid},
     * along with the corresponding value.
     *
     * @return the {@link Stream} of {@link Coordinate} to value {@link Entry Entries}
     */
    public Stream<Entry<Coordinate, Integer>> entries() {
        final Coordinate min = min();
        final Coordinate max = max();
        return IntStream.rangeClosed(min.y(), max.y())
                .mapToObj(y -> IntStream.rangeClosed(min.x(), max.x())
                        .mapToObj(x -> new Coordinate(x, y)))
                .flatMap(s -> s)
                .map(c -> new AbstractMap.SimpleEntry<>(c, grid[c.y()][c.x()]));
    }

    /**
     * Partition this {@link Grid} into slices of the specified dimensions.
     *
     * @param width  the width of the slices
     * @param height the height of the slices
     * @return an array of {@link Grid Grids}
     * @throws IllegalArgumentException if the width or height are invalid
     */
    public Grid[] partition(final int width, final int height) {
        if (height != -1 && grid.length % height != 0)
            throw new IllegalArgumentException("Invalid partition height: " + height + " (h = " + grid.length + ")");
        if (width != -1 && grid[0].length % width != 0)
            throw new IllegalArgumentException("Invalid partition width: " + width + " (w = " + grid[0].length + ")");

        final int resolvedHeight = height == -1 ? grid.length : height;
        final int resolvedWidth = width == -1 ? grid[0].length : width;

        final int ySlices = grid.length / resolvedHeight;
        final int xSlices = grid[0].length / resolvedWidth;

        final Grid[] result = new Grid[ySlices * xSlices];
        for (int dy = 0; dy < ySlices; dy++) {
            for (int dx = 0; dx < xSlices; dx++) {
                final int sliceNum = (dy * xSlices) + dx;

                final int[][] slice = (int[][]) Array.newInstance(int.class, resolvedHeight, resolvedWidth);
                result[sliceNum] = of(slice);
                for (int y = 0; y < resolvedHeight; y++) {
                    if (resolvedWidth >= 0)
                        System.arraycopy(grid[dy * resolvedHeight + y], dx * resolvedWidth, slice[y], 0, width);
                }

            }
        }

        return result;
    }

    /**
     * Calculate a hash code for the content of this {@link Grid}.
     *
     * @return the hash code of this {@link Grid}
     */
    public int calculateHash() {
        return Arrays.deepHashCode(grid);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Get the area of this `Grid`.
     *
     * Overridden, possibly dangerously, by `HorizontallyInfiniteGrid`.
     */
    Area getArea() {
        return area;
    }

    /*
     * Set the value of this `Grid` at the specified `Point`.
     *
     * Shouldn't be used by this class, but provided here to allow
     * `MutableGrid` to expose it.
     */
    private void set(final Coordinate coordinate, final int value) {
        grid[coordinate.y()][coordinate.x()] = value;
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a new {@link Grid} from the provided values.
     *
     * @param grid the raw values
     * @return the {@link Grid}
     */
    public static Grid of(final int[][] grid) {
        return new Grid(grid, calculateArea(grid));
    }

    /**
     * Create a new, {@link MutableGrid} from the provided values.
     *
     * @param grid the raw values
     * @return the {@link Grid}
     */
    public static MutableGrid ofMutable(final int[][] grid) {
        return new MutableGrid(grid, Grid.calculateArea(grid));
    }

    /**
     * Create a new {@link Grid} from the provided values, allowing positions
     * to wrap along the horizontal axis.
     *
     * @param grid the row values
     * @return the {@link Grid}
     */
    public static Grid repeatingHorizontally(final int[][] grid) {
        return new HorizontallyInfiniteGrid(grid, Grid.calculateArea(grid));
    }

    /**
     * Create a new {@link Grid} from the provided values, allowing positions
     * to wrap along both the horizontal and vertical axes.
     *
     * @param grid the row values
     * @return the {@link Grid}
     */
    public static Grid repeating(final int[][] grid) {
        return new InfiniteGrid(grid, Grid.calculateArea(grid));
    }

    /**
     * Create a new, square {@link Grid} of the given dimension.
     *
     * @param widthAndHeight the size of the {@link Grid}
     * @param valueResolver  a {@link ToIntFunction function} that resolves the value for a given {@link Coordinate}
     * @return the {@link Grid}
     */
    public static Grid size(final int widthAndHeight, final ToIntFunction<Coordinate> valueResolver) {
        return Grid.size(widthAndHeight, widthAndHeight, valueResolver);
    }

    /**
     * Create a new {@link Grid} of the given dimensions.
     *
     * @param width         the width of the {@link Grid}
     * @param height        the height of the {@link Grid}
     * @param valueResolver a {@link ToIntFunction function} that resolves the value for a given {@link Coordinate}
     * @return the {@link Grid}
     */
    public static Grid size(final int width, final int height, final ToIntFunction<Coordinate> valueResolver) {
        final int[][] grid = IntStream.range(0, height) // Letters appear to be 6x5?
                .mapToObj(y -> IntStream.range(0, width)
                        .map(x -> valueResolver.applyAsInt(new Coordinate(x, y)))
                        .toArray())
                .toArray(int[][]::new);

        return Grid.of(grid);
    }

    /**
     * Parse a {@link Grid} from a raw {@link InputStream}.
     *
     * @param stream the {@link InputStream} to read from
     * @return the {@link Grid}
     * @throws IOException if an error is encountered whilst reading from the {@link InputStream}
     */
    public static Grid parse(final BufferedInputStream stream) throws IOException {
        final List<int[]> grid = new ArrayList<>();

        int chr;
        int position = 0;

        final int[] row = new int[4096];
        while ((chr = stream.read()) != -1) {
            if (chr == '\n') {
                grid.add(Arrays.copyOf(row, position));
                position = 0;
                continue;
            }
            if (chr == '\r') continue;

            row[position++] = chr;
        }

        if (position > 0)
            grid.add(Arrays.copyOf(row, position));

        return Grid.of(grid.toArray(new int[0][]));
    }

    /**
     * Parse a {@link Grid} from the {@link Stream} of lines.
     *
     * @param lines the lines to parse
     * @return the {@link Grid}
     */
    public static Grid parse(final Stream<String> lines) {
        return parse(lines, Grid::of, chr -> chr);
    }

    /**
     * Parse a {@link Grid} from the {@link Stream} of lines.
     *
     * @param lines         the lines to parse
     * @param valueResolver the {@link IntUnaryOperator function} that resolves the value for each {@link Character}
     * @return the {@link Grid}
     */
    public static Grid parse(final Stream<String> lines, final IntUnaryOperator valueResolver) {
        return parse(lines, Grid::of, valueResolver);
    }

    /**
     * Parse a {@link MutableGrid} from the {@link Stream} of lines.
     *
     * @param lines the lines to parse
     * @return the {@link Grid}
     */
    public static MutableGrid parseMutable(final Stream<String> lines) {
        return parse(lines, Grid::ofMutable, chr -> chr);
    }

    /**
     * Parse a {@link MutableGrid} from the {@link Stream} of lines.
     *
     * @param lines         the lines to parse
     * @param valueResolver the {@link IntUnaryOperator function} that resolves the value for each {@link Character}
     * @return the {@link Grid}
     */
    public static MutableGrid parseMutable(final Stream<String> lines, final IntUnaryOperator valueResolver) {
        return parse(lines, Grid::ofMutable, valueResolver);
    }

    /**
     * Parse a {@link Grid} that repeats horizontally from the {@link Stream}
     * of lines.
     *
     * @param lines the lines to parse
     * @return the {@link Grid}
     */
    public static Grid parseHorizontallyInfinite(final Stream<String> lines) {
        return parse(lines, Grid::repeatingHorizontally, chr -> chr);
    }

    /**
     * Parse a {@link Grid} that repeats horizontally from the {@link Stream}
     * of lines.
     *
     * @param lines the lines to parse
     * @return the {@link Grid}
     */
    public static Grid parseInfinite(final Stream<String> lines) {
        return parse(lines, Grid::repeating, chr -> chr);
    }

    /*
     * Parse the `Stream` of lines into a `Grid`.
     */
    private static <T extends Grid> T parse(final Stream<String> lines, final Function<int[][], T> constructor, final IntUnaryOperator valueResolver) {
        final int[][] grid = lines.map(line -> line.chars()
                        .map(valueResolver)
                        .toArray())
                .toArray(int[][]::new);
        return constructor.apply(grid);
    }

    /*
     * Calculate the underlying `Area` based on the given array.
     */
    private static Area calculateArea(final int[][] grid) {
        return Area.of(Bounds.of(0, grid[0].length - 1), Bounds.of(0, grid.length - 1));
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /**
     * A {@link Grid} that allows modification.
     */
    public static final class MutableGrid extends Grid {

        // Constructors

        public MutableGrid(final int[][] grid, final Area area) {
            super(grid, area);
        }

        // Helper Methods

        /**
         * Set the value of this {@link Grid} at the specified {@link Coordinate}.
         *
         * @param coordinate the {@link Coordinate} to set
         * @param value      the value
         */
        public void set(final Coordinate coordinate, final int value) {
            super.set(coordinate, value);
        }

    }

    /*
     * A `Grid` that repeats horizontally ad infinitum.
     */
    private static final class HorizontallyInfiniteGrid extends Grid {

        // Private Members

        private final Area infiniteArea;

        // Constructors

        HorizontallyInfiniteGrid(final int[][] grid, final Area area) {
            super(grid, area);
            this.infiniteArea = new Area(Bounds.INFINITY, area.h());
        }

        // Grid Methods

        @Override
        Area getArea() {
            return infiniteArea;
        }

        @Override
        public long area() {
            return Long.MAX_VALUE; // Technically infinite, but whatever...
        }

        @Override
        public boolean contains(final Coordinate coordinate) {
            return coordinate.y() >= min().y() && coordinate.y() <= max().y();
        }

        @Override
        public int get(final Coordinate coordinate) {
            if (super.getArea().contains(coordinate)) return super.get(coordinate);
            else return super.get(new Coordinate(coordinate.x() % width(), coordinate.y()));
        }

        @Override
        public boolean matches(final int[][] other) {
            return false;
        }

        @Override
        public Grid[] partition(final int width, final int height) {
            throw new UnsupportedOperationException("Trying to partition an infinite space seems... foolish.");
        }

    }

    /*
     * A `Grid` that repeats horizontally and vertically ad infinitum.
     */
    private static final class InfiniteGrid extends Grid {

        // Private Members

        private final Area infiniteArea;

        // Constructors

        InfiniteGrid(final int[][] grid, final Area area) {
            super(grid, area);
            this.infiniteArea = new Area(Bounds.INFINITY, Bounds.INFINITY);
        }

        // Grid Methods

        @Override
        Area getArea() {
            return infiniteArea;
        }

        @Override
        public long area() {
            return Long.MAX_VALUE; // Technically infinite, but whatever...
        }

        @Override
        public boolean contains(final Coordinate coordinate) {
            return true; // It's infinite, so it contains every point?
        }

        @Override
        public int get(final Coordinate coordinate) {
            if (super.getArea().contains(coordinate)) return super.get(coordinate);


            final int dx = coordinate.x() % width();
            final int dy = coordinate.y() % height();
            return super.get(new Coordinate(dx < 0 ? dx + width() : dx, dy < 0 ? dy + height() : dy));
        }

        @Override
        public boolean matches(final int[][] other) {
            return false;
        }

        @Override
        public Grid[] partition(final int width, final int height) {
            throw new UnsupportedOperationException("Trying to partition an infinite space seems... foolish.");
        }

    }

}
