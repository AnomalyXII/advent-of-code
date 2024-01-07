package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 1.
 */
@Solution(year = 2023, day = 1, title = "Trebuchet?!")
public class Day1 {

    private static final Pattern NUMBERS = Pattern.compile("([0-9]|zero|one|two|three|four|five|six|seven|eight|nine)");
    private static final Map<String, String> WORDS_TO_NUMBERS = Map.of(
            "one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9",
            "zero", "0"
    );

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
                .map(line -> NUMBERS.matcher(line).results()
                        .map(MatchResult::group)
                        .filter(mr -> mr.length() == 1)
                        .collect(Collectors.toList()))
                .map(matches -> matches.getFirst() + matches.getLast())
                .mapToInt(Integer::parseInt)
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
                .map(line -> Stream.iterate(NUMBERS.matcher(line), m -> m.find(m.hasMatch() ? m.toMatchResult().start() + 1 : 0), m -> m)
                        .map(Matcher::toMatchResult)
                        .distinct()
                        .map(MatchResult::group)
                        .map(str -> WORDS_TO_NUMBERS.getOrDefault(str, str))
                        .collect(Collectors.toList()))
                .map(matches -> matches.getFirst() + matches.getLast())
                .mapToInt(Integer::parseInt)
                .sum();
    }

}

