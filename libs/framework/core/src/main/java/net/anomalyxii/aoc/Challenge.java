package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import net.anomalyxii.aoc.result.Tuple;

import java.util.Objects;

/**
 * A {@link Challenge} that corresponds to one day of Advent of Code.
 *
 * @param <T1> the answer type for part 1
 * @param <T2> the answer type for part 2
 */
public final class Challenge<T1, T2> implements Comparable<Challenge<?, ?>> {

    // ****************************************
    // Private Members
    // ****************************************

    private final int year;
    private final int day;
    private final String title;
    private final String part1Description;
    private final String part2Description;
    private final Solver<T1> part1;
    private final Solver<T2> part2;
    private final OptimisedSolver<? extends Tuple<T1, T2>, T1, T2> optimised;

    // ****************************************
    // Constructors
    // ****************************************

    public Challenge(
            final int year,
            final int day,
            final String title,
            final String part1Description,
            final String part2Description,
            final Solver<T1> part1,
            final Solver<T2> part2,
            final OptimisedSolver<? extends Tuple<T1, T2>, T1, T2> optimised
    ) {
        this.year = year;
        this.day = day;
        this.title = title;
        this.part1Description = part1Description;
        this.part2Description = part2Description;
        this.part1 = part1;
        this.part2 = part2;
        this.optimised = optimised;
    }

    // ****************************************
    // Public Methods
    // ****************************************

    /**
     * Get the year of this Advent of Code challenge.
     *
     * @return the year
     */
    public int year() {
        return year;
    }

    /**
     * Get the day of this Advent of Code challenge.
     *
     * @return the day
     */
    public int day() {
        return day;
    }

    /**
     * Get the title of this Advent of Code challenge.
     *
     * @return the title
     */
    public String title() {
        return title;
    }

    /**
     * Get the description of Part I of this Advent of Code challenge.
     *
     * @return the year
     */
    public String part1Description() {
        return part1Description;
    }

    /**
     * Get the description of Part II of this Advent of Code challenge.
     *
     * @return the year
     */
    public String part2Description() {
        return part2Description;
    }

    /**
     * Get the display tag for the challenge.
     *
     * @return the tag
     */
    public String tag() {
        return String.format("%04d Day %02d", year, day);
    }

    /**
     * Check if this challenge has an optimised solution.
     *
     * @return {@literal true} if this challenge has an optimised solution; {@literal false} otherwise
     */
    public boolean hasOptimisedSolution() {
        return optimised != null;
    }

    /**
     * Check if this {@link Challenge} matches the specified year/day.
     *
     * @param maybeYear the year, or {@literal null} if any year is acceptable
     * @param maybeDay  the day, or {@literal null} if any year is acceptable
     * @return {@literal true} if this {@link Challenge} matches; {@literal false} otherwise
     */
    public boolean matches(final Integer maybeYear, final Integer maybeDay) {
        return (maybeYear == null || maybeYear == year) && (maybeDay == null || maybeDay == day);
    }

    /**
     * Calculate the answer for Part I of the challenge.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the answer
     */
    public T1 calculateAnswerForPart1(final SolutionContext context) {
        return part1.solve(context);
    }

    /**
     * Calculate the answer for Part II of the challenge.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the answer
     */
    public T2 calculateAnswerForPart2(final SolutionContext context) {
        return part2.solve(context);
    }

    /**
     * Calculate the answers for both Part I and Part II of the challenge
     * simultaneously, and return the result as a {@link Tuple}.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return a {@link Tuple} containing both answers
     */
    public Tuple<T1, T2> calculateAnswers(final SolutionContext context) {
        if (optimised != null) return optimised.solve(context);
        else return new ObjectTuple<>(
                calculateAnswerForPart1(context),
                calculateAnswerForPart2(context)
        );
    }

    // ****************************************
    // CompareTo Methods
    // ****************************************

    @Override
    public int compareTo(final Challenge o) {
        return year != o.year ? year - o.year : day - o.day;
    }

    // ****************************************
    // Equals & Hash Code
    // ****************************************

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final Challenge<?, ?> that = (Challenge<?, ?>) obj;
        return this.year == that.year
                && this.day == that.day
                && Objects.equals(this.title, that.title)
                && Objects.equals(this.part1Description, that.part1Description)
                && Objects.equals(this.part2Description, that.part2Description)
                && Objects.equals(this.part1, that.part1)
                && Objects.equals(this.part2, that.part2)
                && Objects.equals(this.optimised, that.optimised);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, day, title, part1Description, part2Description, part1, part2, optimised);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "Advent of Code %d Day %d solutions" .formatted(year, day);
    }

}
