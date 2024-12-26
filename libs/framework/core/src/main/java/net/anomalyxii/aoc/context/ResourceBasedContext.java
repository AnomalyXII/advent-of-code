package net.anomalyxii.aoc.context;

import net.anomalyxii.aoc.utils.ocr.LetterSet;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A {@link SolutionContext} that is backed by a {@link File}.
 */
public class ResourceBasedContext extends StreamBasedContext {

    // ****************************************
    // Private Members
    // ****************************************

    private final String datafile;

    // ****************************************
    // Constructors
    // ****************************************

    ResourceBasedContext(final String datafile, final LetterSet letters) {
        super(letters);
        this.datafile = datafile;
    }

    // ****************************************
    // StreamBasedContext Methods
    // ****************************************

    @Override
    protected String describe() {
        return datafile;
    }

    @Override
    protected BufferedInputStream openStream() throws IOException {
        final InputStream resource = ResourceBasedContext.class.getClassLoader().getResourceAsStream(datafile);
        if (resource == null)
            throw new FileNotFoundException("Failed to open datafile: 'classpath:/" + datafile + "'");

        return new BufferedInputStream(resource);
    }

    @Override
    protected BufferedReader openReader() throws FileNotFoundException {
        final InputStream resource = ResourceBasedContext.class.getClassLoader().getResourceAsStream(datafile);
        if (resource == null)
            throw new FileNotFoundException("Failed to open datafile: 'classpath:/" + datafile + "'");

        return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
    }

}
