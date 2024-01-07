package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 25.
 */
@Solution(year = 2023, day = 25, title = "Snowverload")
public class Day25 {

    /*
     * A random number generator.
     */
    private static final Random RANDOM = new SecureRandom();

    /*
     * Expected number of `Wire`s remaining after reduction.
     */
    private static final int EXPECTED_WIRE_COUNT = 3;

    /*
     * Maximum attempts before giving up and declaring defeat.
     */
    private static final int MAX_ATTEMPTS = 10;

    /*
     * Scaling co-efficient for Karger-Stein "fast" reduction.
     */
    private static final double SCALING_COEFFICIENT = 2;

    /*
     * Threshold for switching to the "slow" reduction algorithm.
     */
    private static final double SLOW_REDUCTION_THRESHOLD = 6;

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * Solution to part 1.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 1 solution
     * @throws IllegalStateException if no solution is found
     */
    @Part(part = I)
    public int calculateAnswerForPart1(final SolutionContext context) {
        final Wires wires = Wires.parse(context);

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            final Wires reduced = wires.reduce();
            final SequencedMap<Wire, Wire> toCut = new LinkedHashMap<>(reduced.wires);
            if (toCut.size() == EXPECTED_WIRE_COUNT) {
                final Wires disconnect = wires.disconnect(toCut.keySet());
                final Wire first = toCut.firstEntry().getKey();
                return disconnect.countComponentsReachableFrom(first.head) * disconnect.countComponentsReachableFrom(first.tail);
            }
        }

        throw new IllegalStateException("Failed to find a solution in " + MAX_ATTEMPTS + " attempts!");
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return -
     */
    @Part(part = II)
    public NoChallenge calculateAnswerForPart2(final SolutionContext context) {
        return NoChallenge.NO_CHALLENGE;
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * A connection between two components.
     */
    private record Wire(String head, String tail) {

        // Helper Methods

        /*
         * Check if this `Wire` is joined to the specified component.
         */
        public boolean isJoinedTo(final String component) {
            return head.equals(component) || tail.equals(component);
        }

        /*
         * Create a new `Wire` that replaced the connection to a specified
         * component with a connection to a different component.
         */
        public Wire replace(final String component, final String replacement) {
            if (head.equals(component)) return new Wire(tail, replacement);
            if (tail.equals(component)) return new Wire(head, replacement);
            throw new IllegalStateException("Failed to replace " + component + " in " + this);
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Wire wire = (Wire) o;
            return Objects.equals(head, wire.head)
                    && Objects.equals(tail, wire.tail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(head, tail);
        }

        // To String

        @Override
        public String toString() {
            return "%s - %s".formatted(head, tail);
        }
    }

    /*
     * A network of `Wire`s.
     */
    private record Wires(Map<String, Set<Wire>> components, Map<Wire, Wire> wires, int continueFrom) {

        // Helper Methods

        /*
         * Reduce these `Wires` down to the most valuable connections.
         */
        Wires reduce() {
            final int n = components.size();
            final int limit = (int) (n * (Math.log(n) / (n - 1)));

            Wires best = this;
            for (int i = 0; i < limit; i++) {
                final Wires next = reduceFast();
                if (next.wires.size() == EXPECTED_WIRE_COUNT) return next;
                else if (next.wires.size() < best.wires.size())
                    best = next;
            }
            return best;
        }

        /*
         * Return a new bundle of `Wires` after disconnecting certain
         * connections.
         */
        Wires disconnect(final Set<Wire> toDisconnect) {
            final Map<String, Set<Wire>> newConnections = components.entrySet().stream()
                    .map(entry -> {
                        if (toDisconnect.stream().anyMatch(wire -> wire.isJoinedTo(entry.getKey()))) {
                            final Set<Wire> filtered = new HashSet<>(entry.getValue());
                            filtered.removeAll(toDisconnect);
                            return Map.entry(entry.getKey(), filtered);
                        }

                        return entry;
                    })
                    .filter(entry -> !entry.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            final Map<Wire, Wire> newLinks = wires.entrySet().stream()
                    .filter(entry -> !toDisconnect.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new Wires(newConnections, newLinks, 0);
        }

        /*
         * Count the number of components that can be reached from the specified
         * starting component.
         */
        int countComponentsReachableFrom(final String start) {
            final Set<String> visited = new HashSet<>();

            int count = 0;
            final Deque<String> next = new ArrayDeque<>();
            next.add(start);
            while (!next.isEmpty()) {
                final String current = next.pollFirst();
                if (!visited.add(current)) continue;

                ++count;
                this.components.get(current).forEach(wire -> {
                    next.add(wire.head);
                    next.add(wire.tail);
                });
            }

            return count;
        }

        // Private Helper Methods

        /*
         * Perform a "fast" reduction.
         */
        private Wires reduceFast() {
            if (components.size() < SLOW_REDUCTION_THRESHOLD)
                return reduceSlow();

            final int limit = (int) (1 + components.size() / SCALING_COEFFICIENT);
            final Wires w1 = contract(components, wires, continueFrom, limit).reduceFast();
            if (w1.wires.size() == EXPECTED_WIRE_COUNT) return w1;
            final Wires w2 = contract(components, wires, continueFrom, limit).reduceFast();
            if (w2.wires.size() == EXPECTED_WIRE_COUNT) return w2;

            return w1.wires.size() < w2.wires.size() ? w1 : w2;
        }

        /*
         * Perform a "slow" reduction.
         */
        private Wires reduceSlow() {
            Wires best = this;
            final int n = components.size();
            final int limit = (int) (n * (n - 1) * Math.log(n) / 2);
            for (int i = 0; i < limit; i++) {
                final Wires next = contract(components, wires, continueFrom, 2);
                if (next.wires.size() == EXPECTED_WIRE_COUNT) return next;
                else if (next.wires.size() < best.wires.size())
                    best = next;
            }
            return best;
        }


        // Static Methods

        /*
         * Parse a bundle of `Wires`.
         */
        public static Wires parse(final SolutionContext context) {
            final Map<String, Set<Wire>> components = new TreeMap<>();
            final Map<Wire, Wire> wires = new HashMap<>();

            context.stream().forEach(line -> {
                final String[] parts = line.split(":\\s*");

                final String component = parts[0];
                final Set<Wire> outgoing = components.computeIfAbsent(component, k -> new HashSet<>());
                stream(parts[1].split("\\s+")).forEach(otherComponent -> {
                    final Wire wire = new Wire(component, otherComponent);
                    outgoing.add(wire);

                    final Set<Wire> incoming = components.computeIfAbsent(otherComponent, k -> new HashSet<>());
                    wires.put(wire, wire);
                    incoming.add(wire);
                });
            });

            return new Wires(components, wires, 0);
        }

        /*
         * Contract the wires until only two components remain.
         */
        private static Wires contract(
                final Map<String, Set<Wire>> components,
                final Map<Wire, Wire> wires,
                final int continueFrom,
                final int limit
        ) {
            final Map<String, Set<Wire>> nextComponents = new TreeMap<>();
            components.forEach((k, v) -> nextComponents.put(k, new HashSet<>(v)));
            final Map<Wire, Wire> nextWires = new HashMap<>(wires);

            int i = continueFrom;
            for (; nextComponents.size() > limit; i++) {
                final List<Wire> toPick = new ArrayList<>(nextWires.keySet());
                final Wire randomWire = toPick.get(RANDOM.nextInt(toPick.size()));
                mergeVertices(nextComponents, nextWires, randomWire, i);
            }

            return new Wires(nextComponents, nextWires, i);
        }

        /*
         * Merge the components on either side of a `Wire`.
         */
        private static void mergeVertices(
                final Map<String, Set<Wire>> components,
                final Map<Wire, Wire> wires,
                final Wire wireToMerge,
                final int iteration
        ) {
            final Wire wire = wires.remove(wireToMerge);
            final Set<Wire> head = components.remove(wire.head);
            final Set<Wire> tail = components.remove(wire.tail);

            final String name = "merged_" + iteration;
            final Set<Wire> merged = new HashSet<>();
            merged.addAll(head);
            merged.addAll(tail);
            merged.removeIf(next -> next.equals(wireToMerge));
            components.put(name, merged);

            final Iterator<Wire> iter = merged.iterator();
            while (iter.hasNext()) {
                final Wire edge = iter.next();
                final Wire next = wires.get(edge);
                if (next.isJoinedTo(wire.head) && next.isJoinedTo(wire.tail)) {
                    wires.remove(edge);
                    iter.remove();
                    continue;
                }

                if (next.isJoinedTo(wire.head)) {
                    wires.put(edge, next.replace(wire.head, name));
                    continue;
                }

                if (next.isJoinedTo(wire.tail)) {
                    wires.put(edge, next.replace(wire.tail, name));
                    continue;
                }

                throw new IllegalStateException("Found a loose wire - this could cause a fire!");
            }
        }
    }
}

