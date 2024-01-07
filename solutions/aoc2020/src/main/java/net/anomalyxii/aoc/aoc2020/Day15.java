package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 15.
 */
@Solution(year = 2020, day = 15, title = "Rambunctious Recitation")
public class Day15 {

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
        final int[] startingNumbers = context.stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .mapToInt(Integer::parseInt)
                .toArray();
        return playGame(2020, startingNumbers);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final int[] startingNumbers = context.stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .mapToInt(Integer::parseInt)
                .toArray();
        return playGame(30000000, startingNumbers);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /**
     * Play the game, play the game, play the game....
     *
     * @param rounds the number of rounds to play for
     * @param inputs the starting numbers
     * @return the final number
     */
    public static long playGame(final int rounds, final int... inputs) {
        final int[][] memo = new int[rounds][];

        int previousNumber = 0;
        for (int i = 0; i < rounds; i++) {
            final int thisNumber = i < inputs.length
                    ? inputs[i]
                    : memo[previousNumber] != null
                            ? memo[previousNumber][0] - memo[previousNumber][1]
                            : 0;

            if (memo[thisNumber] == null) {
                memo[thisNumber] = new int[]{i, i};
            } else {
                memo[thisNumber][1] = memo[thisNumber][0];
                memo[thisNumber][0] = i;
            }

            previousNumber = thisNumber;
        }

        return previousNumber;
    }

}
