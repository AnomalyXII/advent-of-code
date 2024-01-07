package net.anomalyxii.aoc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a class contains solutions for a given Advent of Code challenge.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Solution {

    // ****************************************
    // Annotation Fields
    // ****************************************

    /**
     * The year that this Advent of Code challenge was set.
     *
     * @return the challenge year
     */
    int year();

    /**
     * The day that this Advent of Code challenge was set.
     *
     * @return the challenge day
     */
    int day();

    /**
     * The title of this Advent of Code challenge.
     *
     * @return the challenge title
     */
    String title();

}
