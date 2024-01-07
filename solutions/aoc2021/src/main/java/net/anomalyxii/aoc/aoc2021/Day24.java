package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 24.
 */
@Solution(year = 2021, day = 24, title = "Arithmetic Logic Unit")
public class Day24 {

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
        final Monad monad = createMonad(context);
        return monad.findMax();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Monad monad = createMonad(context);
        return monad.findMin();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Create a MONAD program from the given input.
     */
    private Monad createMonad(final SolutionContext context) {
        final int divisor = 26; // is this _always_ 26?
        final int[] check = new int[14];
        final int[] offset = new int[14];

        final AtomicInteger phase = new AtomicInteger(0);
        context.consume(line -> {
            if (line.startsWith("inp")) {
                phase.incrementAndGet();
                return;
            }

            if (line.startsWith("add x") && line.matches("add x [-0-9]+")) {
                check[phase.get() - 1] = Integer.parseInt(line.split("\\s+")[2]);
                return;
            }

            if (line.startsWith("add y") && line.matches("add y [0-9]+")) {
                offset[phase.get() - 1] = Integer.parseInt(line.split("\\s+")[2]);
            }

            // We can just ignore everything else?
        });

        return new Monad(divisor, check, offset);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A MONAD program.
     */
    private record Monad(int divisor, int[] checks, int[] offsets) {

        // Helper Methods

        /*
         * Find the maximum value that satisfies this MONAD.
         */
        long findMax() {
            long result = 0;
            final Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < 14; i++) {
                final int check = checks[i];
                if (check > 0) {
                    stack.push(i);
                    continue;
                }

                final int last = stack.pop();
                final int delta = offsets[last] + check;
                if (delta >= 0) {
                    result += 9 * (long) Math.pow(10, 13 - i);
                    result += (9 - delta) * (long) Math.pow(10, 13 - last);
                } else {
                    result += 9 * (long) Math.pow(10, 13 - last);
                    result += (9 + delta) * (long) Math.pow(10, 13 - i);
                }
            }
            return result;
        }

        /*
         * Find the minimum value that satisfies this MONAD.
         */
        long findMin() {
            long result = 0;
            final Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < 14; i++) {
                final int check = checks[i];
                if (check > 0) {
                    stack.push(i);
                    continue;
                }

                final int last = stack.pop();
                final int delta = offsets[last] + check;
                if (delta >= 0) {
                    result += (1 + delta) * (long) Math.pow(10, 13 - i);
                    result += (long) Math.pow(10, 13 - last);
                } else {
                    result += (1 - delta) * (long) Math.pow(10, 13 - last);
                    result += (long) Math.pow(10, 13 - i);
                }
            }

            return result;
        }

    }

}