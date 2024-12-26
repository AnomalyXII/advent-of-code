package net.anomalyxii.aoc.utils.geometry;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

class AreaTest {

    // ****************************************
    // Test Methods
    // ****************************************

    // Area.of

    @Test
    void of_CorrectDimensions() {
        // arrange
        // Nothing to do... :)

        // act
        final Area area = Area.of(Bounds.of(10, 15), Bounds.of(7, 11));

        // assert
        assertThatObject(area)
                .returns(6, Area::width)
                .returns(5, Area::height)
                .returns(new Coordinate(10, 7), Area::min)
                .returns(new Coordinate(15, 11), Area::max);
    }

    // Area.ofOrigin

    @Test
    void ofOrigin_CorrectDimensions() {
        // arrange
        // Nothing to do... :)

        // act
        final Area area = Area.ofOrigin(7, 9);

        // assert
        assertThatObject(area)
                .returns(8, Area::width)
                .returns(10, Area::height)
                .returns(new Coordinate(0, 0), Area::min)
                .returns(new Coordinate(7, 9), Area::max);
    }
    // forEach

    @Test
    void forEach_AllPointsInArea() {
        // arrange
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEach(coordinates::add);

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
    void forEach_ArbitraryBounds() {
        // arrange
        final Area area = Area.of(Bounds.of(10, 15), Bounds.of(7, 11));
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEach(coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(10, 7),
                        new Coordinate(10, 8),
                        new Coordinate(10, 9),
                        new Coordinate(10, 10),
                        new Coordinate(10, 11),
                        new Coordinate(11, 7),
                        new Coordinate(11, 8),
                        new Coordinate(11, 9),
                        new Coordinate(11, 10),
                        new Coordinate(11, 11),
                        new Coordinate(12, 7),
                        new Coordinate(12, 8),
                        new Coordinate(12, 9),
                        new Coordinate(12, 10),
                        new Coordinate(12, 11),
                        new Coordinate(13, 7),
                        new Coordinate(13, 8),
                        new Coordinate(13, 9),
                        new Coordinate(13, 10),
                        new Coordinate(13, 11),
                        new Coordinate(14, 7),
                        new Coordinate(14, 8),
                        new Coordinate(14, 9),
                        new Coordinate(14, 10),
                        new Coordinate(14, 11),
                        new Coordinate(15, 7),
                        new Coordinate(15, 8),
                        new Coordinate(15, 9),
                        new Coordinate(15, 10),
                        new Coordinate(15, 11)
                );
    }

    // forEachMatching

    @Test
    void forEachMatching_AllPointsMatch() {
        // arrange
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachMatching(p -> true, coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachMatching(p -> false, coordinates::add);

        // assert
        assertThat(coordinates)
                .isEmpty();
    }

    @Test
    void forEachMatching_SomePointsMatch() {
        // arrange
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachMatching(p -> p.x() == p.y(), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachAdjacentTo(new Coordinate(1, 1), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachAdjacentTo(new Coordinate(0, 1), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachAdjacentTo(new Coordinate(0, 2), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachNeighbourOf(new Coordinate(1, 1), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachNeighbourOf(new Coordinate(0, 1), coordinates::add);

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
        final Area area = Area.ofOrigin(3, 2);
        final Set<Coordinate> coordinates = new HashSet<>();

        // act
        area.forEachNeighbourOf(new Coordinate(0, 2), coordinates::add);

        // assert
        assertThat(coordinates)
                .containsExactlyInAnyOrder(
                        new Coordinate(0, 1),
                        new Coordinate(1, 1),
                        new Coordinate(1, 2)
                );
    }

}