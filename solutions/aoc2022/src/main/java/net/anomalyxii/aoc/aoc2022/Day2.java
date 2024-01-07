package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 2.
 */
@Solution(year = 2022, day = 2, title = "Rock Paper Scissors")
public class Day2 {

    private static final int ROCK_SCORE = 1;
    private static final int PAPER_SCORE = 2;
    private static final int SCISSORS_SCORE = 3;

    private static final int LOSS_SCORE = 0;
    private static final int DRAW_SCORE = 3;
    private static final int WIN_SCORE = 6;

    private static final short[][] MATCH_UP_SCORES = {
            {ROCK_SCORE + DRAW_SCORE, PAPER_SCORE + WIN_SCORE, SCISSORS_SCORE + LOSS_SCORE},
            {ROCK_SCORE + LOSS_SCORE, PAPER_SCORE + DRAW_SCORE, SCISSORS_SCORE + WIN_SCORE},
            {ROCK_SCORE + WIN_SCORE, PAPER_SCORE + LOSS_SCORE, SCISSORS_SCORE + DRAW_SCORE},
    };
    private static final short[][] OUTCOME_SCORES = {
            {SCISSORS_SCORE + LOSS_SCORE, ROCK_SCORE + DRAW_SCORE, PAPER_SCORE + WIN_SCORE},
            {ROCK_SCORE + LOSS_SCORE, PAPER_SCORE + DRAW_SCORE, SCISSORS_SCORE + WIN_SCORE},
            {PAPER_SCORE + LOSS_SCORE, SCISSORS_SCORE + DRAW_SCORE, ROCK_SCORE + WIN_SCORE},
    };

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
        return context.stream()
                .mapToLong(line -> MATCH_UP_SCORES[line.charAt(0) - 'A'][line.charAt(2) - 'X'])
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
        return context.stream()
                .mapToLong(line -> OUTCOME_SCORES[line.charAt(0) - 'A'][line.charAt(2) - 'X'])
                .sum();
    }

}
