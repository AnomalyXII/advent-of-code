package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 24.
 */
@Solution(year = 2024, day = 24, title = "Crossed Wires")
public class Day24 {

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
        final List<List<String>> batches = context.readBatches();
        final Map<String, Wire> wires = batches.getFirst().stream()
                .map(Wire::parse)
                .collect(Collectors.toMap(w -> w.id, w -> w));

        final Set<Gate> gates = batches.getLast().stream()
                .map(line -> Gate.parse(line, wires))
                .collect(Collectors.toSet());

        int remaining = gates.size();
        while (!gates.isEmpty()) {
            final Iterator<Gate> it = gates.iterator();
            while (it.hasNext()) {
                final Gate gate = it.next();
                if (!gate.canResolve()) continue;
                gate.apply();
                it.remove();
            }

            assert gates.size() != remaining : "Looped but did not resolve any further wires...";
            remaining = gates.size();
        }

        return reconstructNumberFromBits(wires, "z");
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();
        final Map<String, Wire> wires = batches.getFirst().stream()
                .map(Wire::parse)
                .collect(Collectors.toMap(w -> w.id, w -> w));

        final Set<Gate> gates = batches.getLast().stream()
                .map(line -> Gate.parse(line, wires))
                .collect(Collectors.toSet());


        return findSwapsInAReallyDodgyWayThatProbablyIsNotGeneric(gates, wires);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Format an iterate number as a `x`-wire ID.
     */
    private static String x(final int n) {
        return "x%02d".formatted(n);
    }

    /*
     * Format an iterate number as a `y`-wire ID.
     */
    private static String y(final int n) {
        return "y%02d".formatted(n);
    }

    /*
     * Reconstruct the output number from the `z`-wire bits.
     */
    private static long reconstructNumberFromBits(final Map<String, Wire> wires, final String prefix) {
        return wires.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .sorted(Map.Entry.<String, Wire>comparingByKey().reversed())
                .map(Map.Entry::getValue)
                .peek(w -> {
                    assert w.value != null;
                })
                .reduce(
                        0L,
                        (r, w) -> (r << 1) | (w.value ? 1 : 0),
                        (a, b) -> {
                            throw new IllegalArgumentException("Should not merge!");
                        }
                );
    }

    /*
     * Construct a `Set` of wire IDs that must be swapped to get the adder to
     * function correctly.
     *
     * This "works on my machine", but I doubt it'll work for other inputs :(
     */
    private static String findSwapsInAReallyDodgyWayThatProbablyIsNotGeneric(
            final Set<Gate> gates,
            final Map<String, Wire> wires
    ) {
        int maxXY = 0;
        while (wires.containsKey(x(maxXY)) && wires.containsKey(y(maxXY))) ++maxXY;


        final Map<Inputs, Gate> gatesByInputs = gates.stream()
                .collect(Collectors.toMap(
                        Gate::inputs,
                        Function.identity()
                ));

        // For n0 and n1, there should be a pattern of
        //  => xn1 ^ yn1 -> an1    // add x and y
        //  => xn1 & yn1 -> bn1    // carry x and y
        //  => an1 ^ dn0 -> zn1    // add prev carry
        //  => an1 & dn0 -> cn1    // carry prev carry
        //  => bn1 | cn1 -> dn1    // resolve new carry

        final Set<String> swaps = new HashSet<>();
        for (int n = 0; n < maxXY; n++) {
            final Wire xn1 = wires.get(x(n));
            final Wire yn1 = wires.get(y(n));
            assert xn1 != null : "Did not find Wire for " + x(n);
            assert yn1 != null : "Did not find Wire for " + y(n);

            //  => xn1 ^ yn1 -> an1
            //  => xn1 & yn1 -> bn1
            final Gate an1 = gatesByInputs.get(Inputs.of(xn1, yn1, Operation.XOR));
            final Gate bn1 = gatesByInputs.get(Inputs.of(xn1, yn1, Operation.AND));
            assert an1 != null : "Did not find " + xn1 + " XOR " + yn1;
            assert bn1 != null : "Did not find " + xn1 + " AND " + yn1;

            if (n == 0) {
                final boolean an1Valid = an1.out.id.startsWith("z");
                final boolean bn1Valid = !bn1.out.id.startsWith("z");
                assert an1Valid && bn1Valid : "an1 and bn1 should always be valid for n=0...";
                continue;
            }

            final boolean an1Valid = !an1.out.id.startsWith("z");
            assert an1Valid : "an1 should always be valid for n=" + n + "!";

            // Find an XOR with either an1 or bn1
            final Gate[] zn1s = gates.stream()
                    .filter(g -> g.hasAnyInputAndOp(Operation.XOR, an1.out, bn1.out))
                    .toArray(Gate[]::new);
            assert zn1s.length == 1;
            final Gate zn1 = zn1s[0];
            // Find an AND with either an1 or bn1
            final Gate[] cn1s = gates.stream()
                    .filter(g -> g.hasAnyInputAndOp(Operation.AND, an1.out, bn1.out))
                    .toArray(Gate[]::new);
            assert cn1s.length == 1;
            final Gate cn1 = cn1s[0];
            // Find an OR with either bn1, cn1 or zn1
            final Gate[] dn1s = gates.stream()
                    .filter(g -> g.hasAnyInputAndOp(Operation.OR, bn1.out, cn1.out, zn1.out))
                    .toArray(Gate[]::new);
            assert dn1s.length == 1;
            final Gate dn1 = dn1s[0];

            if (!zn1.out.id.startsWith("z")) {
                final boolean az = an1.out.id.startsWith("z");
                final boolean bz = bn1.out.id.startsWith("z");
                final boolean cz = cn1.out.id.startsWith("z");
                final boolean dz = dn1.out.id.startsWith("z");
                assert az || bz || cz || dz : "if zn1 does not output to a z-wire, something else should...";

                // Should swap an1 and zn1...
                if (az) {
                    assert !bz && !cz && !dz : "if an1 outputs to a z-wire, no other wire should...";
                    swaps.add(an1.out.id);
                    swaps.add(zn1.out.id);
                }

                if (bz) {
                    assert !az && !cz && !dz : "if bn1 outputs to a z-wire, no other wire should...";
                    swaps.add(bn1.out.id);
                    swaps.add(zn1.out.id);
                }

                if (cz) {
                    assert !az && !bz && !dz : "if cn1 outputs to a z-wire, no other wire should...";
                    swaps.add(cn1.out.id);
                    swaps.add(zn1.out.id);
                }

                if (dz) {
                    assert !az && !bz && !cz : "if dn1 outputs to a z-wire, no other wire should...";
                    swaps.add(dn1.out.id);
                    swaps.add(zn1.out.id);
                }
            }

            // Find the common input between zn1 and cn1
            final Wire[] dn0s = Stream.of(zn1.inputs.in1, zn1.inputs.in2, cn1.inputs.in1, cn1.inputs.in2, dn1.inputs.in1, dn1.inputs.in2)
                    .distinct()
                    .filter(w -> w != an1.out && w != bn1.out)
                    .filter(w -> zn1.hasInput(w) && cn1.hasInput(w))
                    .toArray(Wire[]::new);
            assert dn0s.length == 1;
            final Wire dn0 = dn0s[0];
            assert dn0 != null : "Did not find a common wire...";

            // Check that the dn0 wire is at least correct for zn1 and cn1...
            assert zn1.hasInput(dn0) : "zn1 should be an XOR with dn0...";
            assert cn1.hasInput(dn0) : "cn1 should be an AND with dn0...";

            if (!zn1.hasInput(an1.out)) {
                // Maybe an1 and bn1 have been swapped, otherwise, wat??
                assert zn1.hasInput(bn1.out);
                swaps.add(an1.out.id);
                swaps.add(bn1.out.id);
            }

            if (!cn1.hasInput(an1.out)) { // This check is mostly redundant, but here for completeness?
                // Maybe an1 and bn1 have been swapped, otherwise, wat??
                assert cn1.hasInput(bn1.out);
                swaps.add(an1.out.id);
                swaps.add(bn1.out.id);
            }

            // Check that the cn1 wire actually feeds into dn1...
            assert dn1.hasInput(cn1.out) || dn1.hasInput(bn1.out) : "dn1 should be an OR with bn1 and cn1";

            if (dn1.hasInput(cn1.out)) {
                if (!dn1.hasInput(bn1.out)) {
                    // Maybe bn1 and an1 have been swapped?
                    if (dn1.hasInput(an1.out)) {
                        swaps.add(an1.out.id);
                        swaps.add(bn1.out.id);
                    }

                    // Or maybe bn1 and zn1 have been swapped?
                    if (dn1.hasInput(zn1.out)) {
                        swaps.add(zn1.out.id);
                        swaps.add(bn1.out.id);
                    }
                }
            } else {
                // Maybe cn1 and an1 have been swapped?
                if (dn1.hasInput(an1.out)) {
                    swaps.add(an1.out.id);
                    swaps.add(cn1.out.id);
                }

                // Or maybe cn1 and zn1 have been swapped?
                if (dn1.hasInput(zn1.out)) {
                    swaps.add(zn1.out.id);
                    swaps.add(cn1.out.id);
                }
            }
        }

        return swaps.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Represents a wire.
     */
    private static class Wire {

        // Private Members

        private final String id;
        private Boolean value;

        // Constructors

        Wire(final String id) {
            this.id = id;
        }

        // Helper Methods

        /*
         * Check to see if this wire has a value.
         */
        boolean hasKnownValue() {
            return value != null;
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final Wire wire = (Wire) o;
            return Objects.equals(id, wire.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        // To String

        @Override
        public String toString() {
            return "Wire{"
                    + "id='" + id + '\''
                    + ", value=" + value
                    + '}';
        }

        // Static Helper Methods

        /*
         * Parse a `Wire` with an initial value.
         */
        static Wire parse(final String line) {
            final String[] parts = line.split(":\\s*");
            final Wire wire = new Wire(parts[0]);
            wire.value = parts[1].equals("1");
            return wire;
        }
    }

    /*
     * Represents an operation on two wires.
     */
    private enum Operation {

        /*
         * An AND gate.
         */
        AND {
            @Override
            boolean apply(final Wire w1, final Wire w2) {
                return w1.value && w2.value;
            }
        },

        /*
         * An OR gate.
         */
        OR {
            @Override
            boolean apply(final Wire w1, final Wire w2) {
                return w1.value || w2.value;
            }
        },

        /*
         * An XOR gate.
         */
        XOR {
            @Override
            boolean apply(final Wire w1, final Wire w2) {
                return w1.value ^ w2.value;
            }
        }

        // End of constants
        ;

        // Helper Methods

        /*
         * Apply this logic gate to the input `Wire`s.
         */
        abstract boolean apply(Wire w1, Wire w2);

    }

    /*
     * Represents the input to a logic gate.
     */
    private record Inputs(Wire in1, Operation operation, Wire in2) {

        // Helper Methods

        /*
         * Check if all of the `Wire`s in this `Input` have a value set.
         */
        boolean canResolve() {
            return in1.hasKnownValue() && in2.hasKnownValue();
        }

        /*
         * Apply the operation to the input `Wire`s.
         */
        public boolean apply() {
            assert in1.value != null;
            assert in2.value != null;
            return operation.apply(in1, in2);
        }

        /*
         * Check if the given `Wire` is one of the inputs.
         */
        public boolean hasInput(final Wire in) {
            return in1.equals(in) || in2.equals(in);
        }

        /*
         * Check if any of the given `Wire`s are one of the inputs.
         */
        public boolean hasAnyInput(final Wire... ins) {
            for (final Wire in : ins) {
                if (hasInput(in)) return true;
            }
            return false;
        }

        /*
         * Check if any of the given `Wire`s are one of the inputs, and that the
         * `Operation` matches the one given.
         */
        public boolean hasAnyInputAndOp(final Operation operation, final Wire... ins) {
            return this.operation == operation && hasAnyInput(ins);
        }

        // Static Helper Methods

        /*
         * Parse the `Inputs` from a line of "<wire> <op> <wire>".
         */
        private static Inputs parse(final String line, final Map<String, Wire> wires) {
            final String[] parts = line.split("\\s+");
            return Inputs.of(
                    wires.computeIfAbsent(parts[0], Wire::new),
                    wires.computeIfAbsent(parts[2], Wire::new),
                    switch (parts[1]) {
                        case "AND" -> Operation.AND;
                        case "OR" -> Operation.OR;
                        case "XOR" -> Operation.XOR;
                        default -> throw new IllegalArgumentException("Unsupported binary operation: " + parts[1]);
                    }
            );
        }

        /*
         * Create an `Inputs` from the given `Wire`s and `Operation`.
         */
        private static Inputs of(final Wire in1, final Wire in2, final Operation operation) {
            // Sort the wire inputs alphabetically, to make finding them easier
            final int cmp = String.CASE_INSENSITIVE_ORDER.compare(in1.id, in2.id);
            final Wire w1 = cmp < 0 ? in1 : in2;
            final Wire w2 = cmp < 0 ? in2 : in1;
            return new Inputs(w1, operation, w2);
        }
    }

    /*
     * Represents a logic gate.
     */
    private record Gate(Inputs inputs, Wire out) {

        // Helper Methods

        /*
         * Check if all the `Wire`s that feed into this `Gate` have a value set.
         */
        boolean canResolve() {
            return inputs.canResolve();
        }

        /*
         * Apply the operation to the input `Wire`s and assign the result to the
         * output `Wire`.
         */
        public void apply() {
            out.value = inputs.apply();
        }

        /*
         * Check if the given `Wire` is is an input to this `Gate`.
         */
        public boolean hasInput(final Wire in) {
            return inputs.hasInput(in);
        }

        /*
         * Check if any of the given `Wire`s are an input to this `Gate`.
         */
        public boolean hasAnyInput(final Wire... ins) {
            return inputs.hasAnyInput(ins);
        }

        /*
         * Check if any of the given `Wire`s are an input to this `Gate`, and
         * that the `Operation` matches the one given.
         */
        public boolean hasAnyInputAndOp(final Operation operation, final Wire... ins) {
            return inputs.hasAnyInputAndOp(operation, ins);
        }

        // Static Helper Methods

        /*
         * Parse a `Gate` from a line of "<wire> <op> <wire> -> <wire>".
         */
        static Gate parse(final String line, final Map<String, Wire> wires) {
            final String[] parts = line.split("\\s*->\\s*");
            final Inputs in = Inputs.parse(parts[0], wires);
            final String out = parts[1];

            return new Gate(in, wires.computeIfAbsent(out, Wire::new));
        }

    }

}

