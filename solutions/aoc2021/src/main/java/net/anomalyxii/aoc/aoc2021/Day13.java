package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 13.
 */
@Solution(year = 2021, day = 13, title = "Transparent Origami")
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
    public long calculateAnswerForPart1(final SolutionContext context) {
        final DotsAndInstructions dotsAndInstructions = parse(context);

        final Instruction firstFold = dotsAndInstructions.instructions.getFirst();
        final Set<Coordinate> transformed = dotsAndInstructions.dots.stream()
                .map(firstFold::transform)
                .collect(Collectors.toSet());

        return transformed.size();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final DotsAndInstructions dotsAndInstructions = parse(context);

        final Set<Coordinate> finalCoordinates = dotsAndInstructions.instructions.stream()
                .reduce(
                        new HashSet<>(dotsAndInstructions.dots),
                        (Set<Coordinate> transformed, Instruction instruction) -> transformed.stream()
                                .map(instruction::transform)
                                .collect(Collectors.toSet()),
                        (a, b) -> {
                            throw new IllegalStateException("Should not need to merge");
                        }
                );

        final Coordinate max = finalCoordinates.stream()
                .reduce(Coordinate.ORIGIN, (r, n) -> new Coordinate(max(r.x(), n.x()), max(r.y(), n.y())));

        // Letters appear to be 5x6?
        final Grid grid = Grid.size(((max.x() + 4) / 5) * 5, max.y() + 1, p -> finalCoordinates.contains(p) ? 1 : 0);
        return context.ocr().recognise(grid);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Parse the input into a `List` of `Coordinate`s and a `List` of
     * `Instruction`s.
     */
    private static DotsAndInstructions parse(final SolutionContext context) {
        final List<List<String>> input = context.readBatches();
        final List<Coordinate> dots = input.getFirst().stream()
                .map(Coordinate::parse)
                .toList();

        final List<Instruction> instructions = input.getLast().stream()
                .map(Instruction::parse)
                .toList();

        return new DotsAndInstructions(dots, instructions);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    private record DotsAndInstructions(List<Coordinate> dots, List<Instruction> instructions) {

    }

    /*
     * The axis to fold along.
     */
    private enum Axis {

        X,
        Y,

        // End of constants
        ;

    }

    /*
     * An instruction on how to fold the paper.
     */
    private record Instruction(Axis axis, int value) {

        // Helper Methods

        /*
         * Transform the given `Point`.
         */
        Coordinate transform(final Coordinate dot) {
            if (axis == Axis.X) return new Coordinate(flip(dot.x(), value), dot.y());
            else if (axis == Axis.Y) return new Coordinate(dot.x(), flip(dot.y(), value));
            else throw new IllegalArgumentException("Invalid instruction!");
        }

        // Static Helper Methods

        /**
         * Create a new {@link Instruction} by parsing the line.
         *
         * @param instruction the instruction to parse
         * @return the parsed {@link Instruction}
         */
        static Instruction parse(final String instruction) {
            if (!instruction.startsWith("fold along "))
                throw new IllegalArgumentException("Invalid instruction: '" + instruction + "'");

            final String[] parts = instruction.split("\\s*=\\s*", 2);
            if (parts[0].endsWith("x")) return new Instruction(Axis.X, Integer.parseInt(parts[1]));
            else if (parts[0].endsWith("y")) return new Instruction(Axis.Y, Integer.parseInt(parts[1]));
            else throw new IllegalArgumentException("Failed to parse instruction: '" + instruction + "'");
        }

        /*
         * Flip the given point relative to the fold.
         */
        private static int flip(final int point, final int fold) {
            if (point <= fold) return point;
            else return fold - (point - fold);
        }

    }

}
