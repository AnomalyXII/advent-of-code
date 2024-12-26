package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.function.BiFunction;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 2.
 */
@Solution(year = 2020, day = 2, title = "Password Philosophy")
public class Day2 {

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
        return context.stream()
                .filter(Day2::isPasswordValidForOldCompany)
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
        return context.stream()
                .filter(Day2::isPasswordValidForNewCompany)
                .count();
    }

    // ****************************************
    // Helper Members
    // ****************************************

    /**
     * Check if a given password is acceptable according to attached password
     * policy.
     *
     * @param passwordAndPolicy the password and associated policy
     * @return {@literal true} if the password is valid; {@literal false} otherwise
     */
    static boolean isPasswordValidForOldCompany(final String passwordAndPolicy) {
        return isPasswordValid(passwordAndPolicy, Day2::isPasswordValidForOldCompany);
    }

    /**
     * Check if a given password is acceptable according to attached password
     * policy.
     *
     * @param passwordAndPolicy the password and associated policy
     * @return {@literal true} if the password is valid; {@literal false} otherwise
     */
    static boolean isPasswordValidForNewCompany(final String passwordAndPolicy) {
        return isPasswordValid(passwordAndPolicy, Day2::isPasswordValidForNewCompany);
    }

    /**
     * Check if a given password is acceptable according to the password
     * policy.
     *
     * @param chr      the character of the password policy
     * @param min      the minimum number of occurrences of the policy character
     * @param max      the maximum number of occurrences of the policy character
     * @param password the password to test
     * @return {@literal true} if the password is valid according to the policy; {@literal false} otherwise
     */
    static boolean isPasswordValidForOldCompany(
            final char chr,
            final int min,
            final int max,
            final String password
    ) {
        if (password == null) {
            return false;
        }

        int count = 0;
        final char[] chars = password.toCharArray();

        for (final char c : chars) {
            if (c == chr) {
                count++;
            }

            if (count > max) {
                return false;
            }
        }

        return count >= min;
    }

    /**
     * Check if a given password is acceptable according to the password
     * policy.
     *
     * @param chr      the character of the password policy
     * @param first    the first acceptable position of the policy character
     * @param second   the second acceptable position of the policy character
     * @param password the password to test
     * @return {@literal true} if the password is valid according to the policy; {@literal false} otherwise
     */
    static boolean isPasswordValidForNewCompany(
            final char chr,
            final int first,
            final int second,
            final String password
    ) {
        if (password == null) {
            return false;
        }

        final char chrAtPosition1 = password.length() >= first ? password.charAt(first - 1) : '\0';
        final char chrAtPosition2 = password.length() >= second ? password.charAt(second - 1) : '\0';

        return (chrAtPosition1 == chr || chrAtPosition2 == chr) && (chrAtPosition1 != chrAtPosition2);
    }

    // ****************************************
    // Private Helper Members
    // ****************************************

    /*
     * Split the given password and policy, and validate against the
     * specified `PolicyInterpretation`.
     */
    private static boolean isPasswordValid(
            final String passwordAndPolicy,
            final PolicyInterpretation interpretation
    ) {
        return splitPolicyAndValidatePassword(
                passwordAndPolicy, (policy, password) -> {
                    final String[] policyParts = policy.split(" ", 2);
                    final String policyRange = policyParts[0];
                    final String[] policyRangeParts = policyRange.split("-", 2);

                    final char policyChar = policyParts[1].charAt(0);
                    final int policyFirstPosition = Integer.parseInt(policyRangeParts[0]);
                    final int policySecondPosition = Integer.parseInt(policyRangeParts[1]);

                    return interpretation.validate(policyChar, policyFirstPosition, policySecondPosition, password);
                }
        );
    }

    /*
     * Split the password and policy on the first occurrence of ': '.
     */
    private static boolean splitPolicyAndValidatePassword(
            final String passwordAndPolicy,
            final BiFunction<String, String, Boolean> validator
    ) {
        final String[] parts = passwordAndPolicy.split(": ", 2);

        final String policy = parts[0];
        final String password = parts[1];

        return validator.apply(policy, password);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /**
     * Validate a given password.
     */
    @FunctionalInterface
    private interface PolicyInterpretation {

        boolean validate(char chr, int first, int second, String password);

    }

}
