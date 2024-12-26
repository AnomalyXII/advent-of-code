package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(21);
    private static final SolutionContext LIVE = SolutionContext.live(2024, 21);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(126384L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(157230L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(154115708116294L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(195969155897936L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(126384L);
        assertThat(answer.answer2())
                .isEqualTo(154115708116294L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day21 challenge = new Day21();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(157230L);
        assertThat(answer.answer2())
                .isEqualTo(195969155897936L);
    }

    // calculateComplexity

    @ParameterizedTest
    @ValueSource(strings = {"029A=178814", "980A=5325320", "179A=1093332", "456A=2773392", "379A=2221698"})
    void calculateComplexity_Example_Depth8(final String input) {
        // arrange
        final String[] parts = input.split("=");
        final String code = parts[0];
        final long expected = Long.parseLong(parts[1]);

        // act
        final long result = Day21.calculateComplexity(code, 8);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"340A=1268816080", "149A=600991798", "582A=2229327720", "780A=2791345440", "463A=1790238578"})
    void calculateComplexity_Live_Depth15(final String input) {
        // arrange
        final String[] parts = input.split("=");
        final String code = parts[0];
        final long expected = Long.parseLong(parts[1]);

        // act
        final long result = Day21.calculateComplexity(code, 15);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"029A=2379451789590", "980A=70797185862200", "179A=14543936021812", "456A=36838581189648", "379A=29556553253044"})
    void calculateComplexity_Example_Depth26(final String input) {
        // arrange
        final String[] parts = input.split("=");
        final String code = parts[0];
        final long expected = Long.parseLong(parts[1]);

        // act
        final long result = Day21.calculateComplexity(code, 26);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"340A=28644350376280", "149A=13567802107652", "582A=50328905710656", "780A=63013362562800", "463A=40414735140548"})
    void calculateComplexity_Live_Depth26(final String input) {
        // arrange
        final String[] parts = input.split("=");
        final String code = parts[0];
        final long expected = Long.parseLong(parts[1]);

        // act
        final long result = Day21.calculateComplexity(code, 26);

        // assert
        assertThat(result)
                .isEqualTo(expected);
    }

}

