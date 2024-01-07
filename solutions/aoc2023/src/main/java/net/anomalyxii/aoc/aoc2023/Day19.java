package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.geometry.Bounds;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 19.
 */
@Solution(year = 2023, day = 19, title = "Aplenty")
public class Day19 {

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
        final List<List<String>> batches = context.readBatches();

        final Map<String, Workflow> workflows = Workflow.fromInput(batches.getFirst());
        final List<MachinePart> machineParts = MachinePart.process(batches.getLast());

        final Workflow in = workflows.get("in");

        final LongAccumulator sum = new LongAccumulator(Long::sum, 0L);
        for (final MachinePart machinePart : machineParts)
            in.process(machinePart, workflows, sum);

        return sum.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final Map<String, Workflow> workflows = Workflow.fromInput(batches.getFirst());
        final Workflow in = workflows.get("in");

        final LongAccumulator sum = new LongAccumulator(Long::sum, 0L);
        final QuantumMachinePart leftovers = in.permutate(workflows, QuantumMachinePart.MAX, sum);
        assert QuantumMachinePart.ZERO.equals(leftovers);

        return sum.longValue();
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
        final List<List<String>> batches = context.readBatches();

        final Map<String, Workflow> workflows = Workflow.fromInput(batches.getFirst());
        final List<MachinePart> machineParts = MachinePart.process(batches.getLast());

        final Workflow in = workflows.get("in");

        final LongAccumulator part1 = new LongAccumulator(Long::sum, 0L);
        for (final MachinePart machinePart : machineParts)
            in.process(machinePart, workflows, part1);

        final LongAccumulator part2 = new LongAccumulator(Long::sum, 0L);
        final QuantumMachinePart leftovers = in.permutate(workflows, QuantumMachinePart.MAX, part2);
        assert QuantumMachinePart.ZERO.equals(leftovers);

        return new LongTuple(part1.longValue(), part2.longValue());
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a decision within a `Workflow`.
     */
    private sealed interface Rule permits AlwaysRule, GreaterThanRule, LessThanRule {

        // Interface Methods

        /*
         * Process a `MachinePart` and determine where to go next.
         */
        boolean process(MachinePart part, Map<String, Workflow> workflows, LongAccumulator sum);

        /*
         * Compute the possible permutations of a `QuantumMachinePart` passing
         * through this `Rule`.
         */
        QuantumMachinePart permutate(Map<String, Workflow> workflows, QuantumMachinePart part, LongAccumulator sum);

        // Static Methods

        /*
         * Parse a `Rule` from a given input line.
         */
        static Rule parse(final String input) {
            if (!input.contains(">") && !input.contains("<"))
                return new AlwaysRule(input);

            final String[] parts = input.split("[<>:]", 3);
            final char type = input.charAt(parts[0].length());
            return switch (type) {
                case '>' -> new GreaterThanRule(parts[0].charAt(0), Integer.parseInt(parts[1]), parts[2]);
                case '<' -> new LessThanRule(parts[0].charAt(0), Integer.parseInt(parts[1]), parts[2]);
                default -> throw new IllegalArgumentException("Invalid rule type: " + type);
            };
        }

        /*
         * Process the next Rule in a chain.
         */
        static boolean next(
                final MachinePart part,
                final String target,
                final Map<String, Workflow> workflows,
                final LongAccumulator sum
        ) {
            if ("A".equals(target)) {
                sum.accumulate(part.score());
                return true;
            }

            if ("R".equals(target)) return true;

            final Workflow workflow = workflows.get(target);
            final boolean downstream = workflow.process(part, workflows, sum);
            assert downstream : "Downstream rule did not process this MachinePart!";
            return true;
        }

    }

    /*
     * A `Rule` that accepts any input.
     */
    private record AlwaysRule(String target) implements Rule {

        // Rule Methods

        @Override
        public boolean process(final MachinePart part, final Map<String, Workflow> workflows, final LongAccumulator sum) {
            return Rule.next(part, target, workflows, sum);
        }

        @Override
        public QuantumMachinePart permutate(final Map<String, Workflow> workflows, final QuantumMachinePart in, final LongAccumulator sum) {
            if ("A".equals(target)) {
                sum.accumulate(in.size());
                return QuantumMachinePart.ZERO;
            }

            if ("R".equals(target))
                return QuantumMachinePart.ZERO;

            final Workflow workflow = workflows.get(target);
            assert workflow != null;

            final QuantumMachinePart leftovers = workflow.permutate(workflows, in, sum);
            assert QuantumMachinePart.ZERO.equals(leftovers);
            return leftovers;
        }

        // To String

        @Override
        public String toString() {
            return "! => %s".formatted(target);
        }

    }

    /*
     * A `Rule` that accepts an input if a given attribute is over the
     * specified threshold limit.
     */
    private record GreaterThanRule(char attribute, int limit, String target) implements Rule {

        // Rule Methods

        @Override
        public boolean process(final MachinePart part, final Map<String, Workflow> workflows, final LongAccumulator sum) {
            final int value = part.get(attribute);
            if (value > limit) return Rule.next(part, target, workflows, sum);
            return false;
        }

        @Override
        public QuantumMachinePart permutate(final Map<String, Workflow> workflows, final QuantumMachinePart in, final LongAccumulator sum) {
            final QuantumMachinePart[] split = in.splitGreaterThan(attribute, limit);
            if ("A".equals(target)) {
                sum.accumulate(split[1].size());
                return split[0];
            }

            if ("R".equals(target))
                return split[0];

            final Workflow workflow = workflows.get(target);
            assert workflow != null;

            final QuantumMachinePart leftovers = workflow.permutate(workflows, split[1], sum);
            assert QuantumMachinePart.ZERO.equals(leftovers) : "Should be nothing left over????";
            return split[0];
        }

        // To String

        @Override
        public String toString() {
            return "%s>%d => %s".formatted(attribute, limit, target);
        }
    }

    /*
     * A `Rule` that accepts an input if a given attribute is below the
     * specified threshold limit.
     */
    private record LessThanRule(char attribute, int limit, String target) implements Rule {

        // Rule Methods

        @Override
        public boolean process(final MachinePart part, final Map<String, Workflow> workflows, final LongAccumulator sum) {
            final int value = part.get(attribute);
            if (value < limit) return Rule.next(part, target, workflows, sum);
            return false;
        }

        @Override
        public QuantumMachinePart permutate(final Map<String, Workflow> workflows, final QuantumMachinePart in, final LongAccumulator sum) {
            final QuantumMachinePart[] split = in.splitLessThan(attribute, limit);
            if ("A".equals(target)) {
                sum.accumulate(split[0].size());
                return split[1];
            }

            if ("R".equals(target))
                return split[1];

            final Workflow workflow = workflows.get(target);
            assert workflow != null;

            final QuantumMachinePart leftovers = workflow.permutate(workflows, split[0], sum);
            assert QuantumMachinePart.ZERO.equals(leftovers) : "Should be nothing left over????";
            return split[1];
        }

        // To String

        @Override
        public String toString() {
            return "%s<%d => %s".formatted(attribute, limit, target);
        }

    }

    /*
     * A workflow of one or more `Rule`s.
     */
    private record Workflow(String name, List<Rule> rules) {

        // Helper Methods

        /*
         * Process a `MachinePart` and determine where it should go next.
         */
        boolean process(final MachinePart machinePart, final Map<String, Workflow> workflows, final LongAccumulator sum) {
            for (final Rule rule : rules)
                if (rule.process(machinePart, workflows, sum))
                    return true;

            return false;
        }

        /*
         * Compute the possible permutations of a `QuantumMachinePart` passing
         * through this `Workflow`.
         */
        QuantumMachinePart permutate(final Map<String, Workflow> workflows, final QuantumMachinePart in, final LongAccumulator sum) {
            QuantumMachinePart leftovers = in;
            for (final Rule rule : rules) {
                leftovers = rule.permutate(workflows, leftovers, sum);
                if (QuantumMachinePart.ZERO.equals(leftovers)) break;
            }
            assert QuantumMachinePart.ZERO.equals(leftovers);
            return leftovers;
        }

        // Static Helper Methods

        /*
         * Extract a `Map` of `Workflow`s from a given input.
         */
        static Map<String, Workflow> fromInput(final List<String> input) {

            return input.stream()
                    .map(Workflow::parse)
                    .collect(Collectors.toMap(
                            Workflow::name,
                            Function.identity()
                    ));

        }

        /*
         * Parse a `Workflow` from a given line.
         */
        private static Workflow parse(final String line) {

            final String[] parts = line.substring(0, line.length() - 1).split("[{]", 2);
            final List<Rule> rules = stream(parts[1].split(",\\s*"))
                    .map(Rule::parse)
                    .toList();

            return new Workflow(parts[0], rules);

        }
    }

    /*
     * Represents an individual machine part with various attributes.
     */
    private record MachinePart(int x, int m, int a, int s) {

        // Helper Methods

        /*
         * Get the value of a specific attribute.
         */
        int get(final char attribute) {
            return switch (attribute) {
                case 'x' -> x;
                case 'm' -> m;
                case 'a' -> a;
                case 's' -> s;
                default -> throw new IllegalArgumentException("Invalid MachinePart attribute: " + attribute);
            };
        }

        /*
         * Calculate the score of this `MachinePart`.
         */
        long score() {
            return x + m + a + s;
        }

        // Static Helper Methods

        /*
         * Extract a `List` of `MachinePart`s from a given input.
         */
        static List<MachinePart> process(final List<String> input) {
            return input.stream()
                    .map(MachinePart::parse)
                    .toList();
        }

        /*
         * Parse a `MachinePart` from a given line.
         */
        private static MachinePart parse(final String line) {
            final String[] parts = line.substring(1, line.length() - 1).split(",\\s*");
            final Map<Character, Integer> parsed = stream(parts)
                    .map(part -> part.split("="))
                    .collect(Collectors.toMap(
                            split -> split[0].charAt(0),
                            split -> Integer.parseInt(split[1])
                    ));
            return new MachinePart(
                    parsed.getOrDefault('x', 0),
                    parsed.getOrDefault('m', 0),
                    parsed.getOrDefault('a', 0),
                    parsed.getOrDefault('s', 0)
            );
        }

    }

    /*
     * Represents a set of potential machine part that have attributes within
     * certain ranges.
     */
    private record QuantumMachinePart(Bounds x, Bounds m, Bounds a, Bounds s) {

        /*
         * A `QuantumMachinePart` that contains no possible parts.
         */
        static final QuantumMachinePart ZERO = new QuantumMachinePart(Bounds.of(0, 0), Bounds.of(0, 0), Bounds.of(0, 0), Bounds.of(0, 0));

        /*
         * A `QuantumMachinePart` that contains every possible part.
         */
        static final QuantumMachinePart MAX = new QuantumMachinePart(Bounds.of(1, 4000), Bounds.of(1, 4000), Bounds.of(1, 4000), Bounds.of(1, 4000));


        // Helper Methods

        /*
         * Determine the number of unique `MachinePart`s that are represented by
         * this `QuantumMachinePart`.
         */
        long size() {
            return (long) x.length() * m.length() * a.length() * s.length();
        }

        /*
         * Split this `QuantumMachinePart` into two. The first
         * `QuantumMachinePart` represents the `MachinePart`s that would not
         * satisfy a `>` condition, and the second `QuantumMachinePart`
         * represents those parts that would.
         */
        QuantumMachinePart[] splitGreaterThan(final char attribute, final int limit) {
            final Bounds[] dx = upperLimit('x', attribute, x, limit);
            final Bounds[] dm = upperLimit('m', attribute, m, limit);
            final Bounds[] da = upperLimit('a', attribute, a, limit);
            final Bounds[] ds = upperLimit('s', attribute, s, limit);
            return new QuantumMachinePart[]{
                    new QuantumMachinePart(dx[0], dm[0], da[0], ds[0]),
                    new QuantumMachinePart(dx[1], dm[1], da[1], ds[1])
            };
        }

        /*
         * Split this `QuantumMachinePart` into two. The first
         * `QuantumMachinePart` represents the `MachinePart`s that would satisfy
         * a `<` condition, and the second `QuantumMachinePart` represents those
         * parts that would not.
         */
        QuantumMachinePart[] splitLessThan(final char attribute, final int limit) {
            final Bounds[] dx = lowerLimit('x', attribute, x, limit);
            final Bounds[] dm = lowerLimit('m', attribute, m, limit);
            final Bounds[] da = lowerLimit('a', attribute, a, limit);
            final Bounds[] ds = lowerLimit('s', attribute, s, limit);
            return new QuantumMachinePart[]{
                    new QuantumMachinePart(dx[0], dm[0], da[0], ds[0]),
                    new QuantumMachinePart(dx[1], dm[1], da[1], ds[1])
            };
        }

        // Private Helper Methods

        /*
         * Calculate a pair of `Bounds` that satisfy the split of a specific
         * attribute below a certain value.
         */
        private static Bounds[] lowerLimit(final char thisAttribute, final char attribute, final Bounds thisValue, final int limit) {
            if (thisAttribute != attribute) return new Bounds[]{thisValue, thisValue};
            if (thisValue.max() < limit) return new Bounds[]{thisValue, Bounds.ZERO};
            return new Bounds[]{
                    Bounds.of(thisValue.min(), limit - 1),
                    Bounds.of(limit, thisValue.max()),
            };
        }

        /*
         * Calculate a pair of `Bounds` that satisfy the split of a specific
         * attribute above a certain value.
         */
        private static Bounds[] upperLimit(final char thisAttribute, final char attribute, final Bounds thisValue, final int limit) {
            if (thisAttribute != attribute) return new Bounds[]{thisValue, thisValue};
            if (thisValue.min() > limit) return new Bounds[]{Bounds.ZERO, thisValue};
            return new Bounds[]{
                    Bounds.of(thisValue.min(), limit),
                    Bounds.of(limit + 1, thisValue.max()),
            };
        }

    }

}

