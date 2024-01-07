package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2022, 9);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(2022, 9, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2022, 9);
    private static final Coordinate C0_0 = new Coordinate(0, 0);
    private static final Coordinate C0_1 = new Coordinate(0, 1);
    private static final Coordinate C0_2 = new Coordinate(0, 2);
    private static final Coordinate C1_0 = new Coordinate(1, 0);
    private static final Coordinate C1_1 = new Coordinate(1, 1);
    private static final Coordinate C1_2 = new Coordinate(1, 2);
    private static final Coordinate C2_0 = new Coordinate(2, 0);
    private static final Coordinate C2_1 = new Coordinate(2, 1);
    private static final Coordinate C2_2 = new Coordinate(2, 2);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    /*
     * Data provider for testing knot physics.
     */
    private static Stream<Arguments> transformations() {
        return Stream.of(
                // Rope#moveUp
                scenario(r(C0_0, C0_0), r(C0_1, C0_0), Day9.Rope::moveUp, "moveUp : Overlapping"),
                scenario(r(C1_0, C2_0), r(C1_1, C2_0), Day9.Rope::moveUp, "moveUp : Left of Tail"),
                scenario(r(C2_0, C1_0), r(C2_1, C1_0), Day9.Rope::moveUp, "moveUp : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C1_2, C1_1), Day9.Rope::moveUp, "moveUp : Above Tail"),
                scenario(r(C1_0, C1_1), r(C1_1, C1_1), Day9.Rope::moveUp, "moveUp : Below Tail"),
                scenario(r(C1_1, C2_0), r(C1_2, C1_1), Day9.Rope::moveUp, "moveUp : Above Left of Tail"),
                scenario(r(C2_1, C1_0), r(C2_2, C2_1), Day9.Rope::moveUp, "moveUp : Above Right of Tail"),
                scenario(r(C1_0, C2_1), r(C1_1, C2_1), Day9.Rope::moveUp, "moveUp : Below Left of Tail"),
                scenario(r(C2_0, C1_1), r(C2_1, C1_1), Day9.Rope::moveUp, "moveUp : Below Right of Tail"),
                // Rope#moveDown
                scenario(r(C1_1, C1_1), r(C1_0, C1_1), Day9.Rope::moveDown, "moveDown : Overlapping"),
                scenario(r(C1_1, C2_1), r(C1_0, C2_1), Day9.Rope::moveDown, "moveDown : Left of Tail"),
                scenario(r(C2_1, C1_1), r(C2_0, C1_1), Day9.Rope::moveDown, "moveDown : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C1_0, C1_0), Day9.Rope::moveDown, "moveDown : Above Tail"),
                scenario(r(C1_1, C1_2), r(C1_0, C1_1), Day9.Rope::moveDown, "moveDown : Below Tail"),
                scenario(r(C1_1, C2_0), r(C1_0, C2_0), Day9.Rope::moveDown, "moveDown : Above Left of Tail"),
                scenario(r(C2_1, C1_0), r(C2_0, C1_0), Day9.Rope::moveDown, "moveDown : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C1_0, C1_1), Day9.Rope::moveDown, "moveDown : Below Left of Tail"),
                scenario(r(C2_1, C1_2), r(C2_0, C2_1), Day9.Rope::moveDown, "moveDown : Below Right of Tail"),
                // Rope#moveLeft
                scenario(r(C1_1, C1_1), r(C0_1, C1_1), Day9.Rope::moveLeft, "moveLeft : Overlapping"),
                scenario(r(C1_1, C2_1), r(C0_1, C1_1), Day9.Rope::moveLeft, "moveLeft : Left of Tail"),
                scenario(r(C2_1, C1_1), r(C1_1, C1_1), Day9.Rope::moveLeft, "moveLeft : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C0_1, C1_0), Day9.Rope::moveLeft, "moveLeft : Above Tail"),
                scenario(r(C1_1, C1_2), r(C0_1, C1_2), Day9.Rope::moveLeft, "moveLeft : Below Tail"),
                scenario(r(C1_1, C2_0), r(C0_1, C1_1), Day9.Rope::moveLeft, "moveLeft : Above Left of Tail"),
                scenario(r(C2_1, C1_0), r(C1_1, C1_0), Day9.Rope::moveLeft, "moveLeft : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C0_1, C1_1), Day9.Rope::moveLeft, "moveLeft : Below Left of Tail"),
                scenario(r(C2_1, C1_2), r(C1_1, C1_2), Day9.Rope::moveLeft, "moveLeft : Below Right of Tail"),
                // Rope#moveRight
                scenario(r(C1_1, C1_1), r(C2_1, C1_1), Day9.Rope::moveRight, "moveRight : Overlapping"),
                scenario(r(C1_1, C2_1), r(C2_1, C2_1), Day9.Rope::moveRight, "moveRight : Left of Tail"),
                scenario(r(C1_1, C0_1), r(C2_1, C1_1), Day9.Rope::moveRight, "moveRight : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C2_1, C1_0), Day9.Rope::moveRight, "moveRight : Above Tail"),
                scenario(r(C1_1, C1_2), r(C2_1, C1_2), Day9.Rope::moveRight, "moveRight : Below Tail"),
                scenario(r(C1_1, C2_0), r(C2_1, C2_0), Day9.Rope::moveRight, "moveRight : Above Left of Tail"),
                scenario(r(C1_1, C0_0), r(C2_1, C1_1), Day9.Rope::moveRight, "moveRight : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C2_1, C2_2), Day9.Rope::moveRight, "moveRight : Below Left of Tail"),
                scenario(r(C1_1, C0_2), r(C2_1, C1_1), Day9.Rope::moveRight, "moveRight : Below Right of Tail"),
                // Rope#moveUpLeft
                scenario(r(C1_1, C1_1), r(C0_2, C1_1), Day9.Rope::moveUpLeft, "moveUpLeft : Overlapping"),
                scenario(r(C1_1, C2_1), r(C0_2, C1_2), Day9.Rope::moveUpLeft, "moveUpLeft : Left of Tail"),
                scenario(r(C1_1, C0_1), r(C0_2, C0_1), Day9.Rope::moveUpLeft, "moveUpLeft : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C0_2, C0_1), Day9.Rope::moveUpLeft, "moveUpLeft : Above Tail"),
                scenario(r(C1_1, C1_2), r(C0_2, C1_2), Day9.Rope::moveUpLeft, "moveUpLeft : Below Tail"),
                scenario(r(C1_1, C2_0), r(C0_2, C1_1), Day9.Rope::moveUpLeft, "moveUpLeft : Above Left of Tail"),
                scenario(r(C1_1, C0_0), r(C0_2, C0_1), Day9.Rope::moveUpLeft, "moveUpLeft : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C0_2, C1_2), Day9.Rope::moveUpLeft, "moveUpLeft : Below Left of Tail"),
                scenario(r(C1_1, C0_2), r(C0_2, C0_2), Day9.Rope::moveUpLeft, "moveUpLeft : Below Right of Tail"),
                // Rope#moveUpRight
                scenario(r(C1_1, C1_1), r(C2_2, C1_1), Day9.Rope::moveUpRight, "moveUpRight : Overlapping"),
                scenario(r(C1_1, C2_1), r(C2_2, C2_1), Day9.Rope::moveUpRight, "moveUpRight : Left of Tail"),
                scenario(r(C1_1, C0_1), r(C2_2, C1_2), Day9.Rope::moveUpRight, "moveUpRight : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C2_2, C2_1), Day9.Rope::moveUpRight, "moveUpRight : Above Tail"),
                scenario(r(C1_1, C1_2), r(C2_2, C1_2), Day9.Rope::moveUpRight, "moveUpRight : Below Tail"),
                scenario(r(C1_1, C2_0), r(C2_2, C2_1), Day9.Rope::moveUpRight, "moveUpRight : Above Left of Tail"),
                scenario(r(C1_1, C0_0), r(C2_2, C1_1), Day9.Rope::moveUpRight, "moveUpRight : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C2_2, C2_2), Day9.Rope::moveUpRight, "moveUpRight : Below Left of Tail"),
                scenario(r(C1_1, C0_2), r(C2_2, C1_2), Day9.Rope::moveUpRight, "moveUpRight : Below Right of Tail"),
                // Rope#moveDownLeft
                scenario(r(C1_1, C1_1), r(C0_0, C1_1), Day9.Rope::moveDownLeft, "moveDownLeft : Overlapping"),
                scenario(r(C1_1, C2_1), r(C0_0, C1_0), Day9.Rope::moveDownLeft, "moveDownLeft : Left of Tail"),
                scenario(r(C1_1, C0_1), r(C0_0, C0_1), Day9.Rope::moveDownLeft, "moveDownLeft : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C0_0, C1_0), Day9.Rope::moveDownLeft, "moveDownLeft : Above Tail"),
                scenario(r(C1_1, C1_2), r(C0_0, C0_1), Day9.Rope::moveDownLeft, "moveDownLeft : Below Tail"),
                scenario(r(C1_1, C2_0), r(C0_0, C1_0), Day9.Rope::moveDownLeft, "moveDownLeft : Above Left of Tail"),
                scenario(r(C1_1, C0_0), r(C0_0, C0_0), Day9.Rope::moveDownLeft, "moveDownLeft : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C0_0, C1_1), Day9.Rope::moveDownLeft, "moveDownLeft : Below Left of Tail"),
                scenario(r(C1_1, C0_2), r(C0_0, C0_1), Day9.Rope::moveDownLeft, "moveDownLeft : Below Right of Tail"),
                // Rope#moveDownRight
                scenario(r(C1_1, C1_1), r(C2_0, C1_1), Day9.Rope::moveDownRight, "moveDownRight : Overlapping"),
                scenario(r(C1_1, C2_1), r(C2_0, C2_1), Day9.Rope::moveDownRight, "moveDownRight : Left of Tail"),
                scenario(r(C1_1, C0_1), r(C2_0, C1_0), Day9.Rope::moveDownRight, "moveDownRight : Right of Tail"),
                scenario(r(C1_1, C1_0), r(C2_0, C1_0), Day9.Rope::moveDownRight, "moveDownRight : Above Tail"),
                scenario(r(C1_1, C1_2), r(C2_0, C2_1), Day9.Rope::moveDownRight, "moveDownRight : Below Tail"),
                scenario(r(C1_1, C2_0), r(C2_0, C2_0), Day9.Rope::moveDownRight, "moveDownRight : Above Left of Tail"),
                scenario(r(C1_1, C0_0), r(C2_0, C1_0), Day9.Rope::moveDownRight, "moveDownRight : Above Right of Tail"),
                scenario(r(C1_1, C2_2), r(C2_0, C2_1), Day9.Rope::moveDownRight, "moveDownRight : Below Left of Tail"),
                scenario(r(C1_1, C0_2), r(C2_0, C1_1), Day9.Rope::moveDownRight, "moveDownRight : Below Right of Tail")
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(13L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(6090L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1L);
    }

    @Test
    void calculateAnswerForPart2_Example2() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(36L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day9 challenge = new Day9();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(2566L);
    }

    // Rope

    @ParameterizedTest
    @MethodSource("transformations")
    void rope_movementShouldBehaveAccordingToKnotPhysics(final KnotScenario scenario) {
        // arrange
        final Day9.Rope copy = scenario.toInitialRope();

        // act
        scenario.action.accept(copy);

        // assert
        assertThat(copy.getPosition())
                .withFailMessage(() -> "Head was in the wrong position: expected " + scenario.end + " but was " + copy)
                .isEqualTo(scenario.end.getPosition());
        assertThat(copy.getTail().getPosition())
                .withFailMessage(() -> "Tail was in the wrong position: expected " + scenario.end + " but was " + copy)
                .isEqualTo(scenario.end.getTail().getPosition());
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Create a new `KnotScenario`.
     */
    private static Arguments scenario(final Day9.Rope start, final Day9.Rope end, final Consumer<Day9.Rope> action, final String name) {
        return Arguments.of(new KnotScenario(name, start, end, action));
    }

    /*
     * Create a new `Rope` with the given head and tail positions.
     */
    private static Day9.Rope r(final Coordinate head, final Coordinate tail) {
        return new Day9.Rope(head, new Day9.Rope(tail, null));
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Container for the `rope_movementShouldBehaveAccordingToKnotPhysics`
     * tests, specifically for getting a nicer description than just the args
     * listed out.
     */
    private record KnotScenario(String name, Day9.Rope start, Day9.Rope end, Consumer<Day9.Rope> action) {

        // Helper Methods

        /*
         * Create a new `Rope` starting at the initial co-ordinates.
         */
        Day9.Rope toInitialRope() {
            return new Day9.Rope(start.getPosition(), start.getTail());
        }

        // To String

        @Override
        public String toString() {
            return name;
        }

    }

}

