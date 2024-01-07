package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Bounds;
import net.anomalyxii.aoc.utils.geometry.ThreeDimensionalSpace;
import net.anomalyxii.aoc.utils.geometry.Volume;

import java.util.ArrayList;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 22.
 */
@Solution(year = 2021, day = 22, title = "Reactor Reboot")
public class Day22 {

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
        final List<Instruction> instructions = context.stream()
                .map(Instruction::parse)
                .toList();

        final ThreeDimensionalSpace relevant = Volume.of(Bounds.of(-50, 50), Bounds.of(-50, 50), Bounds.of(-50, 50));
        return determinePositiveArea(instructions, relevant);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Instruction> instructions = context.stream()
                .map(Instruction::parse)
                .toList();

        final ThreeDimensionalSpace relevant = instructions.stream()
                .filter(instruction -> instruction.on)
                .map(instruction -> instruction.area)
                .reduce(Volume.NULL, ThreeDimensionalSpace::union, ThreeDimensionalSpace::union);

        return determinePositiveArea(instructions, relevant);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Determine the positive area, constrained to the relevant space,
     * defined by the given `Instruction`s.
     */
    private long determinePositiveArea(final List<Instruction> instructions, final ThreeDimensionalSpace relevant) {
        final NegativeArea result = new NegativeArea(relevant);

        for (final Instruction instruction : instructions) {
            final Volume area = instruction.area;
            if (instruction.on) {
                final PositiveArea nested = new PositiveArea(area);
                result.add(nested);
            } else {
                final NegativeArea nested = new NegativeArea(area);
                result.subtract(nested);
            }
        }


        return relevant.volume() - result.volume();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * An instruction for whether a 3D region is "on" or "off".
     */
    private record Instruction(boolean on, Volume area) {

        // Helper Methods

        private static Instruction parse(final String line) {
            if (line.startsWith("on ")) {
                return new Instruction(true, Volume.parse(line.substring(3)));
            } else if (line.startsWith("off ")) {
                return new Instruction(false, Volume.parse(line.substring(4)));
            }
            throw new IllegalStateException("Invalid instruction: " + line);
        }

    }

    /*
     * A 3D area that can have sub-areas removed.
     */
    private sealed interface ComplexArea permits PositiveArea, NegativeArea {

        /*
         * Calculate the volume of this `ComplexArea`.
         */
        long volume();

    }

    /*
     * A `ComplexArea` that is considered "present".
     */
    private static final class PositiveArea implements ComplexArea {

        // Private Members

        private final ThreeDimensionalSpace area;
        private final List<NegativeArea> negatives = new ArrayList<>();

        // Constructors

        PositiveArea(final ThreeDimensionalSpace area) {
            this.area = area;
        }

        // ComplexArea Methods

        @Override
        public long volume() {
            return area.volume() - negatives.stream().mapToLong(NegativeArea::volume).sum();
        }

        // Helper Methods

        void subtract(final NegativeArea negativeArea) {
            final ThreeDimensionalSpace intersect = area.intersect(negativeArea.area);
            if (!intersect.isNull()) {
                final PositiveArea positive = new PositiveArea(intersect);
                negatives.forEach(neg -> neg.add(positive));
                negatives.add(new NegativeArea(intersect));
            }
        }

    }

    /*
     * A `ComplexArea` that is considered "absent".
     */
    private static final class NegativeArea implements ComplexArea {

        // Private Members

        private final ThreeDimensionalSpace area;
        private final List<PositiveArea> positives = new ArrayList<>();

        // Private Members

        NegativeArea(final ThreeDimensionalSpace area) {
            this.area = area;
        }

        // ComplexArea Methods

        @Override
        public long volume() {
            return area.volume() - positives.stream().mapToLong(PositiveArea::volume).sum();
        }

        // Helper Methods

        void add(final PositiveArea positiveArea) {
            final ThreeDimensionalSpace intersect = area.intersect(positiveArea.area);
            if (!intersect.isNull()) {
                final NegativeArea negative = new NegativeArea(intersect);
                positives.forEach(pos -> pos.subtract(negative));
                positives.add(new PositiveArea(intersect));
            }
        }

        void subtract(final NegativeArea negativeArea) {
            final ThreeDimensionalSpace intersect = area.intersect(negativeArea.area);
            if (!intersect.isNull()) {
                final NegativeArea negative = new NegativeArea(intersect);
                positives.forEach(pos -> pos.subtract(negative));
            }
        }

    }

}
