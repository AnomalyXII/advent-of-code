package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 6.
 */
@Solution(year = 2020, day = 6, title = "Custom Customs")
public class Day6 {

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
        return context.streamBatches()
                .map(group -> toCharSet(group.stream().flatMapToInt(String::chars)))
                .mapToLong(Set::size).sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Set<Character> allQuestions = toCharSet("abcdefghijklmnopqrstuvwxyz".chars());
        return context.streamBatches()
                .map(group -> {
                    final Set<Character> remainingQuestions = new HashSet<>(allQuestions);
                    group.forEach(answers -> remainingQuestions.retainAll(toCharSet(answers.chars())));
                    return remainingQuestions;
                })
                .mapToLong(Set::size)
                .sum();
    }

    // ****************************************
    // Private Helper Members
    // ****************************************

    /*
     * Convert the given stream of ASCII to a Set of boxed Characters.
     */
    private static Set<Character> toCharSet(final IntStream asciiStream) {
        return asciiStream
                .mapToObj(ascii -> Character.toLowerCase((char) ascii))
                .collect(Collectors.toSet());
    }

}
