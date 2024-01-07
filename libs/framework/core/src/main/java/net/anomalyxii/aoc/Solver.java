package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;

/**
 * A generic function that will take in a {@link SolutionContext} and
 * generate a "solved" output.
 *
 * @param <T> the type of the "solved" output
 */
@FunctionalInterface
public interface Solver<T> {

    /**
     * Solve a problem for a given {@link SolutionContext} input.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return the "solved" answer
     */
    T solve(SolutionContext context);

}
