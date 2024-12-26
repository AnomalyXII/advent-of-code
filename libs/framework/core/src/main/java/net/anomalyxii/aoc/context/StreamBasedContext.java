package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Grid.MutableGrid;
import net.anomalyxii.aoc.utils.ocr.LetterSet;
import net.anomalyxii.aoc.utils.ocr.OCR;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
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
        final List<String> lines = readAllLines();
        if (lines.isEmpty())
            throw new IllegalStateException("Expected one line from the data file, but nothing was read!");
        return lines.getFirst();
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
        return processor.apply(readSingleLine());
    }

    @Override
    public void consume(final Consumer<String> consumer) {
        try (BufferedReader in = openReader()) {
            in.lines().forEach(consumer);
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    @Override
    public Stream<String> stream() {
        return streamAllLines();
    }

    @Override
    public Stream<List<String>> streamBatches() {
        final Stream.Builder<List<String>> builder = Stream.builder();

        try (BufferedReader reader = openReader()) {
            String line;
            List<String> currentBatch = null;
            while ((line = reader.readLine()) != null) {
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
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    @Override
    public Grid readGrid() {
        try (BufferedInputStream in = openStream()) {
            return Grid.parse(in);
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    @Override
    public Grid readGrid(final IntUnaryOperator valueResolver) {
        try (BufferedReader in = openReader()) {
            return Grid.parse(in.lines(), valueResolver);
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    @Override
    public MutableGrid readMutableGrid() {
        try (BufferedReader in = openReader()) {
            return Grid.parseMutable(in.lines());
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    @Override
    public MutableGrid readMutableGrid(final IntUnaryOperator valueResolver) {
        try (BufferedReader in = openReader()) {
            return Grid.parseMutable(in.lines(), valueResolver);
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
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
     * Return a new {@link BufferedInputStream} based on the {@link InputStream}.
     *
     * @return a new {@link BufferedInputStream}
     * @throws IOException if the {@link BufferedInputStream} cannot be opened
     */
    protected abstract BufferedInputStream openStream() throws IOException;

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
        try (BufferedReader in = openReader()) {
            return in.lines().map(processor).toList();
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
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

    /*
     * Read all lines from the underlying `InputStream`.
     */
    private Stream<String> streamAllLines() {
        try {
            final BufferedReader in = openReader();
            return in.lines()
                    .onClose(() -> {
                        try {
                            in.close();
                        } catch (final IOException e) {
                            throw new IllegalStateException("Failed to close input stream!", e);
                        }
                    });
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

    /*
     * Read a single line from the underlying `InputStream`.
     */
    private String readSingleLine() {
        try (BufferedReader in = openReader()) {
            final String line = in.readLine();
            if (line == null)
                throw new IllegalStateException("Expected one line from the data file, but nothing was read!");
            return line;
        } catch (final IOException e) {
            throw new IllegalArgumentException("An error occurred whilst processing '" + describe() + "'", e);
        }
    }

}
