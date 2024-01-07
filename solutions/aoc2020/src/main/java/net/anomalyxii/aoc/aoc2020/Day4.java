package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import static java.util.Arrays.asList;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 4.
 */
@Solution(year = 2020, day = 4, title = "Passport Processing")
public class Day4 {

    static final String[] REQUIRED_FIELDS = {
            "byr", // (Birth Year)
            "iyr", // (Issue Year)
            "eyr", // (Expiration Year)
            "hgt", // (Height)
            "hcl", // (Hair Color)
            "ecl", // (Eye Color)
            "pid", // (Passport ID)
    };

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
        return context.streamBatches()
                .map(Day4::parsePassport)
                .filter(passport -> isPassportValid(passport, (k, v) -> true))
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
        return context.streamBatches()
                .map(Day4::parsePassport)
                .filter(passport -> isPassportValid(passport, Day4::validateField))
                .count();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Check if the given passport contains all the required keys.
     *
     * @param passport the passport
     * @return {@literal true} if the passport is valid; {@literal false} otherwise
     */
    static boolean isPassportValid(final Map<String, String> passport) {
        return isPassportValid(passport, (k, v) -> true);
    }

    /**
     * Check if the given passport contains all the required keys.
     *
     * @param passport  the passport
     * @param validator the field validator
     * @return {@literal true} if the passport is valid; {@literal false} otherwise
     */
    static boolean isPassportValid(final Map<String, String> passport, final BiPredicate<String, String> validator) {
        final boolean allFields = passport.keySet().containsAll(asList(REQUIRED_FIELDS));
        if (!allFields) {
            return false;
        }

        return passport.entrySet().stream()
                .allMatch(entry -> validator.test(entry.getKey(), entry.getValue()));
    }

    /**
     * Validate a given passport field.
     *
     * @param field the field key
     * @param value the field value
     * @return the answer
     */
    static boolean validateField(final String field, final String value) {
        try {
            switch (field) {
                case "byr":
                    final int byr = Integer.parseInt(value);
                    return byr >= 1920 && byr <= 2002;
                case "iyr":
                    final int iyr = Integer.parseInt(value);
                    return iyr >= 2010 && iyr <= 2020;
                case "eyr":
                    final int eyr = Integer.parseInt(value);
                    return eyr >= 2020 && eyr <= 2030;
                case "hgt":
                    final int hgt = Integer.parseInt(value.substring(0, value.length() - 2));
                    if (value.endsWith("cm"))
                        return hgt >= 150 && hgt <= 193;
                    else if (value.endsWith("in"))
                        return hgt >= 59 && hgt <= 76;
                    else
                        return false;
                case "hcl":
                    return value.matches("#[a-fA-F0-9]{6}");
                case "ecl":
                    return value.matches("amb|blu|brn|gry|grn|hzl|oth");
                case "pid":
                    return value.matches("[0-9]{9}");
                case "cid":
                    return true;
                default:
                    throw new IllegalArgumentException("Invalid field: " + field);
            }
        } catch (final Exception e) {
            return false;
        }
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Parse the passport from the given lines.
     */
    private static Map<String, String> parsePassport(final List<String> lines) {
        final Map<String, String> passport = new HashMap<>();
        for (final String line : lines) {
            final String[] fields = line.split(" ");
            for (final String field : fields) {
                final String[] parts = field.split(":", 2);
                passport.put(parts[0], parts[1]);
            }
        }

        return passport;
    }

}
