package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.ObjectTuple;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 17.
 */
@Solution(year = 2024, day = 17, title = "Chronospatial Computer")
public class Day17 {

    private static final int REGISTER_A = 0;
    private static final int REGISTER_B = 1;
    private static final int REGISTER_C = 2;

    private static final int ADV = 0;
    private static final int BXL = 1;
    private static final int BST = 2;
    private static final int JNZ = 3;
    private static final int BXC = 4;
    private static final int OUT = 5;
    private static final int BDV = 6;
    private static final int CDV = 7;

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
    public String calculateAnswerForPart1(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final long[] registers = loadRegisters(batches);
        final int[] program = loadProgram(batches);

        return runProgram(program, registers)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","));
    }

    private static long[] loadRegisters(final List<List<String>> batches) {
        return batches.getFirst().stream()
                .mapToLong(line -> Integer.parseInt(line.split(":\\s*")[1]))
                .toArray();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();
        final int[] program = loadProgram(batches);

        return findQuine(program);
    }

    private static int[] loadProgram(final List<List<String>> batches) {
        return stream(batches.getLast().getFirst().split(":\\s*")[1].split(",\\s*"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link ObjectTuple} containing the answers for both parts
     */
    @Optimised
    public ObjectTuple<String, Long> calculateAnswers(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();
        final long[] registers = loadRegisters(batches);
        final int[] program = loadProgram(batches);

        return new ObjectTuple<>(
                runProgram(program, registers)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(",")),
                findQuine(program)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find the input that results in the Quine.
     */
    private static IntStream runProgram(final int[] program, final long[] registers) {
        final IntStream.Builder output = IntStream.builder();
        int pc = 0;
        while (pc < program.length) {
            final int operation = program[pc++];
            final int literal = program[pc++];
            final long combo = combo(literal, registers);

            switch (operation) {
                case ADV -> registers[REGISTER_A] = registers[REGISTER_A] >> combo;
                case BXL -> registers[REGISTER_B] = registers[REGISTER_B] ^ literal;
                case BST -> registers[REGISTER_B] = (0x07 & combo);
                case JNZ -> pc = registers[REGISTER_A] != 0 ? literal : pc;
                case BXC -> registers[REGISTER_B] = registers[REGISTER_B] ^ registers[REGISTER_C];
                case OUT -> output.add((int) (0x07 & combo));
                case BDV -> registers[REGISTER_B] = registers[REGISTER_A] >> combo;
                case CDV -> registers[REGISTER_C] = registers[REGISTER_A] >> combo;
                default -> throw new IllegalStateException("Invalid operation: " + operation);
            }
        }

        return output.build();
    }

    /*
     * Find the input that results in the Quine.
     */
    private static long combo(final int i, final long[] registers) {
        return switch (i) {
            case 0, 1, 2, 3 -> i;
            case 4 -> registers[REGISTER_A];
            case 5 -> registers[REGISTER_B];
            case 6 -> registers[REGISTER_C];
            default -> throw new IllegalArgumentException("Invalid value: " + i);
        };
    }

    /*
     * Find the input that results in the Quine.
     */
    private static long findQuine(final int[] program) {
        final long result = findQuine(program, 0, 0);
        if (result == -1) throw new IllegalStateException("Did not find a Quine");
        return result;
    }

    /*
     * Find the input that results in the Quine.
     */
    private static long findQuine(final int[] program, final int pos, final long total) {
        if (pos >= program.length) {
            final int[] result = runProgram(program, new long[]{total, 0, 0})
                    .toArray();
            return Arrays.equals(program, result) ? total : -1;
        }

        for (int x = 0; x < 8; x++) {
            final long newTotal = ((long) x << (3 * (program.length - pos - 1))) | total;
            final int[] result = runProgram(program, new long[]{newTotal, 0, 0})
                    .toArray();

            final int[] subProg = Arrays.copyOfRange(program, (program.length - pos) - 1, program.length);
            final int[] subResult = result.length > pos
                    ? Arrays.copyOfRange(result, (result.length - pos) - 1, result.length)
                    : result;

            if (!Arrays.equals(subProg, subResult)) continue;

            final long score = findQuine(program, pos + 1, newTotal);
            if (score >= 0) return score;
        }

        return -1;
    }

}

