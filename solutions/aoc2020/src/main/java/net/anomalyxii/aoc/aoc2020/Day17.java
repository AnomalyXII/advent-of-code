package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 17.
 */
@Solution(year = 2020, day = 17, title = "Conway Cubes")
public class Day17 {

    /*
     * The number of rounds to simulate.
     */
    private static final int NUMBER_OF_ROUNDS = 6;

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
        final char[][] baseLayer = context.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        return run3dSimulation(NUMBER_OF_ROUNDS, baseLayer);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final char[][] baseLayer = context.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        return run4dSimulation(NUMBER_OF_ROUNDS, baseLayer);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Run the simulation against a 3-dimensional space.
     */
    private long run3dSimulation(final int rounds, final char[][] baseLayer) {
        char[][][] allLayers = new char[][][]{baseLayer};

        final AtomicLong activeCubes = new AtomicLong(0);

        // Pre-count active cubes...
        forEachCubeInLayer(baseLayer, (x, y, c) -> activeCubes.addAndGet(c == '#' ? 1 : 0));

        for (int round = 0; round < rounds; round++) {
            final char[][][] mutatedLayers = copyAndGrowLayer(allLayers);

            for (int z = 0; z < mutatedLayers.length; z++) {
                // Pre-grow each layer, for safety...
                mutatedLayers[z] = copyAndGrowLayer(mutatedLayers[z]);
            }

            updateEachCubeInLayer(
                    mutatedLayers, (x, y, z, c) -> {
                        final int surroundingCubes = countSurroundingCubes(mutatedLayers, x, y, z);

                        if (c == '.' && (surroundingCubes == 3)) {
                            // Activating
                            activeCubes.addAndGet(computeCountModifier(z));
                            return '!';
                        }

                        if (c == '#' && (surroundingCubes < 2 || surroundingCubes > 3)) {
                            // Deactivating
                            activeCubes.addAndGet(-computeCountModifier(z));
                            return '~';
                        }

                        return c;
                    }
            );

            updateEachCubeInLayer(
                    mutatedLayers, (x, y, z, c) -> {
                        if (c == '~') return '.';
                        if (c == '!') return '#';
                        return c;
                    }
            );

            allLayers = mutatedLayers;
        }

        return activeCubes.longValue();
    }

    /*
     * Run the simulation against a 4-dimensional space.
     */
    private long run4dSimulation(final int rounds, final char[][] baseLayer) {
        char[][][][] allLayers = new char[][][][]{new char[][][]{baseLayer}};

        final AtomicLong activeCubes = new AtomicLong(0);

        // Pre-count active cubes...
        forEachCubeInLayer(baseLayer, (x, y, c) -> activeCubes.addAndGet(c == '#' ? 1 : 0));

        for (int round = 0; round < rounds; round++) {
            final char[][][][] mutatedLayers = copyAndGrowLayer(allLayers);

            for (int w = 0; w < mutatedLayers.length; w++) {
                mutatedLayers[w] = copyAndGrowLayer(mutatedLayers[w]);
                for (int z = 0; z < mutatedLayers[w].length; z++) {
                    // Pre-grow each layer, for safety...
                    mutatedLayers[w][z] = copyAndGrowLayer(mutatedLayers[w][z]);
                }
            }

            updateEachCubeInLayer(
                    mutatedLayers, (x, y, z, w, c) -> {
                        final int surroundingCubes = countSurroundingCubes(mutatedLayers, x, y, z, w);

                        if (c == '.' && (surroundingCubes == 3)) {
                            // Activating...
                            activeCubes.addAndGet(computeCountModifier(z, w));
                            return '!';
                        }

                        if (c == '#' && (surroundingCubes < 2 || surroundingCubes > 3)) {
                            // Deactivating...
                            activeCubes.addAndGet(-computeCountModifier(z, w));
                            return '~';
                        }

                        return c;
                    }
            );

            updateEachCubeInLayer(
                    mutatedLayers, (x, y, z, w, c) -> {
                        if (c == '~') return '.';
                        if (c == '!') return '#';
                        return c;
                    }
            );


            allLayers = mutatedLayers;
        }

        return activeCubes.longValue();
    }

    /*
     * Compute the number of times a cube is reflected in 4D space, based on
     * the 'w' and 'z' coordinates.
     */
    private static int computeCountModifier(final int z, final int w) {
        return computeCountModifier(z) * computeCountModifier(w);
    }

    /*
     * Compute the number of times a cube is reflected in 3D space, based on
     * the 'z' coordinate.
     */
    private static int computeCountModifier(final int z) {
        return z == 0 ? 1 : 2;
    }

    /*
     * Transform each cube in a 4D space.
     */
    private static void updateEachCubeInLayer(final char[][][][] layers, final CubeFunction4d function) {
        forEachCubeInLayer(layers, (w) -> forEachCubeInLayer(layers[w], (x, y, z, c) -> layers[w][z][y][x] = function.apply(x, y, z, w, c)));
    }

    /*
     * Transform each cube in a 3D space.
     */
    private static void updateEachCubeInLayer(final char[][][] layers, final CubeFunction3d function) {
        forEachCubeInLayer(layers, (z) -> forEachCubeInLayer(layers[z], (x, y, c) -> layers[z][y][x] = function.apply(x, y, z, c)));
    }

    /*
     * Process each cube in a 4D space.
     */
    private static void forEachCubeInLayer(final char[][][][] layers, final LayerConsumer4d consumer) {
        IntStream.range(0, layers.length)
                .forEach(consumer::accept);
    }

    /*
     * Process each 2D layer in a 3D space.
     */
    private static void forEachCubeInLayer(final char[][][] layers, final LayerConsumer3d consumer) {
        IntStream.range(0, layers.length)
                .forEach(consumer::accept);
    }

    /*
     * Process each cube in a 3D space.
     */
    private static void forEachCubeInLayer(final char[][][] layers, final CubeConsumer3d consumer) {
        forEachCubeInLayer(layers, (z) -> forEachCubeInLayer(layers[z], (x, y, c) -> consumer.accept(x, y, z, c)));
    }

    /*
     * Process each cube in a 2D space.
     */
    private static void forEachCubeInLayer(final char[][] layer, final CubeConsumer consumer) {
        for (int y = 0; y < layer.length; y++) {
            for (int x = 0; x < layer[y].length; x++) {
                consumer.accept(x, y, layer[y][x]);
            }
        }
    }

    /*
     * Get the 3D layer for a given 'w' coordinate in 4D space.
     */
    private static char[][][] getLayerAtDepth(final char[][][][] layers, final int w) {
        final int idx = w < 0 ? -w : w; // Mirror z...
        return idx < layers.length ? layers[idx] : null;
    }

    /*
     * Get the 2D layer for a given 'z' coordinate in 3D space.
     */
    private static char[][] getLayerAtDepth(final char[][][] layers, final int z) {
        final int idx = z < 0 ? -z : z; // Mirror z...
        return idx < layers.length ? layers[idx] : null;
    }

    /*
     * Count the number of active cubes surrounding a point in 4D space.
     */
    private int countSurroundingCubes(final char[][][][] layer, final int x, final int y, final int z, final int w) {
        int surroundingCubes = 0;
        for (int dw = -1; dw <= 1; dw++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if ((dx | dy | dz | dw) != 0 && isCubeActive(layer, x + dx, y + dy, z + dz, w + dw)) {
                            ++surroundingCubes;
                        }
                    }
                }
            }
        }
        return surroundingCubes;
    }

    /*
     * Count the number of active cubes surrounding a point in 3D space.
     */
    private int countSurroundingCubes(final char[][][] layers, final int x, final int y, final int z) {
        int surroundingCubes = 0;
        for (int dz = -1; dz <= 1; dz++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if ((dx | dy | dz) != 0 && isCubeActive(layers, x + dx, y + dy, z + dz)) {
                        ++surroundingCubes;
                    }
                }
            }
        }
        return surroundingCubes;
    }

    /*
     * Check if the cube at a given 4d coordinate is active.
     */
    private static boolean isCubeActive(final char[][][][] layers, final int x, final int y, final int z, final int w) {
        final char[][][] layer = getLayerAtDepth(layers, w);
        return layer != null && isCubeActive(layer, x, y, z);
    }

    /*
     * Check if the cube at a given 3d coordinate is active.
     */
    private static boolean isCubeActive(final char[][][] layers, final int x, final int y, final int z) {
        final char[][] layer = getLayerAtDepth(layers, z);
        return layer != null
                && isCubeActive(layer, x, y);
    }

    /*
     * Check if the cube at a given 2d coordinate is active.
     */
    private static boolean isCubeActive(final char[][] layer, final int x, final int y) {
        return (y >= 0 && y < layer.length)
                && (x >= 0 && x < layer[y].length)
                && (layer[y][x] == '#' || layer[y][x] == '~');
    }

    /*
     * Copy and grow a 4D layer.
     */
    private char[][][][] copyAndGrowLayer(final char[][][][] allLayers) {
        final char[][][][] mutatedLayers = new char[allLayers.length + 1][][][];
        System.arraycopy(allLayers, 0, mutatedLayers, 0, allLayers.length);

        final char[][][] lastLayer = allLayers[allLayers.length - 1];
        mutatedLayers[allLayers.length] = newLayer(lastLayer[0][0].length, lastLayer[0].length, lastLayer.length);
        return mutatedLayers;
    }

    /*
     * Copy and grow a 3D layer.
     */
    private char[][][] copyAndGrowLayer(final char[][][] layers) {
        final char[][][] mutatedLayers = new char[layers.length + 1][][];
        System.arraycopy(layers, 0, mutatedLayers, 0, layers.length);

        final char[][] lastLayer = layers[layers.length - 1];
        mutatedLayers[layers.length] = newLayer(lastLayer[0].length, lastLayer.length);
        return mutatedLayers;
    }

    /*
     * Copy and grow a 2D layer.
     */
    private char[][] copyAndGrowLayer(final char[][] layer) {
        final int height = layer.length;
        final int width = layer[0].length;

        final char[][] mutatedLayer = new char[height + 2][];
        for (int y = 0; y < mutatedLayer.length; y++) {
            mutatedLayer[y] = new char[width + 2];
            Arrays.fill(mutatedLayer[y], '.');
            if (y >= 1 && y <= height) {
                System.arraycopy(layer[y - 1], 0, mutatedLayer[y], 1, width);
            }
        }

        return mutatedLayer;
    }

    /*
     * Create a new 3D layer.
     */
    private char[][][] newLayer(final int width, final int height, final int depth) {
        final char[][][] layer = new char[depth][][];
        for (int i = 0; i < depth; i++) {
            layer[i] = newLayer(width, height);
        }
        return layer;
    }

    /*
     * Create a new 2D layer.
     */
    private char[][] newLayer(final int width, final int height) {
        final char[][] layer = new char[height][];
        for (int y = 0; y < height; y++) {
            layer[y] = new char[width];
            Arrays.fill(layer[y], '.');
        }
        return layer;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Consume a 2D slice of 4d space.
     */
    @FunctionalInterface
    private interface LayerConsumer4d {
        void accept(int z);
    }

    /*
     * Consume a 2D slice of 3d space.
     */
    @FunctionalInterface
    private interface LayerConsumer3d {
        void accept(int z);
    }

    /*
     * Transform a point within a 4D slice of space.
     */
    @FunctionalInterface
    private interface CubeFunction4d {
        char apply(int x, int y, int z, int w, char c);
    }

    /*
     * Transform a point within a 3D slice of space.
     */
    @FunctionalInterface
    private interface CubeFunction3d {
        char apply(int x, int y, int z, char c);
    }

    /*
     * Consume a 3D slice of space.
     */
    @FunctionalInterface
    private interface CubeConsumer3d {
        void accept(int x, int y, int z, char c);
    }

    /*
     * Consume a point within a 2D slice of space.
     */
    @FunctionalInterface
    private interface CubeConsumer {
        void accept(int x, int y, char c);
    }

}
