package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 21.
 */
@Solution(year = 2021, day = 21, title = "Dirac Dice")
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
        GameState state = loadInitialState(context);
        int turn = 0;
        do {
            final int roll = (6 + ((++turn - 1) * 9)) % 100;
            state = state.update(roll);
        } while (state.score1 < 1000 && state.score2 < 1000);

        return ((long) turn * 3) * Math.min(state.score1, state.score2);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final GameState state = loadInitialState(context);
        final DiracResult result = playQuantumGame(state, new HashMap<>());

        return Math.max(result.player1GamesWon, result.player2GamesWon);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Load the initial game state.
     */
    private GameState loadInitialState(final SolutionContext context) {
        final List<String> lines = context.read();

        final String first = lines.getFirst();
        final String second = lines.getLast();
        final int player1Pos = Integer.parseInt(first.substring(first.indexOf(':') + 2));
        final int player2Pos = Integer.parseInt(second.substring(second.indexOf(':') + 2));

        return new GameState(true, player1Pos, 0, player2Pos, 0);
    }

    /*
     * Play a game using the quantum die, caching the result.
     */
    private DiracResult playQuantumGame(final GameState state, final Map<GameState, DiracResult> cache) {
        final DiracResult cached = cache.get(state);
        if (cached != null) return cached;

        final DiracResult result = resolveState(state, cache);
        cache.put(state, result);
        return result;
    }

    /*
     * Play a game using the quantum die.
     */
    private DiracResult resolveState(final GameState state, final Map<GameState, DiracResult> cache) {
        if (state.isP1Turn && state.score1 >= 20) return new DiracResult(27, 0);
        if (!state.isP1Turn && state.score2 >= 20) return new DiracResult(0, 27);

        // 1 1 1
        // 1 1 2    ||    1 2 1    ||    2 1 1
        // 1 1 3    ||    1 3 1    ||    3 1 1    ||    1 2 2    ||    2 1 2    ||    2 2 1
        // 1 2 3    ||    2 1 3    ||    1 3 2    ||    2 3 1    ||    3 2 1    ||    3 1 2    ||    2 2 2
        // 1 3 3    ||    3 1 3    ||    3 3 1    ||    2 2 3    ||    2 3 2    ||    3 2 2
        // 2 3 3    ||    3 2 3    ||    3 3 2
        // 3 3 3
        final DiracResult r3 = advanceQuantumGame(3, state, cache);
        final DiracResult r4 = advanceQuantumGame(4, state, cache);
        final DiracResult r5 = advanceQuantumGame(5, state, cache);
        final DiracResult r6 = advanceQuantumGame(6, state, cache);
        final DiracResult r7 = advanceQuantumGame(7, state, cache);
        final DiracResult r8 = advanceQuantumGame(8, state, cache);
        final DiracResult r9 = advanceQuantumGame(9, state, cache);

        return new DiracResult(
                r3.player1GamesWon
                        + (r4.player1GamesWon * 3)
                        + (r5.player1GamesWon * 6)
                        + (r6.player1GamesWon * 7)
                        + (r7.player1GamesWon * 6)
                        + (r8.player1GamesWon * 3)
                        + r9.player1GamesWon,
                r3.player2GamesWon
                        + (r4.player2GamesWon * 3)
                        + (r5.player2GamesWon * 6)
                        + (r6.player2GamesWon * 6)
                        + (r7.player2GamesWon * 6)
                        + (r8.player2GamesWon * 3)
                        + r9.player2GamesWon
        );
    }

    /*
     * Advance a game being played with a quantum die.
     */
    private DiracResult advanceQuantumGame(final int roll, final GameState state, final Map<GameState, DiracResult> cache) {
        if (state.isP1Turn) {
            final int newPosition = calculateNewPos(state.position1, roll);
            final int newScore = state.score1 + newPosition;
            if (newScore >= 21) return new DiracResult(1, 0);
            return playQuantumGame(new GameState(false, newPosition, newScore, state.position2, state.score2), cache);
        } else {
            final int newPosition = calculateNewPos(state.position2, roll);
            final int newScore = state.score2 + newPosition;
            if (newScore >= 21) return new DiracResult(0, 1);
            return playQuantumGame(new GameState(true, state.position1, state.score1, newPosition, newScore), cache);
        }
    }

    /*
     * Calculate the new position of a player.
     */
    private static int calculateNewPos(final int pos, final int roll) {
        final int totalPos = pos + roll;
        return ((totalPos - 1) % 10) + 1;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * The state of the game at any given point.
     */
    private record GameState(boolean isP1Turn, int position1, int score1, int position2, int score2) {

        // Helper Methods

        GameState update(final int roll) {
            if (isP1Turn) {
                final int newPosition = calculateNewPos(position1, roll);
                final int newScore = score1 + newPosition;
                return new GameState(false, newPosition, newScore, position2, score2);
            } else {
                final int newPosition = calculateNewPos(position2, roll);
                final int newScore = score2 + newPosition;
                return new GameState(true, position1, score1, newPosition, newScore);
            }
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final GameState gameState = (GameState) o;
            return isP1Turn == gameState.isP1Turn
                    && position1 == gameState.position1
                    && score1 == gameState.score1
                    && position2 == gameState.position2
                    && score2 == gameState.score2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(isP1Turn, position1, score1, position2, score2);
        }

    }

    /*
     * The result of playing one, universe splitting, game.
     */
    private record DiracResult(long player1GamesWon, long player2GamesWon) {
    }

}
