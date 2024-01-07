package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;
import net.anomalyxii.aoc.result.Tuple;

/**
 * A generic function that will take in a {@link SolutionContext} and
 * generate a {@link ObjectTuple pair} of "solved" outputs.
 *
 * @param <T>  the type of {@link Tuple} containing the "solved" answers
 * @param <T1> the type of the "solved" part 1 output
 * @param <T2> the type of the "solved" part 2 output
 */
@FunctionalInterface
public interface OptimisedSolver<T extends Tuple<T1, T2>, T1, T2> {

    /**
     * Solve both parts of a problem for a given {@link SolutionContext}
     * input.
     *
     * @param context the {@link SolutionContext} to solve for
     * @return a {@link ObjectTuple} containing both "solved" answers
     */
    T solve(SolutionContext context);

}
