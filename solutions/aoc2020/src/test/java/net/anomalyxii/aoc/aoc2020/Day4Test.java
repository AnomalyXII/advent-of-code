package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day4Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 4);
    private static final SolutionContext EXAMPLE_2 = SolutionContext.example(2020, 4, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 4);

    // ****************************************
    // Data Provider Methods
    // ****************************************

    private static Stream<Arguments> validData() {
        return Stream.of(
                // Examples
                Arguments.of("byr", "2002"),

                Arguments.of("hgt", "60in"),
                Arguments.of("hgt", "190cm"),

                Arguments.of("hcl", "#123abc"),

                Arguments.of("ecl", "brn"),

                Arguments.of("pid", "000000001"),

                // Edge Cases?
                Arguments.of("byr", "1920"),
                Arguments.of("byr", "1920"),

                Arguments.of("iyr", "2010"),
                Arguments.of("iyr", "2020"),

                Arguments.of("eyr", "2020"),
                Arguments.of("eyr", "2030"),

                Arguments.of("hgt", "150cm"),
                Arguments.of("hgt", "193cm"),
                Arguments.of("hgt", "59in"),
                Arguments.of("hgt", "76in"),

                // hcl?

                Arguments.of("ecl", "blu"),
                Arguments.of("ecl", "gry"),
                Arguments.of("ecl", "grn"),
                Arguments.of("ecl", "hzl"),
                Arguments.of("ecl", "oth"),

                Arguments.of("pid", "000000000"),
                Arguments.of("pid", "999999999")
        );
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                // Examples
                Arguments.of("byr", "2003"),

                Arguments.of("hgt", "190in"),
                Arguments.of("hgt", "190"),

                Arguments.of("hcl", "#123abz"),
                Arguments.of("hcl", "123abc"),

                Arguments.of("ecl", "war"),

                Arguments.of("pid", "0123456789"),

                // Edge Cases?
                Arguments.of("byr", "192"),
                Arguments.of("byr", "20020"),
                Arguments.of("byr", "Nineteen Twenty"),

                Arguments.of("iyr", "2009"),
                Arguments.of("iyr", "2021"),

                Arguments.of("eyr", "2019"),
                Arguments.of("eyr", "2031"),

                Arguments.of("eyr", "2019"),
                Arguments.of("eyr", "2031"),

                Arguments.of("hgt", "149cm"),
                Arguments.of("hgt", "194cm"),
                Arguments.of("hgt", "58in"),
                Arguments.of("hgt", "77in"),

                Arguments.of("pid", "0"),
                Arguments.of("pid", "0000000000")
        );
    }

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(256);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE_2);

        // assert
        assertThat(answer)
                .isEqualTo(4);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day4 challenge = new Day4();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(198);
    }

    // isPassportValid

    @Test
    void isPassportValid_ValidPassport_AllFields() {
        // arrange
        final Map<String, String> passport = new HashMap<>();
        passport.put("ecl", "gry");
        passport.put("pid", "860033327");
        passport.put("eyr", "2020");
        passport.put("hcl", "#fffffd");
        passport.put("byr", "1937");
        passport.put("iyr", "2017");
        passport.put("cid", "147");
        passport.put("hgt", "183cm");

        // act
        final boolean valid = Day4.isPassportValid(passport);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPassportValid_ValidPassport_MissingCidField() {
        // arrange
        final Map<String, String> passport = new HashMap<>();
        passport.put("hcl", "#ae17e1");
        passport.put("iyr", "2013");
        passport.put("eyr", "2024");
        passport.put("ecl", "brn");
        passport.put("pid", "760753108");
        passport.put("byr", "1931");
        passport.put("hgt", "179cm");

        // act
        final boolean valid = Day4.isPassportValid(passport);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPassportValid_InvalidPassport_MissingField() {
        // arrange
        final Map<String, String> passport = new HashMap<>();
        passport.put("iyr", "2013");
        passport.put("ecl", "amb");
        passport.put("cid", "350");
        passport.put("eyr", "2023");
        passport.put("pid", "028048884");
        passport.put("hcl", "#cfa07d");
        passport.put("byr", "1929");

        // act
        final boolean valid = Day4.isPassportValid(passport);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPassportValid_InvalidPassport_MissingFieldAndCid() {
        // arrange
        final Map<String, String> passport = new HashMap<>();
        passport.put("hcl", "#cfa07d");
        passport.put("eyr", "2025");
        passport.put("pid", "166559648");
        passport.put("iyr", "2011");
        passport.put("ecl", "brn");
        passport.put("hgt", "59in");

        // act
        final boolean valid = Day4.isPassportValid(passport);

        // assert
        assertThat(valid)
                .isFalse();
    }

    // validateField

    @ParameterizedTest(name = "{0} => {1}")
    @MethodSource("validData")
    public void validateField_Valid(final String field, final String value) {
        // arrange
        // Nothing to do?

        // act
        final boolean valid = Day4.validateField(field, value);

        // assert
        assertThat(valid)
                .withFailMessage(() -> "Expected '" + field + "' => '" + value + "' to be valid")
                .isTrue();
    }

    @ParameterizedTest(name = "{0} => {1}")
    @MethodSource("invalidData")
    public void validateField_Invalid(final String field, final String value) {
        // arrange
        // Nothing to do?

        // act
        final boolean valid = Day4.validateField(field, value);

        // assert
        assertThat(valid)
                .withFailMessage(() -> "Expected '" + field + "' => '" + value + "' to be invalid")
                .isFalse();
    }

}