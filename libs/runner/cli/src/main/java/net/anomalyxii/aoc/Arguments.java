package net.anomalyxii.aoc;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;

import java.util.stream.IntStream;

/**
 * Command line arguments for {@link AdventOfCode}.
 */
public class Arguments {

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Create the {@link AdventOfCode} {@link ArgumentParser}.
     *
     * @return the {@link ArgumentParser}
     */
    public static ArgumentParser create() {

        final ArgumentParser parser = ArgumentParsers.newFor(AdventOfCode.class.getSimpleName()).build()
                .defaultHelp(true)
                .description("Runs the advent of code solutions");

        parser.addArgument("-y", "--year")
                .type(Integer.class)
                .choices(2020, 2021, 2022, 2023)
                .help("Specify a year to run");
        parser.addArgument("-d", "--day")
                .type(Integer.class)
                .choices(IntStream.rangeClosed(1, 25).boxed()
                                 .toArray())
                .help("Specify a day to run");

        return parser;

    }

}
