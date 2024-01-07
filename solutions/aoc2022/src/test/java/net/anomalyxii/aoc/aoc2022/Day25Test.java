package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 25);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 25);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    public static Stream<Arguments> snafu() {
        return Stream.of(
                Arguments.of("1=-0-2", 1747),
                Arguments.of("12111", 906),
                Arguments.of("2=0=", 198),
                Arguments.of("21", 11),
                Arguments.of("2=01", 201),
                Arguments.of("111", 31),
                Arguments.of("20012", 1257),
                Arguments.of("112", 32),
                Arguments.of("1=-1=", 353),
                Arguments.of("1-12", 107),
                Arguments.of("12", 7),
                Arguments.of("1=", 3),
                Arguments.of("122", 37),
                Arguments.of("1", 1),
                Arguments.of("2", 2),
                // Arguments.of("1=", 3),
                Arguments.of("1-", 4),
                Arguments.of("10", 5),
                Arguments.of("11", 6),
                // Arguments.of("12", 7),
                Arguments.of("2=", 8),
                Arguments.of("2-", 9),
                Arguments.of("20", 10),
                Arguments.of("1=0", 15),
                Arguments.of("1-0", 20),
                Arguments.of("1=11-2", 2022),
                Arguments.of("1-0---0", 12345),
                Arguments.of("1121-1110-1=0", 314159265)
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final String answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo("2=-1=0");
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day25 challenge = new Day25();

        // act
        final String answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo("2-=12=2-2-2-=0012==2");
    }


    // snafu2dec

    @ParameterizedTest(name = "\"{0}\" => {1}")
    @MethodSource("snafu")
    public void snafu2dec_ConvertsCorrectly(final String snafu, final long expected) {
        // arrange

        // act
        final long converted = Day25.snafu2dec(snafu);

        // assert
        assertThat(converted)
                .isEqualTo(expected);
    }

    // dec2snafu

    @ParameterizedTest(name = "{1} => \"{0}\"")
    @MethodSource("snafu")
    public void dec2snafu_ConvertsCorrectly(final String expected, final long decimal) {
        // arrange

        // act
        final String converted = Day25.dec2snafu(decimal);

        // assert
        assertThat(converted)
                .isEqualTo(expected);
    }

}

