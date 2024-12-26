package net.anomalyxii.aoc.aoc2021;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(19);
    private static final SolutionContext LIVE = SolutionContext.live(2021, 19);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(79L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(326L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(3621L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day19 challenge = new Day19();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(10630L);
    }

    // isPair

    @Test
    void isPair_ExampleScanner0And1() {
        // arrange
        final Day19.Scanner scanner0 = new Day19.Scanner(
                0,
                new Day19.Position(0, 0, 0),
                asList(
                        new Day19.Position(404, -588, -901), // *
                        new Day19.Position(528, -643, 409), // *
                        new Day19.Position(-838, 591, 734),
                        new Day19.Position(390, -675, -793), // *
                        new Day19.Position(-537, -823, -458), // *
                        new Day19.Position(-485, -357, 347), // *
                        new Day19.Position(-345, -311, 381), // *
                        new Day19.Position(-661, -816, -575), // *
                        new Day19.Position(-876, 649, 763),
                        new Day19.Position(-618, -824, -621), // *
                        new Day19.Position(553, 345, -567),
                        new Day19.Position(474, 580, 667),
                        new Day19.Position(-447, -329, 318), // *
                        new Day19.Position(-584, 868, -557),
                        new Day19.Position(544, -627, -890), // *
                        new Day19.Position(564, 392, -477),
                        new Day19.Position(455, 729, 728),
                        new Day19.Position(-892, 524, 684),
                        new Day19.Position(-689, 845, -530),
                        new Day19.Position(423, -701, 434), // *
                        new Day19.Position(7, -33, -71),
                        new Day19.Position(630, 319, -379),
                        new Day19.Position(443, 580, 662),
                        new Day19.Position(-789, 900, -551),
                        new Day19.Position(459, -707, 401)  // *
                )
        );

        final Day19.Scanner scanner1 = new Day19.Scanner(
                1,
                new Day19.Position(0, 0, 0),
                asList(
                        new Day19.Position(686, 422, 578), // *
                        new Day19.Position(605, 423, 415), // *
                        new Day19.Position(515, 917, -361), // *
                        new Day19.Position(-336, 658, 858), // *
                        new Day19.Position(95, 138, 22),
                        new Day19.Position(-476, 619, 847), // *
                        new Day19.Position(-340, -569, -846),
                        new Day19.Position(567, -361, 727),
                        new Day19.Position(-460, 603, -452), // *
                        new Day19.Position(669, -402, 600),
                        new Day19.Position(729, 430, 532), // *
                        new Day19.Position(-500, -761, 534),
                        new Day19.Position(-322, 571, 750), // *
                        new Day19.Position(-466, -666, -811),
                        new Day19.Position(-429, -592, 574),
                        new Day19.Position(-355, 545, -477), // *
                        new Day19.Position(703, -491, -529),
                        new Day19.Position(-328, -685, 520),
                        new Day19.Position(413, 935, -424), // *
                        new Day19.Position(-391, 539, -444), // *
                        new Day19.Position(586, -435, 557),
                        new Day19.Position(-364, -763, -893),
                        new Day19.Position(807, -499, -711),
                        new Day19.Position(755, -354, -619),
                        new Day19.Position(553, 889, -390)  // *
                )
        );

        // act
        final Optional<Day19.Scanner> isPair = Day19.resolve(scanner0, scanner1);

        // assert
        assertThat(isPair)
                .isPresent();
    }

}
