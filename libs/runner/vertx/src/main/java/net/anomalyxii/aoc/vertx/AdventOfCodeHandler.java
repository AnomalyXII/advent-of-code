package net.anomalyxii.aoc.vertx;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

import java.util.Collections;

/*
 * `Handler` for the `AdventOfCodeVerticle`.
 */
class AdventOfCodeHandler {

    // ****************************************
    // Private Members
    // ****************************************

    private final AdventOfCodeService service;
    private final TemplateEngine templateEngine;

    // ****************************************
    // Constructors
    // ****************************************

    AdventOfCodeHandler(final AdventOfCodeService service, final TemplateEngine templateEngine) {
        this.service = service;
        this.templateEngine = templateEngine;
    }

    // ****************************************
    // Handler Methods
    // ****************************************

    /*
     * Render the index page.
     */
    void index(final RoutingContext context) {
        templateEngine.render(Collections.emptyMap(), "templates/index.html")
                .onSuccess(context::end)
                .onFailure(context::fail);
    }

    /*
     * List all the `Challenge`s available.
     */
    void listAll(final RoutingContext context) {
        service.listAll()
                .onSuccess(context::json)
                .onFailure(context::fail);
    }

    /*
     * List all the `Challenge`s available for a given year.
     */
    void listAllForYear(final RoutingContext context) {
        final String yearStr = context.request().getParam("year");
        if (yearStr == null) {
            context.fail(400);
            return;
        }

        final int year = Integer.parseInt(yearStr);
        service.listAllForYear(year)
                .onSuccess(context::json)
                .onFailure(context::fail);
    }

    /*
     * Display the information of a specific `Challenge`.
     */
    void displayChallengeInfo(final RoutingContext context) {
        final String yearStr = context.request().getParam("year");
        final String dayStr = context.request().getParam("day");
        if (yearStr == null || dayStr == null) {
            context.fail(400);
            return;
        }

        final int year = Integer.parseInt(yearStr);
        final int day = Integer.parseInt(dayStr);
        service.retrieveChallengeInfo(year, day)
                .onSuccess(context::json)
                .onFailure(context::fail);
    }

    /*
     * Solve a specific `Challenge`.
     */
    void solveChallenge(final RoutingContext context) {
        final String yearStr = context.request().getParam("year");
        final String dayStr = context.request().getParam("day");
        final String partStr = context.request().getParam("part");
        if (yearStr == null || dayStr == null || partStr == null) {
            context.fail(400);
            return;
        }

        final int year = Integer.parseInt(yearStr);
        final int day = Integer.parseInt(dayStr);
        final int part = Integer.parseInt(partStr);
        service.solveChallenge(year, day, part)
                .onSuccess(context::json)
                .onFailure(context::fail);
    }

}
