package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class Day20Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(20);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 20);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(20899048083289L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(54755174472007L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(273);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day20 challenge = new Day20();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1692);
    }

    // findMatchingBorders

    @Test
    void findMatchingBorders_LeftMatchesRight() {
        // arrange
        final Day20.Tile left = Day20.Tile.createTile(
                "Tile 1", new char[][]{
                        ".##.#".toCharArray(),
                        "#...#".toCharArray(),
                        "#...#".toCharArray(),
                        "#....".toCharArray(),
                        ".#..#".toCharArray(),
                }
        );

        final Day20.Tile right = Day20.Tile.createTile(
                "Tile 2", new char[][]{
                        "#.#..".toCharArray(),
                        "#....".toCharArray(),
                        "#...#".toCharArray(),
                        ".....".toCharArray(),
                        "##...".toCharArray(),
                }
        );

        // act
        final Set<Day20.Edge> leftBorders = Day20.findMatchingBorders(left, right);
        final Set<Day20.Edge> rightBorders = Day20.findMatchingBorders(right, left);

        // assert
        assertThat(leftBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.RIGHT);
        assertThat(rightBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.LEFT);
    }

    @Test
    void findMatchingBorders_TopMatchesBottom() {
        // arrange
        final Day20.Tile top = Day20.Tile.createTile(
                "Tile 1", new char[][]{
                        "#.#.#".toCharArray(),
                        ".....".toCharArray(),
                        "#....".toCharArray(),
                        ".....".toCharArray(),
                        "##.#.".toCharArray(),
                }
        );

        final Day20.Tile bottom = Day20.Tile.createTile(
                "Tile 2", new char[][]{
                        "##.#.".toCharArray(),
                        "....#".toCharArray(),
                        "....#".toCharArray(),
                        "#....".toCharArray(),
                        ".#.#.".toCharArray(),
                }
        );

        // act
        final Set<Day20.Edge> topBorders = Day20.findMatchingBorders(top, bottom);
        final Set<Day20.Edge> bottomBorders = Day20.findMatchingBorders(bottom, top);

        // assert
        assertThat(topBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.BOTTOM);
        assertThat(bottomBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.TOP);
    }

    @Test
    void findMatchingBorders_LeftMatchesRight_Flipped() {
        // arrange
        final Day20.Tile left = Day20.Tile.createTile(
                "Tile 1", new char[][]{
                        ".##.#".toCharArray(),
                        "#...#".toCharArray(),
                        "#...#".toCharArray(),
                        "#....".toCharArray(),
                        ".#..#".toCharArray(),
                }
        );

        final Day20.Tile right = Day20.Tile.createTile(
                "Tile 2", new char[][]{
                        "..#.#".toCharArray(),
                        "....#".toCharArray(),
                        "#...#".toCharArray(),
                        ".....".toCharArray(),
                        "...##".toCharArray(),
                }
        );

        // act
        final Set<Day20.Edge> leftBorders = Day20.findMatchingBorders(left, right);
        final Set<Day20.Edge> rightBorders = Day20.findMatchingBorders(right, left);

        // assert
        assertThat(leftBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.RIGHT);
        assertThat(rightBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.RIGHT);
    }

    @Test
    void findMatchingBorders_LeftMatchesRight_Rotated180() {
        // arrange
        final Day20.Tile left = Day20.Tile.createTile(
                "Tile 1", new char[][]{
                        ".##.#".toCharArray(),
                        "#...#".toCharArray(),
                        "#...#".toCharArray(),
                        "#....".toCharArray(),
                        ".#..#".toCharArray(),
                }
        );

        final Day20.Tile right = Day20.Tile.createTile(
                "Tile 2", new char[][]{
                        "...##".toCharArray(),
                        ".....".toCharArray(),
                        "#...#".toCharArray(),
                        "....#".toCharArray(),
                        "..#.#".toCharArray(),
                }
        );

        // act
        final Set<Day20.Edge> leftBorders = Day20.findMatchingBorders(left, right);
        final Set<Day20.Edge> rightBorders = Day20.findMatchingBorders(right, left);

        // assert
        assertThat(leftBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.RIGHT);
        assertThat(rightBorders)
                .isNotEmpty()
                .containsExactly(Day20.Edge.RIGHT);
    }

}