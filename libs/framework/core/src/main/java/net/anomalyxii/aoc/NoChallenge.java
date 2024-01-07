package net.anomalyxii.aoc;

/**
 * A special enum that can be used as the result of a {@link Challenge}
 * part to indicate that there was no challenge (and thus no solution).
 */
public enum NoChallenge {

    /**
     * No challenge.
     */
    NO_CHALLENGE

    // End of constants.
    ;

    @Override
    public String toString() {
        return "-";
    }

}
