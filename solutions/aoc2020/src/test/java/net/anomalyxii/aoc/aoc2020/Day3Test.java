package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day3Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 3);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 3);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(7);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(209);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(336);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day3 challenge = new Day3();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(1574890240);
    }

    // countTreesOnRoute

    @Test
    void countTreesOnRoute_SomeTreesEncounteredInsideGrid() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.........##.........##.........##.........##.........##.......".chars().toArray(),
                "#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..".chars().toArray(),
                ".#....#..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.".chars().toArray(),
                "..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#".chars().toArray(),
                ".#...##..#..#...##..#..#...##..#..#...##..#..#...##..#..#...##..#.".chars().toArray(),
                "..#.##.......#.##.......#.##.......#.##.......#.##.......#.##.....".chars().toArray(),
                ".#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#".chars().toArray(),
                ".#........#.#........#.#........#.#........#.#........#.#........#".chars().toArray(),
                "#.##...#...#.##...#...#.##...#...#.##...#...#.##...#...#.##...#...".chars().toArray(),
                "#...##....##...##....##...##....##...##....##...##....##...##....#".chars().toArray(),
                ".#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#".chars().toArray()
        });

        // act
        final long count = Day3.countTreesOnRoute(map, 3, 1);

        // assert
        assertThat(count)
                .isEqualTo(7);
    }

    @Test
    void countTreesOnRoute_SomeTreesEncounteredOutsideGrid() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.......".chars().toArray(),
                "#...#...#..".chars().toArray(),
                ".#....#..#.".chars().toArray(),
                "..#.#...#.#".chars().toArray(),
                ".#...##..#.".chars().toArray(),
                "..#.##.....".chars().toArray(),
                ".#.#.#....#".chars().toArray(),
                ".#........#".chars().toArray(),
                "#.##...#...".chars().toArray(),
                "#...##....#".chars().toArray(),
                ".#..#...#.#".chars().toArray()
        });

        // act
        final long count = Day3.countTreesOnRoute(map, 3, 1);

        // assert
        assertThat(count)
                .isEqualTo(7);
    }

    // isTreeAtCoordinate

    @Test
    void isTreeAtCoordinate_NoTree_InsideGrid() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.......,".chars().toArray(),
                "#...#...#..".chars().toArray(),
                ".#....#..#.".chars().toArray(),
                "..#.#...#.#".chars().toArray(),
                ".#...##..#.".chars().toArray(),
                "..#.##.....".chars().toArray(),
                ".#.#.#....#".chars().toArray(),
                ".#........#".chars().toArray(),
                "#.##...#...".chars().toArray(),
                "#...##....#".chars().toArray(),
                ".#..#...#.#".chars().toArray(),
        });

        // act
        final boolean isTree = Day3.isTreeAtCoordinate(map, new Coordinate(0, 0));

        // assert
        assertThat(isTree)
                .isFalse();
    }

    @Test
    void isTreeAtCoordinate_NoTree_OutsideGridX() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.......".chars().toArray(),
                "#...#...#..".chars().toArray(),
                ".#....#..#.".chars().toArray(),
                "..#.#...#.#".chars().toArray(),
                ".#...##..#.".chars().toArray(),
                "..#.##.....".chars().toArray(),
                ".#.#.#....#".chars().toArray(),
                ".#........#".chars().toArray(),
                "#.##...#...".chars().toArray(),
                "#...##....#".chars().toArray(),
                ".#..#...#.#".chars().toArray(),
        });

        // act
        final boolean isTree = Day3.isTreeAtCoordinate(map, new Coordinate(11, 0));

        // assert
        assertThat(isTree)
                .isFalse();
    }

    @Test
    void isTreeAtCoordinate_ATree_InsideGrid() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.......,".chars().toArray(),
                "#...#...#..".chars().toArray(),
                ".#....#..#.".chars().toArray(),
                "..#.#...#.#".chars().toArray(),
                ".#...##..#.".chars().toArray(),
                "..#.##.....".chars().toArray(),
                ".#.#.#....#".chars().toArray(),
                ".#........#".chars().toArray(),
                "#.##...#...".chars().toArray(),
                "#...##....#".chars().toArray(),
                ".#..#...#.#".chars().toArray(),
        });

        // act
        final boolean isTree = Day3.isTreeAtCoordinate(map, new Coordinate(2, 0));

        // assert
        assertThat(isTree)
                .isTrue();
    }

    @Test
    void isTreeAtCoordinate_ATree_OutsideGridX() {
        // arrange
        final Grid map = Grid.repeatingHorizontally(new int[][]{
                "..##.......".chars().toArray(),
                "#...#...#..".chars().toArray(),
                ".#....#..#.".chars().toArray(),
                "..#.#...#.#".chars().toArray(),
                ".#...##..#.".chars().toArray(),
                "..#.##.....".chars().toArray(),
                ".#.#.#....#".chars().toArray(),
                ".#........#".chars().toArray(),
                "#.##...#...".chars().toArray(),
                "#...##....#".chars().toArray(),
                ".#..#...#.#".chars().toArray(),
        });

        // act
        final boolean isTree = Day3.isTreeAtCoordinate(map, new Coordinate(13, 0));

        // assert
        assertThat(isTree)
                .isTrue();
    }


}