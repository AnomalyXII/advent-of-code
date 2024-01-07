package net.anomalyxii.aoc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a special method constructed to take in one Advent of Code
 * input and solve all parts of of that challenge in one go.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Optimised {

    // ****************************************
    // Annotation Fields
    // ****************************************

    // Nothing to see here?

}
