package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 13.
 */
@Solution(year = 2023, day = 13, title = "Point of Incidence")
public class Day13 {

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
        return context.streamBatches()
                .mapToInt(this::calculateReflectionScore)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        return context.streamBatches()
                .mapToInt(this::calculateReflectionWithSmudgeScore)
                .sum();
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
        return context.streamBatches()
                .reduce(
                        IntTuple.NULL,
                        (tup, line) -> tup.add(
                                calculateReflectionScore(line),
                                calculateReflectionWithSmudgeScore(line)
                        ),
                        IntTuple::add
                );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the "score" of a reflection.
     */
    private int calculateReflectionScore(final List<String> image) {

        final OptionalInt horizontalReflection = findHorizontalReflection(image);
        if (horizontalReflection.isPresent())
            return 100 * (horizontalReflection.getAsInt() + 1);

        final OptionalInt verticalReflection = findVerticalReflection(image);
        if (verticalReflection.isPresent())
            return verticalReflection.getAsInt() + 1;

        throw new IllegalStateException("No reflection found :(");
    }

    /*
     * Calculate the "score" of a reflection that has a single smudge on it.
     */
    private int calculateReflectionWithSmudgeScore(final List<String> image) {
        final OptionalInt horizontalReflection = findHorizontalReflectionWithSmudge(image);
        if (horizontalReflection.isPresent())
            return 100 * (horizontalReflection.getAsInt() + 1);

        final OptionalInt verticalReflection = findVerticalReflectionWithSmudge(image);
        if (verticalReflection.isPresent())
            return verticalReflection.getAsInt() + 1;

        throw new IllegalStateException("No reflection found :(");
    }

    /*
     * Find a horizontal reflection point.
     */
    private OptionalInt findHorizontalReflection(final List<String> image) {

        int border = -1;
        outer:
        for (int r = 0; r < image.size() - 1; r++) {

            final String row = image.get(r);
            if (row.equals(image.get(r + 1))) {
                for (int rt = r, rb = r + 1; rt >= 0 && rb < image.size(); rt--, rb++) {
                    if (!image.get(rt).equals(image.get(rb)))
                        continue outer;
                }

                border = r;
            }

        }

        return border == -1 ? OptionalInt.empty() : OptionalInt.of(border);

    }

    /*
     * Find a horizontal reflection point accounting for a single smudge.
     */
    private OptionalInt findHorizontalReflectionWithSmudge(final List<String> image) {

        int border = -1;
        outer:
        for (int r = 0; r < image.size() - 1; r++) {

            final String row = image.get(r);

            if (row.equals(image.get(r + 1)) || mightHaveASmudge(row, image.get(r + 1))) {
                boolean mightHaveSmudge = true;
                for (int rt = r, rb = r + 1; rt >= 0 && rb < image.size(); rt--, rb++) {
                    if (!image.get(rt).equals(image.get(rb)))
                        if (mightHaveSmudge && mightHaveASmudge(image.get(rt), image.get(rb)))
                            mightHaveSmudge = false;
                        else continue outer;
                }

                if (!mightHaveSmudge)
                    border = r;
            }

        }

        return border == -1 ? OptionalInt.empty() : OptionalInt.of(border);

    }

    /*
     * Find a vertical reflection point.
     */
    private OptionalInt findVerticalReflection(final List<String> image) {
        final List<String> rotated = rotateImage90(image);
        return findHorizontalReflection(rotated);
    }

    /*
     * Find a vertical reflection point, accounting for a single smudge.
     */
    private OptionalInt findVerticalReflectionWithSmudge(final List<String> image) {
        final List<String> rotated = rotateImage90(image);
        return findHorizontalReflectionWithSmudge(rotated);
    }

    /*
     * Rotate an image 90 degrees.
     */
    private static List<String> rotateImage90(final List<String> image) {
        final int width = image.getFirst().length();
        return IntStream.range(0, width)
                .mapToObj(c -> image.stream().map(row -> Character.toString(row.charAt(c))).collect(Collectors.joining()))
                .toList();
    }

    /*
     * Check if an two lines might be reflected, accounting for a smudge.
     */
    private static boolean mightHaveASmudge(final String top, final String bottom) {
        assert top.length() == bottom.length();

        int differences = 0;
        for (int idx = 0; idx < top.length(); idx++)
            if (top.charAt(idx) != bottom.charAt(idx)) ++differences;

        return differences == 1;
    }

}

