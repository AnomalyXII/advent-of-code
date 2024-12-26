package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 3.
 */
@Solution(year = 2024, day = 3, title = "Mull It Over")
public class Day3 {

    private static final Pattern MATCHER = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");

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
        final ResultMemoryContext memory = new BasicMemoryContext();
        context.consume(line -> process(line, memory));
        return memory.result();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final ResultMemoryContext memory = new AdvancedMemoryContext();
        context.consume(line -> process(line, memory));
        return memory.result();
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
        final CombinedMemoryContext memory = new CombinedMemoryContext();
        context.consume(line -> process(line, memory));
        return memory.result();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Process the corrupted memory instructions.
     */
    private static void process(final String line, final MemoryContext memory) {
        final Matcher matcher = MATCHER.matcher(line);
        while (matcher.find()) {
            final String match = matcher.group(0);
            if (match.startsWith("do()"))
                memory.enable();
            else if (match.startsWith("don't()"))
                memory.disable();
            else
                memory.accumulate(matcher.group(1), matcher.group(2));
        }
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Hold the result of the processed memory.
     */
    private interface MemoryContext {

        // Interface Methods

        /**
         * Enable processing of {@code mul()} instructions.
         */
        void enable();

        /**
         * Disable processing of {@code mul()} instructions.
         */
        void disable();

        /**
         * Accumulate the result of a {@code mul()} instruction.
         */
        void accumulate(String first, String second);

    }

    /*
     * A `MemoryContext` that can be queried for the value currently
     * accumulated in memory.
     */
    private interface ResultMemoryContext extends MemoryContext {

        // Interface Methods

        /**
         * Get the accumulated result.
         *
         * @return the result
         */
        long result();

    }

    /*
     * A basic `MemoryContext` that will simply accumulate multiplication
     * results.
     */
    private static class BasicMemoryContext implements ResultMemoryContext {

        // Private Members

        private long result = 0;

        // MemoryContext Methods

        @Override
        public void enable() {
        }

        @Override
        public void disable() {
        }

        @Override
        public void accumulate(final String first, final String second) {
            result += ((long) Integer.parseInt(first) * Integer.parseInt(second));
        }

        // ResultMemoryContext Methods

        @Override
        public long result() {
            return result;
        }
    }

    /*
     * An advanced `MemoryContext` that will enable and disable
     * instructions as appropriate.
     */
    private static final class AdvancedMemoryContext extends BasicMemoryContext {

        // Private Members

        private boolean mulOperationsEnabled = true;

        // MemoryContext Methods

        @Override
        public void enable() {
            mulOperationsEnabled = true;
        }

        @Override
        public void disable() {
            mulOperationsEnabled = false;
        }

        @Override
        public void accumulate(final String first, final String second) {
            if (mulOperationsEnabled)
                super.accumulate(first, second);
        }
    }

    /*
     * A `MemoryContext`
     */
    private static final class CombinedMemoryContext implements MemoryContext {

        // Private Members

        private final ResultMemoryContext part1 = new BasicMemoryContext();
        private final ResultMemoryContext part2 = new AdvancedMemoryContext();

        // MemoryContext Methods

        @Override
        public void enable() {
            part1.enable();
            part2.enable();
        }

        @Override
        public void disable() {
            part1.disable();
            part2.disable();
        }

        @Override
        public void accumulate(final String first, final String second) {
            part1.accumulate(first, second);
            part2.accumulate(first, second);
        }

        // Helper Methods

        /*
         * Get the combined result.
         */
        LongTuple result() {
            return new LongTuple(part1.result(), part2.result());
        }
    }
}

