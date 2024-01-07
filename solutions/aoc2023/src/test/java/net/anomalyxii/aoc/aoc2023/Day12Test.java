package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.LongTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day12Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2023, 12);
    private static final SolutionContext LIVE = SolutionContext.live(2023, 12);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> examples() {
        return Stream.of(
                Arguments.of("???.### 1,1,3", 1, 1),
                Arguments.of(".??..??...?##. 1,1,3", 4, 16384),
                Arguments.of("?#?#?#?#?#?#?#? 1,3,1,6", 1, 1),
                Arguments.of("????.#...#... 4,1,1", 1, 16),
                Arguments.of("????.######..#####. 1,6,5", 4, 2500),
                Arguments.of("?###???????? 3,2,1", 10, 506250)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(21L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(7653L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(525152L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(60681419004564L);
    }

    // calculateAnswers

    @Test
    void calculateAnswers_Example() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final LongTuple answer = challenge.calculateAnswers(EXAMPLE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(21L);
        assertThat(answer.answer2())
                .isEqualTo(525152L);
    }

    @Test
    void calculateAnswers_Live() {
        // arrange
        final Day12 challenge = new Day12();

        // act
        final LongTuple answer = challenge.calculateAnswers(LIVE);

        // assert
        assertThat(answer.answer1())
                .isEqualTo(7653L);
        assertThat(answer.answer2())
                .isEqualTo(60681419004564L);
    }

    // process

    @Test
    void process_WeirdWildcardString() {
        // arrange
        final String input = "?????????? 1,1,1";

        // act
        final long count = Day12.process(input.split("\\s+", 2));

        // assert
        assertThat(count)
                .isEqualTo(56L);
    }

    @Test
    void process_WeirdWildcardStringWhenExpanded() {
        // arrange
        final String input = "?????????? 1,1,1";

        // act
        final long count = Day12.process(Day12.unfoldLine(input.split("\\s+", 2)));

        // assert
        assertThat(count)
                .isEqualTo(40225345056L);
    }

    @ParameterizedTest
    @MethodSource("examples")
    void process_ExampleStrings(final String input, final long expected, final long unused) {
        // arrange
        // Nothing to do?

        // act
        final long count = Day12.process(input.split("\\s+", 2));

        // assert
        assertThat(count)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("examples")
    void process_ExpandedExampleStrings(final String input, final long unused, final long expected) {
        // arrange

        // act
        final long count = Day12.process(Day12.unfoldLine(input.split("\\s+", 2)));

        // assert
        assertThat(count)
                .isEqualTo(expected);
    }

}

