package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 8.
 */
@Solution(year = 2020, day = 8, title = "Handheld Halting")
public class Day8 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        final AtomicLong accumulator = new AtomicLong(0);
        final List<String> instructions = new ArrayList<>(context.read());

        try {
            runCode(accumulator, instructions);
        } catch (final InfiniteLoopException e) {
            return accumulator.get();
        }

        throw new IllegalStateException("Expected an infinite loop!");
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> instructions = new ArrayList<>(context.read());
        return runCodeAndAttemptToPatch(instructions);
    }

    // ****************************************
    // Private Helper Members
    // ****************************************

    /*
     * Run the code, starting at the first instruction.
     */
    private void runCode(final AtomicLong accumulator, final List<String> instructions) {
        runCode(0, accumulator, instructions);
    }

    /*
     * Run the code, starting at the given instruction.
     */
    private void runCode(final int start, final AtomicLong accumulator, final List<String> instructions) {
        for (int i = start; i < instructions.size(); ) {
            final String inst = instructions.get(i);
            if ("-".equalsIgnoreCase(inst)) {
                throw new InfiniteLoopException();
            }

            final String cmd = inst.substring(0, 3);
            final String valStr = inst.substring(4);

            i = process(accumulator, instructions, i, cmd, valStr);
        }
    }

    /*
     * Run the code; however, before executing each instruction, attempt to
     * patch a `nop` to a `jmp` or a `jmp` to a `nop` and run the patched
     * code to see if it would complete.
     */
    private long runCodeAndAttemptToPatch(final List<String> instructions) {
        final AtomicLong accumulator = new AtomicLong(0);
        for (int i = 0; i < instructions.size(); ) {
            final String inst = instructions.get(i);
            if ("-".equalsIgnoreCase(inst)) {
                throw new InfiniteLoopException();
            }

            final String cmd = inst.substring(0, 3);
            final String valStr = inst.substring(4);

            // First, attempt to patch the code from this position...
            switch (cmd) {
                case "nop", "jmp" -> {
                    final String newInst = "nop".equals(cmd)
                            ? inst.replaceFirst("nop", "jmp")
                            : inst.replaceFirst("jmp", "nop");
                    final AtomicLong newAccumulator = new AtomicLong(accumulator.longValue());
                    final List<String> newInstructions = new ArrayList<>(instructions);
                    newInstructions.set(i, newInst);
                    try {
                        runCode(i, newAccumulator, newInstructions);
                        return newAccumulator.longValue();
                    } catch (final InfiniteLoopException e) {
                        // patch failed...
                    }
                }
                default -> {
                    // Can't patch...
                }
            }

            i = process(accumulator, instructions, i, cmd, valStr);
        }

        return accumulator.longValue();
    }

    /*
     * Process a given command and return the index of the next instruction.
     */
    private int process(
            final AtomicLong accumulator,
            final List<String> instructions,
            final int i,
            final String cmd,
            final String valStr
    ) {
        instructions.set(i, "-");

        int next = i;
        switch (cmd) {
            case "nop" -> ++next;
            case "acc" -> {
                accumulator.addAndGet(Long.parseLong(valStr));
                ++next;
            }
            case "jmp" -> next += Integer.parseInt(valStr);
            default -> throw new IllegalStateException("Invalid command: '" + cmd + "'");
        }
        return next;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Thrown if the code detects an infinite loop.
     */
    private static final class InfiniteLoopException extends RuntimeException {
    }

}
