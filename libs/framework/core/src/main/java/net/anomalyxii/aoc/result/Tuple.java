package net.anomalyxii.aoc.result;

import net.anomalyxii.aoc.OptimisedSolver;

/**
 * A pair of answers returned from an {@link OptimisedSolver}.
 *
 * @param <T1> the type of the first answer
 * @param <T2> the type of the second answer
 */
public interface Tuple<T1, T2> {

    // ****************************************
    // Answer Methods
    // ****************************************

    /**
     * Return the first answer.
     *
     * @return the first answer
     */
    T1 getAnswer1();

    /**
     * Return the second answer.
     *
     * @return the second answer
     */
    T2 getAnswer2();

}
