package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;

import java.util.Arrays;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 9.
 */
@Solution(year = 2024, day = 9, title = "Disk Fragmenter")
public class Day9 {

    // ****************************************
    // Private Members
    // ****************************************

    private final int initialDiskSizeHint;

    // ****************************************
    // Constructors
    // ****************************************

    public Day9() {
        this(1024);
    }

    Day9(final int initialDiskSizeHint) {
        this.initialDiskSizeHint = initialDiskSizeHint;
    }

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
                .flatMapToInt(String::chars)
                .map(c -> c - '0')
                .boxed()
                .reduce(
                        DiskMap.ofSize(initialDiskSizeHint),
                        DiskMap::addBlock,
                        Day9::preventMerge
                )
                .defragment()
                .calculateChecksum();
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
                .flatMapToInt(String::chars)
                .map(c -> c - '0')
                .boxed()
                .reduce(
                        DiskMap.ofSize(initialDiskSizeHint),
                        DiskMap::addBlock,
                        Day9::preventMerge
                )
                .optimise()
                .calculateChecksum();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return a {@link LongTuple} containing the answers for both parts
     */
    @Optimised
    public LongTuple calculateAnswers(final SolutionContext context) {
        final DiskMap map = context.stream()
                .flatMapToInt(String::chars)
                .map(c -> c - '0')
                .boxed()
                .reduce(
                        DiskMap.ofSize(1024),
                        DiskMap::addBlock,
                        Day9::preventMerge
                );
        return new LongTuple(
                map.defragment().calculateChecksum(),
                map.optimise().calculateChecksum()
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Prevent merging - it should never happen!
     */
    private static DiskMap preventMerge(final DiskMap a, final DiskMap b) {
        throw new IllegalStateException("No need to merge");
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a map of files and free space on disk.
     */
    private static final class DiskMap {

        /*
         * Indicates free space
         */
        private static final int FREE_SPACE = -1;

        // Private Members

        private int currentId = 0;
        private boolean isFile = true;

        private int[] items;
        private int size = 0;

        // Constructors

        DiskMap(final int[] initialItems) {
            this.items = initialItems;
        }

        // Helper Methods

        /*
         * Defragment the disk by moving all blocks to the left-most free space.
         */
        DiskMap defragment() {
            final int[] defragged = Arrays.copyOf(this.items, size);

            int forward = 0;
            int backwards = size - 1;
            while (forward < backwards) {
                if (defragged[forward] != FREE_SPACE) {
                    ++forward;
                    continue;
                }

                while (defragged[backwards] == FREE_SPACE && forward < backwards)
                    --backwards;

                defragged[forward++] = defragged[backwards];
                defragged[backwards--] = FREE_SPACE;
            }

            final DiskMap map = new DiskMap(defragged);
            map.currentId = this.currentId;
            map.isFile = this.isFile;
            map.size = this.size;
            return map;
        }

        /*
         * Optimise the disk by moving all files to the left-most free space that
         * is large enough to hold the full file.
         */
        DiskMap optimise() {
            final int[] optimised = Arrays.copyOf(this.items, size);

            int backwards = size - 1;
            int knownForward = 0;
            out:
            while (backwards > knownForward) {
                if (optimised[backwards] == FREE_SPACE) {
                    --backwards;
                    continue;
                }

                final int currentId = optimised[backwards];
                final int currentEnd = backwards;
                while (optimised[backwards] == currentId && backwards > 0)
                    --backwards;

                final int itemSize = currentEnd - backwards;

                int forwards = knownForward;
                while (forwards <= backwards) {
                    if (optimised[forwards] != FREE_SPACE) {
                        ++forwards;
                        continue;
                    }
                    knownForward = forwards;
                    break;
                }

                while (forwards <= backwards) {
                    if (optimised[forwards] != FREE_SPACE) {
                        ++forwards;
                        continue;
                    }

                    final int currentSpaceStart = forwards;
                    while (optimised[forwards] == FREE_SPACE && forwards <= backwards)
                        ++forwards;

                    final int currentSpaceSize = forwards - currentSpaceStart;
                    if (currentSpaceSize >= itemSize) {
                        for (int p = 0; p < itemSize; p++) {
                            optimised[currentSpaceStart + p] = currentId;
                            optimised[backwards + p + 1] = FREE_SPACE;
                        }
                        continue out;
                    }
                }
            }

            final DiskMap map = new DiskMap(optimised);
            map.currentId = this.currentId;
            map.isFile = this.isFile;
            map.size = this.size;
            return map;
        }

        /*
         * Calculate the checksum of the disk map.
         */
        long calculateChecksum() {
            long checksum = 0;
            for (int i = 0; i < size; i++)
                if (items[i] != FREE_SPACE)
                    checksum += (long) i * items[i];
            return checksum;
        }

        /*
         * Add a new block to the disk map.
         */
        DiskMap addBlock(final int size) {
            final int id = isFile ? currentId++ : FREE_SPACE;
            final int newSize = this.size + size;
            for (int p = this.size; p < newSize; p++) {
                if (newSize >= items.length)
                    items = Arrays.copyOf(items, items.length * 2);
                items[p] = id;
            }
            this.size = newSize;
            this.isFile = !this.isFile;
            return this;
        }

        // Static Helper Methods

        /*
         * Create a new `DiskMap`, allocating a certain number of blocks for
         * storage. This `DiskMap` can still grow if the number of blocks exceeds
         * this size, it is merely a suggestion for what an appropriate starting
         * point is.
         */
        static DiskMap ofSize(final int initialDiskSizeHint) {
            return new DiskMap(new int[initialDiskSizeHint]);
        }

    }

}

