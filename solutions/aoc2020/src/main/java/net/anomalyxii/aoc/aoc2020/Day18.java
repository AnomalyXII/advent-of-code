package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.ToLongFunction;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 18.
 */
@Solution(year = 2020, day = 18, title = "Operation Order")
public class Day18 {

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
                .mapToLong(Day18::evaluate)
                .sum();
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
                .mapToLong(Day18::evaluateAdv)
                .sum();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Evaluate a simple maths equation.
     *
     * @param equation the equation to evaluate
     * @return the result
     */
    static long evaluate(final String equation) {
        final ArrayDeque<Character> chars = new ArrayDeque<>();
        for (final char c : equation.toCharArray()) {
            chars.add(c);
        }
        return evaluate(chars);
    }

    /**
     * Evaluate an advanced maths equation.
     *
     * @param equation the equation to evaluate
     * @return the result
     */
    static long evaluateAdv(final String equation) {
        final ArrayDeque<Character> chars = new ArrayDeque<>();
        for (final char c : equation.toCharArray()) {
            chars.add(c);
        }
        return evaluateAdv(chars);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Evaluate a simple maths equation.
     */
    private static long evaluate(final Deque<Character> equation) {
        return parse(equation, Day18::evaluateStack);
    }

    /*
     * Evaluate an advanced maths equation.
     */
    private static long evaluateAdv(final Deque<Character> equation) {
        return parse(equation, Day18::evaluateStackAdv);
    }

    /*
     * Parse the maths equation and evaluate sub-expressions.
     */
    private static long parse(final Deque<Character> equation, final ToLongFunction<List<String>> evaluator) {
        StringBuilder token = null;

        final List<String> stack = new ArrayList<>();
        Character c;
        while ((c = equation.pollFirst()) != null) {
            if (c == ')') {
                if (token != null) {
                    stack.add(token.toString());
                }

                return evaluator.applyAsLong(stack);
            }
            if (c == '(') {
                final long sub = parse(equation, evaluator);
                stack.add(Long.toString(sub));
                continue;
            }

            if (c == ' ') {
                if (token != null) {
                    stack.add(token.toString());
                    token = null;
                }
                continue;
            }
            if (c == '+' || c == '*') {
                if (token != null) {
                    stack.add(token.toString());
                    token = null;
                }

                stack.add(Character.toString(c));
                continue;
            }

            if (token == null) token = new StringBuilder();
            token.append(c);
        }

        if (token != null) {
            stack.add(token.toString());
        }

        return evaluator.applyAsLong(stack);
    }

    /*
     * Evaluate a stack of tokens in a simple way.
     */
    private static long evaluateStack(final List<String> stack) {
        long result = Long.parseLong(stack.getFirst());
        for (int i = 1; i < stack.size() - 1; i += 2) {
            final String token = stack.get(i);
            if (!"*".equalsIgnoreCase(token) && !"+".equalsIgnoreCase(token)) {
                throw new IllegalStateException("Expected '+' or '*' but found '" + token + "'");
            }

            final String next = stack.get(i + 1);
            final long right = Long.parseLong(next);

            if ("*".equalsIgnoreCase(token)) {
                result *= right;
            } else {
                result += right;
            }
        }

        return result;
    }

    /*
     * Evaluate a stack of tokens in an advanced way.
     */
    private static long evaluateStackAdv(final List<String> stack) {
        final List<String> newStack = new ArrayList<>();
        for (int i = 0; i < stack.size(); i++) {
            final String token = stack.get(i);

            if ("+".equalsIgnoreCase(token)) {
                final String prev = newStack.removeLast();
                final String next = stack.get(++i);

                final long left = Long.parseLong(prev);
                final long right = Long.parseLong(next);
                final long result = left + right;
                newStack.add(Long.toString(result));
                continue;
            }

            newStack.add(token);
        }

        return evaluateStack(newStack);
    }

}
