package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Area;
import net.anomalyxii.aoc.utils.geometry.Bounds;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2023, Day 22.
 */
@Solution(year = 2023, day = 22, title = "Sand Slabs")
public class Day22 {

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
        final Set<Brick> bricks = Brick.simulateFalling(context);
        return bricks.stream()
                .filter(brick -> brick.countDependents() == 0)
                .mapToInt(brick -> 1)
                .sum();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Set<Brick> bricks = Brick.simulateFalling(context);
        return bricks.stream()
                .mapToInt(Brick::countChainReaction)
                .sum();
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
        final Set<Brick> bricks = Brick.simulateFalling(context);
        return bricks.stream()
                .reduce(
                        IntTuple.NULL,
                        (answer, brick) -> {
                            final int mod1 = brick.countDependents() == 0 ? 1 : 0;
                            final int mod2 = brick.countChainReaction();

                            return answer.add(mod1, mod2);
                        },
                        IntTuple::add
                );
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A sand brick.
     */
    private static final class Brick implements Comparable<Brick> {

        /*
         * Not _really_ a `Brick`, but represents the ground level.
         */
        private static final Brick GROUND = new Brick(Area.INFINITY, Bounds.ZERO);

        /*
         * How to compare and thus sort `Brick`s.
         */
        private static final Comparator<Brick> COMPARATOR = Comparator.comparing((Brick r) -> r.depth)
                .thenComparing((Brick r) -> r.surface.w())
                .thenComparing((Brick r) -> r.surface.h());

        // Private Members

        private final Area surface;
        private final Set<Brick> supporting = new HashSet<>();
        private final Set<Brick> supportedBy = new HashSet<>();

        private Bounds depth;

        // Constructors

        private Brick(final Area surface, final Bounds depth) {
            this.surface = surface;
            this.depth = depth;
        }

        // Helper Methods

        /*
         * Register that this `Brick` is supporting the given other `Brick`.
         */
        void registerSupporting(final Brick other) {
            this.supporting.add(other);
        }

        /*
         * Count the number of supported `Brick`s that would fall is this `Brick`
         * was disintegrated.
         */
        int countDependents() {
            return supporting.stream()
                    .filter(next -> next.supportedBy.size() == 1)
                    .mapToInt(i -> 1)
                    .sum();
        }

        /*
         * Count the total number of `Brick`s that would fall if this `Brick` was
         * disintegrated.
         */
        int countChainReaction() {
            final Set<Brick> fallen = new HashSet<>();

            Set<Brick> wouldFall = Set.of(this);
            do {
                fallen.addAll(wouldFall);
                wouldFall = wouldFall.stream()
                        .flatMap(brick -> brick.supporting.stream())
                        .filter(next -> next.wouldFall(fallen))
                        .collect(Collectors.toSet());

            } while (!wouldFall.isEmpty());

            return fallen.size() - 1;
        }

        /*
         * Simulate this `Brick` falling one unit.
         */
        void fall(final SortedSet<Brick> fallenBricks) {
            final SortedSet<Brick> overlapping = fallenBricks.stream()
                    .filter(other -> !surface.intersect(other.surface).isNull())
                    .reduce(
                            new TreeSet<>(Set.of(GROUND)),
                            (set, other) -> {
                                final Brick first = set.getFirst();
                                if (other.depth.max() > first.depth.max())
                                    return new TreeSet<>(Set.of(other));

                                if (other.depth.max() == first.depth.max())
                                    set.add(other);
                                return set;
                            },
                            (a, b) -> {
                                throw new IllegalStateException("Should not need to merge!");
                            }
                    );

            final Brick first = overlapping.first();
            final int adjust = depth.min() - (first.depth.max() + 1);
            if (adjust != 0)
                depth = Bounds.of(depth.min() - adjust, depth.max() - adjust);

            supportedBy.addAll(overlapping);
            overlapping.forEach(support -> support.registerSupporting(this));
        }

        /*
         * Check if this `Brick` would fall down if all of the given other
         * `Brick`s had either fallen or been disintegrated.
         */
        private boolean wouldFall(final Set<Brick> others) {
            final HashSet<Brick> hypothetical = new HashSet<>(supportedBy);
            hypothetical.removeAll(others);
            return hypothetical.isEmpty();
        }

        // Comparable Methods

        @Override
        public int compareTo(final Brick o) {
            return COMPARATOR.compare(this, o);
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            final Brick that = (Brick) obj;
            return Objects.equals(this.surface, that.surface)
                    && Objects.equals(this.depth, that.depth);
        }

        @Override
        public int hashCode() {
            return Objects.hash(surface, depth);
        }

        // Static Helper Methods

        /*
         * Simulate the falling of `Brick`s.
         */
        static SortedSet<Brick> simulateFalling(final SolutionContext context) {
            final SortedSet<Brick> bricks = context.stream()
                    .map(Brick::parse)
                    .collect(Collectors.toCollection(TreeSet::new));

            final SortedSet<Brick> fallenBricks = new TreeSet<>();
            for (final Brick brick : bricks) {
                brick.fall(fallenBricks);
                fallenBricks.add(brick);
            }

            return fallenBricks;
        }

        /*
         * Parse a rock.
         */
        private static Brick parse(final String line) {
            final String[] parts = line.split("~");
            final int[] front = stream(parts[0].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            final int[] back = stream(parts[1].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            return new Brick(
                    Area.of(Bounds.of(front[0], back[0]), Bounds.of(front[1], back[1])),
                    Bounds.of(front[2], back[2])
            );
        }

    }


}

