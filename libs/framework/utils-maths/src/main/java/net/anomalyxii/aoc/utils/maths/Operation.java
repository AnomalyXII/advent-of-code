package net.anomalyxii.aoc.utils.maths;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

/**
 * A mathematical operation.
 */
public enum Operation implements IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator {

    /**
     * Apply an addition.
     */
    ADD('+') {
        @Override
        public int apply(final int left, final int right) {
            return left + right;
        }

        @Override
        public long apply(final long left, final long right) {
            return left + right;
        }

        @Override
        public double apply(final double left, final double right) {
            return left + right;
        }

        @Override
        public int reverseLeft(final int target, final int right) {
            return target - right;
        }

        @Override
        public long reverseLeft(final long target, final long right) {
            return target - right;
        }

        @Override
        public double reverseLeft(final double target, final double right) {
            return target - right;
        }

        @Override
        public int reverseRight(final int target, final int left) {
            return target - left;
        }

        @Override
        public long reverseRight(final long target, final long left) {
            return target - left;
        }

        @Override
        public double reverseRight(final double target, final double left) {
            return target - left;
        }
    },

    /**
     * Apply a subtraction.
     */
    SUBTRACT('-') {
        @Override
        public int apply(final int left, final int right) {
            return left - right;
        }

        @Override
        public long apply(final long left, final long right) {
            return left - right;
        }

        @Override
        public double apply(final double left, final double right) {
            return left - right;
        }

        @Override
        public int reverseLeft(final int target, final int right) {
            return target + right;
        }

        @Override
        public long reverseLeft(final long target, final long right) {
            return target + right;
        }

        @Override
        public double reverseLeft(final double target, final double right) {
            return target + right;
        }

        @Override
        public int reverseRight(final int target, final int left) {
            return left - target;
        }

        @Override
        public long reverseRight(final long target, final long left) {
            return left - target;
        }

        @Override
        public double reverseRight(final double target, final double left) {
            return left - target;
        }
    },

    /**
     * Apply a multiplication.
     */
    MULTIPLY('*') {
        @Override
        public int apply(final int left, final int right) {
            return left * right;
        }

        @Override
        public long apply(final long left, final long right) {
            return left * right;
        }

        @Override
        public double apply(final double left, final double right) {
            return left * right;
        }

        @Override
        public int reverseLeft(final int target, final int right) {
            return target / right;
        }

        @Override
        public long reverseLeft(final long target, final long right) {
            return target / right;
        }

        @Override
        public double reverseLeft(final double target, final double right) {
            return target / right;
        }

        @Override
        public int reverseRight(final int target, final int left) {
            return target / left;
        }

        @Override
        public long reverseRight(final long target, final long left) {
            return target / left;
        }

        @Override
        public double reverseRight(final double target, final double left) {
            return target / left;
        }
    },

    /**
     * Apply a division.
     */
    DIVIDE('/') {
        @Override
        public int apply(final int left, final int right) {
            return left / right;
        }

        @Override
        public long apply(final long left, final long right) {
            return left / right;
        }

        @Override
        public double apply(final double left, final double right) {
            return left / right;
        }

        @Override
        public int reverseLeft(final int target, final int right) {
            return target * right;
        }

        @Override
        public long reverseLeft(final long target, final long right) {
            return target * right;
        }

        @Override
        public double reverseLeft(final double target, final double right) {
            return target * right;
        }

        @Override
        public int reverseRight(final int target, final int left) {
            return left / target;
        }

        @Override
        public long reverseRight(final long target, final long left) {
            return left / target;
        }

        @Override
        public double reverseRight(final double target, final double left) {
            return left / target;
        }
    },

    // End of constants
    ;

    // ****************************************
    // Private Members
    // ****************************************

    private final char chr;


    // ****************************************
    // Constructors
    // ****************************************

    Operation(final char chr) {
        this.chr = chr;
    }

    // ****************************************
    // IntBinaryOperator Methods
    // ****************************************


    @Override
    public int applyAsInt(final int left, final int right) {
        return apply(left, right);
    }

    // ****************************************
    // LongBinaryOperator Methods
    // ****************************************


    @Override
    public long applyAsLong(final long left, final long right) {
        return apply(left, right);
    }

    // ****************************************
    // DoubleBinaryOperator Methods
    // ****************************************

    @Override
    public double applyAsDouble(final double left, final double right) {
        return apply(left, right);
    }

    // ****************************************
    // Abstract Methods
    // ****************************************

    /**
     * Apply the {@link Operation}.
     *
     * @param left  the left-hand side of the {@link Operation}
     * @param right the right-hand side of the {@link Operation}
     * @return the result
     */
    public abstract int apply(int left, int right);

    /**
     * Apply the {@link Operation}.
     *
     * @param left  the left-hand side of the {@link Operation}
     * @param right the right-hand side of the {@link Operation}
     * @return the result
     */
    public abstract long apply(long left, long right);

    /**
     * Apply the {@link Operation}.
     *
     * @param left  the left-hand side of the {@link Operation}
     * @param right the right-hand side of the {@link Operation}
     * @return the result
     */
    public abstract double apply(double left, double right);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code y} given {@code x} and {@code z}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param right  the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract int reverseLeft(int target, int right);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code y} given {@code x} and {@code z}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param right  the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract long reverseLeft(long target, long right);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code y} given {@code x} and {@code z}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param right  the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract double reverseLeft(double target, double right);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code z} given {@code x} and {@code y}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param left   the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract int reverseRight(int target, int left);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code z} given {@code x} and {@code y}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param left   the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract long reverseRight(long target, long left);

    /**
     * Reverse this operation, given the result and right-hand side.
     * <p>
     * For example, taking {@code x = y + z}, this method will solve for
     * {@code z} given {@code x} and {@code y}.
     *
     * @param target the target result of applying this {@link Operation}
     * @param left   the known right-hand side of the {@link Operation}
     * @return the left-hand side of the {@link Operation}
     */
    public abstract double reverseRight(double target, double left);

    // ****************************************
    // To String
    // ****************************************

    @Override
    public String toString() {
        return Character.toString(chr);
    }

    // ****************************************
    // Static Helper Methods
    // ****************************************

    /**
     * Parse an {@link Operation} from a {@link String}.
     *
     * @param str the operation {@link String}
     * @return the {@link Operation}
     * @throws IllegalArgumentException if a non-operator symbol is provided
     */
    public static Operation fromString(final String str) {
        return switch (str) {
            case "+" -> ADD;
            case "-" -> SUBTRACT;
            case "*" -> MULTIPLY;
            case "/" -> DIVIDE;
            default -> throw new IllegalArgumentException("Invalid operation: " + str);
        };
    }

}
