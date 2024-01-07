package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.utils.ocr.LetterSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A {@link SolutionContext} that is backed by a {@link File}.
 */
public class FileBasedContext extends StreamBasedContext {

    // ****************************************
    // Private Members
    // ****************************************

    private final Path datafile;

    // ****************************************
    // Constructors
    // ****************************************

    FileBasedContext(final Path datafile, final LetterSet letters) {
        super(letters);
        this.datafile = datafile;
    }

    // ****************************************
    // StreamBasedContext Methods
    // ****************************************

    @Override
    protected BufferedReader openReader() throws IOException {
        return Files.newBufferedReader(datafile, StandardCharsets.UTF_8);
    }

    @Override
    protected String describe() {
        return datafile.toAbsolutePath().toString();
    }

}
