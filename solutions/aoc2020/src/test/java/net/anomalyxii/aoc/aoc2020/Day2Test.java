package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(2020, 2);
    private static final SolutionContext LIVE = SolutionContext.live(2020, 2);

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(2);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(500);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(1);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day2 challenge = new Day2();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(313);
    }

    // isPasswordValid

    @Test
    void isPasswordValidForOldCompany_ValidPassword_CharacterInRange() {
        // arrange
        final String password = "password!";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForOldCompany_ValidPassword_CharacterOnMinimumBoundary() {
        // arrange
        final String password = "past-word!";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForOldCompany_ValidPassword_CharacterOnMaximumBoundary() {
        // arrange
        final String password = "hexagonsarethebestagons!";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForOldCompany_InvalidPassword_CharacterBelowMinimumBoundary() {
        // arrange
        final String password = "totallyunhackable!";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForOldCompany_InvalidPassword_CharacterAboveMaximumBoundary() {
        // arrange
        final String password = "mysneksayshissssssss";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForOldCompany_InvalidPassword_PasswordIsNull() {
        // arrange
        final String password = null;

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForOldCompany_InvalidPassword_PasswordIsBlank() {
        // arrange
        final String password = "";

        // act
        final boolean valid = Day2.isPasswordValidForOldCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    // isPasswordValidForNewCompany

    @Test
    void isPasswordValidForNewCompany_ValidPassword_CharacterIsInFirstPositionOnly() {
        // arrange
        final String password = "password!";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('p', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_CharacterIsInFirstPositionAndNotSecond() {
        // arrange
        final String password = "passport~";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('p', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_CharacterIsInSecondPositionOnly() {
        // arrange
        final String password = "past-word!";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_CharacterIsInSecondPositionAndNotFirst() {
        // arrange
        final String password = "password!";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_PasswordIsShorterThanSecondPosition() {
        // arrange
        final String password = "s!";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_CharacterNeverOccurs() {
        // arrange
        final String password = "totallyunhackable!";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_CharacterOccursInBothFirstAndSecondPosition() {
        // arrange
        final String password = "smsistotallysecure";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_PasswordIsNull() {
        // arrange
        final String password = null;

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_PasswordIsBlank() {
        // arrange
        final String password = "";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_PasswordIsShorterThanFirstPosition() {
        // arrange
        final String password = "s";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 2, 3, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_PasswordIsExactlyTheLengthToFirstPosition() {
        // arrange
        final String password = "s";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 9, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_ValidPassword_PasswordIsExactlyTheLengthToSecondPosition() {
        // arrange
        final String password = "hisssssss";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 9, password);

        // assert
        assertThat(valid)
                .isTrue();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_PasswordIsExactlyTheLengthToFirstPosition() {
        // arrange
        final String password = "s";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('p', 1, 9, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

    @Test
    void isPasswordValidForNewCompany_InvalidPassword_PasswordIsExactlyTheLengthToSecondPosition() {
        // arrange
        final String password = "susssssss";

        // act
        final boolean valid = Day2.isPasswordValidForNewCompany('s', 1, 9, password);

        // assert
        assertThat(valid)
                .isFalse();
    }

}