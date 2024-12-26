package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.Challenge;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Grid.MutableGrid;
import net.anomalyxii.aoc.utils.ocr.OCR;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

/**
 * Provides invocation-specific information to a {@link Challenge}.
 */
public interface SolutionContext {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * Read all lines in the challenge data file and return them as a
     * {@link List}.
     *
     * @return the {@link List} of lines from the file
     */
    List<String> read();

    /**
     * Read a single line in the challenge data file and return the value.
     *
     * @return the first line of the data file
     * @throws IllegalStateException if the data file was empty
     */
    String readLine();

    /**
     * Read the input lines in chunks ("batches") of continuous
     * lines separated by a single blank line.
     *
     * @return a {@link List} of batched lines
     */
    List<List<String>> readBatches();

    /**
     * Process all lines in the challenge data file using the specified
     * {@link Function} and return the results.
     *
     * @param processor the {@link Function} to apply to each line
     * @param <T>       the type of result returned for each line
     * @return the {@link List} of results
     */
    <T> List<T> process(Function<String, T> processor);

    /**
     * Process a single line from the challenge data file using the specified
     * {@link Function} and return the result.
     *
     * @param processor the {@link Function} to apply to the line
     * @param <T>       the type of result returned for the line
     * @return the result of processing the first line
     * @throws IllegalStateException if the data file was empty
     */
    <T> T processLine(Function<String, T> processor);

    /**
     * Process all lines without returning a result.
     *
     * @param consumer the line consumer
     */
    void consume(Consumer<String> consumer);

    /**
     * {@link Stream} the input lines.
     *
     * @return a {@link Stream} of lines
     */
    Stream<String> stream();

    /**
     * {@link Stream} the input lines in chunks ("batches") of continuous
     * lines separated by a single blank line.
     *
     * @return a {@link Stream} of batched lines
     */
    Stream<List<String>> streamBatches();

    /**
     * Parse all lines in the challenge data file into a {@link Grid}.
     *
     * @return the {@link Grid}
     */
    Grid readGrid();

    /**
     * Parse all lines in the challenge data file into a {@link Grid}.
     *
     * @param valueResolver the {@link IntUnaryOperator function} that resolves the value for each {@link Character}
     * @return the {@link Grid}
     */
    Grid readGrid(IntUnaryOperator valueResolver);

    /**
     * Parse all lines in the challenge data file into a {@link MutableGrid}.
     *
     * @return the {@link MutableGrid}
     */
    MutableGrid readMutableGrid();

    /**
     * Parse all lines in the challenge data file into a {@link MutableGrid}.
     *
     * @param valueResolver the {@link IntUnaryOperator function} that resolves the value for each {@link Character}
     * @return the {@link MutableGrid}
     */
    MutableGrid readMutableGrid(IntUnaryOperator valueResolver);

    /**
     * Get an {@link OCR} relevant to this {@link SolutionContext}.
     *
     * @return the {@link OCR}
     */
    OCR ocr();

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Create a {@link SolutionContextBuilder}.
     *
     * @return the {@link SolutionContextBuilder}
     */
    static SolutionContextBuilder builder() {
        return new SolutionContextBuilder();
    }

    /**
     * Create a {@link SolutionContext} for the default example for a given day.
     *
     * @param day the day
     * @return the {@link SolutionContext}
     */
    static SolutionContext example(final int day) {
        return builder().example(day).build();
    }

    /**
     * Create a {@link SolutionContext} for an alternative example for a given day.
     *
     * @param day     the day
     * @param variant the example variant
     * @return the {@link SolutionContext}
     */
    static SolutionContext example(final int day, final int variant) {
        return builder().example(day, variant).build();
    }

    /**
     * Create a {@link SolutionContext} for a given day.
     *
     * @param year the year
     * @param day  the day
     * @return the {@link SolutionContext}
     */
    static SolutionContext live(final int year, final int day) {
        return builder().live(year, day).build();
    }
}
