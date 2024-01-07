package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SequencedSet;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 15.
 */
@Solution(year = 2023, day = 15, title = "Lens Library")
public class Day15 {

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
        return stream(context.readLine().split(","))
                .mapToInt(Day15::hash)
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
        final Map<String, Integer> lenses = new HashMap<>();
        final Box[] boxes = IntStream.range(0, 256)
                .mapToObj(id -> new Box(new LinkedHashSet<>()))
                .toArray(Box[]::new);

        stream(context.readLine().split(","))
                .forEach(instruction -> organiseLens(instruction, boxes, lenses));

        return calculateFocusingPower(boxes, lenses);
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
        final Map<String, Integer> lenses = new HashMap<>();
        final Box[] boxes = new Box[256];

        final int answer1 = stream(context.readLine().split(","))
                .peek(instruction -> organiseLens(instruction, boxes, lenses))
                .mapToInt(Day15::hash)
                .sum();

        final int answer2 = calculateFocusingPower(boxes, lenses);

        return new IntTuple(answer1, answer2);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Calculate the HASH of a {@link String}.
     *
     * @param str the {@link String}
     * @return the HASH
     */
    static int hash(final String str) {
        int value = 0;
        for (int i = 0; i < str.length(); i++) {
            final int code = str.charAt(i);
            value += code;
            value *= 17;
            value %= 256;
        }
        return value;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Organise a specific lens into a box.
     */
    private static void organiseLens(final String instruction, final Box[] boxes, final Map<String, Integer> lenses) {
        final String[] parts = instruction.split("[-=]", 2);
        final String label = parts[0];
        final char operation = parts[1].isBlank() ? '-' : '=';

        final int boxId = hash(label);
        final Box box = box(boxes, boxId);

        if (operation == '-') {
            box.lenses.remove(label);
        } else {
            final int focalLength = Integer.parseInt(parts[1]);
            box.lenses.add(label);
            lenses.put(label, focalLength);
        }
    }

    /*
     * Calculate the focusing power of the lenses.
     */
    private static int calculateFocusingPower(final Box[] boxes, final Map<String, Integer> lenses) {
        int result = 0;
        int boxId = 0;
        for (final Box box : boxes) {
            ++boxId;
            if (box == null) continue;

            int lensId = 0;
            for (final String lens : box.lenses)
                result += (boxId) * (++lensId) * lenses.get(lens);
        }
        return result;
    }

    /*
     * Get the box with the given index.
     */
    private static Box box(final Box[] boxes, final int index) {
        final Box box = boxes[index];
        if (box != null) return box;

        return (boxes[index] = new Box(new LinkedHashSet<>()));
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A box that may contain some lenses.
     */
    private record Box(SequencedSet<String> lenses) {
    }

}

