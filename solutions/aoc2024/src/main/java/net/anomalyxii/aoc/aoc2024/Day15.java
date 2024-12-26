package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Direction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 15.
 */
@Solution(year = 2024, day = 15, title = "Warehouse Woes")
public class Day15 {

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
    public int calculateAnswerForPart1(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final Map<Coordinate, Character> coordinates = findObjects(batches.getFirst());
        final String instructions = String.join("", batches.getLast());

        traceRobotPath(instructions, coordinates);

        return calculateGpsCoordinateSum(coordinates);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final Map<Coordinate, Character> smolCoordinates = findObjects(batches.getFirst());
        final String instructions = String.join("", batches.getLast());

        final Map<Coordinate, Character> coordinates = smolCoordinates.entrySet().stream()
                .flatMap(e -> {
                    final Coordinate c = e.getKey();
                    final Character object = e.getValue();
                    assert object != null;

                    if (object == '@')
                        return Stream.of(Map.entry(new Coordinate(c.x() * 2, c.y()), '@'));

                    if (object == '#')
                        return Stream.of(
                                Map.entry(new Coordinate(c.x() * 2, c.y()), '#'),
                                Map.entry(new Coordinate(c.x() * 2 + 1, c.y()), '#')
                        );

                    if (object == 'O')
                        return Stream.of(
                                Map.entry(new Coordinate(c.x() * 2, c.y()), '['),
                                Map.entry(new Coordinate(c.x() * 2 + 1, c.y()), ']')
                        );

                    throw new IllegalStateException("Invalid object to expand: " + object);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        traceRobotPath(instructions, coordinates);

        return calculateGpsCoordinateSum(coordinates);
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final List<List<String>> batches = context.readBatches();

        final Map<Coordinate, Character> smolCoordinates = findObjects(batches.getFirst());
        final String instructions = String.join("", batches.getLast());

        final Map<Coordinate, Character> coordinates = smolCoordinates.entrySet().stream()
                .flatMap(e -> {
                    final Coordinate c = e.getKey();
                    final Character object = e.getValue();
                    assert object != null;

                    if (object == '@')
                        return Stream.of(Map.entry(new Coordinate(c.x() * 2, c.y()), '@'));

                    if (object == '#')
                        return Stream.of(
                                Map.entry(new Coordinate(c.x() * 2, c.y()), '#'),
                                Map.entry(new Coordinate(c.x() * 2 + 1, c.y()), '#')
                        );

                    if (object == 'O')
                        return Stream.of(
                                Map.entry(new Coordinate(c.x() * 2, c.y()), '['),
                                Map.entry(new Coordinate(c.x() * 2 + 1, c.y()), ']')
                        );

                    throw new IllegalStateException("Invalid object to expand: " + object);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        traceRobotPath(instructions, smolCoordinates);
        traceRobotPath(instructions, coordinates);

        return new IntTuple(
                calculateGpsCoordinateSum(smolCoordinates),
                calculateGpsCoordinateSum(coordinates)
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Find all the `Character`s.
     */
    private static Map<Coordinate, Character> findObjects(final List<String> batches) {
        final Map<Coordinate, Character> objects = new HashMap<>();
        for (int y = 0; y < batches.size(); y++) {
            final String line = batches.get(y);
            for (int x = 0; x < line.length(); x++) {
                final char sym = line.charAt(x);
                if (sym == '.') continue;

                final Coordinate coordinate = new Coordinate(x, y);
                objects.put(coordinate, sym);
            }
        }
        return objects;
    }

    /*
     * Find the starting position of the `Robot` in the warehouse.
     */
    private static Coordinate findRobot(final Map<Coordinate, Character> coordinates) {
        for (final Map.Entry<Coordinate, Character> entry : coordinates.entrySet())
            if (entry.getValue() == '@')
                return entry.getKey();
        throw new IllegalStateException("Did not find Robot starting position");
    }

    /*
     * Trace the path of a `Robot` through the warehouse.
     */
    @SuppressWarnings("BoxedPrimitiveEquality")
    private static void traceRobotPath(
            final String instructions,
            final Map<Coordinate, Character> coordinates
    ) {
        Coordinate robot = findRobot(coordinates);
        out:
        for (int i = 0; i < instructions.length(); i++) {
            assert coordinates.get(robot) == '@';

            final char instruction = instructions.charAt(i);
            final Direction d = Direction.fromChar(instruction);
            assert d != null;

            final Coordinate nextRobot = robot.adjustBy(d);
            final SequencedSet<Coordinate> nexts = new LinkedHashSet<>(Set.of(nextRobot));
            final SequencedSet<Coordinate> objects = new LinkedHashSet<>();
            while (!nexts.isEmpty()) {
                final Coordinate next = nexts.removeFirst();
                final Character c = coordinates.get(next);
                if (c == null) continue;
                if (c == '#') continue out; // Can't move, give up...

                if (c == 'O') {
                    objects.add(next);
                    nexts.add(next.adjustBy(d));
                    continue;
                }

                if (c == '[') {
                    final Coordinate nextRight = next.adjustBy(Direction.RIGHT);
                    final boolean addedLeft = objects.add(next);
                    final boolean addedRight = objects.add(nextRight);
                    assert addedLeft == addedRight;
                    if (addedLeft) {
                        nexts.add(next.adjustBy(d));
                        nexts.add(nextRight.adjustBy(d));
                    }
                    continue;
                }

                if (c == ']') {
                    final Coordinate nextLeft = next.adjustBy(Direction.LEFT);
                    final boolean addedRight = objects.add(next);
                    final boolean addedLeft = objects.add(nextLeft);
                    assert addedLeft == addedRight;
                    if (addedLeft) {
                        nexts.add(nextLeft.adjustBy(d));
                        nexts.add(next.adjustBy(d));
                    }
                    continue;
                }

                throw new IllegalStateException("Should not get here!");
            }

            objects.reversed().forEach(c -> {
                final Character object = coordinates.remove(c);
                assert object == 'O' || object == '[' || object == ']';

                final Character replaced = coordinates.put(c.adjustBy(d), object);
                assert replaced == null;
            });

            coordinates.remove(robot);
            coordinates.put(nextRobot, '@');
            robot = nextRobot;
        }
    }

    /*
     * Calculate the sum of all Goods Positioning System coordinates.
     */
    private static int calculateGpsCoordinateSum(final Map<Coordinate, Character> coordinates) {
        return coordinates.entrySet().stream()
                .filter(e -> e.getValue() == 'O' || e.getValue() == '[')
                .map(Map.Entry::getKey)
                .mapToInt(c -> 100 * c.y() + c.x())
                .sum();
    }

}

