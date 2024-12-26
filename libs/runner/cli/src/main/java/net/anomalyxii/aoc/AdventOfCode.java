package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Run all the advent of code challenges.
 */
public class AdventOfCode {

    private static final SpiSolutionLoader SOLUTION_LOADER = new SpiSolutionLoader();

    // ****************************************
    // Main Method
    // ****************************************

    /**
     * Run the Advent of Code solutions.
     *
     * @param args any command line arguments
     */
    public static void main(final String[] args) {
        final ArgumentParser parser = Arguments.create();
        final Namespace namespace;
        try {
            namespace = parser.parseArgs(args);
        } catch (final ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }

        SOLUTION_LOADER.allChallenges(namespace.getInt("year"), namespace.getInt("day")).stream()
                .sorted()
                .parallel()
                .flatMap(AdventOfCode::solve)
                .forEachOrdered(Solution::print);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Solve all parts of the given challenge, timing how long each takes.
     */
    private static Stream<Solution> solve(final Challenge<?, ?> day) {
        final Stream.Builder<Solution> builder = Stream.builder();
        builder.add(solveAndTimeChallengeSolution(day, "I", Challenge::calculateAnswerForPart1));
        builder.add(solveAndTimeChallengeSolution(day, "II", Challenge::calculateAnswerForPart2));
        if (day.hasOptimisedSolution()) {
            builder.add(solveAndTimeChallengeSolution(day, "&", Challenge::calculateAnswers));
        }
        return builder.build();
    }

    /*
     * Solve the given challenge, timing how long it takes.
     */
    private static Solution solveAndTimeChallengeSolution(
            final Challenge<?, ?> challenge,
            final String part,
            final BiFunction<Challenge<?, ?>, SolutionContext, ?> function
    ) {
        final SolutionContext context = SolutionContext.live(challenge.year(), challenge.day());

        final long start = System.nanoTime();
        final Object answer = function.apply(challenge, context);
        final long end = System.nanoTime();

        final String answerStr = String.valueOf(answer);
        return new Solution(challenge.tag(), part, answerStr, TimeUnit.NANOSECONDS.toMicros(end - start));
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The solution to a `Challenge`.
     */
    private record Solution(String tag, String part, String result, long duration) {

        // Helper Methods

        public void print() {
            System.out.printf("[%s PART %2s] %-49s (%12dμs)%n", tag, part, result, duration);
        }

    }


}
