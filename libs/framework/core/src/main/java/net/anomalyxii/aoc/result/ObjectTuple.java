package net.anomalyxii.aoc.result;

/**
 * A {@link Tuple} that contains two arbitrary objects.
 *
 * @param answer1 the first answer
 * @param answer2 the second answer
 * @param <T1> the type of the first answer
 * @param <T2> the type of the second answer
 */
public record ObjectTuple<T1, T2>(T1 answer1, T2 answer2) implements Tuple<T1, T2> {

    // ****************************************
    // Tuple Methods
    // ****************************************

    @Override
    public T1 getAnswer1() {
        return answer1;
    }

    @Override
    public T2 getAnswer2() {
        return answer2;
    }

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return "%s & %s" .formatted(answer1, answer2);
    }

}
