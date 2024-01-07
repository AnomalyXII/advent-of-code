package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Day1Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 1);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 1);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(514579);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(876459);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(241861950);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day1 challenge = new Day1();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(116168640);
    }

    // findProductOf2020Pairs

    @Test
    void findProductOf2020Pairs_ReturnsMatch() {
        // arrange
        final int[] numbers = {1721, 979, 366, 299, 675, 1456};

        // act
        final long match = Day1.findProductOf2020Pairs(numbers);

        // assert
        assertThat(match)
                .isEqualTo(299 * 1721);
    }

    @Test
    void findProductOf2020Pairs_ReturnsFirstMatchIfMultiplePairsMatch() {
        // arrange
        final int[] numbers = {1721, 979, 366, 299, 675, 1456, 1041};

        // act
        final long match = Day1.findProductOf2020Pairs(numbers);

        // assert
        assertThat(match)
                .isEqualTo(299 * 1721);
    }


    @Test
    void findProductOf2020Pairs_ReturnsMatchWithNegativePair() {
        // arrange
        final int[] numbers = {-1, 1, 2, 3, 2020, 2021, 2022};

        // act
        final long match = Day1.findProductOf2020Pairs(numbers);

        // assert
        assertThat(match)
                .isEqualTo(-1 * 2021);
    }

    @Test
    void findProductOf2020Pairs_ThrowsExceptionIfNoPairsFound() {
        // arrange
        final int[] numbers = {1721, 979, 366, 298, 675, 1456};

        // act
        final IllegalStateException error = assertThrows(IllegalStateException.class, () -> Day1.findProductOf2020Pairs(numbers));

        // assert
        assertThat(error)
                .isNotNull(); // Todo: assert error message?
    }

    // findProductOf2020Triples


    @Test
    void findProductOf2020Triples_ReturnsMatch() {
        // arrange
        final int[] numbers = {1721, 979, 366, 299, 675, 1456};

        // act
        final long match = Day1.findProductOf2020Triples(numbers);

        // assert
        assertThat(match)
                .isEqualTo(366 * 675 * 979);
    }

    @Test
    void findProductOf2020Triples_ReturnsFirstMatchIfMultipleTriplesMatch() {
        // arrange
        final int[] numbers = {1721, 979, 366, 299, 675, 1456, 1000, 41};

        // act
        final long match = Day1.findProductOf2020Triples(numbers);

        // assert
        assertThat(match)
                .isEqualTo(41 * 979 * 1000);
    }


    @Test
    void findProductOf2020Triples_ReturnsMatchWithNegativeTriple() {
        // arrange
        final int[] numbers = {-10, 1, 2, 3, 9, 2021, 2022};

        // act
        final long match = Day1.findProductOf2020Triples(numbers);

        // assert
        assertThat(match)
                .isEqualTo(-10 * 9 * 2021);
    }

    @Test
    void findProductOf2020Triples_ThrowsExceptionIfNoTriplesFound() {
        // arrange
        final int[] numbers = {1721, 979, 365, 299, 675, 1456};

        // act
        final IllegalStateException error = assertThrows(IllegalStateException.class, () -> Day1.findProductOf2020Triples(numbers));

        // assert
        assertThat(error)
                .isNotNull(); // Todo: assert error message?
    }

}
