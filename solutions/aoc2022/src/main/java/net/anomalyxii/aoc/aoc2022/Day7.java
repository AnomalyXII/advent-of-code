package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 7.
 */
@Solution(year = 2022, day = 7, title = "No Space Left On Device")
public class Day7 {

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
        final Map<String, Directory> dirs = parseCommandsAndOutput(context);
        return dirs.values().stream()
                .mapToLong(Directory::computeSize)
                .filter(size -> size <= 100000)
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
        final Map<String, Directory> dirs = parseCommandsAndOutput(context);

        final long requiredSpace = 30000000 - (70000000 - dirs.get("/").computeSize());
        return dirs.values().stream()
                .mapToLong(Directory::computeSize)
                .filter(size -> size >= requiredSpace)
                .min()
                .orElseThrow();
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Parse the command line output into a directory tree.
     */
    private static Map<String, Directory> parseCommandsAndOutput(final SolutionContext context) {
        final Map<String, Directory> dirs = new HashMap<>();
        dirs.put("/", Directory.createRoot());

        final AtomicReference<Directory> cwdHolder = new AtomicReference<>(dirs.get("/"));
        context.consume(line -> {
            if (line.startsWith("$")) parseCommand(dirs, cwdHolder, line);
            else parseDirectoryItem(dirs, line, cwdHolder.get());
        });
        return dirs;
    }

    /*
     * Parse a line of output representing a command input.
     */
    private static void parseCommand(final Map<String, Directory> dirs, final AtomicReference<Directory> cwdHolder, final String line) {
        if (line.startsWith("$ cd")) {
            final String dirName = line.substring(5);
            final Directory nwd = resolveChangeDirectory(dirs, cwdHolder.get(), dirName);
            if (nwd == null)
                throw new IllegalStateException("Directory not found: " + dirName);
            cwdHolder.set(nwd);
        }
    }

    /*
     * Parse a line of output representing part of a command output.
     */
    private static void parseDirectoryItem(final Map<String, Directory> dirs, final String line, final Directory cwd) {
        if (line.startsWith("dir")) parseDirectoryEntry(dirs, line, cwd);
        else parseFileEntry(line, cwd);
    }

    /*
     * Resolve a directory specified by a `cd` command.
     *
     * Resolve relative directories (i.e. directories that do not start with
     * a `/`) against the current directory; resolve absolute directories
     * directly against the root tree.
     *
     * Resolve the directory `..` as the current directory's parent.
     */
    private static Directory resolveChangeDirectory(final Map<String, Directory> dirs, final Directory cwd, final String dirName) {
        return "..".equals(dirName)
                ? cwd.parent
                : dirName.startsWith("/")
                        ? dirs.get(dirName)
                        : cwd.subDirectories.get(dirName);
    }

    /*
     * Parse a command line output containing a directory entry.
     */
    private static void parseDirectoryEntry(final Map<String, Directory> dirs, final String line, final Directory cwd) {
        final Directory dir = Directory.create(line.substring(4), cwd);
        dirs.put(dir.absolutePath, dir);
        cwd.subDirectories.put(dir.name, dir);
    }

    /*
     * Parse a command line output containing a file entry, with a file size.
     */
    private static void parseFileEntry(final String line, final Directory cwd) {
        final String[] entries = line.split(" ");
        final int fileSize = Integer.parseInt(entries[0]);
        final String fileName = entries[1];
        cwd.files.put(fileName, File.create(fileSize));
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A file entry.
     *
     * A file has one property: the file size.
     */
    private record File(int size) {

        // Static Helper Methods

        /**
         * Create a new {@link File} with a given size.
         *
         * @param size the file size
         * @return the {@link File}
         */
        public static File create(final int size) {
            return new File(size);
        }

    }

    /*
     * A directory entry.
     *
     * Directories have no inherent size; their computed size is the sum of
     * the size of all files contained within plus the computed size of all
     * sub-directories.
     */
    private static final class Directory {

        // Private Members

        private final String absolutePath;
        private final String name;
        private final Directory parent;

        private final Map<String, Directory> subDirectories = new HashMap<>();
        private final Map<String, File> files = new HashMap<>();

        private volatile long computedSize = -1;

        // Constructors

        private Directory(final String absolutePath, final String name, final Directory parent) {
            this.absolutePath = absolutePath;
            this.name = name;
            this.parent = parent;
        }

        // Helper Methods

        /**
         * Get the computed size of this {@link Directory}.
         *
         * @return the total size
         */
        long computeSize() {
            if (computedSize < 0) {
                final long indirectSize = subDirectories.values().stream()
                        .mapToLong(Directory::computeSize)
                        .sum();
                final long directSize = files.values().stream()
                        .mapToLong(file -> file.size)
                        .sum();
                computedSize = indirectSize + directSize;
            }

            return computedSize;
        }

        // Static Helper Methods

        public static Directory createRoot() {
            return new Directory("/", "/", null);
        }

        public static Directory create(final String name, final Directory parent) {
            final String parentPath = parent.absolutePath.endsWith("/")
                    ? parent.absolutePath
                    : parent.absolutePath + "/";
            final String absolutePath = parentPath + name;
            return new Directory(absolutePath, name, parent);
        }
    }

}
