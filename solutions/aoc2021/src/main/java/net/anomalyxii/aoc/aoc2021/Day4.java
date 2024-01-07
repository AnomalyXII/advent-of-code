package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 4.
 */
@Solution(year = 2021, day = 4, title = "Giant Squid")
public class Day4 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final List<Integer> inputs = batches.getFirst().stream()
                .flatMap(line -> Arrays.stream(line.split(",\\s*")))
                .map(Integer::valueOf)
                .toList();

        final List<Board> boards = batches.subList(1, batches.size()).stream()
                .map(Board::create)
                .toList();

        for (final Integer input : inputs) {
            final List<Board> completedBoards = boards.stream()
                    .peek(board -> board.consume(input))
                    .filter(Board::isComplete)
                    .toList();

            final int numCompletedBoards = completedBoards.size();
            if (numCompletedBoards > 1)
                throw new IllegalStateException("Multiple boards (" + numCompletedBoards + ") completed!");
            else if (numCompletedBoards == 1)
                return completedBoards.getFirst().calculateScore();
        }

        throw new IllegalStateException("The game completed with no board winning?");
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final List<Integer> inputs = batches.getFirst().stream()
                .flatMap(line -> Arrays.stream(line.split(",\\s*")))
                .map(Integer::valueOf)
                .toList();

        final List<Board> boards = batches.subList(1, batches.size()).stream()
                .map(Board::create)
                .collect(Collectors.toCollection(ArrayList::new)); // Make it mutable!

        List<Board> mostRecentlyCompleted = null;
        for (final Integer input : inputs) {
            if (boards.isEmpty()) break;

            final List<Board> completedBoards = boards.stream()
                    .peek(board -> board.consume(input))
                    .filter(Board::isComplete)
                    .toList();

            boards.removeAll(completedBoards);
            mostRecentlyCompleted = completedBoards;
        }

        if (mostRecentlyCompleted == null)
            throw new IllegalStateException("The game completed with no board winning (thus, none losing)?");
        else if (mostRecentlyCompleted.size() > 1)
            throw new IllegalStateException("More than 1 board lost :(");

        return mostRecentlyCompleted.getFirst().calculateScore();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A bingo Board.
     */
    private static final class Board {

        // Private Members

        private final int size;
        private final int[][] board;

        private int lastNumberConsumed = 0;

        // Constructors

        Board(final int size, final int[][] board) {
            this.size = size;
            this.board = board;
        }

        // Board Methods

        /*
         * Consume a number, marking it off the board if possible.
         */
        void consume(final int input) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == input) {
                        board[i][j] = Integer.MIN_VALUE;
                        lastNumberConsumed = input;
                    }
                }
            }
        }

        /*
         * Check if this `Board` is now complete.
         */
        boolean isComplete() {
            for (int i = 0; i < size; i++) {
                boolean complete = board[i][0] == Integer.MIN_VALUE;
                for (int j = 1; j < size && complete; j++) {
                    complete = board[i][j] == Integer.MIN_VALUE;
                }
                if (complete) return true;
            }

            for (int j = 0; j < size; j++) {
                boolean complete = board[0][j] == Integer.MIN_VALUE;
                for (int i = 1; i < size && complete; i++) {
                    complete = board[i][j] == Integer.MIN_VALUE;
                }
                if (complete) return true;
            }

            return false;
        }

        /*
         * Calculate the score of the board.
         */
        long calculateScore() {
            long sum = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != Integer.MIN_VALUE) {
                        sum += board[i][j];
                    }
                }
            }

            return sum * lastNumberConsumed;
        }

        // Static Helper Methods

        /*
         * Create a new `Board` from the given input.
         */
        static Board create(final List<String> inputs) {
            final int size = inputs.size();
            final int[][] board = new int[size][];
            for (int i = 0; i < inputs.size(); i++) {
                final String line = inputs.get(i).trim();
                final String[] split = line.split("\\s+", size);
                board[i] = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
            }

            return new Board(size, board);
        }
    }

}
