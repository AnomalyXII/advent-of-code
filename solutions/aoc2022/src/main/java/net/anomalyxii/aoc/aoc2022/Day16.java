package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 16.
 */
@Solution(year = 2022, day = 16, title = "Proboscidea Volcanium")
public class Day16 {

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
        final Network network = Network.parse(context);

        final Map<Long, Long> result = network.solve(30);
        return result.values().stream()
                .mapToLong(l -> l)
                .max()
                .orElseThrow();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Network network = Network.parse(context);
        final Map<Long, Long> result = network.solve(26);
        return result.entrySet().stream()
                .flatMapToLong(entry1 -> result.entrySet().stream()
                        .filter(entry2 -> (entry1.getKey() & entry2.getKey()) == 0)
                        .mapToLong(entry2 -> entry1.getValue() + entry2.getValue()))
                .max()
                .orElseThrow();
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A room within the tunnel `Network` that contains a valve.
     */
    private record Room(long id, String name, int rate, Set<String> tunnels) {

        private static final Pattern INPUT_REGEX = Pattern.compile("Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ((?:[A-Z]+, ?)*[A-Z]+)");

        // Constructors

        private Room(final long id, final String name, final int rate, final Set<String> tunnels) {
            this.id = id;
            this.name = name;
            this.rate = rate;
            this.tunnels = new TreeSet<>(tunnels);
        }

        // Helper Methods

        /**
         * Get the name of this {@link Room}.
         *
         * @return the name of this {@link Room}
         */
        @Override
        public String name() {
            return name;
        }

        /**
         * Check if this {@link Room} is actually worth visiting.
         * <p>
         * Although every {@link Room} contains a valve, some valves are jammed
         * and thus will not release any pressure if opened. This makes visiting
         * these {@link Room Rooms} pointless.
         *
         * @return {@literal true} if this {@link Room} is worth visiting; {@literal false} otherwise
         */
        boolean worthOpening() {
            return rate > 0;
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Room room = (Room) o;
            return id == room.id && rate == room.rate
                    && Objects.equals(name, room.name)
                    && Objects.equals(tunnels, room.tunnels);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, rate, tunnels);
        }


        // Static Helper Methods

        /*
         * Parse a `Room` specification from the given input line.
         */
        static Room parse(final int count, final String line) {
            final Matcher matcher = INPUT_REGEX.matcher(line);
            if (!matcher.matches())
                throw new IllegalArgumentException("Invalid room specification: '" + line + "'");

            assert count < 64 : "We're gonna need a bigger int...";
            return new Room(
                    1L << count,
                    matcher.group(1),
                    Integer.parseInt(matcher.group(2)),
                    new LinkedHashSet<>(asList(matcher.group(3).split(",\\s*")))
            );
        }
    }

    /*
     * A network of `Room`s, connected via tunnels, within a volcano.
     */
    private static final class Network {

        // Private Members

        private final Map<String, Room> rooms;
        private final Map<Long, Room> valves;

        private final Map<Room, Map<Room, Integer>> distanceCache = new HashMap<>();

        // Constructors

        Network(final Map<String, Room> rooms, final Map<Long, Room> valves) {
            this.rooms = new TreeMap<>(rooms);
            this.valves = valves;
        }

        // Helper Methods

        Map<Long, Long> solve(final int timeLimit) {
            final Map<Long, Long> results = new HashMap<>();
            findRoutes(findRoom("AA"), timeLimit, 0, 0, results);
            return results;
        }

        // Private Helper Methods

        /*
         * Find best routes, shamelessly stolen from
         * https://github.com/juanplopes/advent-of-code-2022/blob/main/day16.py ...
         */
        private void findRoutes(final Room room, final long timeLeft, final long pressure, final long visited, final Map<Long, Long> answer) {
            answer.compute(visited, (key, prev) -> prev != null ? max(prev, pressure) : pressure);
            for (final Room next : valves()) {
                final long wouldLeave = timeLeft - distanceTo(room, next) - 1;
                if ((visited & next.id) != 0 || wouldLeave <= 0)
                    continue;

                findRoutes(next, wouldLeave, pressure + wouldLeave * next.rate, visited | next.id, answer);
            }
        }

        /*
         * Find a `Room` with a given name.~
         */
        private Room findRoom(final String room) {
            return rooms.get(room);
        }

        /*
         * Return a `Set` of all `Room`s that contain a (useful) valve.
         */
        private Set<Room> valves() {
            return new HashSet<>(valves.values());
        }

        // Static Helper Methods

        /*
         * Create a new `Network` by parsing the given input.
         */
        static Network parse(final SolutionContext context) {
            final AtomicInteger counter = new AtomicInteger(0);

            final Map<String, Room> rooms = context.stream()
                    .map(line -> Room.parse(counter.incrementAndGet(), line))
                    .collect(Collectors.toMap(Room::name, Function.identity()));
            final Map<Long, Room> valves = rooms.values().stream()
                    .filter(Room::worthOpening)
                    .collect(Collectors.toMap(room -> room.id, room -> room));

            final Network network = new Network(rooms, valves);

            // Pre-cache all distances...
            valves.forEach((k1, from) -> valves.forEach((k2, to) -> network.distanceTo(from, to)));

            return network;

        }

        /*
         * Calculate the distance (in minutes) between two `Room`s.
         */
        private int distanceTo(final Room from, final Room to) {
            return distanceCache
                    .computeIfAbsent(from, k -> new HashMap<>())
                    .computeIfAbsent(to, k -> {
                        final Deque<Room> paths = new ArrayDeque<>();
                        paths.add(from);
                        for (int i = 0; i < rooms.size(); i++) {
                            if (paths.contains(to))
                                return i;
                            final Set<Room> next = new HashSet<>();
                            while (!paths.isEmpty()) {
                                for (final String tunnel : paths.removeFirst().tunnels)
                                    next.add(findRoom(tunnel));
                            }
                            paths.addAll(next);
                        }
                        throw new IllegalStateException("Failed to find valid path from " + from + " to " + to);
                    });
        }
    }

}

