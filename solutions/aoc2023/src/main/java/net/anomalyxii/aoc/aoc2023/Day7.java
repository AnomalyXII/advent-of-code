package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 7.
 */
@Solution(year = 2023, day = 7, title = "Camel Cards")
public class Day7 {

    /*
     * Index of the Jokers.
     */
    private static final int JARED_LETOS = 0;

    /*
     * The relative score of various cards:
     */
    private static final int VALUE_JOKER = 0;
    private static final int VALUE_2 = 1;
    private static final int VALUE_3 = 2;
    private static final int VALUE_4 = 3;
    private static final int VALUE_5 = 4;
    private static final int VALUE_6 = 5;
    private static final int VALUE_7 = 6;
    private static final int VALUE_8 = 7;
    private static final int VALUE_9 = 8;
    private static final int VALUE_10 = 9;
    private static final int VALUE_JACK = 10;
    private static final int VALUE_QUEEN = 11;
    private static final int VALUE_KING = 12;
    private static final int VALUE_ACE = 13;

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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final Hand[] hands = context.stream()
                .map(line -> Hand.parse(line, VALUE_JACK))
                .sorted()
                .toArray(Hand[]::new);
        return IntStream.range(0, hands.length)
                .map(pos -> (hands.length - pos) * hands[pos].wager)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Hand[] hands = context.stream()
                .map(line -> Hand.parse(line, VALUE_JOKER))
                .sorted()
                .toArray(Hand[]::new);
        return IntStream.range(0, hands.length)
                .map(pos -> (hands.length - pos) * hands[pos].wager)
                .sum();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        int count = 0;
        final List<Hand> part1 = new ArrayList<>();
        final List<Hand> part2 = new ArrayList<>();
        for (final String line : context.read()) {
            ++count;
            part1.add(Hand.parse(line, VALUE_JACK));
            part2.add(Hand.parse(line, VALUE_JOKER));
        }

        part1.sort(Hand::compareTo);
        part2.sort(Hand::compareTo);

        int answer1 = 0;
        int answer2 = 0;
        for (int pos = 0; pos < count; pos++) {
            answer1 += (part1.size() - pos) * part1.get(pos).wager;
            answer2 += (part2.size() - pos) * part2.get(pos).wager;
        }

        return new IntTuple(answer1, answer2);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The kind of hand being held.
     */
    private enum Kind {

        /*
         * 5 cards of the same value.
         */
        FIVE_OF_A_KIND {
            @Override
            protected Kind combine(final Kind other) {
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * 4 cards of the same value.
         */
        FOUR_OF_A_KIND {
            @Override
            protected Kind combine(final Kind other) {
                if (other == HIGH_CARD) return FOUR_OF_A_KIND;
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return FIVE_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * 1 pair and 1 3-of-a-kind.
         */
        FULL_HOUSE {
            @Override
            protected Kind combine(final Kind other) {
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return FOUR_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * 3 cards of the same value.
         */
        THREE_OF_A_KIND {
            @Override
            protected Kind combine(final Kind other) {
                if (other == ONE_PAIR) return FULL_HOUSE;
                if (other == HIGH_CARD) return THREE_OF_A_KIND;
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return FOUR_OF_A_KIND;
                if (numJokers == 2) return FIVE_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * 2 pairs (what else is there to say?).
         */
        TWO_PAIR {
            @Override
            protected Kind combine(final Kind other) {
                if (other == HIGH_CARD) return TWO_PAIR;
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return FULL_HOUSE;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * 2 cards of the same value.
         */
        ONE_PAIR {
            @Override
            protected Kind combine(final Kind other) {
                if (other == ONE_PAIR) return TWO_PAIR;
                if (other == THREE_OF_A_KIND) return FULL_HOUSE;
                if (other == HIGH_CARD) return ONE_PAIR;
                throw throwIllegalCombination(other);
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return THREE_OF_A_KIND;
                if (numJokers == 2) return FOUR_OF_A_KIND;
                if (numJokers == 3) return FIVE_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * Any single card.
         */
        HIGH_CARD {
            @Override
            protected Kind combine(final Kind other) {
                return other;
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return ONE_PAIR;
                if (numJokers == 2) return THREE_OF_A_KIND;
                if (numJokers == 3) return FOUR_OF_A_KIND;
                if (numJokers == 4) return FIVE_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        /*
         * I guess maybe this is what happens when you play Camel Cards with a
         * crocodile.
         */
        NONE {
            @Override
            protected Kind combine(final Kind other) {
                return other;
            }

            @Override
            protected Kind improve(final int numJokers) {
                if (numJokers == 1) return HIGH_CARD;
                if (numJokers == 2) return TWO_PAIR;
                if (numJokers == 3) return THREE_OF_A_KIND;
                if (numJokers == 4) return FOUR_OF_A_KIND;
                if (numJokers == 5) return FIVE_OF_A_KIND;
                throw throwIllegalImprovement(numJokers);
            }
        },

        // End of constants
        ;

        // Helper Methods

        /*
         * Possibly upgrade the current hand based on a new set of cards.
         */
        protected abstract Kind combine(Kind other);

        /*
         * Possibly upgrade the current hand based on a number of jokers.
         */
        protected abstract Kind improve(int numJokers);

        /*
         * Throw an error if the current hand is incompatible with the suggested
         * other hand. This would normally happen if the number of cards in the
         * combined hand would exceed 5.
         */
        protected IllegalStateException throwIllegalCombination(final Kind other) {
            return new IllegalStateException("Cannot combine " + this + " and " + other);
        }

        /*
         * Throw an error if the current hand is incompatible with the provided
         * number of jokers. This would normally happen if the number of cards
         * in the improved hand would exceed 5.
         */
        protected IllegalStateException throwIllegalImprovement(final int numJokers) {
            return new IllegalStateException("Cannot improve " + this + " with " + numJokers);
        }

        // Static Helper Methods

        /*
         * Detect what kind of hand is being held.
         */
        private static Kind detect(final int[] cards) {
            final int[] count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (final int card : cards) ++count[card];

            Kind best = NONE;
            for (int k = 1; k <= 13; k++) {
                best = switch (count[k]) {
                    case 5 -> best.combine(FIVE_OF_A_KIND);
                    case 4 -> best.combine(FOUR_OF_A_KIND);
                    case 3 -> best.combine(THREE_OF_A_KIND);
                    case 2 -> best.combine(ONE_PAIR);
                    case 1 -> best.combine(HIGH_CARD);
                    default -> best;
                };
            }

            return count[JARED_LETOS] > 0 ? best.improve(count[0]) : best;
        }

    }

    /*
     * Represents one hand in a game of cards, plus the associated wager.
     */
    private record Hand(int[] cards, int wager) implements Comparable<Hand> {

        // Comparable Methods

        @Override
        public int compareTo(final Hand o) {
            final Kind myKind = Kind.detect(cards);
            final Kind otherKind = Kind.detect(o.cards);
            final int cmp = myKind.compareTo(otherKind);
            if (cmp != 0)
                return cmp;

            // final int[] myCards = IntStream.of(cards).sorted().toArray();
            final int[] myCards = cards;
            // final int[] otherCards = IntStream.of(o.cards).sorted().toArray();
            final int[] otherCards = o.cards;
            for (int c = 0; c < myCards.length; c++)
                if (myCards[c] > otherCards[c]) return -1;
                else if (myCards[c] < otherCards[c]) return 1;

            return 0;
        }

        // Static Helper Methods

        /*
         * Parse a `Hand` from a given line.
         */
        static Hand parse(final String line, final int valueJ) {
            final String[] parts = line.split("\\s+", 2);
            return new Hand(
                    parts[0].chars()
                            .map(chr -> toCardValue(chr, valueJ))
                            .toArray(),
                    Integer.parseInt(parts[1])
            );
        }

        /*
         * Convert a single card into a comparable value.
         */
        private static int toCardValue(final int chr, final int valueJ) {
            return switch (chr) {
                case 'A' -> VALUE_ACE;
                case 'K' -> VALUE_KING;
                case 'Q' -> VALUE_QUEEN;
                case 'J' -> valueJ;
                case 'T' -> VALUE_10;
                case '9' -> VALUE_9;
                case '8' -> VALUE_8;
                case '7' -> VALUE_7;
                case '6' -> VALUE_6;
                case '5' -> VALUE_5;
                case '4' -> VALUE_4;
                case '3' -> VALUE_3;
                case '2' -> VALUE_2;
                default -> throw new IllegalArgumentException("Invalid card: " + chr);
            };
        }

    }

}

