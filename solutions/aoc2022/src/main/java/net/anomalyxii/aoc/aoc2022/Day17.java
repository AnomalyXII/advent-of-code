package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 17.
 */
@Solution(year = 2022, day = 17, title = "Pyroclastic Flow")
public class Day17 {

    private static final Velocity BLOW_LEFT = new Velocity(-1, 0);
    private static final Velocity BLOW_RIGHT = new Velocity(1, 0);
    private static final Velocity GRAVITY = new Velocity(0, -1);

    private static final Grid[] ROCKS = {
            Grid.of(new int[][]{
                    {1, 1, 1, 1}
            }),
            Grid.of(new int[][]{
                    {0, 1, 0, 0},
                    {1, 1, 1, 0},
                    {0, 1, 0, 0},
            }),
            Grid.of(new int[][]{
                    {1, 1, 1, 0},
                    {0, 0, 1, 0},
                    {0, 0, 1, 0},
            }),
            Grid.of(new int[][]{
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
            }),
            Grid.of(new int[][]{
                    {1, 1, 0, 0},
                    {1, 1, 0, 0},
                    {0, 0, 0, 0},
            })
    };

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
        return simulateRockFall(context, 2022);
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return simulateRockFall(context, 1_000_000_000_000L);
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Simulate the specified number of blocks falling.
     */
    private static long simulateRockFall(final SolutionContext context, final long weWillWeWillRockYou) {
        final String input = context.readLine();

        int highestPoint = -1;
        final Set<Coordinate> fallenRocks = new HashSet<>();

        long target = weWillWeWillRockYou;
        long surplus = 0; // The quantity that we repeat in the middle... might be huge!

        int wind = -1;
        final Map<Long, State> states = new HashMap<>();
        for (int i = 0; i < target; i++) {

            final int fallingRock = i % ROCKS.length;

            final Coordinate startingPosition = new Coordinate(2, highestPoint + 4);
            final Rock rock = Rock.fromGrid(ROCKS[fallingRock], startingPosition);
            boolean isFalling;
            do {
                // Keep fallin', fallin', fallin', fallin'...
                isFalling = rock.tryFall(input.charAt(wind = ((wind + 1) % input.length())), fallenRocks);
            } while (isFalling);

            fallenRocks.addAll(rock.shape);
            final int max = max(rock.top(), highestPoint);
            highestPoint = max;

            final long state = calculateStateKey(wind, fallingRock, max, fallenRocks);
            if (states.containsKey(state)) {
                // Calculate the difference between this and the previous state,
                // and zap along to the end...
                final State old = states.get(state);

                final int rockDiff = i - old.numRocks;
                final int heightDiff = highestPoint - old.height;

                if (rockDiff != old.rockDiff || heightDiff != old.heightDiff) {
                    // It seems we can reach the same state from two different configurations,
                    //   ... so keep going until we settle on the longest
                    states.put(state, new State(i, highestPoint, rockDiff, heightDiff));
                    continue;
                }

                // Work out how many times we can loop without reaching our target...
                final long remaining = target - i;
                final long loops = remaining / rockDiff;
                surplus = (heightDiff * loops);
                target -= (rockDiff * loops);

                states.clear(); // We don't want to use the cache anymore
                continue;
            }

            states.put(state, new State(i, highestPoint, 0, 0));
        }

        //dumpChamber(highestPoint, fallenRocks);
        return highestPoint + surplus + 1;
    }

    /*
     * Calculate a unique state for the fallen rocks by taking the top 5 rows
     * the current index of wind-direction and the rock that most recently
     * came to rest.
     *
     * (NB: seems like we can get away with just using the top 3 rows, but
     *  since the combination of other factors pushes us into a long, might
     *  as well just err on the side of caution and take as many as we can!).
     */
    private static long calculateStateKey(final int wind, final int fallingRock, final int max, final Set<Coordinate> fallenRocks) {
        // Calculate the top 5 rows mask:
        final Grid top = Grid.size(7, 5, c -> fallenRocks.contains(new Coordinate(c.x(), max - c.y())) ? 1 : 0);

        //   |----- wind ----|  | ri |  |------------- rock mask -------------|
        //                               34                            6     0
        // 0b 011111111111111 00 1111 00  111111_1111111_111111_111111_1111111

        final long[] top5 = {0};
        top.forEachValue((pos, val) -> top5[0] = (top5[0] << 1) | val);

        long state = wind;
        state = (state << 6) | fallingRock;
        state = (state << 39) | top5[0];
        assert state > 0; // Just check we haven't overflowed (we really shouldn't have, and also this is a weak check!)
        return state;
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * Tracks the state of rock fall so we can skip repeated layouts.
     */
    private record State(int numRocks, int height, int rockDiff, int heightDiff) {

        // Nothing to see here ;)

    }

    /*
     * Describes the shape of a rock.
     */
    private record Rock(Set<Coordinate> shape) {

        // NOTE: Kinda weird that this can be a record, since it's technically mutable...

        // Helper Methods

        /**
         * Get the height of this {@link Rock}, above ground-level.
         *
         * @return the highest point of this {@link Rock}
         */
        public int top() {
            return shape.stream()
                    .mapToInt(Coordinate::y)
                    .max()
                    .orElseThrow();
        }

        /**
         * Cause this {@link Rock} to fall one space vertically, whilst
         * simultaneously allowing it to be affected by wind from a given
         * direction.
         *
         * @param draft  the wind direction
         * @param cavern the current state of the cavern
         * @return {@literal true} if this {@link Rock} was able to fall vertically; {@literal false} otherwise
         */
        boolean tryFall(final char draft, final Set<Coordinate> cavern) {
            final Set<Coordinate> afterDraft = simulate(shape, draft(draft));
            final boolean draftMoved = check(afterDraft, cavern);

            final Set<Coordinate> afterGravity = draftMoved
                    ? simulate(afterDraft, GRAVITY)
                    : simulate(shape, GRAVITY);

            final boolean gravityMoved = check(afterGravity, cavern);

            if (draftMoved || gravityMoved) {
                shape.clear();
                shape.addAll(gravityMoved ? afterGravity : afterDraft);
            }

            return gravityMoved;
        }

        // Static Helper Methods

        /**
         * Create a new {@link Rock} from the given {@link Grid} specification,
         * with the bottom-left corner of the {@link Rock} starting at the
         * given start {@link Coordinate}.
         *
         * @param spec  the {@link Grid} specification
         * @param start the start {@link Coordinate}
         * @return the new {@link Rock}
         */
        public static Rock fromGrid(final Grid spec, final Coordinate start) {
            return new Rock(spec.entries()
                                    .filter(entry -> entry.getValue() != 0)
                                    .map(entry -> start.adjustBy(entry.getKey().x(), entry.getKey().y()))
                                    .collect(Collectors.toSet()));
        }

        // Private Helper Methods

        /*
         * Convert a draft symbol ('<' or '>') into a `Velocity`.
         */
        private static Velocity draft(final char draft) {
            return switch (draft) {
                case '<' -> BLOW_LEFT;
                case '>' -> BLOW_RIGHT;
                default -> throw new IllegalArgumentException("Invalid draft: " + draft);
            };
        }

        /*
         * Simulate this `Rock` being affected by the given `Velocity`.
         */
        private static Set<Coordinate> simulate(final Set<Coordinate> shape, final Velocity velocity) {
            return shape.stream()
                    .map(c -> c.adjustBy(velocity))
                    .collect(Collectors.toSet());
        }

        /*
         * Check if the destination `Coordinate`s are valid, given the current
         * state of the cavern.
         *
         * Specifically, check that the `Rock` hasn't gone through a wall, floor
         * or another `Rock`.
         */
        private static boolean check(final Set<Coordinate> destination, final Set<Coordinate> cavern) {
            return destination.stream()
                    .noneMatch(c -> c.x() < 0 || c.x() >= 7 || c.y() < 0 || cavern.contains(c));
        }

    }

}

