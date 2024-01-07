package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 10.
 */
@Solution(year = 2021, day = 10, title = "Syntax Scoring")
public class Day10 {

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
        final List<OptionalLong> maybeScores = context.process(line -> processLine(
                line,
                corrupted -> OptionalLong.of(corrupted.corruptedScore()),
                incomplete -> OptionalLong.empty()
        ));

        return maybeScores.stream()
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<OptionalLong> maybeScores = context.process(line -> processLine(
                line,
                corrupted -> OptionalLong.empty(),
                incomplete -> OptionalLong.of(Stream.of(incomplete)
                                                      .mapToLong(CloseBracket::incompleteScore)
                                                      .reduce(0L, (result, next) -> (result * 5) + next))
        ));

        final long[] scores = maybeScores.stream()
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .sorted()
                .toArray();
        return scores[scores.length / 2];
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Process a line containing a syntax error.s
     */
    private static OptionalLong processLine(
            final String line,
            final Function<CloseBracket, OptionalLong> corrupted,
            final Function<CloseBracket[], OptionalLong> incomplete
    ) {

        final Deque<OpenBracket> stack = new ArrayDeque<>();
        for (final char chr : line.toCharArray()) {
            final Bracket bracket = Bracket.parse(chr);
            if (bracket.isOpen()) {
                stack.addFirst((OpenBracket) bracket);
                continue;
            }

            final OpenBracket prev = stack.removeFirst();
            if (prev.complements(bracket)) continue;

            return corrupted.apply((CloseBracket) bracket);
        }

        final CloseBracket[] missingChars = stack.stream()
                .map(OpenBracket::complement)
                .toArray(CloseBracket[]::new);
        return incomplete.apply(missingChars);

    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Represents a type of bracket.
     */
    private interface Bracket {

        // Interface Methods

        /*
         * Return the complementing `Bracket`.
         */
        Bracket complement();

        /*
         * Check if this is an opening or closing `Bracket`.
         */
        boolean isOpen();

        /*
         * Check if the given `Bracket` is the complement of this one.
         */
        default boolean complements(final Bracket other) {
            return complement().equals(other);
        }

        // Static Helper Methods

        /*
         * Parse the bracket character.
         */
        static Bracket parse(final char chr) {
            return switch (chr) {
                case '(' -> OpenBracket.PARENTHESIS;
                case ')' -> CloseBracket.PARENTHESIS;
                case '[' -> OpenBracket.SQUARE;
                case ']' -> CloseBracket.SQUARE;
                case '{' -> OpenBracket.BRACE;
                case '}' -> CloseBracket.BRACE;
                case '<' -> OpenBracket.ANGLE;
                case '>' -> CloseBracket.ANGLE;
                default -> throw new IllegalStateException("Invalid bracket: '" + chr + "'");
            };
        }
    }

    /*
     * An opening bracket: `(`, `[`, `{`, `<`.
     */
    private enum OpenBracket implements Bracket {

        PARENTHESIS,
        SQUARE,
        BRACE,
        ANGLE,

        // End of constants
        ;

        // Bracket Methods

        @Override
        public CloseBracket complement() {
            return CloseBracket.valueOf(name()); // Bit hacky, but we get circular references
        }

        @Override
        public boolean isOpen() {
            return true;
        }
    }

    /*
     * A closing bracket: `)`, `]`, `}`, `>`.
     */
    private enum CloseBracket implements Bracket {

        PARENTHESIS(3, 1),
        SQUARE(57, 2),
        BRACE(1197, 3),
        ANGLE(25137, 4),

        // End of constants
        ;

        private final long corruptedScore;
        private final long incompleteScore;

        // Constructors

        CloseBracket(final long corruptedScore, final long incompleteScore) {
            this.corruptedScore = corruptedScore;
            this.incompleteScore = incompleteScore;
        }

        // Bracket Methods

        @Override
        public OpenBracket complement() {
            return OpenBracket.valueOf(name()); // Bit hacky, but we get circular references
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        // Helper Methods

        /*
         * Get the corrupted score for this `Bracket`.
         */
        long corruptedScore() {
            return corruptedScore;
        }

        /*
         * Get the incomplete score for this `Bracket`.
         */
        long incompleteScore() {
            return incompleteScore;
        }

    }

}
