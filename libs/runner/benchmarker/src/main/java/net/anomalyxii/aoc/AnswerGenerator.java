package net.anomalyxii.aoc;

import net.anomalyxii.aoc.context.SolutionContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Generate the input and answer files for benchmarking.
 */
public class AnswerGenerator {

    /*
     * Path to the resources relative to the working directory.
     */
    private static final String RESOURCES_DIRECTORY_FORMAT = "solutions/aoc%d/src/main/resources/";

    // ****************************************
    // Main Method
    // ****************************************

    /**
     * Run the answer generator.
     *
     * @param args any command line arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: AnswerGenerator <directory> [year] [day] [replace]");
            System.exit(1);
        }

        final SolutionLoader loader = new SpiSolutionLoader();
        final Path workingDirectory = Path.of(args[0]).toAbsolutePath();
        loader.allChallenges().stream()
                .filter(challenge -> challenge.matches(intArg(args, 1), intArg(args, 2)))
                .forEach(challenge -> generateFilesForChallenge(challenge, workingDirectory, boolArg(args, 3)));
    }

    // ****************************************
    // Private Helper Method
    // ****************************************

    /*
     * Get an `Integer` argument, or `null` if it is not set
     */
    private static Integer intArg(final String[] args, final int idx) {
        if (args.length > idx) return Integer.valueOf(args[idx]);
        return null;
    }

    /*
     * Get a `boolean` argument, or false if it is not set
     */
    private static boolean boolArg(final String[] args, final int idx) {
        if (args.length > idx) return Boolean.parseBoolean(args[idx]);
        return false;
    }

    /*
     * Extract the input for a given `Challenge`.
     */
    private static void generateFilesForChallenge(
            final Challenge<?, ?> challenge,
            final Path workingDirectory,
            final boolean replaceExisting) {
        final Path resourceDirectory = workingDirectory
                .resolve(RESOURCES_DIRECTORY_FORMAT.formatted(challenge.year()));
        final String inputFile = String.format("%d/day%d.txt", challenge.year(), challenge.day());
        final String answersFile = String.format("%d/day%d-answers.txt", challenge.year(), challenge.day());

        if (!Files.exists(resourceDirectory.resolve(inputFile)) || replaceExisting) {
            try {
                extractInputForChallenge(challenge, resourceDirectory, inputFile);
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate input for Challenge", e);
            }
        }

        if (!Files.exists(resourceDirectory.resolve(answersFile)) || replaceExisting) {
            try {
                generateAnswers(challenge, resourceDirectory, inputFile, answersFile);
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate input for Challenge", e);
            }
        }
    }

    /*
     * Extract the input for a given `Challenge`.
     */
    private static void extractInputForChallenge(
            final Challenge<?, ?> challenge,
            final Path resourceDirectory,
            final String inputFile
    ) throws IOException {
        final InputStream in = challenge.getClass().getClassLoader().getResourceAsStream(inputFile);
        if (in == null) throw new IllegalStateException("Failed to load input [" + inputFile + "]");
        Files.copy(in, resourceDirectory.resolve(inputFile), StandardCopyOption.REPLACE_EXISTING);
    }

    /*
     * Generate the expected answers for a given `Challenge`.
     */
    private static void generateAnswers(
            final Challenge<?, ?> challenge,
            final Path resourceDirectory,
            final String inputFile,
            final String answersFile
    ) throws IOException {
        final SolutionContext context = SolutionContext.builder()
                .path(resourceDirectory.resolve(inputFile).toAbsolutePath().toString())
                .build();

        try (OutputStream out = Files.newOutputStream(resourceDirectory.resolve(answersFile), CREATE, TRUNCATE_EXISTING)) {
            out.write(String.valueOf(challenge.calculateAnswerForPart1(context)).getBytes(StandardCharsets.UTF_8));
            out.write('\n');
            out.write(String.valueOf(challenge.calculateAnswerForPart2(context)).getBytes(StandardCharsets.UTF_8));
            out.write('\n');
        }
    }

}
