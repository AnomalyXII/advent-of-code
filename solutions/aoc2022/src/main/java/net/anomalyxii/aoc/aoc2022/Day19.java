package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 19.
 */
@Solution(year = 2022, day = 19, title = "Not Enough Minerals")
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
        final Blueprints blueprints = Blueprints.parse(context);
        return blueprints.calculateQuantityLevelScore();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final Blueprints blueprints = Blueprints.parse(context);
        return blueprints.calculateMaxGeodeHaul(3);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * The state of a simulation.
     *
     * Contains the current minute, and a packed store of currently owned
     * `@Robots` and `@Resources`.
     */
    private record State(int minute, @Robots int robots, @Resources int resources) {
    }

    /*
     * A template on how to build a robot.
     */
    private record RobotSpec(int id, String name, int ore, int clay, int obsidian, int geode) {

        // Static Helper Methods

        /**
         * Parse a {@link RobotSpec} from the given line.
         *
         * @param line the line of text to parse
         * @return the {@link RobotSpec}
         */
        public static RobotSpec parse(final String line) {
            final String[] words = line.split(" ");

            final String name = words[1];
            final Map<String, Integer> costs = new HashMap<>();
            for (int i = 4; i < words.length; i += 3)
                costs.put(words[i + 1], Integer.parseInt(words[i]));

            return new RobotSpec(
                    Blueprint.type2idx(name),
                    name,
                    costs.getOrDefault("ore", 0),
                    costs.getOrDefault("clay", 0),
                    costs.getOrDefault("obsidian", 0),
                    costs.getOrDefault("geode", 0)
            );
        }
    }

    /*
     * A blueprint for creating robots.
     */
    static class Blueprint {

        private static final int GEODE = 3;
        private static final int OBSIDIAN = 2;
        private static final int CLAY = 1;
        private static final int ORE = 0;

        private static final int ONE_GEODE = resource(GEODE);
        private static final int ONE_OBSIDIAN = resource(OBSIDIAN);
        private static final int ONE_CLAY = resource(CLAY);
        private static final int ONE_ORE = resource(ORE);

        private static final String[] MINERALS = {"ore", "clay", "obsidian", "geode"};

        // Private Members

        private final int id;
        private final Map<String, RobotSpec> specs;
        private final int maxOre;
        private final int maxClay;
        private final int maxObsidian;

        // Constructors

        Blueprint(final int id, final Map<String, RobotSpec> specs) {
            this.id = id;
            this.specs = specs;
            this.maxOre = specs.values().stream().mapToInt(spec -> spec.ore).max().orElse(0);
            this.maxClay = specs.values().stream().mapToInt(spec -> spec.clay).max().orElse(0);
            this.maxObsidian = specs.values().stream().mapToInt(spec -> spec.obsidian).max().orElse(0);
        }

        // Helper Methods

        /**
         * Calculate the optimum yield of geodes with a given time limit.
         *
         * @param timeLimit the time limit
         * @return the maximum number of geodes that can be farmed
         */
        long calculateGeodeReturn(final int timeLimit) {
            return makeDifficultDecisions(new State(0, 1, 0), 0L, new HashMap<>(), timeLimit);
        }

        // Private Helper Methods

        /*
         * Make decisions on what kind of robot to build next
         */
        private long makeDifficultDecisions(final State state, final long bestSoFar, final Map<State, Long> cache, final int timeLimit) {
            if (state.minute == timeLimit)
                return resource(GEODE, state.resources);
            if (cache.containsKey(state))
                return cache.get(state);

            @Resource final short geodes = resource(GEODE, state.resources);
            @Robot final short geobots = robot(GEODE, state.robots);
            final long timeLeft = timeLimit - state.minute;
            final long optimisticScore = geodes + (geobots * timeLeft) + (timeLeft * (timeLeft - 1) / 2);
            if (optimisticScore <= bestSoFar)
                return Long.MIN_VALUE; // Give up early?

            long best = bestSoFar;

            final RobotSpec geodeSpec = this.specs.get(MINERALS[GEODE]);
            if (canMakeRobot(geodeSpec, state.resources))
                best = max(best, simulate(state, geodeSpec, best, cache, timeLimit));

            if (robot(OBSIDIAN, state.robots) < maxObsidian) {
                final RobotSpec spec = this.specs.get(MINERALS[OBSIDIAN]);
                if (canMakeRobot(spec, state.resources))
                    best = max(best, simulate(state, spec, best, cache, timeLimit));
            }

            if (robot(CLAY, state.robots) < maxClay && robot(GEODE, state.robots) == 0) {
                final RobotSpec spec = this.specs.get(MINERALS[CLAY]);
                if (canMakeRobot(spec, state.resources))
                    best = max(best, simulate(state, spec, best, cache, timeLimit));
            }

            if (robot(ORE, state.robots) < maxOre && robot(OBSIDIAN, state.robots) == 0) {
                final RobotSpec spec = this.specs.get(MINERALS[ORE]);
                if (canMakeRobot(spec, state.resources))
                    best = max(best, simulate(state, spec, best, cache, timeLimit));
            }

            best = max(best, simulate(state, bestSoFar, cache, timeLimit));

            cache.put(state, best);
            return best;
        }

        /*
         * Simulate what would happen if a robot isn't built this minute.
         */
        private long simulate(final State state, final long bestSoFar, final Map<State, Long> cache, final int timeLimit) {
            @Robots final int nextRobots = state.robots;
            @Resources int nextResources = state.resources;

            // Farm the new materials
            nextResources +=
                    (ONE_GEODE * robot(GEODE, state.robots))
                            + (ONE_OBSIDIAN * robot(OBSIDIAN, state.robots))
                            + (ONE_CLAY * robot(CLAY, state.robots))
                            + (ONE_ORE * robot(ORE, state.robots));

            return makeDifficultDecisions(new State(state.minute + 1, nextRobots, nextResources), bestSoFar, cache, timeLimit);
        }

        /*
         * Simulate what would happen if the specified `RobotSpec` was built.
         */
        private long simulate(final State state, final RobotSpec spec, final long bestSoFar, final Map<State, Long> cache, final int timeLimit) {
            @Robots int nextRobots = state.robots;
            @Resources int nextResources = state.resources;

            // Input a new Robot into the factory
            nextResources -=
                    (ONE_GEODE * spec.geode)
                            + (ONE_OBSIDIAN * spec.obsidian)
                            + (ONE_CLAY * spec.clay)
                            + (ONE_ORE * spec.ore);

            // Farm the new materials
            nextResources +=
                    (ONE_GEODE * robot(GEODE, state.robots))
                            + (ONE_OBSIDIAN * robot(OBSIDIAN, state.robots))
                            + (ONE_CLAY * robot(CLAY, state.robots))
                            + (ONE_ORE * robot(ORE, state.robots));

            // Receive a new Robot from the factory
            nextRobots += robot(spec.id);

            return makeDifficultDecisions(new State(state.minute + 1, nextRobots, nextResources), bestSoFar, cache, timeLimit);
        }

        // Static Helper Methods

        /**
         * Parse a {@link Blueprint} from the given line of text.
         *
         * @param line the line of text to parse
         * @return the {@link Blueprint}
         */
        public static Blueprint parse(final String line) {
            final String[] parts = line.split(": ");
            final int id = Integer.parseInt(parts[0].substring(10));
            final Map<String, RobotSpec> specs = stream(parts[1].split("[.] *"))
                    .map(RobotSpec::parse)
                    .collect(Collectors.toMap(spec -> spec.name, spec -> spec));
            return new Blueprint(id, specs);
        }

        // Private Static Helper Methods

        /*
         * Check if there are enough resources available to build a robot.
         */
        private static boolean canMakeRobot(final RobotSpec spec, @Resources final int resources) {
            return (resource(GEODE, resources) >= spec.geode)
                    && (resource(OBSIDIAN, resources) >= spec.obsidian)
                    && (resource(CLAY, resources) >= spec.clay)
                    && (resource(ORE, resources) >= spec.ore);
        }

        /*
         * Convert the text-based resource/robot name to an ID.
         */
        private static int type2idx(final String type) {
            return switch (type) {
                case "geode" -> GEODE;
                case "obsidian" -> OBSIDIAN;
                case "clay" -> CLAY;
                case "ore" -> ORE;
                default -> throw new IllegalArgumentException("Invalid resource type: " + type);
            };
        }

        /*
         * Represent a single robot of the given type.
         */
        private static @Robots int robot(final int id) {
            return (0x0001 << (8 * id));
        }

        /*
         * Extract the count of robots of the given type from the compressed
         * storage of robots.
         */
        private static @Robot short robot(final int id, @Robots final int robots) {
            final int r = 0x00FF & (robots >> (8 * id));
            return (short) r;
        }

        /*
         * Represent a single resource of the given type.
         */
        private static @Resources int resource(final int id) {
            return (0x0001 << (8 * id));
        }

        /*
         * Extract the count of resources of the given type from the compressed
         * storage of resources.
         */
        private static @Resource short resource(final int id, @Resources final int resources) {
            final int r = 0x00FF & (resources >> (8 * id));
            return (short) r;
        }

    }

    /*
     * A storage of multiple `Blueprint`s.
     */
    private record Blueprints(List<Blueprint> blueprints) {

        // Helper Methods

        /**
         * Calculate the total quality score of all the stored
         * {@link Blueprint Blueprints}.
         * <p>
         * The quality score is the {@link Blueprint#id ID} multiplied by the
         * {@link Blueprint#calculateGeodeReturn(int) maximum number of geodes}
         * that can be farmed in 24 minutes.
         *
         * @return the sum of all quality scores
         */
        long calculateQuantityLevelScore() {
            return blueprints.stream()
                    .mapToLong(blueprint -> blueprint.id * blueprint.calculateGeodeReturn(24))
                    .sum();
        }

        /**
         * Calculate the total geode haul of the first {@literal n} stored
         * {@link Blueprint Blueprints} and return the product of these.
         *
         * @return the product of the haul of {@literal n} {@link Blueprint blueprints}
         */
        long calculateMaxGeodeHaul(final int limit) {
            return blueprints.subList(0, min(limit, blueprints.size())).stream()
                    .mapToLong(blueprint -> blueprint.calculateGeodeReturn(32))
                    .reduce(1, (a, b) -> a * b);
        }

        // Static Helper Methods

        /**
         * Parse {@link Blueprints} from the given {@link SolutionContext}.
         *
         * @param context the {@link SolutionContext} to load from
         * @return the {@link Blueprints}
         */
        public static Blueprints parse(final SolutionContext context) {
            return new Blueprints(context.process(Blueprint::parse));
        }

    }

    /*
     * Checker annotation (maybe not enforced?) to indicate a packed count
     * of multiple `@Robot`s.
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    private @interface Robots {

    }

    /*
     * Checker annotation (maybe not enforced?) to indicate an unpacked count
     * of a single type of robot.
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    private @interface Robot {

    }

    /*
     * Checker annotation (maybe not enforced?) to indicate a packed count
     * of multiple `@Resource`s.
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    private @interface Resources {

    }

    /*
     * Checker annotation (maybe not enforced?) to indicate an unpacked count
     * of a single type of resources.
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    private @interface Resource {

    }

}

