package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.stream.IntStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 23.
 */
@Solution(year = 2020, day = 23, title = "Crab Cups")
public class Day23 {

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
        final Cups cups = Cups.createFrom(context.stream()
                                                  .flatMapToInt(String::chars)
                                                  .map(i -> i - '0')
                                                  .toArray());

        playCupGame(cups, 100);

        final Cup cup1 = cups.getNodeWithValue(1);

        long result = 0;
        Cup next = cup1.next;
        do {
            result = (result * 10) + next.value;
            next = next.next;
        } while (next != cup1);

        return result;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final int[] labelledCups = context.stream()
                .flatMapToInt(String::chars)
                .map(i -> i - '0')
                .toArray();
        final int maxLabelledCup = IntStream.of(labelledCups).max().orElseThrow();
        final int[] startingCups = IntStream.concat(
                IntStream.of(labelledCups),
                IntStream.rangeClosed(maxLabelledCup + 1, 1_000_000)
        ).toArray();

        final Cups cups = Cups.createFrom(startingCups);

        playCupGame(cups, 10_000_000);

        final Cup cup1 = cups.getNodeWithValue(1);
        final Cup next1 = cup1.next;
        final Cup next2 = next1.next;
        return ((long) next1.value * (long) next2.value);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Play the cups game.
     */
    private static void playCupGame(final Cups cups, final int rounds) {
        Cup currentCup = cups.root;

        for (int round = 0; round < rounds; round++) {
            final Cup next1 = currentCup.next;
            final Cup next2 = next1.next;
            final Cup next3 = next2.next;

            currentCup.setNext(next3.next);

            int destination = currentCup.value;
            do {
                destination = --destination < cups.min()
                        ? cups.max()
                        : destination;
            } while (destination == next1.value || destination == next2.value || destination == next3.value);

            final Cup insertAfter = cups.getNodeWithValue(destination);
            final Cup insertBefore = insertAfter.next;

            insertAfter.setNext(next1);
            next3.setNext(insertBefore);
            currentCup = currentCup.next;
        }
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * All the cups.
     */
    private record Cups(Cup root, Cup[] lookupByValue) {

        // Helper Methods

        /*
         * Get the minimum cup value.
         */
        public int min() {
            return 1;
        }

        /*
         * Get the maximum cup value.
         */
        public int max() {
            return lookupByValue.length;
        }

        /*
         * Get the Cup with the given value.
         */
        public Cup getNodeWithValue(final int i) {
            return lookupByValue[i - 1];
        }

        // Static Helper Methods

        /*
         * Create a new set of Cups.
         */
        public static Cups createFrom(final int[] cups) {
            final Cup tmp = new Cup(0);
            final Cup last = IntStream.of(cups)
                    .mapToObj(Cup::new)
                    .reduce(tmp, (result, next) -> {
                        result.next = next;
                        return next;
                    });

            final Cup root = tmp.next;
            last.next = root;

            final Cup[] lookup = new Cup[cups.length];

            Cup current = root;
            do {
                lookup[current.value - 1] = current;
                current = current.next;
            } while (current != root);

            return new Cups(root, lookup);
        }

    }

    /*
     * One cup.
     */
    private static final class Cup {

        // Private Members

        private final int value;
        private Cup next;

        // Constructors

        Cup(final int value) {
            this.value = value;
        }

        // Helper Methods

        /*
         * Set the next cup, implicitly making the reverse link.
         */
        public void setNext(final Cup other) {
            this.next = other;
        }

    }

}
