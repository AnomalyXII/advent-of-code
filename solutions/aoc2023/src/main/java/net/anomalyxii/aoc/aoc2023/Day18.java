package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.algorithms.ShoelaceCalculator;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 18.
 */
@Solution(year = 2023, day = 18, title = "Lavaduct Lagoon")
public class Day18 {

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
        return calculateArea(Instruction.process(context), Instruction::toVelocity);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return calculateArea(Instruction.process(context), Instruction::toHexVelocity);
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return a {@link LongTuple} containing the answers for both parts
     */
    @Optimised
    public LongTuple calculateAnswers(final SolutionContext context) {
        final List<Instruction> instructions = Instruction.process(context);
        return new LongTuple(
            calculateArea(instructions, Instruction::toVelocity),
            calculateArea(instructions, Instruction::toHexVelocity)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the area of the pool created by following the provided
     * instructions.
     */
    private static long calculateArea(final List<Instruction> instructions, final Function<Instruction, Velocity> parser) {
        final List<Coordinate> points = new ArrayList<>();

        long trenchLength = 0;
        Coordinate current = Coordinate.ORIGIN;
        for (final Instruction line : instructions) {
            final Velocity adjustment = parser.apply(line);
            current = current.adjustBy(adjustment);

            points.add(current);
            trenchLength += adjustment.magnitude();
        }

        final long area = new ShoelaceCalculator().area(points);

        final long internalPoints = (long) ((area - (0.5 * trenchLength)) + 1);
        return internalPoints + trenchLength;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * An instruction for digging a trench.
     */
    private record Instruction(char direction, int magnitude, String colour) {

        // Helper Methods

        /*
         * Convert this `Instruction` into a `Velocity`.
         */
        Velocity toVelocity() {
            return switch (direction) {
                case 'U' -> Velocity.inDirection(Direction.UP, magnitude);
                case 'D' -> Velocity.inDirection(Direction.DOWN, magnitude);
                case 'L' -> Velocity.inDirection(Direction.LEFT, magnitude);
                case 'R' -> Velocity.inDirection(Direction.RIGHT, magnitude);
                default -> throw new IllegalStateException("Invalid direction: " + direction);
            };
        }

        /*
         * Convert the hexadecimal colour of this `Instruction` into a
         * `Velocity`.
         */
        Velocity toHexVelocity() {
            final char direction = colour.charAt(6);
            final int length = Integer.parseInt(colour.substring(1, 6), 16);

            return switch (direction) {
                case '0' -> Velocity.inDirection(Direction.RIGHT, length);
                case '1' -> Velocity.inDirection(Direction.DOWN, length);
                case '2' -> Velocity.inDirection(Direction.LEFT, length);
                case '3' -> Velocity.inDirection(Direction.UP, length);
                default -> throw new IllegalStateException("Invalid direction: " + direction);
            };
        }

        // Static Helper Methods

        /*
         * Read all the `Instruction`s from the given input.
         */
        static List<Instruction> process(final SolutionContext context) {
            return context.stream()
                    .map(Instruction::parse)
                    .collect(Collectors.toList());
        }

        /*
         * Parse a line into an instruction.
         */
        private static Instruction parse(final String line) {
            final String[] parts = line.split("\\s+");
            final char direction = parts[0].charAt(0);
            final int length = Integer.parseInt(parts[1]);

            final String hex = parts[2].substring(1, 8);

            return new Instruction(direction, length, hex);
        }

    }

}

