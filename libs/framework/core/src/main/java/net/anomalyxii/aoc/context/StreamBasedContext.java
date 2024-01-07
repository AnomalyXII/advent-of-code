package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.utils.ocr.LetterSet;
import net.anomalyxii.aoc.utils.ocr.OCR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link SolutionContext} that loads from a {@link InputStream}.
 */
public abstract class StreamBasedContext implements SolutionContext {

    // ****************************************
    // Private Members
    // ****************************************

    private final LetterSet letters;

    // ****************************************
    // Constructors
    // ****************************************

    StreamBasedContext(final LetterSet letters) {
        this.letters = letters;
    }

    // ****************************************
    // SolutionContext Methods
    // ****************************************

    @Override
    public List<String> read() {
        return readAllLines();
    }

    @Override
    public String readLine() {
        return readAllLines()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected one line from the data file, but nothing was read!"));
    }

    @Override
    public List<List<String>> readBatches() {
        return streamBatches().toList();
    }

    @Override
    public <T> List<T> process(final Function<String, T> processor) {
        return processDataFile(processor);
    }

    @Override
    public <T> T processLine(final Function<String, T> processor) {
        return processSingleLineDataFile(processor)
                .orElseThrow(() -> new IllegalStateException("Expected one line from the data file, but nothing was read!"));
    }

    @Override
    public void consume(final Consumer<String> consumer) {
        readAllLines().forEach(consumer);
    }

    @Override
    public Stream<String> stream() {
        return readAllLines().stream();
    }

    @Override
    public Stream<List<String>> streamBatches() {
        final Stream.Builder<List<String>> builder = Stream.builder();

        final List<String> lines = readAllLines();

        List<String> currentBatch = null;
        for (final String line : lines) {
            if (line.isBlank()) {
                currentBatch = null;
                continue;
            }

            if (currentBatch == null) {
                currentBatch = new ArrayList<>();
                builder.add(currentBatch);
            }

            currentBatch.add(line);
        }

        return builder.build();
    }

    @Override
    public OCR ocr() {
        return OCR.ofLetters(letters);
    }

    // ****************************************
    // Abstract Helper Methods
    // ****************************************

    /**
     * Describe the underlying {@link InputStream}.
     * <p>
     * This is useful for diagnostics for when something goes wrong.
     *
     * @return the description
     */
    protected abstract String describe();

    /**
     * Return a new {@link BufferedReader} based on the {@link InputStream}.
     *
     * @return a new {@link BufferedReader}
     * @throws IOException if the {@link BufferedReader} cannot be opened
     */
    protected abstract BufferedReader openReader() throws IOException;

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "SolutionContext[" + describe() + "]";
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the given data file on the classpath and process each line.
     */
    private <T> List<T> processDataFile(final Function<String, T> processor) {
        return readAllLines()
                .stream()
                .map(processor)
                .toList();
    }

    /*
     * Find the given data file on the classpath and process each line.
     */
    private <T> Optional<T> processSingleLineDataFile(final Function<String, T> processor) {
        return readAllLines()
                .stream()
                .map(processor)
                .findFirst();
    }

    /*
     * Read all lines from the underlying `InputStream`.
     */
    private List<String> readAllLines() {
        try (BufferedReader in = openReader()) {
            return in.lines().toList();
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

}
