package net.anomalyxii.aoc.utils.geometry;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

class GridTest {

    // ****************************************
    // Test Methods
    // ****************************************

    // Grid.of

    @Test
    void of_CorrectDimensions() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        // act
        final Grid grid = Grid.of(raw);

        // assert
        assertThatObject(grid)
                .returns(4, Grid::width)
                .returns(3, Grid::height)
                .returns(new Coordinate(0, 0), Grid::min)
                .returns(new Coordinate(3, 2), Grid::max);
    }

    @Test
    void of_CorrectValues() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        // act
        final Grid grid = Grid.of(raw);

        // assert
        assertThatObject(grid)
                .returns(0, g -> g.get(new Coordinate(0, 0)))
                .returns(1, g -> g.get(new Coordinate(3, 0)))
                .returns(3, g -> g.get(new Coordinate(1, 2)));
    }

    // forEach

    @Test
    void forEach_AllPointsInArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEach(coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(0, 2),
                        new Coordinate(1, 0),
                        new Coordinate(1, 1),
                        new Coordinate(1, 2),
                        new Coordinate(2, 0),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(3, 0),
                        new Coordinate(3, 1),
                        new Coordinate(3, 2)
                );
    }

    // forEachMatching

    @Test
    void forEachMatching_AllPointsMatch() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachMatching(p -> true, coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(0, 2),
                        new Coordinate(1, 0),
                        new Coordinate(1, 1),
                        new Coordinate(1, 2),
                        new Coordinate(2, 0),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(3, 0),
                        new Coordinate(3, 1),
                        new Coordinate(3, 2)
                );
    }

    @Test
    void forEachMatching_NoPointsMatch() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachMatching(p -> false, coordinates::add);

        // assert
        assertThat(coordinates)
                .isEmpty();
    }

    @Test
    void forEachMatching_SomePointsMatch() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachMatching(p -> p.x() == p.y(), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(1, 1),
                        new Coordinate(2, 2)
                );
    }

    // forEachAdjacentTo

    @Test
    void forEachAdjacentTo_AllPointsInArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachAdjacentTo(new Coordinate(1, 1), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 1),
                        new Coordinate(1, 0),
                        new Coordinate(1, 2),
                        new Coordinate(2, 1)
                );
    }

    @Test
    void forEachAdjacentTo_OnEdgeOfArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachAdjacentTo(new Coordinate(0, 1), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(1, 1),
                        new Coordinate(0, 2)
                );
    }

    @Test
    void forEachAdjacentTo_InCornerOfArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachAdjacentTo(new Coordinate(0, 2), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 1),
                        new Coordinate(1, 2)
                );
    }

    // forEachNeighbourOf

    @Test
    void forEachNeighbourOf_AllPointsInArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachNeighbourOf(new Coordinate(1, 1), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(0, 2),
                        new Coordinate(1, 0),
                        new Coordinate(1, 2),
                        new Coordinate(2, 0),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2)
                );
    }

    @Test
    void forEachNeighbourOf_OnEdgeOfArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachNeighbourOf(new Coordinate(0, 1), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0),
                        new Coordinate(1, 1),
                        new Coordinate(0, 2),
                        new Coordinate(1, 2)
                );
    }

    @Test
    void forEachNeighbourOf_InCornerOfArea() {
        // arrange
        final int[][] raw = {
                new int[]{0, 0, 0, 1},
                new int[]{0, 0, 2, 0},
                new int[]{0, 3, 0, 1},
        };

        final Grid grid = Grid.of(raw);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        grid.forEachNeighbourOf(new Coordinate(0, 2), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 1),
                        new Coordinate(1, 1),
                        new Coordinate(1, 2)
                );
    }

}