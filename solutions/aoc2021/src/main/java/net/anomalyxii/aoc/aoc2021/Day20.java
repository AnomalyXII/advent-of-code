package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Area;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 20.
 */
@Solution(year = 2021, day = 20, title = "Trench Map")
public class Day20 {

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
        final List<String> lines = new ArrayList<>(context.read());

        final String algorithm = extractAlgorithm(lines);
        final Set<Coordinate> lightPixels = enhance(algorithm, lines, 2);
        return lightPixels.size();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> lines = new ArrayList<>(context.read());

        final String algorithm = extractAlgorithm(lines);
        final Set<Coordinate> lightPixels = enhance(algorithm, lines, 50);
        return lightPixels.size();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Extract the algorithm from the first line of the input.
     */
    private String extractAlgorithm(final List<String> lines) {
        final String algorithm = lines.removeFirst();
        assert algorithm.length() == 512;
        assert algorithm.matches("[.#]+");

        final String blankLine = lines.removeFirst();
        assert blankLine.isBlank(); // Skip blank line...
        return algorithm;
    }

    /*
     * Enhance the given "image" a number of times.
     */
    private Set<Coordinate> enhance(final String algorithm, final List<String> image, final int iterations) {
        final int height = image.size();
        final int width = image.getFirst().length();

        final boolean[] cache = new boolean[algorithm.length()];
        for (int i = 0; i < algorithm.length(); i++)
            cache[i] = algorithm.charAt(i) == '#';

        final Set<Coordinate> lightPixels = new HashSet<>();
        for (int y = 0; y < height; y++) {
            final String line = image.get(y);
            for (int x = 0; x < width; x++) {
                assert line.length() == width;
                assert line.matches("[.#]+");

                if (line.charAt(x) == '#')
                    lightPixels.add(new Coordinate(x, y));
            }
        }

        final boolean[] borderIsWhite = new boolean[]{false};
        final ToIntFunction<Coordinate> inArea = p -> lightPixels.contains(p) ? 1 : 0;
        final ToIntFunction<Coordinate> inBorder = p -> borderIsWhite[0] ? 1 : 0;

        final Area area = Area.ofOrigin(width - 1, height - 1);
        for (int i = 0; i < iterations; i++) {
            final Set<Coordinate> newLightPixels = new HashSet<>();

            final Area currentArea = area.grow(i);
            currentArea.grow(1).forEach(p -> {
                final int value = p.neighboursAndSelf()
                        .mapToInt(n -> currentArea.contains(n)
                                ? inArea.applyAsInt(n)
                                : inBorder.applyAsInt(n))
                        .reduce(0, (result, val) -> (result << 1) | val);

                if (cache[value]) newLightPixels.add(p);
            });

            if (!borderIsWhite[0]) borderIsWhite[0] = cache[0];
            else borderIsWhite[0] = cache[511];

            lightPixels.clear();
            lightPixels.addAll(newLightPixels);
        }

        assert !borderIsWhite[0]; // If border is white, we have an infinite number of pixels...
        return lightPixels;
    }

}
