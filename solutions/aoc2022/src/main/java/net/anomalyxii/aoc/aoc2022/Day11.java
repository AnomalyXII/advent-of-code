package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.maths.Operation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongUnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 11.
 */
@Solution(year = 2022, day = 11, title = "Monkey in the Middle")
public class Day11 {

    private static final Pattern STARTING_ITEMS = Pattern.compile(" {2}Starting items: ((?:[0-9]+, )*[0-9]+)");
    private static final Pattern OPERATION = Pattern.compile(" {2}Operation: new = (old|[0-9]+) ([+/*-]) (old|[0-9]+)");
    private static final Pattern TEST_CONDITION = Pattern.compile(" {2}Test: divisible by ([0-9]+)");
    private static final Pattern TEST_IF_TRUE = Pattern.compile(" {4}If true: throw to monkey ([0-9]+)");
    private static final Pattern TEST_IF_FALSE = Pattern.compile(" {4}If false: throw to monkey ([0-9]+)");

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
        final List<Monkey> monkeys = Monkey.parseAll(context);
        return playKeepAway(20, monkeys, panik -> panik / 3);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Monkey> monkeys = Monkey.parseAll(context);
        final long reliefMod = monkeys.stream()
                .mapToLong(m -> m.test.divisor)
                .reduce(1, (a, b) -> a * b);
        return playKeepAway(10_000, monkeys, panik -> panik % reliefMod);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Simulate the `Monkey`s playing keep-away.
     */
    private static long playKeepAway(final int rounds, final List<Monkey> monkeys, final LongUnaryOperator calmingOperation) {
        for (int round = 0; round < rounds; round++)
            monkeys.forEach(monkey -> monkey.muckAround(calmingOperation, monkeys::get));

        final long[] business = monkeys.stream()
                .mapToLong(monkey -> monkey.counter)
                .sorted()
                .toArray();
        return business[business.length - 2] * business[business.length - 1];
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Inspect an item, and work out the intensity of panic that inspecting
     * the item will generate.
     */
    private record Inspector(LongUnaryOperator lhs, Operation op, LongUnaryOperator rhs) {

        // Inspector Methods

        /**
         * Inspect an item with a given level of panic and work out what the new
         * amount of panic will be after the inspection is finished.
         *
         * @param panic the panic-level of an item
         * @return the new panic level
         */
        public long inspect(final long panic) {
            return op.apply(lhs.applyAsLong(panic), rhs.applyAsLong(panic));
        }

    }

    /*
     * Work out where to throw an item to,
     */
    private record Discarder(int divisor, int toTrue, int toFalse) {

        // Discarder Methods

        /**
         * Test an item, with a given worry level, and decide which
         * {@link Monkey} to throw the item to next.
         *
         * @param panik the panik level of the item
         * @return the ID of the {@link Monkey} to throw to
         */
        public int test(final long panik) {
            return panik % divisor == 0 ? toTrue : toFalse;
        }

    }

    /*
     * A Monkey, that contains some items that will be inspected and then
     * discarded.
     */
    private static final class Monkey {

        // Private members

        private final Deque<Long> items;
        private final Inspector operation;
        private final Discarder test;

        private int counter = 0;

        // Constructors

        Monkey(final Deque<Long> items, final Inspector operation, final Discarder test) {
            this.items = items;
            this.operation = operation;
            this.test = test;
        }

        // Monkey Methods

        /**
         * Simulate a {@link Monkey} messing with the items they are currently
         * holding.
         * <p>
         * For each item, the {@link Monkey} will first inspect the item (raising
         * the panic-level for that item) and then discard the item (reducing the
         * panic-level) before tossing the item to a different {@link Monkey}.
         *
         * @param calmingOperation   a calming operation, used to reduce panic-levels
         * @param nextMonkeyResolver resolve a {@link Monkey} based on its ID
         */
        public void muckAround(final LongUnaryOperator calmingOperation, final IntFunction<Monkey> nextMonkeyResolver) {
            while (!items.isEmpty()) {
                final long item = items.removeFirst();
                final long panik = operation.inspect(item);
                assert panik >= 0 : "Panik overflow (" + panik + ")";

                final long relief = calmingOperation.applyAsLong(panik);

                final int nextMonkey = test.test(relief);

                ++counter;

                nextMonkeyResolver.apply(nextMonkey).receive(relief);
            }
        }

        /**
         * Receive an item from another {@link Monkey}.
         *
         * @param item the panic-level of the item to receive
         */
        public void receive(final long item) {
            items.addLast(item);
        }

        // Static Helper Methods

        /**
         * Parse a {@link List} of {@link Monkey Monkies}.
         *
         * @param context the {@link SolutionContext} to read from
         * @return the {@link List} of {@link Monkey Monkies}
         */
        public static List<Monkey> parseAll(final SolutionContext context) {
            return context.streamBatches()
                    .map(Monkey::parse)
                    .toList();
        }

        /**
         * Parse a {@link Monkey}.
         *
         * @param batch the lines defining the {@link Monkey}
         * @return the {@link Monkey}
         */
        public static Monkey parse(final List<String> batch) {
            return new Monkey(
                    extractStartingItems(batch),
                    extractOperation(batch),
                    extractTest(batch)
            );
        }

        /*
         * Extract the `List` of starting items.
         */
        private static ArrayDeque<Long> extractStartingItems(final List<String> batch) {
            return new ArrayDeque<>(
                    Arrays.stream(getMatchResult(STARTING_ITEMS, batch.get(1))
                                          .group(1)
                                          .split(", "))
                            .map(Long::valueOf)
                            .toList()
            );
        }

        /*
         * Extract the inspection operation.
         */
        private static Inspector extractOperation(final List<String> batch) {
            final MatchResult result = getMatchResult(OPERATION, batch.get(2));
            final LongUnaryOperator lhs = "old".equals(result.group(1))
                    ? old -> old
                    : old -> Long.parseLong(result.group(1));
            final LongUnaryOperator rhs = "old".equals(result.group(3))
                    ? old -> old
                    : old -> Long.parseLong(result.group(3));
            return new Inspector(lhs, Operation.fromString(result.group(2)), rhs);
        }

        /*
         * Extract the discarder operation.
         */
        private static Discarder extractTest(final List<String> batch) {
            final MatchResult testResult = getMatchResult(TEST_CONDITION, batch.get(3));
            final MatchResult testTrueResult = getMatchResult(TEST_IF_TRUE, batch.get(4));
            final MatchResult testFalseResult = getMatchResult(TEST_IF_FALSE, batch.get(5));

            final int worryMod = Integer.parseInt(testResult.group(1));
            final int toTrue = Integer.parseInt(testTrueResult.group(1));
            final int toFalse = Integer.parseInt(testFalseResult.group(1));

            return new Discarder(worryMod, toTrue, toFalse);
        }

        /*
         * Verify a line matches and create a `MatchResult`.
         */
        private static MatchResult getMatchResult(final Pattern pattern, final String line) {
            final Matcher matcher = pattern.matcher(line);
            if (!matcher.matches())
                throw new IllegalStateException("Expected to process a line matching /" + pattern + "/; but was: " + line);
            return matcher.toMatchResult();
        }

    }

}

