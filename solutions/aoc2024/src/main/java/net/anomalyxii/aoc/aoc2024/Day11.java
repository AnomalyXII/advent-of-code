package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 11.
 */
@Solution(year = 2024, day = 11, title = "Plutonian Pebbles")
public class Day11 {

    private static final int MIN_RUNS = 25;
    private static final int MAX_RUNS = 75;

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
        final List<Stone> stones = context.processLine(line -> stream(line.split("\\s+"))
                .map(Stone::create)
                .peek(s -> s.age[0] = 1)
                .toList());

        return countStones(blinkRepeatedly(stones, MIN_RUNS), MIN_RUNS);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Stone> stones = context.processLine(line -> stream(line.split("\\s+"))
                .map(Stone::create)
                .peek(s -> s.age[0] = 1)
                .toList());

        return countStones(blinkRepeatedly(stones, MAX_RUNS), MAX_RUNS);
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
        final List<Stone> stones = context.processLine(line -> stream(line.split("\\s+"))
                .map(Stone::create)
                .peek(s -> s.age[0] = 1)
                .toList());

        final Collection<Stone> finalStones = blinkRepeatedly(stones, MAX_RUNS);

        return new LongTuple(
                countStones(finalStones, 25),
                countStones(finalStones, MAX_RUNS)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Blink a given number of times.
     */
    private static Collection<Stone> blinkRepeatedly(final Collection<Stone> stones, final int numberOfBlinks) {
        final Map<Long, Stone> stoneIdx = stones.stream()
                .collect(Collectors.toMap(s -> s.number, Function.identity()));

        final Set<Long> next = new HashSet<>(stoneIdx.keySet());
        for (int age = 0; age < numberOfBlinks; age++)
            next.addAll(blink(next, stoneIdx, age));

        return stoneIdx.values();
    }

    /*
     * Blink once.
     */
    private static Set<Long> blink(final Set<Long> numbers, final Map<Long, Stone> stoneIdx, final int age) {
        final Set<Long> nextNumbers = new HashSet<>();
        final Iterator<Long> iterator = numbers.iterator();
        while (iterator.hasNext()) {
            final long number = iterator.next();
            iterator.remove();

            final Stone stone = stoneIdx.get(number);
            assert stone != null : "Stone " + number + " not found!";

            if (number == 0) {
                update(stone, age, 1L, nextNumbers, stoneIdx);
                continue;
            }

            final int length = (int) Math.log10(number);
            if (length % 2 == 0) {
                update(stone, age, number * 2024, nextNumbers, stoneIdx);
                continue;
            }

            final int divisor = (int) Math.pow(10.0, (length + 1) / 2.0);
            final long lhs = number / divisor;
            final long rhs = number - (lhs * divisor);
            update(stone, age, lhs, nextNumbers, stoneIdx);
            update(stone, age, rhs, nextNumbers, stoneIdx);
        }
        return nextNumbers;
    }

    /*
     * Update the queue and index state for the next number.
     */
    private static void update(
            final Stone stone,
            final int age,
            final long nextNumber,
            final Set<Long> next,
            final Map<Long, Stone> stoneIdx
    ) {
        final long count = stone.age[age];
        final Stone nextStone = stoneIdx.computeIfAbsent(nextNumber, Stone::new);
        nextStone.age[age + 1] += count;
        next.add(nextNumber);
    }

    /*
     * Count the number of stones that exist after a given number of blinks.
     */
    private static long countStones(final Collection<Stone> stones, final int age) {
        return stones.stream()
                .mapToLong(s -> s.age[age])
                .sum();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A stone with a given number, and the number of times it appears in the
     * line after a number of blinks.
     */
    private static final class Stone {

        // Private Members

        private final long number;
        private final long[] age = new long[MAX_RUNS + 1];

        // Constructors

        private Stone(final long number) {
            this.number = number;
        }

        // Static Helper Methods

        /*
         * Create a new stone.
         */
        static Stone create(final String number) {
            return new Stone(Long.parseLong(number));
        }

    }

}

