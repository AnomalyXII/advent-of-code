package net.anomalyxii.aoc;

import java.util.Optional;
import java.util.Set;

/**
 * Load all available {@link Solutions}.
 */
public interface SolutionLoader {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Get ALL the {@link Challenge challenges}.
     *
     * @return all {@link Challenge Challenges}
     */
    Set<Challenge<?, ?>> allChallenges();

    /**
     * Get the {@link Challenge challenges} for a specific year.
     *
     * @param year the year to find {@link Challenge Challenges} for
     * @return all {@link Challenge Challenges} for the given year
     */
    Set<Challenge<?, ?>> allChallengesForYear(int year);

    /**
     * Get a specific {@link Challenge challenge}.
     *
     * @param year the year of the {@link Challenge} to find
     * @param day  the day of the {@link Challenge} to find
     * @return the {@link Challenge} for the given year and day
     */
    Optional<Challenge<?, ?>> findChallenge(int year, int day);

}
