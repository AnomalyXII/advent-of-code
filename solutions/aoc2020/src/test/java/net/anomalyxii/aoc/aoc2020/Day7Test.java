package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class Day7Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 7);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 7);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(144);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(32);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day7 challenge = new Day7();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(5956);
    }

    // parseBagSpec

    @Test
    void parseBagSpec_EmptyBag() {
        // arrange
        final String spec = "dotted black bags contain no other bags.";

        // act
        final Day7.BagSpec result = Day7.parseBagSpec(spec);

        // assert
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("myColour", "dotted black")
                .hasFieldOrPropertyWithValue("childBags", Collections.emptyMap());
    }

    @Test
    void parseBagSpec_ContainsOneOtherTypeOfBag() {
        // arrange
        final String spec = "bright white bags contain 1 shiny gold bag.";

        // act
        final Day7.BagSpec result = Day7.parseBagSpec(spec);

        // assert
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("myColour", "bright white")
                .hasFieldOrPropertyWithValue("childBags", Collections.singletonMap("shiny gold", 1));
    }

    @Test
    void parseBagSpec_ContainsMultipleOtherTypeOfBags() {
        // arrange
        final String spec = "dark orange bags contain 3 bright white bags, 4 muted yellow bags.";

        // act
        final Day7.BagSpec result = Day7.parseBagSpec(spec);

        // assert
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("myColour", "dark orange")
                .hasFieldOrPropertyWithValue("childBags", Map.of("bright white", 3, "muted yellow", 4));
    }

}