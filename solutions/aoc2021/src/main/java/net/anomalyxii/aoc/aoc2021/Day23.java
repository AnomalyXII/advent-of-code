package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2021, Day 23.
 */
@Solution(year = 2021, day = 23, title = "Amphipod")
public class Day23 {

    private static final Set<Position>[][] CACHED_PATHS = ValidPosition.cachePaths();

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
        return solve(context, false);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return solve(context, true);
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Find the lowest amount of energy needed to organise the Amphiods
     * correctly.
     */
    private long solve(final SolutionContext context, final boolean useExtraPositions) {
        final BurrowWithCost initial = readInitialBurrow(context, useExtraPositions);

        final Map<Burrow, Long> dist = new HashMap<>();
        final PriorityQueue<BurrowWithCost> queue = new PriorityQueue<>(Comparator.comparing((BurrowWithCost b) -> b.cost));

        dist.put(initial.burrow, 0L);
        queue.add(initial);

        BurrowWithCost uk;
        while ((uk = queue.poll()) != null) {
            final Burrow u = uk.burrow;
            final long priority = dist.getOrDefault(u, (long) Integer.MAX_VALUE);
            if (uk.cost > priority) continue;

            u.resolveFutures(useExtraPositions).forEach(future -> {
                final long futurePriority = dist.getOrDefault(future.burrow, (long) Integer.MAX_VALUE);
                final long alt = priority + future.cost;
                if (alt < futurePriority) {
                    dist.put(future.burrow, alt);
                    queue.add(new BurrowWithCost(future.burrow, alt));
                }
            });
        }

        return dist.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isComplete())
                .mapToLong(Map.Entry::getValue)
                .min()
                .orElseThrow();
    }

    /*
     * Read in the initial `Burrow` layout.
     */
    private BurrowWithCost readInitialBurrow(final SolutionContext context, final boolean useExtraPositions) {
        final List<String> lines = context.read();
        assert "#############".equals(lines.get(0));
        assert "#...........#".equals(lines.get(1));
        assert "  #########".equals(lines.get(4));

        final Amphipod[] amphipods = new Amphipod[ValidPosition.values().length];
        setAmphipod(amphipods, ValidPosition.ROOM1_0, lines.get(2).charAt(3));
        setAmphipod(amphipods, ValidPosition.ROOM2_0, lines.get(2).charAt(5));
        setAmphipod(amphipods, ValidPosition.ROOM3_0, lines.get(2).charAt(7));
        setAmphipod(amphipods, ValidPosition.ROOM4_0, lines.get(2).charAt(9));

        if (!useExtraPositions) {
            setAmphipod(amphipods, ValidPosition.ROOM1_1, lines.get(3).charAt(3));
            setAmphipod(amphipods, ValidPosition.ROOM2_1, lines.get(3).charAt(5));
            setAmphipod(amphipods, ValidPosition.ROOM3_1, lines.get(3).charAt(7));
            setAmphipod(amphipods, ValidPosition.ROOM4_1, lines.get(3).charAt(9));
        } else {
            setAmphipod(amphipods, ValidPosition.ROOM1_1, 'D');
            setAmphipod(amphipods, ValidPosition.ROOM2_1, 'C');
            setAmphipod(amphipods, ValidPosition.ROOM3_1, 'B');
            setAmphipod(amphipods, ValidPosition.ROOM4_1, 'A');

            setAmphipod(amphipods, ValidPosition.ROOM1_2, 'D');
            setAmphipod(amphipods, ValidPosition.ROOM2_2, 'B');
            setAmphipod(amphipods, ValidPosition.ROOM3_2, 'A');
            setAmphipod(amphipods, ValidPosition.ROOM4_2, 'C');

            setAmphipod(amphipods, ValidPosition.ROOM1_3, lines.get(3).charAt(3));
            setAmphipod(amphipods, ValidPosition.ROOM2_3, lines.get(3).charAt(5));
            setAmphipod(amphipods, ValidPosition.ROOM3_3, lines.get(3).charAt(7));
            setAmphipod(amphipods, ValidPosition.ROOM4_3, lines.get(3).charAt(9));
        }
        final Burrow burrow = new Burrow(amphipods);
        return new BurrowWithCost(burrow, 0);
    }

    /*
     * Set an `Amphipod` at a specific starting location.
     */
    private void setAmphipod(final Amphipod[] amphipods, final ValidPosition position, final char type) {
        amphipods[position.ordinal()] = Amphipod.of(position, type);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * The position of an `Amphipod` within the `Burrow`.
     */
    record Position(Position.Type type, int depth) {

        // Helper Methods

        /*
         * Calculate a path of `Positions` needed to traverse from this
         * `Position` to the "next" `Position`.
         */
        Set<Position> navigateTo(final Position next) {
            if (type == next.type && depth == next.depth)
                return Collections.emptySet();
            if (type == next.type) {
                final Set<Position> positions = new LinkedHashSet<>();
                final int inc = depth < next.depth ? 1 : -1;
                for (int i = depth + inc; i != next.depth; i += inc)
                    positions.add(new Position(type, i));
                positions.add(next);
                return positions;
            }

            final Set<Position> positions = new LinkedHashSet<>();

            // Walk to corridor, if required
            if (type != Type.CORRIDOR)
                positions.addAll(navigateTo(new Position(type, 0)));

            Position current = this;
            // Go into the corridor if required...
            if (type != Type.CORRIDOR) {
                if (type == Type.ROOM_1) current = new Position(Type.CORRIDOR, 2);
                else if (type == Type.ROOM_2) current = new Position(Type.CORRIDOR, 4);
                else if (type == Type.ROOM_3) current = new Position(Type.CORRIDOR, 6);
                else if (type == Type.ROOM_4) current = new Position(Type.CORRIDOR, 8);
                else throw new IllegalStateException();
                positions.add(current);
            }

            Position dest = next;
            if (next.type != Type.CORRIDOR) {
                if (next.type == Type.ROOM_1) dest = new Position(Type.CORRIDOR, 2);
                else if (next.type == Type.ROOM_2) dest = new Position(Type.CORRIDOR, 4);
                else if (next.type == Type.ROOM_3) dest = new Position(Type.CORRIDOR, 6);
                else if (next.type == Type.ROOM_4) dest = new Position(Type.CORRIDOR, 8);
                else throw new IllegalStateException();
            }
            // We should always be moving between corridor positions at this point!
            assert current.type == Type.CORRIDOR;
            assert dest.type == Type.CORRIDOR;

            positions.addAll(current.navigateTo(dest));

            // Walk out of the corridor, if required
            if (next.type != Type.CORRIDOR) {
                final Position entrance = new Position(next.type, 0);
                positions.add(entrance);
                positions.addAll(entrance.navigateTo(next));
            }

            return positions;
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Position position = (Position) o;
            return depth == position.depth && type == position.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, depth);
        }

        // Helper Classes

        /*
         * The type of `Position`.
         */
        enum Type {
            CORRIDOR,
            ROOM_1,
            ROOM_2,
            ROOM_3,
            ROOM_4,
        }

    }

    /*
     * An enumeration of all possible valid `Position`s within a `Burrow`.
     */
    enum ValidPosition {

        // Helper Methods

        CORRIDOR_0(new Position(Position.Type.CORRIDOR, 0), true, false, false),
        CORRIDOR_1(new Position(Position.Type.CORRIDOR, 1), true, false, false),
        CORRIDOR_2(new Position(Position.Type.CORRIDOR, 2), false, false, false),
        CORRIDOR_3(new Position(Position.Type.CORRIDOR, 3), true, false, false),
        CORRIDOR_4(new Position(Position.Type.CORRIDOR, 4), false, false, false),
        CORRIDOR_5(new Position(Position.Type.CORRIDOR, 5), true, false, false),
        CORRIDOR_6(new Position(Position.Type.CORRIDOR, 6), false, false, false),
        CORRIDOR_7(new Position(Position.Type.CORRIDOR, 7), true, false, false),
        CORRIDOR_8(new Position(Position.Type.CORRIDOR, 8), false, false, false),
        CORRIDOR_9(new Position(Position.Type.CORRIDOR, 9), true, false, false),
        CORRIDOR_10(new Position(Position.Type.CORRIDOR, 10), true, false, false),

        ROOM1_0(new Position(Position.Type.ROOM_1, 0), false, true, false),
        ROOM1_1(new Position(Position.Type.ROOM_1, 1), false, true, false),
        ROOM1_2(new Position(Position.Type.ROOM_1, 2), false, true, true),
        ROOM1_3(new Position(Position.Type.ROOM_1, 3), false, true, true),

        ROOM2_0(new Position(Position.Type.ROOM_2, 0), false, true, false),
        ROOM2_1(new Position(Position.Type.ROOM_2, 1), false, true, false),
        ROOM2_2(new Position(Position.Type.ROOM_2, 2), false, true, true),
        ROOM2_3(new Position(Position.Type.ROOM_2, 3), false, true, true),

        ROOM3_0(new Position(Position.Type.ROOM_3, 0), false, true, false),
        ROOM3_1(new Position(Position.Type.ROOM_3, 1), false, true, false),
        ROOM3_2(new Position(Position.Type.ROOM_3, 2), false, true, true),
        ROOM3_3(new Position(Position.Type.ROOM_3, 3), false, true, true),

        ROOM4_0(new Position(Position.Type.ROOM_4, 0), false, true, false),
        ROOM4_1(new Position(Position.Type.ROOM_4, 1), false, true, false),
        ROOM4_2(new Position(Position.Type.ROOM_4, 2), false, true, true),
        ROOM4_3(new Position(Position.Type.ROOM_4, 3), false, true, true),
        ;

        private final Position position;
        private final boolean isValidIntermediatePosition;
        private final boolean isValidFinalPosition;
        private final boolean extraPosition;

        // Constructors

        ValidPosition(final Position position, final boolean isValidIntermediatePosition, final boolean isValidFinalPosition, final boolean extraPosition) {
            this.position = position;
            this.isValidIntermediatePosition = isValidIntermediatePosition;
            this.isValidFinalPosition = isValidFinalPosition;
            this.extraPosition = extraPosition;
        }

        // Static Helper Methods

        /*
         * Cache the paths needed to move from one `ValidPosition` to another.
         */
        @SuppressWarnings("unchecked")
        static Set<Position>[][] cachePaths() {
            // This is a bit ick...
            final ValidPosition[] values = ValidPosition.values();
            final int length = values.length;

            final Set<Position>[][] cache = new Set[length][];
            for (final ValidPosition vp : values) {
                final Set<Position>[] row = new Set[length];
                for (final ValidPosition np : values) {
                    row[np.ordinal()] = vp.position.navigateTo(np.position);
                }
                cache[vp.ordinal()] = row;
            }
            return cache;
        }
    }

    /*
     * An amphipid within a `Burrow`.
     */
    record Amphipod(ValidPosition position, Amphipod.Type type, int turn) {

        // Helper Methods

        /*
         * Check if this `Amphipod` is in a home `Position`.
         */
        boolean isHome() {
            return position.position.type == type.destination;
        }

        /*
         * Check if this `Amphipod` can, in general, move to a given
         * `ValidPosition`.
         *
         * `false` here means "no", whilst `true` means "eh, maybe...".
         */
        boolean canMoveTo(final ValidPosition next) {
            return next != position
                    && (next.position.type == Position.Type.CORRIDOR || next.position.type == type.destination)
                    && (next.isValidFinalPosition || next.isValidIntermediatePosition && turn == 0);
        }

        /*
         * Calculate the absolute shortest path needed to get this `Amphipod`
         * into a home `Position`, ignoring whether or not this is actually
         * possible.
         */
        Set<Position> minimumPathHome() {
            return !isHome()
                    ? switch (type) {
                case AMBER -> CACHED_PATHS[position.ordinal()][ValidPosition.ROOM1_0.ordinal()];
                case BRONZE -> CACHED_PATHS[position.ordinal()][ValidPosition.ROOM2_0.ordinal()];
                case COPPER -> CACHED_PATHS[position.ordinal()][ValidPosition.ROOM3_0.ordinal()];
                case DESERT -> CACHED_PATHS[position.ordinal()][ValidPosition.ROOM4_0.ordinal()];
            }
                    : Collections.emptySet();
        }

        // Static Helper Methods

        /*
         * Create a new `Amphipod` of the given type at the given `Position`.
         */
        static Amphipod of(final ValidPosition position, final char chr) {
            return switch (chr) {
                case 'A' -> new Amphipod(position, Type.AMBER, 0);
                case 'B' -> new Amphipod(position, Type.BRONZE, 0);
                case 'C' -> new Amphipod(position, Type.COPPER, 0);
                case 'D' -> new Amphipod(position, Type.DESERT, 0);
                default -> throw new IllegalArgumentException("Invalid amphipod: " + chr);
            };
        }


        // Helper Classes

        /*
         * The types of `Amphipod` available.
         */
        enum Type {

            AMBER(1, Position.Type.ROOM_1),
            BRONZE(10, Position.Type.ROOM_2),
            COPPER(100, Position.Type.ROOM_3),
            DESERT(1000, Position.Type.ROOM_4),

            // End of constants
            ;

            private final long cost;
            private final Position.Type destination;

            Type(final long cost, final Position.Type destination) {
                this.cost = cost;
                this.destination = destination;
            }

        }

    }

    /*
     * A burrow containing a number of `Amphipod`s in various `Position`s.
     */
    record Burrow(Amphipod[] amphipods) {

        // Helper Methods

        /*
         * Check if this `Burrow` has all the `Amphipod`s in "home" `Position`s.
         */
        boolean isComplete() {
            for (final Amphipod amphipod : amphipods) {
                if (amphipod != null && amphipod.position.position.type != amphipod.type.destination)
                    return false;
            }
            return true;
        }

        /*
         * Resolve possible futures by moving one `Amphipod` in this `Burrow`.
         */
        Set<BurrowWithCost> resolveFutures(final boolean useExtraPositions) {
            if (isComplete()) return Collections.emptySet();

            final Set<BurrowWithCost> futures = new HashSet<>();
            for (int a = 0, amphipodsLength = amphipods.length; a < amphipodsLength; a++) {
                final Amphipod amphipod = amphipods[a];
                if (amphipod == null || amphipod.turn == 2) continue; // Can't move again!

                final Amphipod[] others = new Amphipod[useExtraPositions ? 15 : 7];
                for (int i = 0, j = 0; i < amphipodsLength; i++)
                    if (i != a && amphipods[i] != null)
                        others[j++] = amphipods[i];

                // At home and happy....
                if (amphipod.isHome()) {
                    if (!isOtherAmphipodInRoom(amphipod.type.destination, others)) continue;
                }

                final Set<BurrowWithCost> amphiodFutures = new HashSet<>();
                final Set<BurrowWithCost> amphiodCompleteFutures = new HashSet<>();

                positions:
                for (final ValidPosition next : ValidPosition.values()) {
                    // General checks
                    if (!amphipod.canMoveTo(next)) continue;
                    if (next.extraPosition && !useExtraPositions) continue;

                    // Check if the amphipod can move into a room
                    if (next.position.type != Position.Type.CORRIDOR) {
                        if (isOtherAmphipodInRoom(next.position.type, others)) continue;
                        final int maxHomeDepthAvailable = determineMaximumAvailableDepth(useExtraPositions, next, others);
                        if (next.position.depth != maxHomeDepthAvailable) continue;
                    }

                    // Check if moving to this position would cause a deadlock
                    final Amphipod movedAmphipod = new Amphipod(next, amphipod.type, amphipod.turn + 1);
                    if (couldDeadlock(movedAmphipod, others)) continue;

                    // Check if moving to this position requires moving through another amphipod
                    final Set<Position> path = CACHED_PATHS[amphipod.position.ordinal()][next.ordinal()];
                    for (final Amphipod other : others) {
                        if (other.position == next) continue positions;
                        if (path.contains(other.position.position)) continue positions;
                    }

                    final Amphipod[] copy = new Amphipod[amphipodsLength];
                    System.arraycopy(amphipods, 0, copy, 0, amphipodsLength);
                    copy[a] = null;
                    copy[next.ordinal()] = movedAmphipod;
                    final Burrow newBurrow = new Burrow(copy);
                    final BurrowWithCost future = new BurrowWithCost(newBurrow, path.size() * amphipod.type.cost);

                    if (next.position.type == Position.Type.CORRIDOR)
                        amphiodFutures.add(future);
                    else
                        amphiodCompleteFutures.add(future);
                }

                // If we can complete, only consider those futures?
                futures.addAll(
                        !amphiodCompleteFutures.isEmpty()
                                ? amphiodCompleteFutures
                                : amphiodFutures
                );
            }

            return futures;
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Burrow burrow = (Burrow) o;
            return Arrays.equals(amphipods, burrow.amphipods);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(amphipods);
        }

        // Private Helper Methods

        /*
         * Check if another `Amphipod` is in a room.
         */
        private boolean isOtherAmphipodInRoom(final Position.Type destination, final Amphipod[] values) {
            for (final Amphipod other : values)
                if (other.type.destination != destination && other.position.position.type == destination)
                    return true;
            return false;
        }

        /*
         * Determine the maximum depth in a home that an Amphipod can move to.
         */
        private static int determineMaximumAvailableDepth(final boolean useExtraPositions, final ValidPosition next, final Amphipod[] others) {
            int maxHomeDepthAvailable = 0;
            roomDepths:
            for (final ValidPosition roomPosition : ValidPosition.values()) {
                if (roomPosition.position.type != next.position.type) continue;
                if (roomPosition.extraPosition && !useExtraPositions) continue;
                if (roomPosition.position.depth < maxHomeDepthAvailable) continue;
                for (final Amphipod other : others)
                    if (other.position == roomPosition) continue roomDepths;
                maxHomeDepthAvailable = roomPosition.position.depth;
            }
            return maxHomeDepthAvailable;
        }

        /*
         * Check if moving an `Amphipod` to a particular `Position` would cause a
         * deadlock.
         */
        private boolean couldDeadlock(final Amphipod movedAmphipod, final Amphipod[] others) {
            if (movedAmphipod.turn == 2) return false; // If it's moved home, it can't deadlock?

            final Set<Position> movedPathHome = movedAmphipod.minimumPathHome();
            for (final Amphipod other : others) {
                // Probably can resolve by moving into a free corridor space?
                if (other.turn > 0) {
                    final boolean thisBlocksOther = other.minimumPathHome().contains(movedAmphipod.position.position);
                    final boolean otherBlocksThis = movedPathHome.contains(other.position.position);
                    if (thisBlocksOther && otherBlocksThis)
                        return true;
                }
            }
            return false;
        }

    }

    /*
     * A `Burrow` and an associated cost (either relative or absolute).
     */
    record BurrowWithCost(Burrow burrow, long cost) {

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final BurrowWithCost that = (BurrowWithCost) o;
            return cost == that.cost && Objects.equals(burrow, that.burrow);
        }

        @Override
        public int hashCode() {
            return Objects.hash(burrow, cost);
        }

    }

}