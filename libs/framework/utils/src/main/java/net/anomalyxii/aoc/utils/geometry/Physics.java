package net.anomalyxii.aoc.utils.geometry;

/**
 * Model some arbitrary physics.
 */
public interface Physics {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Adjust a given horizontal velocity.
     *
     * @param h the horizontal velocity.
     * @return the adjusted horizontal velocity
     */
    int adjustForResistance(int h);

    /**
     * Adjust a given vertical velocity.
     *
     * @param v the vertical velocity.
     * @return the adjusted vertical velocity
     */
    int adjustForGravity(int v);

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create {@link Physics} that model being underwater.
     * <p>
     * Underwater, a given horizontal velocity will tend towards {@literal 0}
     * and a given vertical velocity will continuously decrease.
     *
     * @return the underwater {@link Physics}
     */
    static Physics simpleUnderwaterPhysics() {
        return new Physics() {
            @Override
            public int adjustForResistance(final int h) {
                return h > 0 ? h - 1 : h < 0 ? h + 1 : 0;
            }

            @Override
            public int adjustForGravity(final int v) {
                return v - 1;
            }
        };
    }

}
