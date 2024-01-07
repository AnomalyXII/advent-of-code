package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.maths.Operation;

import java.util.*;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 21.
 */
@Solution(year = 2022, day = 21, title = "Monkey Math")
public class Day21 {

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
        final List<MathsMonkey> monkeys = new ArrayList<>(context.process(MathsMonkey::parse));

        final Map<String, Long> solved = new HashMap<>();
        while (!solved.containsKey("root") && !monkeys.isEmpty()) {
            final Iterator<MathsMonkey> it = monkeys.iterator();
            while (it.hasNext()) {
                final MathsMonkey monkey = it.next();
                if (monkey.canSolve(solved)) {
                    final long answer = monkey.shoutNumber(solved);
                    solved.put(monkey.name(), answer);
                    it.remove();
                }
            }
        }

        assert solved.containsKey("root");
        return solved.get("root");
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<MathsMonkey> monkeys = new ArrayList<>(context.process(MathsMonkey::parse));

        final MathsMonkey humn = monkeys.stream().filter(m -> "humn".equals(m.name())).findFirst().orElseThrow();
        final MathsMonkey root = monkeys.stream().filter(m -> "root".equals(m.name())).findFirst().orElseThrow();
        monkeys.remove(humn);
        monkeys.remove(root);

        final Map<String, Long> solved = new HashMap<>();
        final Map<String, MathsMonkey> cantSolveYet = new HashMap<>();
        while (!solved.containsKey("root") && !monkeys.isEmpty()) {
            final Iterator<MathsMonkey> it = monkeys.iterator();
            while (it.hasNext()) {
                final MathsMonkey monkey = it.next();
                if (monkey.canSolve(solved)) {
                    final long answer = monkey.shoutNumber(solved);
                    solved.put(monkey.name(), answer);
                    it.remove();
                } else if (monkey.dependsOnHuman(cantSolveYet)) {
                    cantSolveYet.put(monkey.name(), monkey);
                    it.remove();
                }
            }
        }

        cantSolveYet.put("humn", new HumanMonkey());
        return root.determineHumanNumber(solved, cantSolveYet);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A monkey that can, apparently, also do some kind of maths.
     */
    private sealed interface MathsMonkey permits HumanMonkey, NumberMonkey, OperationMonkey {

        // Interface Methods

        /**
         * Get the name of this {@link MathsMonkey monkey}.
         *
         * @return the monkey's name
         */
        String name();

        /**
         * Check if this {@link MathsMonkey monkey} has enough information to
         * determine what number it should shout.
         *
         * @param previous a {@link Map} of {@link MathsMonkey monkeys} and the number they shouted
         * @return {@literal true} if this {@link MathsMonkey} can shout its number; {@literal false} otherwise
         */
        boolean canSolve(Map<String, Long> previous);

        /**
         * Check if this {@link MathsMonkey monkey} depends on a
         * {@link HumanMonkey human} shouting a number.
         *
         * @param needsHuman a {@link Map} of {@link MathsMonkey monkeys} are blocked on a {@link HumanMonkey human}
         * @return {@literal true} if this {@link MathsMonkey} cannot shout a number; {@literal false} otherwise
         */
        boolean dependsOnHuman(Map<String, MathsMonkey> needsHuman);

        /**
         * Get the {@link MathsMonkey monkey} to shout out its number.
         *
         * @param previous a {@link Map} of {@link MathsMonkey monkeys} and the number they shouted
         * @return the number that this {@link MathsMonkey} should shout
         */
        long shoutNumber(Map<String, Long> previous);

        /**
         * Convince this {@link MathsMonkey monkey} to reverse engineer the
         * puzzle and determine what number a {@link HumanMonkey human} should
         * shout for the game to complete successfully.
         *
         * @param previous   a {@link Map} of {@link MathsMonkey monkeys} and the number they shouted
         * @param needsHuman a {@link Map} of {@link MathsMonkey monkeys} are blocked on a {@link HumanMonkey human}
         * @return the number that a {@link HumanMonkey} should shout
         */
        long determineHumanNumber(Map<String, Long> previous, Map<String, MathsMonkey> needsHuman);

        /**
         * Convince this {@link MathsMonkey monkey} to reverse engineer the
         * puzzle and determine what number a {@link HumanMonkey human} should
         * shout for the game to complete successfully.
         *
         * @param toSolveFor the target that this {@link MathsMonkey} is aiming for
         * @param previous   a {@link Map} of {@link MathsMonkey monkeys} and the number they shouted
         * @param needsHuman a {@link Map} of {@link MathsMonkey monkeys} are blocked on a {@link HumanMonkey human}
         * @return the number that a {@link HumanMonkey} should shout
         */
        long determineHumanNumber(long toSolveFor, Map<String, Long> previous, Map<String, MathsMonkey> needsHuman);

        // Helper Methods

        /**
         * Create a new {@link MathsMonkey monkey} from the given input line.
         *
         * @param line the input
         * @return the {@link MathsMonkey}
         */
        static MathsMonkey parse(final String line) {
            final String[] parts = line.split(": ");
            final String name = parts[0];
            if (parts[1].matches("[0-9]+"))
                return new NumberMonkey(name, Integer.parseInt(parts[1]));

            final String[] parts2 = parts[1].split(" ");
            return new OperationMonkey(name, parts2[0], Operation.fromString(parts2[1]), parts2[2]);
        }

    }

    /*
     * A `MathsMonkey` that will just shout a number.
     */
    private record NumberMonkey(String name, int number) implements MathsMonkey {

        // MathsMonkey Methods

        @Override
        public boolean canSolve(final Map<String, Long> previous) {
            return true;
        }

        @Override
        public boolean dependsOnHuman(final Map<String, MathsMonkey> needsHuman) {
            return false;
        }

        @Override
        public long shoutNumber(final Map<String, Long> previous) {
            return number;
        }

        @Override
        public long determineHumanNumber(final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            return number;
        }

        @Override
        public long determineHumanNumber(final long toSolveFor, final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            assert toSolveFor == number;
            return number;
        }

    }

    /*
     * A `MathsMonkey` that must perform a maths operation to determine what
     * number it needs to shout.
     */
    private record OperationMonkey(String name, String lhs, Operation op, String rhs) implements MathsMonkey {

        // MathsMonkey Methods

        @Override
        public boolean canSolve(final Map<String, Long> previous) {
            return previous.containsKey(lhs) && previous.containsKey(rhs);
        }

        @Override
        public boolean dependsOnHuman(final Map<String, MathsMonkey> needsHuman) {
            return "humn".equals(lhs)
                    || needsHuman.containsKey(lhs)
                    || "humn".equals(rhs)
                    || needsHuman.containsKey(rhs);
        }

        @Override
        public long determineHumanNumber(final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            if (previous.containsKey(lhs)) {
                final long left = previous.get(lhs);
                return needsHuman.get(rhs).determineHumanNumber(left, previous, needsHuman);
            }

            if (previous.containsKey(rhs)) {
                final long right = previous.get(rhs);
                return needsHuman.get(lhs).determineHumanNumber(right, previous, needsHuman);
            }

            throw new IllegalStateException("Eeek!");
        }

        @Override
        public long determineHumanNumber(final long toSolveFor, final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            if (previous.containsKey(lhs)) {
                final long left = op.reverseRight(toSolveFor, previous.get(lhs));
                return needsHuman.get(rhs).determineHumanNumber(left, previous, needsHuman);
            }

            if (previous.containsKey(rhs)) {
                final long right = op.reverseLeft(toSolveFor, previous.get(rhs));
                return needsHuman.get(lhs).determineHumanNumber(right, previous, needsHuman);
            }

            throw new IllegalStateException("Eeek!");
        }

        @Override
        public long shoutNumber(final Map<String, Long> previous) {
            final long left = previous.get(lhs);
            final long right = previous.get(rhs);
            return op.apply(left, right);
        }
    }

    /*
     * A "Human" `MathsMonkey` that doesn't know what number it needs to
     * shout but can, possibly, derive it from what the monkeys that depend
     * on it.
     */
    private static final class HumanMonkey implements MathsMonkey {

        // MathsMonkey Methods

        @Override
        public String name() {
            return "humn";
        }

        @Override
        public boolean canSolve(final Map<String, Long> previous) {
            return false;
        }

        @Override
        public boolean dependsOnHuman(final Map<String, MathsMonkey> needsHuman) {
            return false;
        }

        @Override
        public long shoutNumber(final Map<String, Long> previous) {
            throw new IllegalStateException("Should not be solving for HUMN");
        }

        @Override
        public long determineHumanNumber(final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            throw new IllegalStateException("Should not be solving for HUMN");
        }

        @Override
        public long determineHumanNumber(final long toSolveFor, final Map<String, Long> previous, final Map<String, MathsMonkey> needsHuman) {
            return toSolveFor;
        }

    }

}

