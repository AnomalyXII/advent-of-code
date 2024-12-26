package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.Tuple;

import java.util.Objects;

/**
 * A {@link Challenge} that corresponds to one day of Advent of Code.
 *
 * @param year             the year of the challenge
 * @param day              the day of the challenge
 * @param title            the title of the challenge
 * @param part1Description the description of part 1
 * @param part2Description the description of part 2
 * @param solvers          the {@link SolutionWrapper}
 * @param <T1>             the answer type for part 1
 * @param <T2>             the answer type for part 2
 */
public record Challenge<T1, T2>(
        int year,
        int day,
        String title,
        String part1Description,
        String part2Description,
        SolutionWrapper<T1, T2> solvers
) implements Comparable<Challenge<?, ?>> {

    // ****************************************
    // Public Methods
    // ****************************************

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
        return solvers.hasOptimisedSolution();
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
        return solvers.calculateAnswerForPart1(context);
    }

    /**
     * Calculate the answer for Part II of the challenge.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the answer
     */
    public T2 calculateAnswerForPart2(final SolutionContext context) {
        return solvers.calculateAnswerForPart2(context);
    }

    /**
     * Calculate the answers for both Part I and Part II of the challenge
     * simultaneously, and return the result as a {@link Tuple}.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return a {@link Tuple} containing both answers
     */
    public Tuple<T1, T2> calculateAnswers(final SolutionContext context) {
        return solvers.calculateAnswers(context);
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
                && Objects.equals(this.solvers, that.solvers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, day, title, part1Description, part2Description, solvers);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "Advent of Code %d Day %d solutions".formatted(year, day);
    }

}
