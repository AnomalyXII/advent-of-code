package net.anomalyxii.aoc;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    // ****************************************
    // Default Methods
    // ****************************************

    /**
     * Get the {@link Challenge challenges} for a given year.
     *
     * @param maybeYear the year to filter to
     * @return all {@link Challenge Challenges}
     */
    default Set<Challenge<?, ?>> allChallenges(final Integer maybeYear) {
        return maybeYear == null ? allChallenges() : allChallengesForYear(maybeYear);
    }

    /**
     * Get the {@link Challenge challenges} for a given year and/or day.
     *
     * @param maybeYear the year to filter to
     * @param maybeDay  the day to filter to
     * @return all {@link Challenge Challenges}
     */
    default Set<Challenge<?, ?>> allChallenges(final Integer maybeYear, final Integer maybeDay) {
        return maybeDay == null
                ? allChallenges(maybeYear)
                : maybeYear == null
                        ? allChallenges().stream()
                            .filter(challenge -> challenge.matches(null, maybeDay))
                            .collect(Collectors.toSet())
                        : findChallenge(maybeYear, maybeDay).stream()
                                .collect(Collectors.toSet());
    }


}
