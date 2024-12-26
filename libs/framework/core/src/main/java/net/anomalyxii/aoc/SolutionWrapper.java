package net.anomalyxii.aoc;

import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.Tuple;

/**
 * Wrap a {@link Solution} to provide a consistent interface for tooling.
 *
 * @param <T1> the answer type for part 1
 * @param <T2> the answer type for part 2
 */
public interface SolutionWrapper<T1, T2> {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Check if this {@link SolutionWrapper} has an optimised solution.
     *
     * @return {@literal true} if there is an optimised solution; {@literal false} otherwise
     */
    boolean hasOptimisedSolution();

    /**
     * Calculate the answer for Part I of the challenge.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the answer
     */
    T1 calculateAnswerForPart1(SolutionContext context);

    /**
     * Calculate the answer for Part II of the challenge.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the answer
     */
    T2 calculateAnswerForPart2(SolutionContext context);

    /**
     * Calculate the answers for both Part I and Part II of the challenge
     * simultaneously, and return the result as a {@link Tuple}.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return a {@link Tuple} containing both answers
     */
    Tuple<T1, T2> calculateAnswers(SolutionContext context);

}
