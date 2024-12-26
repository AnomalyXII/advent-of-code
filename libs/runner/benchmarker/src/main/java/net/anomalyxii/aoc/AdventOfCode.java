package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.Tuple;

import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Run a given challenge in a format suitable for the
 * <a href="https://github.com/ShaneMcC/AoCBench/">AoC Bench</a>
 * framework to use.
 */
public class AdventOfCode {

    // ****************************************
    // Main Method
    // ****************************************

    /**
     * Run the Advent of Code solutions.
     *
     * @param args any command line arguments
     */
    public static void main(final String[] args) {
        // Validate at least a year and a day has been given
        if (args.length < 3) {
            System.err.println("Usage: AdventOfCode <year> <day> <input>");
            System.exit(1);
            return;
        }

        final int year = Integer.parseInt(args[0]);
        final int day = Integer.parseInt(args[1]);

        final SolutionWrapper<?, ?> challenge = findChallenge(year, day);

        final SolutionContext context = loadContext(args[2]);
        final Tuple<?, ?> results = challenge.calculateAnswers(context);

        System.out.print("Part 1: ");
        System.out.println(results.getAnswer1());

        System.out.print("Part 2: ");
        System.out.println(results.getAnswer2());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Look up solutions for a given year and day.
     */
    private static SolutionWrapper<?, ?> findChallenge(final int year, final int day) {
        return findChallengesForYear(year).solutions(day);
    }

    /*
     * Look up solutions for a given year.
     */
    private static Solutions findChallengesForYear(final int year) {
        return switch (year) {
            case 2020 -> Solutions2020.AOC_2020;
            case 2021 -> Solutions2021.AOC_2021;
            case 2022 -> Solutions2022.AOC_2022;
            case 2023 -> Solutions2023.AOC_2023;
            case 2024 -> Solutions2024.AOC_2024;
            default -> fatalError("Unable to find any challenges for year " + year);
        };
    }

    /*
     * Load an input file to solve for.
     */
    private static SolutionContext loadContext(final String input) {
        final Path inputPath = Path.of(input).toAbsolutePath();
        if (!Files.exists(inputPath))
            return fatalError("Failed to find challenge input: " + input);

        return SolutionContext.builder().path(inputPath.toString()).build();
    }

    /*
     * Print an error message and exit.
     */
    private static <T> T fatalError(final String message) {
        System.err.println(message);
        System.exit(2);

        throw new IllegalStateException(message);
    }

}
