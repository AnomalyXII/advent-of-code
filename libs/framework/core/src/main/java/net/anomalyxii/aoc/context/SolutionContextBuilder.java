package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.utils.ocr.LetterSet;

import java.nio.file.Path;

/**
 * Builds a {@link SolutionContext}.
 */
public class SolutionContextBuilder {

    // ****************************************
    // Private Members
    // ****************************************

    private String dataFile;
    private Type type;
    private LetterSet letters = LetterSet.standardLetters();

    // ****************************************
    // Builder Methods
    // ****************************************

    /**
     * Create a {@link SolutionContext} for the default example for a given day.
     *
     * @param day the day
     * @return a {@link SolutionContextBuilder} for chaining
     */
    public SolutionContextBuilder example(final int day) {
        return loading("day" + day + "-test.txt", Type.CLASSPATH);
    }

    /**
     * Create a {@link SolutionContext} for an alternative example for a given day.
     *
     * @param day     the day
     * @param variant the example variant
     * @return a {@link SolutionContextBuilder} for chaining
     */
    public SolutionContextBuilder example(final int day, final int variant) {
        return loading("day" + day + "_" + variant + "-test.txt", Type.CLASSPATH);
    }

    /**
     * Create a {@link SolutionContext} for a given day.
     *
     * @param year the year
     * @param day  the day
     * @return a {@link SolutionContextBuilder} for chaining
     */
    public SolutionContextBuilder live(final int year, final int day) {
        return loading(year + "/day" + day + ".txt", Type.CLASSPATH);
    }

    /**
     * Create a {@link SolutionContext} taking data from the specified file.
     *
     * @param dataFile the data file to load
     * @return a {@link SolutionContextBuilder} for chaining
     */
    public SolutionContextBuilder path(final String dataFile) {
        return loading(dataFile, Type.FILESYSTEM);
    }

    /**
     * Register the specified {@link LetterSet} to be used with the resulting
     * {@link SolutionContext}.
     * <p>
     * If no {@link LetterSet} is explicitly specified, the
     * {@link LetterSet#standardLetters() standard LetterSet} will be used.
     *
     * @param letters the {@link LetterSet}
     * @return a {@link SolutionContextBuilder} for chaining
     */
    public SolutionContextBuilder withLetterSet(final LetterSet letters) {
        this.letters = letters;
        return this;
    }

    /**
     * Build the {@link SolutionContext}.
     *
     * @return the new {@link SolutionContext}
     */
    public SolutionContext build() {
        return switch (type) {
            case CLASSPATH -> new ResourceBasedContext(dataFile, letters);
            case FILESYSTEM -> new FileBasedContext(Path.of(dataFile), letters);
        };
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Set the data file to load and how to load it.
     */
    private SolutionContextBuilder loading(final String dataFile, final Type type) {
        this.dataFile = dataFile;
        this.type = type;
        return this;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The type of input to load.
     */
    private enum Type {

        /*
         * Load a resource from the classpath.
         */
        CLASSPATH,

        /*
         * Load a resource from the filesystem.
         */
        FILESYSTEM,

        // End of constants
        ;
    }

}
