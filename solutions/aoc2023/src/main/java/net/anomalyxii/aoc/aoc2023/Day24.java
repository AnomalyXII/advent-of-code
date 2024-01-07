package net.anomalyxii.aoc.aoc2023;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.Arrays;
import java.util.List;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;
import static net.anomalyxii.aoc.utils.maths.Factors.highestCommonFactor;

/**
 * Advent of Code 2023, Day 24.
 */
@Solution(year = 2023, day = 24, title = "Never Tell Me The Odds")
public class Day24 {

    private static final long DEFAULT_LOWER_BOUND = 200000000000000L;
    private static final long DEFAULT_UPPER_BOUND = 400000000000000L;

    // ****************************************
    // Private Members
    // ****************************************

    private final long min;
    private final long max;

    // ****************************************
    // Constructors
    // ****************************************

    public Day24() {
        this(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND);
    }

    Day24(final long min, final long max) {
        this.min = min;
        this.max = max;
    }

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
    public long calculateAnswerForPart1(final SolutionContext context) {
        final List<Hailstone> hailstones = context.stream()
                .map(Hailstone::parse)
                .toList();

        int count = 0;
        for (int h1 = 0; h1 < hailstones.size(); h1++)
            for (int h2 = h1 + 1; h2 < hailstones.size(); h2++)
                if (hailstones.get(h1).intersectXY(hailstones.get(h2)).inBoundsXY(min, max))
                    ++count;

        return count;
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<Hailstone> hailstones = context.stream()
                .map(Hailstone::parse)
                .toList();

        final Hailstone rock = calculateRockToThrow(hailstones);
        return rock.pos.x + rock.pos.y + rock.pos.z;
    }

    // ****************************************
    // Private Helper Methods
    // ****************************************

    /*
     * Brute-force (ew) likely velocities to find one that will pass through
     * every `Hailstone`.
     */
    private static Hailstone calculateRockToThrow(final List<Hailstone> hailstones) {
        assert hailstones.size() > 2;

        final Hailstone h1 = hailstones.get(0);
        final Hailstone h2 = hailstones.get(1);
        final Hailstone h3 = hailstones.get(2);

        final Hailstone h2x = h2.sub(h1);
        final Hailstone h3x = h3.sub(h1);

        final LongVector h2nv = h2x.findNormalVector();
        final LongVector h3nv = h3x.findNormalVector();
        final LongVector s = h2nv.crossMultiply(h3nv).scale();

        final long h2t = (h2x.pos.y * s.x - h2x.pos.x * s.y) / (h2x.vel.x * s.y - h2x.vel.y * s.x);
        final long h3t = (h3x.pos.y * s.x - h3x.pos.x * s.y) / (h3x.vel.x * s.y - h3x.vel.y * s.x);
        assert h2t != h3t;

        final long h2ix = h2.pos.x + (h2.vel.x * h2t); // h2 intersect x
        final long h3ix = h3.pos.x + (h3.vel.x * h3t);
        final long h2iy = h2.pos.y + (h2.vel.y * h2t); // h2 intersect y
        final long h3iy = h3.pos.y + (h3.vel.y * h3t);
        final long h2iz = h2.pos.z + (h2.vel.z * h2t); // h2 intersect z
        final long h3iz = h3.pos.z + (h3.vel.z * h3t);

        final long dx = (h3ix - h2ix) / (h3t - h2t); // Rock velocity x
        final long dy = (h3iy - h2iy) / (h3t - h2t); // Rock velocity y
        final long dz = (h3iz - h2iz) / (h3t - h2t); // Rock velocity z

        return new Hailstone(h2ix - (dx * h2t), h2iy - (dy * h2t), h2iz - (dz * h2t), dx, dy, dz);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Hold a vector of 3 `long`s.
     */
    record LongVector(long x, long y, long z) {

        // Helper Methods

        /*
         * Subtract a given `LongVector` from this one.
         */
        LongVector sub(final LongVector o) {
            return new LongVector(
                    x - o.x,
                    y - o.y,
                    z - o.z
            );
        }

        /*
         * Cross-multiply with a given `LongVector`.
         */
        LongVector crossMultiply(final LongVector other) {
            final long x = this.y * other.z - this.z * other.y;
            final long y = this.z * other.x - this.x * other.z;
            final long z = this.x * other.y - this.y * other.x;

            return new LongVector(x, y, z);
        }

        /*
         * Reduce this `LongVector` by calculating the highest common factor of
         * the `x`, `y` and `z` components and scaling them back accordingly.
         */
        LongVector scale() {
            final long hcf = highestCommonFactor(highestCommonFactor(x, y), z);

            final long x = this.x / hcf;
            final long y = this.y / hcf;
            final long z = this.z / hcf;
            return new LongVector(x, y, z);
        }
    }

    /*
     * A Hailstone.
     */
    record Hailstone(LongVector pos, LongVector vel) {

        // Helper Constructor

        Hailstone(final long px, final long py, final long pz, final long vx, final long vy, final long vz) {
            this(new LongVector(px, py, pz), new LongVector(vx, vy, vz));
        }

        // Helper Methods

        /*
         * Find the "normal vector" to this line.
         */
        LongVector findNormalVector() {
            return vel.crossMultiply(pos).scale();
        }

        /*
         * Subtract the position and velocity of a given `Hailstone` from this
         * one.
         */
        Hailstone sub(final Hailstone o) {
            return new Hailstone(pos.sub(o.pos), vel.sub(o.vel));
        }

        /*
         * Check if the path of this `Hailstone` intersects with the path of the
         * given other `Hailstone` considering only the XY trajectory.
         */
        IntersectionPoint intersectXY(final Hailstone other) {
            final long a = vel.x * (other.pos.y - pos.y);
            final long b = vel.y * (other.pos.x - pos.x);
            final long c = vel.y * (other.vel.x);
            final long d = vel.x * (other.vel.y);

            if (c == d) return IntersectionPoint.NULL;

            final double ot = (a - b) / (double) (c - d);
            if (ot < 0) return IntersectionPoint.NULL;

            final double tt = (other.pos.x - pos.x + ot * other.vel.x) / vel.x;
            if (tt < 0) return IntersectionPoint.NULL;

            return IntersectionPoint.of(pos.x + tt * vel.x, pos.y + tt * vel.y, pos.z + tt * vel.z);
        }

        // Static Helper Methods

        /*
         * Parse a `Hailstone` from the given line.
         */
        private static Hailstone parse(final String line) {
            final String[] parts = line.split("\\s*@\\s*", 2);

            final long[] cs = Arrays.stream(parts[0].split(",\\s*")).mapToLong(Long::parseLong).toArray();
            final long[] vs = Arrays.stream(parts[1].split(",\\s*")).mapToLong(Long::parseLong).toArray();

            return new Hailstone(new LongVector(cs[0], cs[1], cs[2]), new LongVector(vs[0], vs[1], vs[2]));
        }
    }

    /*
     * Return the coordinates at which the path of two `Hailstone`s
     * intersects.
     */
    record IntersectionPoint(double x0, double y0, double z0) {

        /*
         * Represents that no `IntersectionPoint` was found.
         */
        static final IntersectionPoint NULL = new IntersectionPoint(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        // Helper Methods

        /*
         * Check if this `IntersectionPoint` is within the given boundary area.
         */
        boolean inBoundsXY(final long min, final long max) {
            return x0 >= min && x0 <= max && y0 >= min && y0 <= max;
        }

        // Static Helper Methods

        /*
         * Return a new `IntersectionPoint` from the given co-ordinates.
         */
        static IntersectionPoint of(final double x0, final double y0, final double z0) {
            if (!Double.isFinite(x0)) return NULL;
            if (!Double.isFinite(y0)) return NULL;
            if (!Double.isFinite(z0)) return NULL;

            return new IntersectionPoint(round(x0), round(y0), round(z0));
        }

        /*
         * Round to a reasonable precision.
         *
         * This makes things easier to test and doesn't _seem_ to affect the
         * results.
         */
        private static double round(final double val) {
            return Math.round(val * 100) / 100d;
        }

    }

}