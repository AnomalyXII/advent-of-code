package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.UnaryOperator;

import static java.lang.Math.abs;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 19.
 */
@Solution(year = 2021, day = 19, title = "Beacon Scanner")
public class Day19 {

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
        return resolveScanners(context).stream()
                .map(Scanner::beacons)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Set<Scanner> scanners = resolveScanners(context);
        final LongAccumulator result = new LongAccumulator(Math::max, Long.MIN_VALUE);
        for (final Scanner scanner : scanners) {
            for (final Scanner other : scanners) {
                final int dx = abs(scanner.position.x - other.position.x);
                final int dy = abs(scanner.position.y - other.position.y);
                final int dz = abs(scanner.position.z - other.position.z);
                result.accumulate(dx + dy + dz);
            }
        }
        return result.longValue();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Read in the relative `Scanner`s and attempt to normalise them all.
     */
    private Set<Scanner> resolveScanners(final SolutionContext context) {
        final List<Scanner> scanners = context.streamBatches()
                .map(Scanner::parse)
                .toList();

        final Scanner origin = scanners.getFirst();
        final Deque<Scanner> otherScanners = new ArrayDeque<>(scanners.subList(1, scanners.size()));

        final Set<Scanner> absoluteScanners = new HashSet<>();
        absoluteScanners.add(origin);
        while (!otherScanners.isEmpty()) {
            final Scanner scanner = otherScanners.removeFirst();
            resolve(scanner, absoluteScanners)
                    .ifPresentOrElse(
                            absoluteScanners::add,
                            () -> otherScanners.addLast(scanner)
                    );
        }
        return absoluteScanners;
    }

    /*
     * Attempt to resolve the given `Scanner` to an absolute position, based
     * on the overlap with another, already resolved, `Scanner`.
     */
    static Optional<Scanner> resolve(final Scanner scanner, final Set<Scanner> others) {
        for (final Scanner other : others) {
            final Optional<Scanner> resolved = resolve(scanner, other);
            if (resolved.isPresent())
                return resolved;
        }

        return Optional.empty();
    }

    /*
     * Attempt to resolve the given `Scanner` to an absolute position, based
     * on the overlap with a given, already resolved, `Scanner`.
     */
    static Optional<Scanner> resolve(final Scanner scanner, final Scanner other) {
        for (final Orientation o : Orientation.values()) {
            final Map<Position, AtomicInteger> counts = new HashMap<>();
            for (final Position reference : scanner.beacons) {
                for (final Position beacon : other.beacons) {
                    final Position rotatedReference = o.apply(reference);
                    final Position relativePosition = beacon.sub(rotatedReference);
                    counts.computeIfAbsent(relativePosition, k -> new AtomicInteger())
                            .incrementAndGet();
                }
            }

            final Optional<Scanner> result = counts.entrySet().stream()
                    .filter(matches -> matches.getValue().intValue() >= 12)
                    .findFirst()
                    .map(matches -> scanner.resolve(o, matches.getKey()));

            if (result.isPresent())
                return result;
        }
        return Optional.empty();
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Various orientations that a `Scanner` can be in.
     */
    enum Orientation {

        O00(p -> p),
        O01(p -> new Position(-p.y, p.x, p.z)),
        O02(p -> new Position(-p.x, -p.y, p.z)),
        O03(p -> new Position(p.y, -p.x, p.z)),
        O04(p -> new Position(-p.z, p.y, p.x)),
        O05(p -> new Position(-p.y, -p.z, p.x)),
        O06(p -> new Position(p.z, -p.y, p.x)),
        O07(p -> new Position(p.y, p.z, p.x)),
        O08(p -> new Position(-p.x, p.y, -p.z)),
        O09(p -> new Position(-p.y, -p.x, -p.z)),
        O10(p -> new Position(p.x, -p.y, -p.z)),
        O11(p -> new Position(p.y, p.x, -p.z)),
        O12(p -> new Position(p.z, p.y, -p.x)),
        O13(p -> new Position(-p.y, p.z, -p.x)),
        O14(p -> new Position(-p.z, -p.y, -p.x)),
        O15(p -> new Position(p.y, -p.z, -p.x)),
        O16(p -> new Position(p.x, -p.z, p.y)),
        O17(p -> new Position(p.z, p.x, p.y)),
        O18(p -> new Position(-p.x, p.z, p.y)),
        O19(p -> new Position(-p.z, -p.x, p.y)),
        O21(p -> new Position(-p.x, -p.z, -p.y)),
        O22(p -> new Position(p.z, -p.x, -p.y)),
        O23(p -> new Position(p.x, p.z, -p.y)),
        O24(p -> new Position(-p.z, p.x, -p.y)),

        // End of constants
        ;

        private final UnaryOperator<Position> transformation;

        // Constructors

        Orientation(final UnaryOperator<Position> transformation) {
            this.transformation = transformation;
        }

        // Helper Methods

        /*
         * Transform a given position based on this orientation.
         */
        Position apply(final Position pos) {
            return transformation.apply(pos);
        }

    }

    /*
     * A position, in 3D space.
     */
    record Position(int x, int y, int z) {

        // Helper Methods

        /*
         * Add two `Position`s.
         */
        Position add(final Position other) {
            final Position transformed = Orientation.O00.apply(other);
            return new Position(
                    x + transformed.x,
                    y + transformed.y,
                    z + transformed.z
            );
        }

        /*
         * Subtract two `Position`s.
         */
        Position sub(final Position other) {
            final Position transformed = Orientation.O00.apply(other);
            return new Position(
                    x - transformed.x,
                    y - transformed.y,
                    z - transformed.z
            );
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

    }

    /*
     * A scanner and the associated positions of detected beacons.
     */
    record Scanner(int id, Position position, List<Position> beacons) {

        // Helper Methods

        /*
         * Resolve the absolute location of this `Scanner`.
         */
        Scanner resolve(final Orientation o, final Position actualPos) {
            return new Scanner(
                    id,
                    actualPos,
                    beacons.stream()
                            .map(pos -> o.apply(pos).add(actualPos))
                            .toList()
            );
        }

        // Static Helper Methods

        /*
         * Parse the given lines into a `Scanner`.
         */
        static Scanner parse(final List<String> lines) {
            final String title = lines.removeFirst();
            final int id = Integer.parseInt(title.substring(12).substring(0, title.length() - 16));
            final List<Position> beacons = lines.stream()
                    .map(line -> {
                        final String[] parts = line.split("\\s*,\\s*");
                        return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    })
                    .toList();
            return new Scanner(id, new Position(0, 0, 0), beacons);
        }

    }

}
