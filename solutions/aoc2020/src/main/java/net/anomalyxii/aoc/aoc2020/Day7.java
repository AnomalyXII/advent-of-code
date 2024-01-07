package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 7.
 */
@Solution(year = 2020, day = 7, title = "Handy Haversacks")
public class Day7 {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        final Map<String, Boolean> memo = new HashMap<>();
        final Map<String, BagSpec> bagSpecsByName = new HashMap<>();

        final List<BagSpec> bagSpecs = context.process(Day7::parseBagSpec);
        bagSpecs.forEach(spec -> bagSpecsByName.put(spec.myColour, spec));

        return bagSpecs.stream()
                .filter(spec -> hasPathToShinyGoldBag(spec, bagSpecsByName, memo))
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Map<String, BagSpec> bagSpecsByName = new HashMap<>();

        final List<BagSpec> bagSpecs = context.process(Day7::parseBagSpec);
        bagSpecs.forEach(spec -> bagSpecsByName.put(spec.myColour, spec));

        return countNestedBags(bagSpecsByName.get("shiny gold"), bagSpecsByName);
    }

    // ****************************************
    // Helper Members
    // ****************************************

    private static final Pattern SPECIFICATION_PATTERN = Pattern.compile("([a-z ]+) bags contain ([0-9a-z, ]+)[.]", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTAINER_PATTERN = Pattern.compile("([0-9]+) ([a-z ]+) bags?,?", Pattern.CASE_INSENSITIVE);

    /**
     * Parse the given bag specification into a {@link BagSpec}.
     *
     * @param spec the specification
     * @return the {@link BagSpec}
     * @throws IllegalArgumentException if the specification is invalid
     */
    static BagSpec parseBagSpec(final String spec) {
        final Matcher m;
        if (!(m = SPECIFICATION_PATTERN.matcher(spec)).matches()) {
            throw new IllegalArgumentException("Invalid bag specification: '" + spec + "'");
        }

        final String myColour = m.group(1);
        final Map<String, Integer> childBags = parseChildBagSpec(m);
        return new BagSpec(myColour, childBags);
    }

    // ****************************************
    // Private Helper Members
    // ****************************************

    /*
     * Parse the list of child (contained) bag specifications.
     */
    private static Map<String, Integer> parseChildBagSpec(final Matcher m) {
        final String childBagsSpec = m.group(2);
        final Map<String, Integer> childBags = new HashMap<>();
        final Matcher c = CONTAINER_PATTERN.matcher(childBagsSpec);
        while (c.find()) {
            childBags.put(c.group(2), Integer.parseInt(c.group(1)));
        }
        return childBags;
    }

    /*
     * Determine whether this `BagSpec` has a path to containing a
     * "shiny gold" bag.
     */
    private static boolean hasPathToShinyGoldBag(
            final BagSpec spec,
            final Map<String, BagSpec> allSpecs,
            final Map<String, Boolean> memo
    ) {
        if (memo.containsKey(spec.myColour)) {
            return memo.get(spec.myColour);
        }

        for (final Map.Entry<String, Integer> entry : spec.childBags.entrySet()) {
            final String ref = entry.getKey();
            final Integer count = entry.getValue();
            if (count > 0) {
                final boolean result = "shiny gold".equalsIgnoreCase(ref)
                        || hasPathToShinyGoldBag(allSpecs.get(ref), allSpecs, memo);
                if (result) {
                    memo.put(spec.myColour, true);
                    return true;
                }
            }
        }

        memo.put(spec.myColour, false);
        return false;
    }

    /*
     * Count the number of bags contained inside a given colour.
     */
    private static long countNestedBags(final BagSpec spec, final Map<String, BagSpec> allBags) {
        long result = 0;
        for (final Map.Entry<String, Integer> entry : spec.childBags.entrySet()) {
            final String ref = entry.getKey();
            final Integer count = entry.getValue();

            result += (count * (1 + countNestedBags(allBags.get(ref), allBags)));
        }
        return result;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /**
     * Holds the specification of a bag.
     */
    static class BagSpec {

        // Members

        final String myColour;
        final Map<String, Integer> childBags = new HashMap<>();

        // Constructors

        BagSpec(final String myColour, final Map<String, Integer> childBags) {
            this.myColour = requireNonNull(myColour, "My colour must not be null!");
            this.childBags.putAll(requireNonNull(childBags, "Child bags must not be null!"));
        }

    }

}
