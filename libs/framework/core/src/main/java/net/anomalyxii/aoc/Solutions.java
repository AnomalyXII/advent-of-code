package net.anomalyxii.aoc;

/**
 * A collection of {@link Challenge} solutions for a specific year.
 */
public interface Solutions {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Get all the {@link Challenge Challenges} for this year.
     *
     * @return an array of {@link Challenge Challenges}
     */
    Challenge<?, ?>[] allChallenges();

    // ****************************************
    // Default Methods
    // ****************************************

    /**
     * Return the {@link SolutionWrapper} for a given day.
     *
     * @param day the day
     * @return the {@link SolutionWrapper}
     */
    default SolutionWrapper<?, ?> solutions(int day) {
        return challenge(day).solvers();
    }

    /**
     * Return the {@link Challenge} for a given day.
     *
     * @param day the day
     * @return the {@link Challenge}
     */
    default Challenge<?, ?> challenge(int day) {
        return allChallenges()[day - 1];
    }

}
