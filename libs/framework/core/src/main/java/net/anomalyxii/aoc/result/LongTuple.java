package net.anomalyxii.aoc.result;

/**
 * A {@link Tuple} that contains two {@code long} answers.
 *
 * @param answer1 the answer for Part I
 * @param answer2 the answer for Part II
 */
public record LongTuple(long answer1, long answer2) implements Tuple<Long, Long> {

    /**
     * A {@link LongTuple} with no value for either answer.
     */
    public static final LongTuple NULL = new LongTuple(0, 0);

    // ****************************************
    // Tuple Methods
    // ****************************************

    @Override
    public Long getAnswer1() {
        return answer1;
    }

    @Override
    public Long getAnswer2() {
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
     * @return the new {@link LongTuple}
     */
    public LongTuple add(final long part1, final long part2) {
        return new LongTuple(answer1 + part1, answer2 + part2);
    }

    /**
     * Accumulate new data for each answer.
     *
     * @param other a {@link LongTuple} containing the amounts to add
     * @return the new {@link LongTuple}
     */
    public LongTuple add(final LongTuple other) {
        return new LongTuple(answer1 + other.answer1, answer2 + other.answer2);
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "%d & %d" .formatted(answer1, answer2);
    }

}
