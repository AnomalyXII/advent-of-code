package net.anomalyxii.aoc.vertx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import net.anomalyxii.aoc.Challenge;
import net.anomalyxii.aoc.NoChallenge;
import net.anomalyxii.aoc.SpiSolutionLoader;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A blocking {@link AdventOfCodeService}.
 */
class AsyncAdventOfCodeService implements AdventOfCodeService {

    private static final SpiSolutionLoader SOLUTION_LOADER = new SpiSolutionLoader();

    // ****************************************
    // Private Members
    // ****************************************

    private final Vertx vertx;

    // ****************************************
    // Constructors
    // ****************************************

    AsyncAdventOfCodeService(final Vertx vertx) {
        this.vertx = vertx;
    }

    // ****************************************
    // AdventOfCodeService Methods
    // ****************************************

    @Override
    public Future<List<JsonObject>> listAll() {
        return vertx.executeBlocking(
                () -> SOLUTION_LOADER.allChallenges().stream()
                        .sorted()
                        .map(AsyncAdventOfCodeService::toJson)
                        .toList(),
                false
        );
    }

    @Override
    public Future<List<JsonObject>> listAllForYear(final int year) {
        return vertx.executeBlocking(
                () -> SOLUTION_LOADER.allChallengesForYear(year).stream()
                        .sorted()
                        .map(AsyncAdventOfCodeService::toJson)
                        .toList(),
                false
        );
    }

    @Override
    public Future<JsonObject> retrieveChallengeInfo(final int year, final int day) {
        return vertx.executeBlocking(
                () -> SOLUTION_LOADER.findChallenge(year, day)
                        .map(AsyncAdventOfCodeService::toJson)
                        .orElseThrow(() -> new IllegalArgumentException("Challenge not found")),
                false
        );
    }

    @Override
    public Future<JsonObject> solveChallenge(final int year, final int day, final int part) {
        return vertx.executeBlocking(
                () -> {
                    final Optional<JsonObject> maybeChallenge = SOLUTION_LOADER.findChallenge(year, day)
                            .map(challenge -> runChallenge(part, challenge));
                    return maybeChallenge.orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
                },
                false
        );
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Run a given `Challenge` and encode the result as a `JsonObject`.
     */
    private static JsonObject runChallenge(final int part, final Challenge<?, ?> challenge) {
        final Map<String, Object> fields = new HashMap<>();
        fields.put("year", challenge.year());
        fields.put("day", challenge.day());
        fields.put("title", challenge.title());

        final SolutionContext context = SolutionContext.live(challenge.year(), challenge.day());

        switch (part) {
            case 1 -> {
                fields.put("description", challenge.part1Description());
                final long start = System.nanoTime();
                final Object result = challenge.calculateAnswerForPart1(context);
                final long end = System.nanoTime();
                fields.put("result", result != NoChallenge.NO_CHALLENGE ? result : '-');
                fields.put("duration", TimeUnit.NANOSECONDS.toMillis(end - start));
            }
            case 2 -> {
                fields.put("description", challenge.part2Description());
                final long start = System.nanoTime();
                final Object result = challenge.calculateAnswerForPart2(context);
                final long end = System.nanoTime();
                fields.put("result", result != NoChallenge.NO_CHALLENGE ? result : '-');
                fields.put("duration", TimeUnit.NANOSECONDS.toMillis(end - start));
            }
            default -> throw new IllegalArgumentException("Invalid part: '" + part + "'");
        }

        return new JsonObject(fields);
    }

    /*
     * Convert a `Challenge` to a `JsonObject`.
     */
    private static JsonObject toJson(final Challenge<?, ?> obj) {
        final JsonObject object = new JsonObject();
        object.put("year", obj.year());
        object.put("day", obj.day());
        object.put("title", obj.title());
        object.put("part1Description", "<p>" + obj.part1Description());
        object.put("part2Description", "<p>" + obj.part2Description());
        System.out.println(obj.part1Description());
        System.out.println(obj.part2Description());
        return object;
    }

}
