package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 22.
 */
@Solution(year = 2020, day = 22, title = "Crab Combat")
public class Day22 {

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
        final Deque<Integer> player1 = new ArrayDeque<>();
        final Deque<Integer> player2 = new ArrayDeque<>();

        buildDecks(context, player1, player2);

        final Deque<Integer> winningDeck = playSimpleGame(player1, player2);

        return calculateWinningScore(winningDeck);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Deque<Integer> player1 = new ArrayDeque<>();
        final Deque<Integer> player2 = new ArrayDeque<>();

        buildDecks(context, player1, player2);

        final Deque<Integer> winningDeck = playRecursiveGame(player1, player2);

        return calculateWinningScore(winningDeck);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Calculate the winning score.
     *
     * @param cards the winning cards
     * @return the score
     */
    static long calculateWinningScore(final Deque<Integer> cards) {
        int count = 0;
        long result = 0;
        while (!cards.isEmpty()) {
            final int nextCard = cards.removeLast();
            result += ((long) ++count * nextCard);
        }
        return result;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Build the decks for the two players.
     */
    private void buildDecks(final SolutionContext context, final Deque<Integer> player1, final Deque<Integer> player2) {
        final String[] player = new String[1];
        context.consume(line -> {
            if (line.isBlank()) {
                return;
            }

            if (line.startsWith("Player")) {
                player[0] = line;
                return;
            }

            if (player[0] == null) {
                throw new IllegalStateException("No player specified!");
            }

            ("Player 1:".equals(player[0]) ? player1 : player2).addLast(Integer.parseInt(line));
        });
    }

    /*
     * Play a simple game of Combat with the crab.
     */
    private static Deque<Integer> playSimpleGame(final Deque<Integer> player1, final Deque<Integer> player2) {
        while (!player1.isEmpty() && !player2.isEmpty()) {
            final int card1 = player1.removeFirst();
            final int card2 = player2.removeFirst();

            if (card1 > card2) {
                player1.addLast(card1);
                player1.addLast(card2);
            } else if (card2 > card1) {
                player2.addLast(card2);
                player2.addLast(card1);
            } else {
                throw new IllegalStateException("Should never be a tie!");
            }
        }
        return player2.isEmpty() ? player1 : player2;
    }

    /*
     * Play a simple game of Combat with the crab.
     */
    private static Deque<Integer> playRecursiveGame(final Deque<Integer> player1, final Deque<Integer> player2) {
        return determineIfPlayer1WinsRecursiveGame(player1, player2, 1) ? player1 : player2;
    }

    /*
     * Run a (recursive) game of Combat and determine if player 1 wins.
     */
    private static boolean determineIfPlayer1WinsRecursiveGame(
            final Deque<Integer> player1,
            final Deque<Integer> player2,
            final int gameNo
    ) {
        final Set<String> previousRoundDecks = new HashSet<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {

            // Check for previous deck
            final String deck = deck2str(player1) + "|" + deck2str(player2);
            if (!previousRoundDecks.add(deck)) {
                // End the _game_ in a win for player 1...
                return true;
            }

            final int card1 = player1.removeFirst();
            final int card2 = player2.removeFirst();

            final boolean player1WinsRound;
            if (card1 <= player1.size() && card2 <= player2.size()) {
                final Deque<Integer> player1Sub = new ArrayDeque<>(new ArrayList<>(player1).subList(0, card1));
                final Deque<Integer> player2Sub = new ArrayDeque<>(new ArrayList<>(player2).subList(0, card2));
                player1WinsRound = determineIfPlayer1WinsRecursiveGame(player1Sub, player2Sub, gameNo + 1);
            } else {
                player1WinsRound = (card1 > card2);
            }

            // System.out.println("Player " + (player1WinsRound ? "1" : "2") + " wins round " + round + " of game " + gameNo);
            if (player1WinsRound) {
                player1.addLast(card1);
                player1.addLast(card2);
            } else {
                player2.addLast(card2);
                player2.addLast(card1);
            }
        }

        return player2.isEmpty();
    }

    /*
     * Serialise the provided deck.
     */
    private static String deck2str(final Deque<Integer> player1) {
        return player1.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

}
