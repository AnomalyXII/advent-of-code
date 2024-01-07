package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 2.
 */
@Solution(year = 2023, day = 2, title = "Cube Conundrum")
public class Day2 {

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
                .map(Game::parse)
                .filter(game -> game.isPossible(12, 13, 14))
                .mapToInt(game -> game.id)
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
                .map(Game::parse)
                .mapToInt(Game::calculatePower)
                .sum();
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
    public LongTuple calculateAnswers(final SolutionContext context) {
        return context.stream()
                .map(Game::parse)
                .reduce(
                        LongTuple.NULL,
                        (tup, game) ->
                                game.isPossible(12, 13, 14)
                                        ? tup.add(game.id, game.calculatePower())
                                        : tup.add(0, game.calculatePower()),
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The colour of a ball.
     */
    private enum Colour {
        RED,
        GREEN,
        BLUE,

        // End of constants
        ;
    }

    /*
     * Represents multiple draws within a single game round.
     */
    private record Game(int id, List<Sample> samples) {

        // Helper Methods

        /**
         * Decide whether this {@link Game} would have been possible with the
         * specified number of red, green and blue balls.
         *
         * @param red   the maximum number of red balls available
         * @param green the maximum number of green balls available
         * @param blue  the maximum number of blue balls available
         * @return {@literal true} if this {@link Game} would have been possible; {@literal false} otherwise
         */
        public boolean isPossible(final int red, final int green, final int blue) {
            return samples.stream().noneMatch(sample -> sample.exceeds(red, green, blue));
        }

        /**
         * Calculate the "power" of this {@link Game}.
         * <p>
         * The "power" is the product of the minimum number of red, blue and
         * green balls that would have been needed for the {@link Game} to be
         * possible.
         *
         * @return the "power" of this {@link Game}
         */
        public int calculatePower() {
            return samples.stream()
                    .reduce(Sample::reduce)
                    .map(Sample::power)
                    .orElse(0);
        }

        // Static Helper Methods

        /**
         * Parse a {@link Game} from a textual representation.
         *
         * @param line the text
         * @return the {@link Game}
         */
        static Game parse(final String line) {
            final String[] parts = line.split(": *", 2);
            final String[] identifier = parts[0].split(" ");

            return new Game(
                    Integer.parseInt(identifier[1]),
                    stream(parts[1].split("; *"))
                            .map(Sample::parse)
                            .toList()
            );
        }
    }

    /*
     * A single sample of balls drawn during a `Game`.
     */
    private record Sample(Map<Colour, Integer> counts) {

        // Helper Methods

        /**
         * Check if this {@link Sample} exceeds a given number of balls.
         *
         * @param red   the maximum number of red balls
         * @param green the maximum number of green balls
         * @param blue  the maximum number of blue balls
         * @return {@literal true} if the red, green or blue balls of this sample exceeds the maximum allowed; {@literal false} otherwise
         */
        public boolean exceeds(final int red, final int green, final int blue) {
            return counts.getOrDefault(Colour.RED, 0) > red
                    || counts.getOrDefault(Colour.GREEN, 0) > green
                    || counts.getOrDefault(Colour.BLUE, 0) > blue;
        }

        /**
         * Combine this {@link Sample} with another to produce one that contains
         * the greatest number of balls sampled for each {@link Colour}.
         *
         * @param other the other {@link Sample}
         * @return the combined {@link Sample}
         */
        public Sample reduce(final Sample other) {
            final Map<Colour, Integer> reduced = new EnumMap<>(Colour.class);
            reduced.put(Colour.RED, max(red(), other.red()));
            reduced.put(Colour.GREEN, max(green(), other.green()));
            reduced.put(Colour.BLUE, max(blue(), other.blue()));
            return new Sample(reduced);
        }

        /**
         * Calculate the "power" of this {@link Sample}.
         *
         * @return the "power"
         */
        public int power() {
            return counts.getOrDefault(Colour.RED, 1)
                    * counts.getOrDefault(Colour.GREEN, 1)
                    * counts.getOrDefault(Colour.BLUE, 1);
        }

        // Private Helper Methods

        /*
         * Get the number of red balls in this `Sample`.
         */
        private int red() {
            return counts.getOrDefault(Colour.RED, 0);
        }

        /*
         * Get the number of green balls in this `Sample`.
         */
        private int green() {
            return counts.getOrDefault(Colour.GREEN, 0);
        }

        /*
         * Get the number of blue balls in this `Sample`.
         */
        private int blue() {
            return counts.getOrDefault(Colour.BLUE, 0);
        }

        // Static Helper Methods

        /**
         * Parse a {@link Sample} from a textual representation.
         *
         * @param line the text
         * @return the {@link Sample}
         */
        static Sample parse(final String line) {
            final String[] parts = line.trim().split(", *");
            return new Sample(
                    stream(parts)
                            .map(part -> part.split(" ", 2))
                            .collect(Collectors.groupingBy(
                                    part -> Colour.valueOf(part[1].toUpperCase(Locale.UK)),
                                    () -> new EnumMap<>(Colour.class),
                                    Collectors.summingInt(part -> Integer.parseInt(part[0]))
                            ))
            );
        }
    }

}

