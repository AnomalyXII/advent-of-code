package net.anomalyxii.aoc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method contains the solution for one part of an Advent of
 * Code challenge.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Part {

    // ****************************************
    // Annotation Fields
    // ****************************************

    /**
     * The {@link PartNumber part number} (either {@code I} or {@code II}).
     *
     * @return the {@link PartNumber part number}
     */
    PartNumber part();

    // ****************************************
    // Helper Classes
    // ****************************************

    /**
     * Either Part I or Part II.
     */
    enum PartNumber {

        /**
         * Part 1.
         */
        I,

        /**
         * Part 2.
         */
        II,

        // End of constants
        ;

    }

}
