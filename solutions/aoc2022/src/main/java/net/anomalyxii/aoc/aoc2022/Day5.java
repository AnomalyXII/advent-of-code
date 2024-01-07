package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 5.
 */
@Solution(year = 2022, day = 5, title = "Supply Stacks")
public class Day5 {

    /*
     * Matches a re-arrangement instruction of the form:
     *   "move n from X to Y".
     */
    private static final Pattern INSTRUCTION_REGEX = Pattern.compile("move ([0-9]+) from ([0-9]+) to ([0-9]+)");

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
    public String calculateAnswerForPart1(final SolutionContext context) {
        return new CrateMover9000().organiseCargo(context.stream());
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        return new CrateMover9001().organiseCargo(context.stream());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * A parsing phase.
     */
    private enum Phase {

        /**
         * The initial parsing phase, which involves interpreting the diagram
         * showing the layout of crates.
         */
        ASSEMBLE {
            @Override
            public Phase nextPhase() {
                return REARRANGE;
            }
        },

        /**
         * The subsequent parsing phase, which involves processing the
         * instructions for moving crates between stacks.
         */
        REARRANGE {
            @Override
            public Phase nextPhase() {
                throw new IllegalStateException("Cannot transition onwards from REARRANGE phase!");
            }
        },

        // End of constants
        ;

        public abstract Phase nextPhase();
    }

    /*
     * A system of assembling and rearranging crates.
     */
    private abstract static class CrateMover {

        // Private Members

        private Phase phase = Phase.ASSEMBLE;

        // Public Methods

        /**
         * Process the given input in two phases:
         * <ol>
         * <li>First: read in the diagram of crates;</li>
         * <li>Next: reorder the crates based on the given instructions.</li>
         * </ol>
         *
         * @param input the input lines
         * @return the top crate on each of the rearranged stacks.
         */
        public String organiseCargo(final Stream<String> input) {
            final List<Deque<Character>> stacks = new ArrayList<>();
            input.forEach(line -> {
                if (line.isBlank()) phase = phase.nextPhase();
                else if (phase == Phase.ASSEMBLE) assemble(stacks, line);
                else if (phase == Phase.REARRANGE) rearrange(stacks, line);
                else throw new IllegalStateException("In neither ASSEMBLE nor REARRANGE phase - aborting!");
            });

            return stacks.stream()
                    .map(Deque::removeFirst)
                    .map(Object::toString)
                    .collect(Collectors.joining());
        }

        // Protected Helper Methods

        /**
         * Rearrange one or more crates by removing them from the top of one
         * stack and placing them onto the top of another.
         *
         * @param stacks   the crate stacks
         * @param from     the stack to remove crates from
         * @param to       the stack to add crates to
         * @param quantity the quantity of crates to move
         */
        protected abstract void performRearrangement(List<Deque<Character>> stacks, int from, int to, int quantity);

        // Private Helper Methods

        /*
         * Process a given line by inserting any crates specified onto the
         * corresponding crate stack.
         */
        private void assemble(final List<Deque<Character>> stacks, final String line) {
            for (int i = 1; i < line.length(); i += 4) {
                if (Character.isAlphabetic(line.charAt(i))) {
                    while (stacks.size() <= i / 4) {
                        stacks.add(new ArrayDeque<>());
                    }

                    stacks.get(i / 4).addLast(line.charAt(i));
                }
            }
        }

        /*
         * Rearrange one or more crates by removing them from the top of one
         * stack and placing them onto the top of another.
         */
        private void rearrange(final List<Deque<Character>> stacks, final String line) {
            final Matcher matcher = INSTRUCTION_REGEX.matcher(line);
            if (!matcher.matches())
                throw new IllegalStateException("Invalid instruction: \"" + line + "\"");

            final int quantity = Integer.parseInt(matcher.group(1));
            final int from = Integer.parseInt(matcher.group(2));
            final int to = Integer.parseInt(matcher.group(3));

            performRearrangement(stacks, from, to, quantity);
        }

    }

    /*
     * Imitate a CrateMover9000 - this will move crates one at a time.
     */
    private static final class CrateMover9000 extends CrateMover {

        @Override
        protected void performRearrangement(
                final List<Deque<Character>> stacks,
                final int from,
                final int to,
                final int quantity
        ) {
            int remainingQuantity = quantity;
            while (remainingQuantity-- > 0) {
                final Character top = stacks.get(from - 1).removeFirst();
                stacks.get(to - 1).addFirst(top);
            }
        }

    }

    /*
     * Imitate a CrateMover9001 - this will move multiple crates at once.
     */
    private static final class CrateMover9001 extends CrateMover {

        @Override
        protected void performRearrangement(
                final List<Deque<Character>> stacks,
                final int from,
                final int to,
                final int quantity
        ) {
            final Deque<Character> interim = new ArrayDeque<>();
            int remainingQuantity = quantity;
            while (remainingQuantity-- > 0) {
                final Character top = stacks.get(from - 1).removeFirst();
                interim.addLast(top);
            }

            while (!interim.isEmpty()) {
                stacks.get(to - 1).addFirst(interim.removeLast());
            }
        }

    }

}
