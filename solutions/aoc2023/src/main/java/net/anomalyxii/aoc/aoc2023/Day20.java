package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.maths.Factors;

import java.util.*;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 20.
 */
@Solution(year = 2023, day = 20, title = "Pulse Propagation")
public class Day20 {

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
        final Wiring wiring = Wiring.fromInput(context);

        for (int i = 0; i < 1000; i++)
            wiring.pushTheButton();

        final long highPulses = wiring.highPulses.get();
        final long lowPulses = wiring.lowPulses.get();
        return highPulses * lowPulses;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Wiring wiring = Wiring.fromInput(context);

        while (!wiring.isStrongAndStable())
            wiring.pushTheButton();

        return wiring.calculateFirstCompleteCycle();
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
        final Wiring wiring = Wiring.fromInput(context);

        LongTuple answer = LongTuple.NULL;
        for (int i = 0; i < 1000 || !wiring.isStrongAndStable(); i++) {
            if (i == 1000)
                answer = answer.add(wiring.highPulses.get() * wiring.lowPulses.get(), 0);

            wiring.pushTheButton();
        }

        return answer.add(0, wiring.calculateFirstCompleteCycle());
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Whether a pulse is a high pulse or a low pulse.
     */
    private enum PulseType {

        /*
         * A high pulse.
         */
        HIGH,

        /*
         * A low pulse.
         */
        LOW,

        // End of constants
        ;

        // Helper Methods

        /*
         * Invert this `PulseType`.
         */
        PulseType invert() {
            return switch (this) {
                case HIGH -> LOW;
                case LOW -> HIGH;
            };
        }

    }

    /*
     * Represents a pulse sent through the machinery.
     */
    private record Pulse(String from, String to, PulseType type) {

        // Helper Methods

        /*
         * Increase the total pulse count for this type of `Pulse`.
         */
        void accumulate(final LongConsumer onHigh, final LongConsumer onLow) {
            switch (type) {
                case HIGH -> onHigh.accept(1);
                case LOW -> onLow.accept(1);
                default -> throw new IllegalArgumentException("Invalid PulseType: " + type);
            }
        }

    }

    /*
     * Represents the wiring of modules within the complicated Elf machinery.
     */
    private static class Wiring {

        /*
         * Identify the broadcaster node.
         */
        private static final String BROADCASTER_LABEL = "broadcaster";

        // Private Members#

        private final Map<String, Module> modules;
        private final List<FlipFlopModule> flipFlops;
        private final LongAccumulator highPulses = new LongAccumulator(Long::sum, 0L);
        private final LongAccumulator lowPulses = new LongAccumulator(Long::sum, 0L);

        // Constructors

        Wiring(final Map<String, Module> modules, final List<FlipFlopModule> flipFlops) {
            this.modules = modules;
            this.flipFlops = flipFlops;
        }

        // Helper Methods

        /*
         * Initialise the wiring by sending a low pulse to all modules.
         */
        void init() {
            final Set<String> keys = new HashSet<>(modules.keySet());
            keys.stream()
                    .map(this::resolve)
                    .forEach(module -> module.downstream()
                            .forEach(downstream -> resolve(downstream).init(module.name())));
        }

        /*
         * Push the button.
         */
        void pushTheButton() {

            flipFlops.forEach(FlipFlopModule::prePress);

            final Deque<Pulse> queue = new ArrayDeque<>();
            queue.addFirst(new Pulse(null, BROADCASTER_LABEL, PulseType.LOW));
            while (!queue.isEmpty()) {
                final Pulse pulse = queue.pollFirst();
                final Module to = resolve(pulse.to);

                pulse.accumulate(highPulses::accumulate, lowPulses::accumulate);
                queue.addAll(to.accept(pulse));
            }

            flipFlops.forEach(FlipFlopModule::postPress);

        }

        /*
         * Check if this wiring is "strong and stable" by deciding whether every
         * `FlipFlopModule` has been able to complete a "stable" loop.
         *
         * We detect a "stable" loop by tracking the number of button presses it
         * takes to flip one of these modules and mark the "stable" loop as
         * complete at the end of the first "irregular" period.
         */
        boolean isStrongAndStable() {
            return flipFlops.stream().allMatch(flipflop -> flipflop.loopLength > 0);
        }

        /*
         * Calculate the number of button presses it would take to complete a
         * full cycle of the wiring grid. A complete cycle would require all
         * `FlipFlopModule`s to be at the end of their "stable" loop at the
         * same time.
         */
        long calculateFirstCompleteCycle() {
            final Map<Integer, Long> loops = flipFlops.stream()
                    .collect(Collectors.groupingBy(mod -> mod.loopLength, Collectors.counting()));

            return loops.entrySet().stream()
                    .filter(entry -> entry.getValue() > 1)
                    .mapToLong(Map.Entry::getKey)
                    .reduce(Factors::lowestCommonMultiple)
                    .orElseThrow();
        }

        /*
         * Resolve a `Module` based on the name.
         */
        private Module resolve(final String from) {
            return modules.computeIfAbsent(from, Sink::new);
        }

        // Static Helper Methods

        /*
         * Create a new `Wiring` from the given input.
         */
        static Wiring fromInput(final SolutionContext context) {
            final Map<String, Module> modules = Module.fromInput(context);
            final List<FlipFlopModule> flipFlops = modules.values().stream()
                    .filter(module -> module instanceof FlipFlopModule)
                    .map(FlipFlopModule.class::cast)
                    .toList();

            final Wiring wiring = new Wiring(modules, flipFlops);
            wiring.init();
            return wiring;
        }

    }

    /*
     * A module that receives and sends pulses.
     */
    private sealed interface Module
            permits AbstractModule, FlipFlopModule, ConjunctionModule,
            BroadcasterModule, Sink {

        // Interface Methods

        /*
         * Get the name of this `Module`.
         */
        String name();

        /*
         * Get all the downstream `Module` names.
         */
        List<String> downstream();

        /*
         * Tell a module about an upstream connection.
         */
        void init(String upstream);

        /*
         * Accept a `Pulse`.
         */
        List<Pulse> accept(Pulse pulse);

        // Static Helper Methods

        /*
         * Parse some `Module`s from the input text.
         */
        static Map<String, Module> fromInput(final SolutionContext context) {
            return context.stream()
                    .map(Module::parse)
                    .collect(Collectors.toMap(
                            Module::name,
                            Function.identity()
                    ));
        }

        /*
         * Parse a `Module` from a given line.
         */
        private static Module parse(final String line) {
            final String[] parts = line.split("\\s*->\\s*");
            final String identifier = parts[0];
            final List<String> downstream = Arrays.asList(parts[1].split(",\\s*"));

            if ("broadcaster".equals(identifier)) {
                return new BroadcasterModule(identifier, downstream);
            }

            if (identifier.startsWith("%")) {
                final String name = identifier.substring(1);
                return new FlipFlopModule(name, downstream);
            }

            if (identifier.startsWith("&")) {
                final String name = identifier.substring(1);
                return new ConjunctionModule(name, downstream);
            }

            throw new IllegalStateException("Could not process module definition: [" + line + "]");
        }

    }

    /*
     * A base class for various other `Module`s.
     */
    private abstract static sealed class AbstractModule
            implements Module
            permits FlipFlopModule, ConjunctionModule, BroadcasterModule {

        // Private Members

        protected final String name;
        protected final List<String> downstream;

        // Constructors

        AbstractModule(final String name, final List<String> downstream) {
            this.name = name;
            this.downstream = downstream;
        }

        // Module Methods

        @Override
        public String name() {
            return name;
        }

        @Override
        public List<String> downstream() {
            return Collections.unmodifiableList(downstream);
        }

        @Override
        public void init(final String upstream) {

        }

        // Helper Methods

        /*
         * Send a pulse to all downstream `Module`s.
         */
        List<Pulse> sendPulse(final PulseType type) {
            return downstream.stream()
                    .map(next -> new Pulse(name, next, type))
                    .toList();
        }

    }

    /*
     * A `Module` that flips state on receiving a low pulse.
     */
    private static final class FlipFlopModule extends AbstractModule implements Module {

        // Private Members

        private PulseType state = PulseType.LOW;

        // Stuff for part 2...
        private PulseType cachedState;
        private int loopLength = -1;
        private int cumulativeSegmentLength = 0;
        private int segmentLength = 0;


        // Constructors

        FlipFlopModule(final String name, final List<String> downstream) {
            super(name, downstream);
        }

        // Module Methods

        @Override
        public List<Pulse> accept(final Pulse pulse) {
            if (pulse.type == PulseType.HIGH) return Collections.emptyList();
            state = state.invert();
            return sendPulse(state);
        }

        // Helper Methods

        /*
         * Cache the current state of this `FlipFlopModule` prior to pressing the
         * button.
         */
        public void prePress() {
            cachedState = state;
            ++segmentLength;
        }

        /*
         * Update the loop tracking for this `FlipFlopModule` after all pulses
         * originating from a single button-press have been processed.
         */
        public void postPress() {
            if (state == cachedState || loopLength != -1)
                return;

            cumulativeSegmentLength += segmentLength;

            if (!isPowerOf2(segmentLength))
                this.loopLength = cumulativeSegmentLength;

            segmentLength = 0;
        }

        // Static Helper Methods

        /*
         * Check if a given number is a power of 2.
         */
        private static boolean isPowerOf2(final int loopLength) {
            int cmp = 1;
            while (cmp <= loopLength) {
                if ((cmp <<= 1) == loopLength) return true;
            }
            return false;
        }

    }

    /*
     * A `Module` that acts as NAND gate for multiple inputs.
     */
    private static final class ConjunctionModule extends AbstractModule implements Module {

        // Private Members

        private final Map<String, PulseType> upstreams = new HashMap<>();

        // Constructors

        ConjunctionModule(final String name, final List<String> downstream) {
            super(name, downstream);
        }

        // Module Methods

        @Override
        public void init(final String upstream) {
            upstreams.put(upstream, PulseType.LOW);
        }

        @Override
        public List<Pulse> accept(final Pulse pulse) {
            upstreams.put(pulse.from, pulse.type);

            final PulseType type = upstreams.values().stream().allMatch(val -> val == PulseType.HIGH)
                    ? PulseType.LOW
                    : PulseType.HIGH;
            return sendPulse(type);
        }

    }

    /*
     * A `Module` that forwards on `Pulse`s.
     */
    private static final class BroadcasterModule extends AbstractModule implements Module {

        // Constructors

        BroadcasterModule(final String name, final List<String> downstream) {
            super(name, downstream);
        }

        // Module Methods

        @Override
        public List<Pulse> accept(final Pulse pulse) {
            return sendPulse(pulse.type);
        }

    }

    /*
     * A `Module` that just receives `Pulse`s but does nothing with them.
     */
    private record Sink(String name) implements Module {

        // Module Methods

        @Override
        public List<String> downstream() {
            return Collections.emptyList();
        }

        @Override
        public void init(final String upstream) {

        }

        @Override
        public List<Pulse> accept(final Pulse pulse) {
            return Collections.emptyList();
        }

    }


}

