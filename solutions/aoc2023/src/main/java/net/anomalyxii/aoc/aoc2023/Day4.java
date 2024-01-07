package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 4.
 */
@Solution(year = 2023, day = 4, title = "Scratchcards")
public class Day4 {

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
        return context.stream()
                .mapToInt(Day4::scoreScratchCard)
                .map(score -> score == 0 ? 0 : 1 << (score - 1))
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
        final int[] scores = scoreScratchCards(context.stream());
        final int[] counts = new int[scores.length];

        int total = 0;
        for (int id = 0; id < scores.length; id++) {
            final int count = ++counts[id];
            ++total;

            for (int s = 1; s <= scores[id]; s++) {
                total += count;
                counts[id + s] += count;
            }
        }

        return total;
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
    public IntTuple calculateAnswers(final SolutionContext context) {
        final int[] scores = scoreScratchCards(context.stream());
        final int[] counts = new int[scores.length];

        int part1 = 0;
        int part2 = 0;
        for (int id = 0; id < scores.length; id++) {
            // Part 1
            part1 += scores[id] == 0 ? 0 : 1 << (scores[id] - 1);

            // Part 2
            final int count = ++counts[id];
            ++part2;

            for (int s = 1; s <= scores[id]; s++) {
                part2 += count;
                counts[id + s] += count;
            }
        }

        return new IntTuple(part1, part2);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Calculate the scores of various scratchcards.
     */
    private static int[] scoreScratchCards(final Stream<String> lines) {
        return lines.mapToInt(Day4::scoreScratchCard).toArray();
    }

    /*
     * Calculate the score of a scratchcard.
     */
    private static int scoreScratchCard(final String line) {
        final String[] parts = line.split(":\\s*", 2);
        return calculateScore(parts[1]);
    }

    /*
     * Calculate the score of the numbers on a scratchcard.
     */
    private static int calculateScore(final String numbers) {
        final String[] parts = numbers.split("\\s*[|]\\s*");
        final Set<Integer> winningNumbers = stream(parts[0].split("\\s+"))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());

        final Set<Integer> matches = stream(parts[1].split("\\s+"))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
        matches.retainAll(winningNumbers);
        return matches.size();
    }

}

