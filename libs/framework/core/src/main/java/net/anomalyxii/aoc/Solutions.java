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

}
