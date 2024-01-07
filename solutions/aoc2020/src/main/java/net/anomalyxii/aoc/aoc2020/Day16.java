package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 16.
 */
@Solution(year = 2020, day = 16, title = "Ticket Translation")
public class Day16 {

    /*
     * Default field filter: any fields that start with 'departure'.
     */
    private static final Predicate<String> DEFAULT_FIELD_FILTER = line -> line.startsWith("departure");

    // ****************************************
    // Private Members
    // ****************************************

    private final Predicate<String> relevantFieldFilter;

    // ****************************************
    // Constructors
    // ****************************************

    public Day16() {
        this(DEFAULT_FIELD_FILTER);
    }

    Day16(final Predicate<String> relevantFieldFilter) {
        this.relevantFieldFilter = relevantFieldFilter;
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
        final Map<String, Set<Integer>> rules = new HashMap<>();

        final AtomicLong errorRate = new AtomicLong(0);
        forEachLineOfNotes(
                context,
                rule -> addRule(rules, rule),
                ticket -> {}, // Ignored, for now
                ticket -> errorRate.addAndGet(validateTicket(rules, ticket))
        );

        return errorRate.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Map<String, Set<Integer>> rules = new HashMap<>();

        final int[][] myTicket = new int[1][];
        final List<int[]> validTickets = new ArrayList<>();

        forEachLineOfNotes(
                context,
                rule -> addRule(rules, rule),
                ticket -> myTicket[0] = ticket,
                ticket -> {
                    if (validateTicket(rules, ticket) == 0) {
                        validTickets.add(ticket);
                    }
                }
        );

        final Map<String, Integer> fieldMappings = resolveFieldMappings(rules, validTickets, myTicket[0]);
        return fieldMappings.entrySet().stream()
                .filter(entry -> relevantFieldFilter.test(entry.getKey()))
                .mapToLong(entry -> myTicket[0][entry.getValue()])
                .reduce(1L, (result, val) -> result * val);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Process each line of notes, performing an action on each non-empty
     * line depending on which section is currently being parsed.
     */
    private void forEachLineOfNotes(
            final SolutionContext context,
            final Consumer<String> onRule,
            final Consumer<int[]> onMyTicket,
            final Consumer<int[]> onNearbyTicket
    ) {
        final Phase[] phase = {Phase.RULES};
        context.consume(line -> {
            if (line.isEmpty() || line.isBlank()) {
                return;
            }

            if ("your ticket:".equalsIgnoreCase(line)) {
                phase[0] = Phase.MY_TICKET;
                return;
            }

            if ("nearby tickets:".equalsIgnoreCase(line)) {
                phase[0] = Phase.NEARBY_TICKETS;
                return;
            }

            switch (phase[0]) {
                case RULES -> onRule.accept(line);
                case MY_TICKET -> onMyTicket.accept(parseTicket(line));
                case NEARBY_TICKETS -> onNearbyTicket.accept(parseTicket(line));
                default -> throw new AssertionError("Invalid phase: " + phase[0]);
            }
        });
    }

    /*
     * Add a rule to the rule catalogue.
     */
    private static void addRule(final Map<String, Set<Integer>> rules, final String line) {
        final String[] parts = line.split(": *", 2);
        if (rules.containsKey(parts[0])) {
            throw new IllegalStateException("Duplicate rule: '" + parts[0] + "'");
        }

        rules.put(parts[0], parseRule(parts[1]));
    }

    /*
     * Parse and expand a rule.
     */
    private static Set<Integer> parseRule(final String rule) {
        final String[] parts = rule.split(" or ");
        return Stream.of(parts)
                .map(part -> part.split("-", 2))
                //.map(lowHigh -> new int[]{lowHigh[0], Integer.parseInt(lowHigh[1]))})
                .map(lowHigh -> IntStream.rangeClosed(Integer.parseInt(lowHigh[0]), Integer.parseInt(lowHigh[1])))
                .flatMap(IntStream::boxed)
                .collect(Collectors.toSet());
    }

    /*
     * Parse a ticket.
     */
    private int[] parseTicket(final String line) {
        return Stream.of(line.split(", *"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    /*
     * Use a `List` of valid tickets to resolve each rule to exactly one
     * position within a ticket.
     */
    private static Map<String, Integer> resolveFieldMappings(
            final Map<String, Set<Integer>> rules,
            final List<int[]> validTickets,
            final int[] myTicket
    ) {
        final int ticketLength = myTicket.length;

        final Map<String, Set<Integer>> candidateFieldMappings = buildCandidateFieldMappings(rules, validTickets, ticketLength);
        final Map<String, Integer> resolvedFieldMappings = buildFinalisedFieldMappings(candidateFieldMappings);

        // Validate against my ticket!
        resolvedFieldMappings.forEach((key, field) -> {
            final Set<Integer> rule = rules.get(key);
            if (!rule.contains(myTicket[field])) {
                throw new IllegalStateException("My ticket is not valid according to the resolved rule mappings!");
            }
        });

        return resolvedFieldMappings;
    }

    /*
     * Construct a mapping of rules to field indexes for which the field on
     * every valid ticket holds true to that rule.
     */
    private static Map<String, Set<Integer>> buildCandidateFieldMappings(
            final Map<String, Set<Integer>> rules,
            final List<int[]> validTickets,
            final int ticketLength
    ) {
        final Map<String, Set<Integer>> candidateFieldMappings = new HashMap<>();
        rules.forEach((key, rule) -> candidateFieldMappings.put(key, computeDefaultFieldMappings(ticketLength)));
        validTickets.forEach(ticket -> {
            assert ticket.length == ticketLength;
            IntStream.range(0, ticketLength)
                    .forEach(field -> filterCandidateFieldPositions(rules, candidateFieldMappings, ticket, field));
        });
        return candidateFieldMappings;
    }

    /*
     * Resolve each rule down to a single field mapping.
     */
    private static Map<String, Integer> buildFinalisedFieldMappings(final Map<String, Set<Integer>> candidateFieldMappings) {
        final Map<String, Integer> resolvedFieldMappings = new HashMap<>();
        while (!candidateFieldMappings.isEmpty()) {
            final boolean[] changed = {false};
            // Pass through trying to find fields that only have one candidate!
            candidateFieldMappings.entrySet().stream()
                    .peek(entry -> {
                        if (entry.getValue().isEmpty()) {
                            throw new IllegalStateException("No possible fields were found for rule '" + entry.getKey() + "'");
                        }
                    })
                    .filter(entry -> entry.getValue().size() == 1)
                    .forEach(entry -> {
                        changed[0] = true;
                        resolvedFieldMappings.put(entry.getKey(), entry.getValue().iterator().next());
                    });

            resolvedFieldMappings.forEach((resolvedKey, resolvedField) -> {
                candidateFieldMappings.remove(resolvedKey);
                candidateFieldMappings.forEach((key, fields) -> fields.remove(resolvedField));
            });

            if (!changed[0]) {
                throw new IllegalStateException("Unable to resolve all rules: " + candidateFieldMappings.size() + " rules remaining");
            }
        }
        return resolvedFieldMappings;
    }

    /*
     * Filter the candidate field positions for every rule based on whether
     * the field in the supplied ticket is valid.
     */
    private static void filterCandidateFieldPositions(
            final Map<String, Set<Integer>> rules,
            final Map<String, Set<Integer>> candidateFieldMappings,
            final int[] ticket,
            final int field
    ) {
        rules.forEach((key, rule) -> {
            if (!rule.contains(ticket[field])) {
                candidateFieldMappings.get(key).remove(field);
            }
        });
    }

    /*
     * Create a `Set` of all field positions for a given ticket size.
     */
    private static Set<Integer> computeDefaultFieldMappings(final int size) {
        return IntStream.range(0, size)
                .boxed()
                .collect(Collectors.toSet());
    }

    /*
     * Validate whether a given ticket is valid for the provided rules.
     */
    private static long validateTicket(final Map<String, Set<Integer>> rules, final int[] ticketFields) {
        return validateTicket(rules, Arrays.stream(ticketFields));
    }

    /*
     * Validate whether a given ticket is valid for the provided rules.
     */
    private static long validateTicket(final Map<String, Set<Integer>> rules, final IntStream ticketFields) {
        return ticketFields
                .filter(field -> rules.values().stream().noneMatch(rule -> rule.contains(field)))
                .sum();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Phases of the input.
     */
    private enum Phase {

        RULES,
        MY_TICKET,
        NEARBY_TICKETS,

        // End of constants
        ;

    }

}
