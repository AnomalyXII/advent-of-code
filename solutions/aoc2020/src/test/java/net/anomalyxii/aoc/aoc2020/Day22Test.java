package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class Day22Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(22);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 22);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(306L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(32677L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(291);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day22 challenge = new Day22();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(33661);
    }

    // calculateWinningScore

    @Test
    void calculateWinningScore_ExampleDeck() {
        // arrange
        final Deque<Integer> cards = new ArrayDeque<>(asList(3, 2, 10, 6, 8, 5, 9, 4, 7, 1));

        // act
        final long score = Day22.calculateWinningScore(cards);

        // assert
        assertThat(score)
                .isEqualTo(306);
    }

}