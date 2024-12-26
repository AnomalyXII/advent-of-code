package net.anomalyxii.aoc.utils.geometry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoundsTest {

    // ****************************************
    // Test Methods
    // ****************************************

    // Bounds.of

    @Test
    void of_CorrectDimensions() {
        // arrange
        // Nothing to do...?

        // act
        final Bounds bounds = Bounds.of(10, 15);

        // assert
        assertThat(bounds)
                .returns(10, Bounds::min)
                .returns(15, Bounds::max)
                .returns(6, Bounds::length);
    }

}