package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 16.
 */
@Solution(year = 2021, day = 16, title = "Packet Decoder")
public class Day16 {

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
        return context.processLine(line -> {
            final AtomicLong versionSum = new AtomicLong(0);
            Packet.parse(line, versionSum::addAndGet);
            return versionSum.longValue();
        });
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return context.processLine(line -> Packet.parse(line, version -> {
        }).value);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Read bits from the input String.
     */
    private static final class BitReader {

        // Private Members

        private final String input;
        private int position = 0;

        // Constructors

        BitReader(final String input) {
            this.input = input;
        }

        // Helper Methods

        /*
         * Get the current position in the input String.
         */
        int currentPos() {
            return position;
        }

        /*
         * Read a single bit as a `boolean`.
         */
        boolean readBitAsBoolean() {
            return "1".equals(readBits(1));
        }

        /*
         * Read a number of bits and return the value as an `int`.
         */
        int readBitsAsInt(final int size) {

            return Integer.parseInt(readBits(size), 2);
        }

        // Private Methods

        /*
         * Read a String of bits.
         */
        private String readBits(final int size) {
            return input.substring(position, (position += size));
        }

        // Static Methods

        /*
         * Create a `BitReader` for the given line.
         */
        static BitReader fromHexString(final String line) {
            return new BitReader(line.chars()
                                         .map(chr -> Integer.parseInt(Character.toString(chr), 16))
                                         .mapToObj(Integer::toBinaryString)
                                         .map(b -> String.format("%4s", b).replace(' ', '0'))
                                         .collect(Collectors.joining()));
        }

    }

    /*
     * A packet.
     */
    private record Packet(int version, PacketType type, long value) {

        // Helper Methods

        /*
         * Parse a packet.
         */
        private static Packet parse(final String line, final IntConsumer onNewPacket) {
            return parse(BitReader.fromHexString(line), onNewPacket);
        }

        /*
         * Parse a packet.
         */
        private static Packet parse(final BitReader reader, final IntConsumer onNewPacket) {
            final int version = reader.readBitsAsInt(3);
            final int typeCode = reader.readBitsAsInt(3);
            final PacketType packetType = PacketType.fromCode(typeCode);

            onNewPacket.accept(version);
            final long value = packetType.resolve(reader, onNewPacket);

            return new Packet(version, packetType, value);
        }

    }

    /*
     * The various packet types.
     */
    private enum PacketType {

        LITERAL(4) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                boolean last;
                long result = 0;
                do {
                    last = !reader.readBitAsBoolean();
                    result = (result << 4) | reader.readBitsAsInt(4);
                } while (!last);

                return result;
            }
        },

        SUM(0) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                return parseChildren(reader, versionSum)
                        .sum();
            }
        },
        PRODUCT(1) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                return parseChildren(reader, versionSum)
                        .reduce(1L, (result, next) -> result * next);
            }
        },
        MINIMUM(2) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                return parseChildren(reader, versionSum).min().orElseThrow();
            }
        },
        MAXIMUM(3) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                return parseChildren(reader, versionSum).max().orElseThrow();
            }
        },
        GREATER(5) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                final long[] literals = parseChildren(reader, versionSum).toArray();
                return literals[0] > literals[1] ? 1L : 0L;
            }
        },
        LESS_THAN(6) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                final long[] literals = parseChildren(reader, versionSum).toArray();
                return literals[0] < literals[1] ? 1L : 0L;
            }
        },
        EQUAL_TO(7) {
            @Override
            long resolve(final BitReader reader, final IntConsumer versionSum) {
                final long[] literals = parseChildren(reader, versionSum).toArray();
                return literals[0] == literals[1] ? 1L : 0L;
            }
        },

        // End of constants
        ;

        private final int code;

        // Constructors

        PacketType(final int code) {
            this.code = code;
        }

        // Helper Methods

        /*
         * Resolve the value of this `Packet`, based on the `PacketType`.
         */
        abstract long resolve(BitReader reader, IntConsumer onNewPacket);

        // Private Helper Methods

        /*
         * Resolve the `PacketType` from the given code.
         */
        static PacketType fromCode(final int code) {
            for (final PacketType type : values()) {
                if (type.code == code)
                    return type;
            }

            throw new IllegalArgumentException("Invalid packet type: " + code);
        }

        /*
         * Parse the payload of the `Packet`.
         */
        private static LongStream parseChildren(final BitReader reader, final IntConsumer onNewPacket) {
            final LongStream.Builder builder = LongStream.builder();
            final boolean relativeLength = reader.readBitAsBoolean();
            if (!relativeLength) {
                final int length = reader.readBitsAsInt(15);
                final int endPosition = reader.currentPos() + length;
                while (reader.currentPos() < endPosition)
                    builder.add(Packet.parse(reader, onNewPacket).value);
            } else {
                final int length = reader.readBitsAsInt(11);
                for (int i = 0; i < length; i++)
                    builder.add(Packet.parse(reader, onNewPacket).value);

            }
            return builder.build();
        }

    }

}
