package net.anomalyxii.aoc.result;

/**
 * A {@link Tuple} that contains two {@code int} answers.
 *
 * @param answer1 the answer for Part I
 * @param answer2 the answer for Part II
 */
public record IntTuple(int answer1, int answer2) implements Tuple<Integer, Integer> {

    /**
     * An {@link IntTuple} with no value for either answer.
     */
    public static final IntTuple NULL = new IntTuple(0, 0);

    // ****************************************
    // Tuple Methods
    // ****************************************

    @Override
    public Integer getAnswer1() {
        return answer1;
    }

    @Override
    public Integer getAnswer2() {
        return answer2;
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Accumulate new data for each answer.
     *
     * @param part1 the amount to add to the answer for part 1
     * @param part2 the amount to add to the answer for part 2
     * @return the new {@link IntTuple}
     */
    public IntTuple add(final int part1, final int part2) {
        if (part1 == 0 && part2 == 0) return this;
        return new IntTuple(answer1 + part1, answer2 + part2);
    }

    /**
     * Accumulate new data for each answer.
     *
     * @param other an {@link IntTuple} containing the amounts to add
     * @return the new {@link IntTuple}
     */
    public IntTuple add(final IntTuple other) {
        if (other.answer1 == 0 && other.answer2 == 0) return this;
        return new IntTuple(answer1 + other.answer1, answer2 + other.answer2);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "%d & %d" .formatted(answer1, answer2);
    }

}
