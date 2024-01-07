package net.anomalyxii.aoc.vertx;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import net.anomalyxii.aoc.Challenge;

import java.util.List;

/**
 * Retrieve details of Advent of Code {@link Challenge Challenges}.
 */
public interface AdventOfCodeService {

    // ****************************************
    // Interface Methods
    // ****************************************

    /**
     * List all the {@link Challenge Challenges}.
     *
     * @return all {@link Challenge Challenges}
     */
    Future<List<JsonObject>> listAll();

    /**
     * List all the {@link Challenge Challenges} for a given year.
     *
     * @param year the year to find {@link Challenge Challenges} for
     * @return all {@link Challenge Challenges} for the given year
     */
    Future<List<JsonObject>> listAllForYear(int year);

    /**
     * Retrieve a specific {@link Challenge}.
     *
     * @param year the year of the {@link Challenge} to find
     * @param day  the day of the {@link Challenge} to find
     * @return the {@link Challenge} for the given year and day
     */
    Future<JsonObject> retrieveChallengeInfo(int year, int day);

    /**
     * Solve a specific {@link Challenge}.
     *
     * @param year the year of the {@link Challenge} to solve
     * @param day  the day of the {@link Challenge} to solve
     * @param part the part of the {@link Challenge} to solve
     * @return the {@link Challenge}, with solution, for the given year and day
     */
    Future<JsonObject> solveChallenge(int year, int day, int part);

}
