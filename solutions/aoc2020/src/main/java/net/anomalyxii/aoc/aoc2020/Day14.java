package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 14.
 */
@Solution(year = 2020, day = 14, title = "Docking Data")
public class Day14 {

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
        final List<String> instructions = context.read();

        final long[] bitMasks = {-1L, 0L};
        final long[] assignment = {-1L, 0L};

        final Map<Long, Long> memory = new HashMap<>();
        instructions.forEach(inst -> {
            if (inst.startsWith("mask = ")) {
                parseBitMasks(inst, bitMasks);
                return;
            }

            if (inst.startsWith("mem[")) {
                parseMemoryAssignment(inst, assignment);

                final long maskedValue = maskValue(assignment[1], bitMasks);
                memory.put(assignment[0], maskedValue);
                return;
            }

            throw new IllegalArgumentException("Invalid instruction: '" + inst + "'");
        });

        return memory.values().stream()
                .mapToLong(val -> val)
                .sum();
    }


    /**
     * For some reason, the sea port's computer system still can't communicate with your ferry's docking program.
     * It must be using <i>version 2</i> of the decoder chip!
     * <p>
     * A version 2 decoder chip doesn't modify the values being written at all.
     * Instead, it acts as a <a href="https://www.youtube.com/watch?v=PvfhANgLrm4">memory address decoder</a>.
     * Immediately before a value is written to memory, each bit in the bitmask modifies the corresponding bit of the destination <i>memory address</i> in the following way:
     * <ul>
     * <li> If the bitmask bit is <code>0</code>, the corresponding memory address bit is <i>unchanged</i>. </li>
     * <li> If the bitmask bit is <code>1</code>, the corresponding memory address bit is <i>overwritten with <code>1</code></i>. </li>
     * <li> If the bitmask bit is <code>X</code>, the corresponding memory address bit is <i>floating</i>. </li>
     * </ul>
     * A floating bit is not connected to anything and instead fluctuates unpredictably.
     * In practice, this means the floating bits will take on all possible values, potentially causing many memory addresses to be written all at once!
     * <p>
     * For example, consider the following program:
     * <pre>
     * mask = 000000000000000000000000000000X1001X
     * mem[42] = 100
     * mask = 00000000000000000000000000000000X0XX
     * mem[26] = 1
     * </pre>
     * <p>
     * When this program goes to write to memory address 42, it first applies the bitmask:
     * <pre>
     * address: 000000000000000000000000000000101010  (decimal 42)
     * mask:    000000000000000000000000000000X1001X
     * result:  000000000000000000000000000000X1101X
     * </pre>
     * <p>
     * After applying the mask, four bits are overwritten, three of which are different, and two of which are <i>floating</i>.
     * Floating bits take on every possible combination of values; with two floating bits, four actual memory addresses are written:
     * <pre>
     * 000000000000000000000000000000011010  (decimal 26)
     * 000000000000000000000000000000011011  (decimal 27)
     * 000000000000000000000000000000111010  (decimal 58)
     * 000000000000000000000000000000111011  (decimal 59)
     * </pre>
     * <p>
     * Next, the program is about to write to memory address 26 with a different bitmask:
     * <pre>
     * address: 000000000000000000000000000000011010  (decimal 26)
     * mask:    00000000000000000000000000000000X0XX
     * result:  00000000000000000000000000000001X0XX
     * </pre>
     * <p>
     * This results in an address with three floating bits, causing writes to <i>eight</i> memory addresses:
     * <pre>
     * 000000000000000000000000000000010000  (decimal 16)
     * 000000000000000000000000000000010001  (decimal 17)
     * 000000000000000000000000000000010010  (decimal 18)
     * 000000000000000000000000000000010011  (decimal 19)
     * 000000000000000000000000000000011000  (decimal 24)
     * 000000000000000000000000000000011001  (decimal 25)
     * 000000000000000000000000000000011010  (decimal 26)
     * 000000000000000000000000000000011011  (decimal 27)
     * </pre>
     * <p>
     * The entire 36-bit address space still begins initialized to the value 0 at every address, and you still need the sum of all values left in memory at the end of the program.
     * In this example, the sum is <i><code>208</code></i>.
     * <p>
     * Execute the initialization program using an emulator for a version 2 decoder chip.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return What is the sum of all values left in memory after it completes?
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> instructions = context.read();

        final String[] bitMasks = new String[1];
        final long[] assignment = {-1L, 0L};

        final Map<Long, Long> memory = new HashMap<>();
        instructions.forEach(inst -> {
            if (inst.startsWith("mask")) {
                parseVariableBitMasks(inst, bitMasks);
                return;
            }

            if (inst.startsWith("mem")) {
                parseMemoryAssignment(inst, assignment);

                final List<Long> maskedAddresses = maskAddress(assignment[0], bitMasks[0]);
                maskedAddresses.forEach(maddr -> memory.put(maddr, assignment[1]));
                return;
            }

            throw new IllegalArgumentException("Invalid instruction: '" + inst + "'");
        });

        return memory.values().stream()
                .mapToLong(val -> val)
                .sum();
    }

    // ****************************************
    // Private Helper
    // ****************************************

    /*
     * Parse a bitmask definition.
     */
    private static void parseBitMasks(final String inst, final long[] bitMasks) {
        final String mask = inst.substring(7);

        bitMasks[0] = andMask(mask);
        bitMasks[1] = orMask(mask);
    }

    /*
     * Parse a variable bitmask definition.
     */
    private static void parseVariableBitMasks(final String inst, final String[] bitMasks) {
        bitMasks[0] = inst.substring(7);
    }

    /*
     * Parse a memory assignment instruction.
     */
    private static void parseMemoryAssignment(final String inst, final long[] assignment) {
        final String[] parts = inst.split(" *= *", 2);

        assignment[0] = Long.parseLong(parts[0].trim().substring(4, parts[0].length() - 1));
        assignment[1] = Long.parseLong(parts[1].trim());
    }

    /*
     * Create a bitmask for setting 1-bits.
     */
    private static long orMask(final String mask) {
        return mask.chars()
                .mapToLong(chr -> chr == '1' ? 1L : 0L)
                .reduce(0L, (result, val) -> (result << 1) | val);
    }

    /*
     * Create a bitmask for setting 0-bits.
     */
    private static long andMask(final String mask) {
        return mask.chars()
                .mapToLong(chr -> chr == '0' ? 0L : 1L)
                .reduce(0L, (result, val) -> (result << 1) | val);
    }

    /*
     * Mask the given long value with the two provided bitmasks:
     *   - bitMasks[0] = andMask
     *   - bitMasks[1] = orMask
     */
    private static long maskValue(final long value, final long[] bitMasks) {
        return (value & bitMasks[0]) | bitMasks[1];
    }

    /*
     * Mask the given address with a variable bitmask.
     */
    private static List<Long> maskAddress(final long address, final String bitMask) {

        final long baseMask = andMask(bitMask);
        final long baseAddress = address | baseMask;

        final List<Long> maskedAddresses = produceFluxBitMasks(bitMask);
        return maskedAddresses.stream()
                .map(mask -> baseAddress & mask)
                .collect(Collectors.toList());
    }

    /*
     * Produce all the variations for the given variable bitmask.
     */
    private static List<Long> produceFluxBitMasks(final String bitMask) {
        if (bitMask.isEmpty()) {
            return Collections.singletonList(0L);
        }

        final char maskChr = bitMask.charAt(0);
        final List<Long> subMasks = produceFluxBitMasks(bitMask.substring(1));

        if (maskChr == '0' || maskChr == '1') {
            return subMasks.stream()
                    .map(mask -> (1L << bitMask.length() - 1) | mask)
                    .collect(Collectors.toList());
        }

        if (maskChr == 'X') {
            return subMasks.stream()
                    .flatMap(mask -> Stream.of(
                            (~(1L << (bitMask.length() - 1))) & mask,
                            (1L << (bitMask.length() - 1)) | mask
                    ))
                    .collect(Collectors.toList());
        }

        throw new IllegalArgumentException("Invalid mask character: '" + maskChr + "'");
    }

}
