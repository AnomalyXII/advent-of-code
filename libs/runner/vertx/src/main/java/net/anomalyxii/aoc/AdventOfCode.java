package net.anomalyxii.aoc;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import net.anomalyxii.aoc.vertx.AdventOfCodeVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run all the advent of code challenges.
 */
public class AdventOfCode {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode.class);

    // ****************************************
    // Main Method
    // ****************************************

    /**
     * Run the Advent of Code solutions.
     *
     * @param args any command line arguments
     */
    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        final AdventOfCodeVerticle verticle = new AdventOfCodeVerticle(vertx.createHttpServer());
        vertx.deployVerticle(verticle, new DeploymentOptions())
                .onSuccess(id -> LOGGER.info("Deployed AdventOfCodeVerticle with deployment ID '{}'", id))
                .onFailure(err -> LOGGER.error("Failed to deploy AdventOfCodeVerticle", err));
    }

}
