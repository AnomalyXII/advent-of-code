package net.anomalyxii.aoc.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

/**
 * The main {@link Verticle} for displaying Advent of Code solutions.
 */
public class AdventOfCodeVerticle extends AbstractVerticle {

    // ****************************************
    // Private Members
    // ****************************************

    private final HttpServer httpServer;

    // ****************************************
    // Constructors
    // ****************************************

    public AdventOfCodeVerticle(final HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    // ****************************************
    // AbstractVerticle Methods
    // ****************************************

    @Override
    public void start(final Promise<Void> startPromise) {
        final AdventOfCodeService serviceProxy = new AsyncAdventOfCodeService(vertx);
        final TemplateEngine templateEngine = ThymeleafTemplateEngine.create(vertx);
        final AdventOfCodeHandler handler = new AdventOfCodeHandler(serviceProxy, templateEngine);

        final Router router = Router.router(vertx);
        router.get("/").handler(handler::index);
        router.get("/favicon.ico").handler(context -> context.fail(404));
        router.get("/all").handler(handler::listAll);
        router.get("/:year").handler(handler::listAllForYear);
        router.get("/:year/:day").handler(handler::displayChallengeInfo);
        router.get("/:year/:day/:part").handler(handler::solveChallenge);

        httpServer.requestHandler(router)
                .listen(8888)
                .onSuccess(nil -> startPromise.complete())
                .onFailure(startPromise::fail);
    }

    @Override
    public void stop(final Promise<Void> stopPromise) {
        httpServer.close()
                .onSuccess(nil -> stopPromise.complete())
                .onFailure(stopPromise::fail);
    }

}
