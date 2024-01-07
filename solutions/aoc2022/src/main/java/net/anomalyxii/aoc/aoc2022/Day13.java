package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 13.
 */
@Solution(year = 2022, day = 13, title = "Distress Signal")
public class Day13 {

    private static final CompoundPacketData SEPARATOR_PACKET_1 = new CompoundPacketData(new CompoundPacketData(new SimplePacketData(2)));
    private static final CompoundPacketData SEPARATOR_PACKET_2 = new CompoundPacketData(new CompoundPacketData(new SimplePacketData(6)));

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
        final AtomicInteger index = new AtomicInteger(0);
        final LongAccumulator sum = new LongAccumulator(Long::sum, 0);
        context.streamBatches().forEach(batch -> {
            final int idx = index.incrementAndGet(); // We start from 1?

            final PacketData lhs = PacketData.parse(batch.getFirst());
            final PacketData rhs = PacketData.parse(batch.getLast());

            final int cmp = lhs.compareTo(rhs);
            if (cmp < 0)
                sum.accumulate(idx);
        });
        return sum.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<PacketData> packets = new ArrayList<>();

        packets.add(SEPARATOR_PACKET_1);
        packets.add(SEPARATOR_PACKET_2);

        context.streamBatches().forEach(batch -> {
            packets.add(PacketData.parse(batch.getFirst()));
            packets.add(PacketData.parse(batch.getLast()));
        });

        packets.sort(Comparator.naturalOrder());

        final long packet1Position = packets.indexOf(SEPARATOR_PACKET_1) + 1;
        final long packet2Position = packets.indexOf(SEPARATOR_PACKET_2) + 1;
        return packet1Position * packet2Position;
    }

    // ****************************************
    // Challenge Methods
    // ****************************************

    /*
     * Represents a single piece of data contained within a packet.
     */
    private sealed interface PacketData extends Comparable<PacketData> permits CompoundPacketData, SimplePacketData {

        // Interface Methods

        /**
         * Convert this {@link PacketData} into a {@link CompoundPacketData}.
         *
         * @return the {@link CompoundPacketData}
         */
        CompoundPacketData asCompoundPacketData();

        /**
         * Apply a {@link Function transformation} to this {@link PacketData}.
         *
         * @param onSimple   the {@link Function} to apply if this is {@link SimplePacketData}
         * @param onCompound the {@link Function} to apply if this is {@link CompoundPacketData}
         * @param <T>        the type of the resulting value
         * @return the resulting value
         */
        <T> T fold(Function<SimplePacketData, T> onSimple, Function<CompoundPacketData, T> onCompound);

        // Static Helper Methods

        /*
         * Parse the given line into some `PacketData`.
         */
        static PacketData parse(final String line) {

            final Deque<CompoundPacketData> data = new ArrayDeque<>();
            data.addLast(new CompoundPacketData()); // Root...

            for (int i = 0; i < line.length(); i++) {
                final char c = line.charAt(i);
                if (c == '[') {
                    final CompoundPacketData current = new CompoundPacketData();
                    final CompoundPacketData prev = data.getLast();

                    prev.add(current);
                    data.addLast(current);
                    continue;
                }

                if (c == ']') {
                    data.removeLast();
                    continue;
                }

                if (Character.isDigit(c)) {
                    int val = c - '0';
                    while (Character.isDigit(line.charAt(i + 1))) {
                        final int n = line.charAt(++i);
                        val = (val * 10) + (n - '0');
                    }

                    final CompoundPacketData prev = data.getLast();
                    prev.add(new SimplePacketData(val));
                    continue;
                }

                if (c == ',' || c == ' ')
                    continue;

                throw new IllegalStateException("Could not parse input: '" + line + "': invalid character [" + c + "] at position " + i);
            }

            final CompoundPacketData root = data.getFirst();
            return root.data.getFirst();
        }

    }

    /*
     * `PacketData` consisting of multiple pieces of sub-data.
     */
    private static final class CompoundPacketData implements PacketData {

        // Private Members

        private final List<PacketData> data;

        // Constructors

        private CompoundPacketData() {
            this.data = new ArrayList<>();
        }

        private CompoundPacketData(final PacketData... data) {
            this.data = asList(data);
        }

        private CompoundPacketData(final SimplePacketData data) {
            this.data = Collections.singletonList(data);
        }

        // Public Methods

        /**
         * Add some sub-data to this {@link CompoundPacketData}.
         *
         * @param data the {@link PacketData} to add
         */
        void add(final PacketData data) {
            this.data.add(data);
        }

        // PacketData Methods

        @Override
        public CompoundPacketData asCompoundPacketData() {
            return this;
        }

        @Override
        public <T> T fold(final Function<SimplePacketData, T> onSimple, final Function<CompoundPacketData, T> onCompound) {
            return onCompound.apply(this);
        }

        // Comparable Methods

        @Override
        public int compareTo(final PacketData o) {
            final CompoundPacketData lo = o.asCompoundPacketData();

            int i, j;
            for (i = 0, j = 0; i < data.size() && j < lo.data.size(); i++, j++) {
                final PacketData lhs = data.get(i);
                final PacketData rhs = lo.data.get(j);

                final int cmp = lhs.compareTo(rhs);
                if (cmp != 0) return cmp;
            }

            return Integer.compare(data.size(), lo.data.size());
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final CompoundPacketData that = (CompoundPacketData) o;
            return data.equals(that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }

        // To String

        @Override
        public String toString() {
            return data.stream()
                    .map(PacketData::toString)
                    .collect(Collectors.joining(",", "[", "]"));
        }

    }

    /*
     * `PacketData` containing only a single `Integer` value.
     */
    private record SimplePacketData(int val) implements PacketData {

        // PacketData Methods

        @Override
        public CompoundPacketData asCompoundPacketData() {
            return new CompoundPacketData(this);
        }

        @Override
        public <T> T fold(final Function<SimplePacketData, T> onSimple, final Function<CompoundPacketData, T> onCompound) {
            return onSimple.apply(this);
        }

        // Comparable Methods

        @Override
        public int compareTo(final PacketData o) {
            return o.fold(
                    so -> val - so.val,
                    co -> asCompoundPacketData().compareTo(co)
            );
        }

        // To String

        @Override
        public String toString() {
            return Integer.toString(val);
        }

    }

}

