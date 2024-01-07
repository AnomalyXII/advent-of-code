package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 21.
 */
@Solution(year = 2020, day = 21, title = "Allergen Assessment")
public class Day21 {

    private static final Pattern INGREDIENTS_PATTERN = Pattern.compile("([a-z ]+)(?: [(]contains ([a-z, ]+)[)])?");

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
        final Map<String, Long> allIngredients = new HashMap<>();
        final Map<String, Set<String>> allergensToPossibleIngredients = new HashMap<>();

        context.consume(line -> {
            final Matcher m = INGREDIENTS_PATTERN.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("Invalid ingredient list: '" + line + "'");
            }

            final String ingredientList = m.group(1);
            final List<String> ingredients = asList(ingredientList.split(" +"));
            ingredients.forEach(ingredient -> {
                final long count = allIngredients.getOrDefault(ingredient, 0L);
                allIngredients.put(ingredient, count + 1);
            });

            maybeFilterAllergens(m, allergensToPossibleIngredients, ingredients);
        });

        return allIngredients.entrySet().stream()
                .filter(entry -> allergensToPossibleIngredients.values().stream()
                        .noneMatch(allergens -> allergens.contains(entry.getKey())))
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final Map<String, Set<String>> allergensToPossibleIngredients = new HashMap<>();

        context.consume(line -> {
            final Matcher m = INGREDIENTS_PATTERN.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("Invalid ingredient list: '" + line + "'");
            }

            final String ingredientList = m.group(1);
            final List<String> ingredients1 = asList(ingredientList.split(" +"));

            maybeFilterAllergens(m, allergensToPossibleIngredients, ingredients1);
        });

        final Map<String, String> ingredientToAllergenMap = matchAllergenToIngredient(allergensToPossibleIngredients);
        return ingredientToAllergenMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Maybe filter the list of Allergens to the ones that may apply to each
     * ingredient.
     */
    private static void maybeFilterAllergens(
            final Matcher m,
            final Map<String, Set<String>> allergensToPossibleIngredients,
            final List<String> ingredients
    ) {

        if (m.groupCount() < 2) return;

        final String allergenList = m.group(2);
        final String[] allergens = allergenList.split(", +");

        Arrays.stream(allergens)
                .forEach(allergen -> allergensToPossibleIngredients
                        .computeIfAbsent(allergen, x -> new HashSet<>(ingredients))
                        .retainAll(ingredients));
    }

    /*
     * Process the `Map` of allergens to ingredients and determine exactly
     * which ingredient contains each allergen.
     */
    private static Map<String, String> matchAllergenToIngredient(final Map<String, Set<String>> allergensToPossibleIngredients) {
        final Map<String, String> ingredientToAllergenMap = new HashMap<>();
        while (!allergensToPossibleIngredients.isEmpty()) {
            final Set<String> allergens = new HashSet<>(allergensToPossibleIngredients.keySet());
            allergens.forEach((allergen) -> {
                final Set<String> ingredients = allergensToPossibleIngredients.get(allergen);
                ingredients.removeAll(ingredientToAllergenMap.keySet());

                if (ingredients.isEmpty())
                    throw new IllegalStateException("No possible ingredients for '" + allergen + "'?");


                if (ingredients.size() == 1) {
                    final String ingredient = ingredients.iterator().next();
                    allergensToPossibleIngredients.remove(allergen);
                    ingredientToAllergenMap.put(ingredient, allergen);
                }
            });
        }
        return ingredientToAllergenMap;
    }

}
