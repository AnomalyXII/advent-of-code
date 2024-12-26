package net.anomalyxii.aoc.aoc2024;

import net.anomalyxii.aoc.annotations.Optimised;
import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.result.IntTuple;
import net.anomalyxii.aoc.utils.geometry.Coordinate;
import net.anomalyxii.aoc.utils.geometry.Grid;
import net.anomalyxii.aoc.utils.geometry.Velocity;

import java.util.concurrent.atomic.AtomicInteger;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2024, Day 4.
 */
@Solution(year = 2024, day = 4, title = "Ceres Search")
public class Day4 {

    /*
     * Search criteria for the XMAS word search.
     */
    private static final char[] SEARCH = new char[]{'X', 'M', 'A', 'S'};

    // ****************************************
    // Challenge Methods
    // ****************************************

    /**
      * Solution to part 1.
      *
      * @param context the {@link SolutionContext} to solve against
      * @return the part 1 solution
      */
     @Part(part = I)
    public int calculateAnswerForPart1(final SolutionContext context) {
        final Grid grid = context.readGrid();
        final AtomicInteger counter = new AtomicInteger(0);
        grid.forEach((px) -> {
            final int vx = grid.get(px);
            if (vx != 'X') return;

            xmasSearch(grid, px, counter);
        });
        return counter.get();
    }

    /**
     * The Elf looks quizzically at you.
     * Did you misunderstand the assignment?
     * <p>
     * Looking for the instructions, you flip over the word search to find that this isn't actually an <code><em>XMAS</em></code> puzzle; it's an <span title="This part originally involved searching for something else, but this joke was too dumb to pass up."><code><em>X-MAS</em></code></span> puzzle in which you're supposed to find two <code>MAS</code> in the shape of an <code>X</code>.
     * One way to achieve that is like this:
     * <pre>
     * M.S
     * .A.
     * M.S
     * </pre>
     * <p>
     * Irrelevant characters have again been replaced with <code>.</code> in the above diagram.
     * Within the <code>X</code>, each <code>MAS</code> can be written forwards or backwards.
     * <p>
     * Here's the same example from before, but this time all of the <code>X-MAS</code>es have been kept instead:
     * <pre>
     * .M.S......
     * ..A..MSMS.
     * .M.S.MAA..
     * ..A.ASMSM.
     * .M.S.M....
     * ..........
     * S.S.S.S.S.
     * .A.A.A.A..
     * M.M.M.M.M.
     * ..........
     * </pre>
     * <p>
     * In this example, an <code>X-MAS</code> appears <code><em>9</em></code> times.
     * <p>
     * Flip the word search from the instructions back over to the word search side and try again.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return How many times does an <code>X-MAS</code> appear?
     */
    @Part(part = II)
    public int calculateAnswerForPart2(final SolutionContext context) {
        final Grid grid = context.readGrid();
        final AtomicInteger counter = new AtomicInteger(0);
        grid.forEach((pa) -> {
            final int va = grid.get(pa);
            if (va != 'A') return;

            crossMasSearch(grid, pa, counter);
        });
        return counter.get();
    }

    // ****************************************
    // Optimised Challenge Methods
    // ****************************************

    /**
     * An optimised solution for parts 1 and 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return an {@link IntTuple} containing the answers for both parts
     */
    @Optimised
    public IntTuple calculateAnswers(final SolutionContext context) {
        final Grid grid = context.readGrid();
        final AtomicInteger part1 = new AtomicInteger(0);
        final AtomicInteger part2 = new AtomicInteger(0);

        grid.forEach((p) -> {
            final int v = grid.get(p);
            if (v == 'X') xmasSearch(grid, p, part1);
            else if (v == 'A') crossMasSearch(grid, p, part2);
        });

        return new IntTuple(part1.get(), part2.get());
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Search for possible occurrences of XMAS.
     */
    private static void xmasSearch(
            final Grid grid,
            final Coordinate start,
            final AtomicInteger counter
    ) {
        Velocity.directions()
                .forEach(direction -> {
                    Coordinate p = start;
                    for (final char search : SEARCH) {
                        if (!grid.contains(p)) return;
                        if (grid.get(p) != search) return;
                        p = p.adjustBy(direction);
                    }

                    counter.incrementAndGet();
                });
    }

    /*
     * Search for a possible occurrence of a MASxMAS.
     */
    private static void crossMasSearch(
            final Grid grid,
            final Coordinate start,
            final AtomicInteger counter
    ) {
        final Coordinate nw = start.adjustBy(Velocity.NORTH_EAST);
        if (!grid.contains(nw)) return;
        final Coordinate se = start.adjustBy(Velocity.SOUTH_WEST);
        if (!grid.contains(se)) return;

        final boolean m1 = (grid.get(nw) == 'M' && grid.get(se) == 'S')
                || (grid.get(nw) == 'S' && grid.get(se) == 'M');
        if (!m1) return;

        final Coordinate ne = start.adjustBy(Velocity.NORTH_WEST);
        if (!grid.contains(ne)) return;
        final Coordinate sw = start.adjustBy(Velocity.SOUTH_EAST);
        if (!grid.contains(sw)) return;

        final boolean m2 = (grid.get(ne) == 'M' && grid.get(sw) == 'S')
                || (grid.get(ne) == 'S' && grid.get(sw) == 'M');
        if (!m2) return;

        counter.incrementAndGet();
    }

}

