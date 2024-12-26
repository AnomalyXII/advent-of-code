#!/usr/bin/env bash

SCRIPT_DIR="$(realpath -s "$(dirname "$0")")"
WORKING_DIR=$(dirname "${SCRIPT_DIR}")

YEAR=${1:-$(date +"%Y")}
MONTH=${1:-$(date +"%m")}
DAY=${2:-$(date +"%-d")}

if [ "$MONTH" -ne 12 ]
then
  echo 'You should only run this script in December!' >&2
  exit 1
elif [ "$DAY" -gt 25 ]
then
  echo 'You should not run this script after Christmas!' >&2
  exit 1
fi

# Sort out the file paths

MAIN_JAVA_DIR="solutions/aoc${YEAR}/src/main/java/net/anomalyxii/aoc/aoc${YEAR}"
MAIN_JAVA_FILE="${MAIN_JAVA_DIR}/Day${DAY}.java"

MAIN_RESOURCE_DIR="solutions/aoc${YEAR}/src/main/resources/${YEAR}"
MAIN_RESOURCE_FILE="${MAIN_RESOURCE_DIR}/day${DAY}.txt"

TEST_JAVA_DIR="solutions/aoc${YEAR}/src/test/java/net/anomalyxii/aoc/aoc${YEAR}"
TEST_JAVA_FILE="${TEST_JAVA_DIR}/Day${DAY}Test.java"

TEST_RESOURCE_DIR="solutions/aoc${YEAR}/src/test/resources/"
TEST_RESOURCE_FILE="${TEST_RESOURCE_DIR}/day${DAY}-test.txt"

# Create the stuff
{

cd "${WORKING_DIR}" || exit 1

mkdir -p "${MAIN_JAVA_DIR}"
echo "package net.anomalyxii.aoc.aoc${YEAR};

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Day ${DAY}: .
 */
@Solution(year = ${YEAR}, day = ${DAY}, title = \"\")
public class Day${DAY} {

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
     * <p>.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return -
     */
    @Part(part = I)
    public long calculateAnswerForPart1(final SolutionContext context) {
        return 0L;
    }

   /**
      * Solution to part 2.
      *
      * @param context the {@link SolutionContext} to solve against
      * @return the part 2 solution
      */
     @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        return 0L;
    }

}
" > "${MAIN_JAVA_FILE}"

mkdir -p "${MAIN_RESOURCE_DIR}"
touch "${MAIN_RESOURCE_FILE}"

mkdir -p "${TEST_JAVA_DIR}"
echo "package net.anomalyxii.aoc.aoc${YEAR};

import net.anomalyxii.aoc.context.SolutionContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day${DAY}Test {

    private static final SolutionContext EXAMPLE = SolutionContext.example(${DAY});
    private static final SolutionContext LIVE = SolutionContext.live(${YEAR}, ${DAY});

    // ****************************************
    // Test Methods
    // ****************************************

    // calculateAnswerForPart1

    @Test
    void calculateAnswerForPart1_Example() {
        // arrange
        final Day${DAY} challenge = new Day${DAY}();

        // act
        final long answer = challenge.calculateAnswerForPart1(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    @Test
    void calculateAnswerForPart1_Live() {
        // arrange
        final Day${DAY} challenge = new Day${DAY}();

        // act
        final long answer = challenge.calculateAnswerForPart1(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    // calculateAnswerForPart2

    @Test
    void calculateAnswerForPart2_Example() {
        // arrange
        final Day${DAY} challenge = new Day${DAY}();

        // act
        final long answer = challenge.calculateAnswerForPart2(EXAMPLE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

    @Test
    void calculateAnswerForPart2_Live() {
        // arrange
        final Day${DAY} challenge = new Day${DAY}();

        // act
        final long answer = challenge.calculateAnswerForPart2(LIVE);

        // assert
        assertThat(answer)
                .isEqualTo(0L);
    }

}
" > "${TEST_JAVA_FILE}"

# Ensure the .git/hooks exist
[ -e .git/hooks/pre-commit ] || ln -snf ../../.githooks/pre-commit .git/hooks/pre-commit

mkdir -p "${TEST_RESOURCE_DIR}"
touch "${TEST_RESOURCE_FILE}"

}

# Add to git? ... I'll decide if I actually want to do this later :/

# git add "${MAIN_JAVA_FILE}"
# git add "${MAIN_RESOURCE_FILE}"
# git add "${TEST_JAVA_FILE}"
# git add "${TEST_RESOURCE_FILE}"
