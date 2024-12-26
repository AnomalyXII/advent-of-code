package net.anomalyxii.aoc.annotations;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.Comparator;
import java.util.Objects;

/*
 * Holds information related to an Advent of Code solution.
 */
record SolutionClass(
        int year,
        int day,
        String title,
        PackageElement packageElement,
        Element solutionElement,
        SolutionPartMethod part1,
        SolutionPartMethod part2,
        OptimisedMethod optimised
) implements Comparable<SolutionClass> {

    private static final Comparator<SolutionClass> COMPARATOR = Comparator
            .comparing((SolutionClass sc) -> sc.year)
            .thenComparing(sc -> sc.day);

    // Helper Methods

    /*
     * The fully-qualified name of the solution class.
     */
    String fqn() {
        return !packageElement.isUnnamed()
                ? packageElement.getQualifiedName() + "." + solutionElement.getSimpleName().toString()
                : solutionElement.getSimpleName().toString();
    }

    // Comparable Methods

    @Override
    public int compareTo(final SolutionClass o) {
        return COMPARATOR.compare(this, o);
    }

    // Equals & Hash Code

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SolutionClass that = (SolutionClass) o;
        return year == that.year && day == that.day && packageElement.equals(that.packageElement) && solutionElement.equals(that.solutionElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, day, packageElement, solutionElement);
    }

}
