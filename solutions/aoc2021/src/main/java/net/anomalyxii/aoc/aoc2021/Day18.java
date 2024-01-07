package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 18.
 */
@Solution(year = 2021, day = 18, title = "Snailfish")
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
        return context.process(SnailfishNumber::parse).stream()
                .reduce(Day18::add)
                .orElseThrow()
                .magnitude();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<SnailfishNumber> numbers = context.process(SnailfishNumber::parse);
        return numbers.stream()
                .mapToLong(number -> numbers.stream()
                        .filter(other -> number != other)
                        .map(other -> add(number, other))
                        .mapToLong(SnailfishNumber::magnitude)
                        .max()
                        .orElse(0)
                )
                .max()
                .orElseThrow();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Add the two `SnailfishNumber`s together.
     */
    private static SnailfishNumber add(final SnailfishNumber number, final SnailfishNumber other) {
        return reduce(new CompoundNumber(number.adjustDepth(1), other.adjustDepth(1), 0));
    }

    /*
     * Reduce the given `SnailfishNumber`.
     */
    private static SnailfishNumber reduce(final SnailfishNumber number) {
        SnailfishNumber current = number;
        Optional<SnailfishNumber> next = Optional.of(current);
        while (next.isPresent()) {
            current = next.get();
            next = reduceOnce(current);
        }
        return current;
    }

    /*
     * Perform a single reduction on the given `SnailfishNumber`.
     */
    private static Optional<SnailfishNumber> reduceOnce(final SnailfishNumber number) {
        final ExplosionResult explosionResult = number.maybeExplode();
        if (explosionResult != ExplosionResult.NO_EXPLOSION)
            return Optional.of(explosionResult.epicentre);

        final SplitResult splitResult = number.maybeSplit();
        if (splitResult != SplitResult.NO_SPLIT)
            return Optional.of(splitResult.result);

        return Optional.empty();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A number used in snailfish arithmetic.
     */
    private sealed interface SnailfishNumber permits SimpleNumber, CompoundNumber {

        // Interface Methods

        /*
         * Calculate the magnitude of this `SnailfishNumber`.
         */
        long magnitude();

        /*
         * Adjust the left-hand side of this `SnailfishNumber` by the given
         * amount.
         */
        SnailfishNumber adjustLeft(long amount);

        /*
         * Adjust the right-hand side of this `SnailfishNumber` by the given
         * amount.
         */
        SnailfishNumber adjustRight(long amount);

        /*
         * Adjust the depth of this `SnailfishNumber` by the given amount.
         */
        SnailfishNumber adjustDepth(int amount);

        /*
         * Trigger an explosion, if possible, on this `SnailfishNumber`.
         */
        ExplosionResult maybeExplode();

        /*
         * Trigger a split, if possible, on this `SnailfishNumber`.
         */
        SplitResult maybeSplit();

        // Static Helper Methods

        /*
         * Parse a `SnailfishNumber` from the given line.
         */
        static SnailfishNumber parse(final String line) {
            final SnailfishNumberBuilder builder = new SnailfishNumberBuilder();
            for (int i = 0; i < line.length(); i++) {
                final char c = line.charAt(i);
                switch (c) {
                    case '[' -> builder.onStartCompound();
                    case ']' -> builder.onEndCompound();
                    case ',' -> builder.onNextInPair();
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> builder.onRegularDigit(c - '0');
                    default -> throw new IllegalArgumentException("Invalid character '" + c + "' in Snailfish number");
                }
            }
            return builder.build();
        }
    }

    /*
     * A "simple" number.
     */
    private record SimpleNumber(long value, int depth) implements SnailfishNumber {

        // SnailfishNumber Methods

        @Override
        public long magnitude() {
            return value;
        }

        @Override
        public SnailfishNumber adjustLeft(final long amount) {
            return new SimpleNumber(value + amount, depth);
        }

        @Override
        public SnailfishNumber adjustRight(final long amount) {
            return new SimpleNumber(value + amount, depth);
        }

        @Override
        public SnailfishNumber adjustDepth(final int amount) {
            return new SimpleNumber(value, depth + amount);
        }

        @Override
        public ExplosionResult maybeExplode() {
            return ExplosionResult.NO_EXPLOSION;
        }

        @Override
        public SplitResult maybeSplit() {
            return value > 9
                    ? SplitResult.of(this)
                    : SplitResult.NO_SPLIT;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }

    /*
     * A "compound" number.
     */
    private record CompoundNumber(SnailfishNumber left, SnailfishNumber right, int depth) implements SnailfishNumber {

        // SnailfishNumber Methods

        @Override
        public long magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        @Override
        public SnailfishNumber adjustLeft(final long amount) {
            return new CompoundNumber(left.adjustLeft(amount), right, depth);
        }

        @Override
        public SnailfishNumber adjustRight(final long amount) {
            return new CompoundNumber(left, right.adjustRight(amount), depth);
        }

        @Override
        public SnailfishNumber adjustDepth(final int amount) {
            return new CompoundNumber(left.adjustDepth(1), right.adjustDepth(1), depth + amount);
        }

        @Override
        public ExplosionResult maybeExplode() {
            if (depth >= 4) return ExplosionResult.of(this);

            final ExplosionResult leftExplosion = left.maybeExplode();
            if (leftExplosion != ExplosionResult.NO_EXPLOSION)
                return leftExplosion.consumeRight(right);

            final ExplosionResult rightExplosion = right.maybeExplode();
            if (rightExplosion != ExplosionResult.NO_EXPLOSION)
                return rightExplosion.consumeLeft(left);

            return ExplosionResult.NO_EXPLOSION;
        }

        @Override
        public SplitResult maybeSplit() {
            final SplitResult leftSplit = left.maybeSplit();
            if (leftSplit != SplitResult.NO_SPLIT)
                return leftSplit.consumeRight(right);
            final SplitResult rightSplit = right.maybeSplit();
            if (rightSplit != SplitResult.NO_SPLIT)
                return rightSplit.consumeLeft(left);
            return SplitResult.NO_SPLIT;
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + "]";
        }

    }

    /*
     * Build a `SnailfishNumber`.
     */
    private static final class SnailfishNumberBuilder {

        // Private Members

        private int depth = 0;
        private final Deque<SnailfishNumber> stack = new ArrayDeque<>();

        // Builder Methods

        /*
         * A new pair (a.k.a. "compound" number) has started.
         */
        public void onStartCompound() {
            ++depth;
        }

        /*
         * A new pair (a.k.a. "compound" number) has ended.
         */
        public void onEndCompound() {
            assert stack.size() >= 2;
            final SnailfishNumber right = stack.removeLast();
            final SnailfishNumber left = stack.removeLast();
            --depth;
            assert depth >= 0;
            stack.addLast(new CompoundNumber(left, right, depth));
        }

        /*
         * The first element in a pair has ended.
         */
        public void onNextInPair() {
            // No-op?
            assert !stack.isEmpty();
        }

        /*
         * A simple number has been read.
         */
        public void onRegularDigit(final int i) {
            stack.addLast(new SimpleNumber(i, depth));
        }

        /*
         * Build the `SnailfishNumber`.
         */
        public SnailfishNumber build() {
            assert stack.size() <= 1;
            return stack.isEmpty()
                    ? null
                    : stack.removeLast();
        }

    }

    /*
     * The result of exploding a `SnailfishNumber`.
     */
    private record ExplosionResult(long left, long right, int depth, SnailfishNumber epicentre) {

        /*
         * Indicates no explosion occurred.
         */
        static final ExplosionResult NO_EXPLOSION = new ExplosionResult(0, 0, 0, null);

        // Helper Methods

        /*
         * Return a new `ExplosionResult` as a result of consuming the left-hand
         * side of a `CompoundNumber` pair.
         */
        ExplosionResult consumeLeft(final SnailfishNumber number) {
            final SnailfishNumber newEpicentre = left > 0
                    ? new CompoundNumber(number.adjustRight(left), epicentre, depth - 1)
                    : new CompoundNumber(number, epicentre, depth - 1);
            return new ExplosionResult(0, right, depth - 1, newEpicentre);
        }

        /*
         * Return a new `ExplosionResult` as a result of consuming the right-hand
         * side of a `CompoundNumber` pair.
         */
        ExplosionResult consumeRight(final SnailfishNumber number) {
            final SnailfishNumber newEpicentre = right > 0
                    ? new CompoundNumber(epicentre, number.adjustLeft(right), depth - 1)
                    : new CompoundNumber(epicentre, number, depth - 1);
            return new ExplosionResult(left, 0, depth - 1, newEpicentre);
        }

        // Static Helper Methods

        /*
         * Create a new explosion for the given `CompoundNumber`.
         */
        static ExplosionResult of(final CompoundNumber number) {
            return new ExplosionResult(
                    number.left.magnitude(),
                    number.right.magnitude(),
                    number.depth,
                    new SimpleNumber(0, number.depth)
            );
        }

    }

    /*
     * The result of splitting a `SnailfishNumber`.
     */
    private record SplitResult(int depth, CompoundNumber result) {

        /*
         * Indicates no split occurred.
         */
        public static final SplitResult NO_SPLIT = new SplitResult(0, null);

        // Helper Methods

        /*
         * Return a new `SplitResult` as a result of consuming the left-hand side
         * of a `CompoundNumber` pair.
         */
        public SplitResult consumeLeft(final SnailfishNumber left) {
            return new SplitResult(depth - 1, new CompoundNumber(left, result, depth - 1));
        }

        /*
         * Return a new `SplitResult` as a result of consuming the right-hand
         * side of a `CompoundNumber` pair.
         */
        public SplitResult consumeRight(final SnailfishNumber right) {
            return new SplitResult(depth - 1, new CompoundNumber(result, right, depth - 1));
        }

        // Static Helper Methods

        /*
         * Creates a new split for the given `SimpleNumber`.
         */
        static SplitResult of(final SimpleNumber number) {
            return new SplitResult(
                    number.depth,
                    new CompoundNumber(
                            new SimpleNumber(number.value / 2, number.depth + 1),
                            new SimpleNumber((number.value + 1) / 2, number.depth + 1),
                            number.depth
                    )
            );
        }
    }

}
