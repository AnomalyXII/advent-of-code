package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 3.
 */
@Solution(year = 2023, day = 3, title = "Gear Ratios")
public class Day3 {

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
        final Schematic schematic = Schematic.parse(context.stream());
        return schematic.symbols.keySet().stream()
                .flatMap(Coordinate::neighbours)
                .map(schematic.parts::get)
                .filter(Objects::nonNull)
                .distinct()
                .mapToInt(part -> part.id)
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
        final Schematic schematic = Schematic.parse(context.stream());
        return schematic.symbols.entrySet().stream()
                .filter(symbol -> symbol.getValue() == '*')
                .map(Map.Entry::getKey)
                .map(coord -> coord.neighbours()
                        .map(schematic.parts::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .filter(neighbours -> neighbours.size() == 2)
                .mapToInt(neighbours -> neighbours.getFirst().id * neighbours.getLast().id)
                .sum();
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
        final Schematic schematic = Schematic.parse(context.stream());
        final Set<MachinePart> uniqueParts = new HashSet<>();
        return schematic.symbols.entrySet().stream()
                .reduce(
                        LongTuple.NULL,
                        (tup, entry) -> {
                            final Coordinate coord = entry.getKey();
                            final SequencedSet<MachinePart> parts = coord.neighbours()
                                    .map(schematic.parts::get)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toCollection(LinkedHashSet::new));

                            final int part1 = parts.stream()
                                    .filter(uniqueParts::add)
                                    .mapToInt(part -> part.id)
                                    .sum();

                            final int part2 = parts.size() == 2
                                    ? parts.getFirst().id * parts.getLast().id
                                    : 0;

                            return tup.add(part1, part2);
                        },
                        LongTuple::add
                );
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Parse a schematic, identifying all `MachinePart`s and symbols.
     */
    private record Schematic(Map<Coordinate, Character> symbols, Map<Coordinate, MachinePart> parts) {

        // Static Helper Methods

        /*
         * Extract the `Schematic` plan from the challenge input.
         */
        static Schematic parse(final Stream<String> lines) {
            final Map<Coordinate, Character> symbols = new HashMap<>();
            final Map<Coordinate, MachinePart> parts = new HashMap<>();

            final Iterator<String> iteratorY = lines.iterator();
            for (int y = 0; iteratorY.hasNext(); y++)
                parseLine(y, iteratorY, symbols, parts);

            return new Schematic(symbols, parts);
        }

        /*
         * Parse a line in a `Schematic`.
         */
        private static void parseLine(
                final int y,
                final Iterator<String> iteratorY,
                final Map<Coordinate, Character> symbols,
                final Map<Coordinate, MachinePart> parts
        ) {
            int value = -1;
            final SequencedSet<Coordinate> coordinates = new LinkedHashSet<>();

            final String line = iteratorY.next();
            for (int x = 0; x < line.length(); x++) {
                final int chr = line.charAt(x);
                final Coordinate current = new Coordinate(x, y);

                if (!Character.isDigit(chr)) {
                    if (chr != '.')
                        symbols.put(current, (char) chr);

                    if (value > -1) {
                        final MachinePart part = new MachinePart(value, coordinates.getFirst(), coordinates.getLast());
                        coordinates.forEach(coord -> parts.put(coord, part));

                        value = -1;
                        coordinates.clear();
                    }
                    continue;
                }

                if (value < 0)
                    value = 0;

                final int val = chr - '0';
                value = (value * 10) + val;
                coordinates.add(current);
            }

            if (value > -1) {
                final MachinePart part = new MachinePart(value, coordinates.getFirst(), coordinates.getLast());
                coordinates.forEach(coord -> parts.put(coord, part));
            }
        }

    }

    /*
     * Store details of a machine part.
     */
    private record MachinePart(int id, Coordinate start, Coordinate end) {
    }

}

