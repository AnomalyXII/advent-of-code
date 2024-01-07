package net.anomalyxii.aoc.aoc2022;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;
import net.anomalyxii.aoc.utils.geometry.Grid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2022, Day 10.
 */
@Solution(year = 2022, day = 10, title = "Cathode-Ray Tube")
public class Day10 {

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
        final CPU cpu = CPU.create(context);

        final LongAccumulator sum = new LongAccumulator(Long::sum, 0);
        cpu.process((tick, x) -> {
            if ((tick - 20) % 40 == 0)
                sum.accumulate(x * tick);
        });

        return sum.longValue();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public String calculateAnswerForPart2(final SolutionContext context) {
        final CPU cpu = CPU.create(context);
        final CRT display = new CRT(40);

        cpu.process((tick, x) -> {
            final int col = (tick - 1) % 40;

            if (col < cpu.x - 1 || col > cpu.x + 1) display.skipPixel();
            else display.drawPixel();
        });

        return context.ocr().recognise(display.toGrid());
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    /*
     * A consumer for each tick.
     */
    private interface TickConsumer {

        /**
         * A consumer that receives a tick and the current value of the {@code X}
         * register.
         *
         * @param tick the current tick
         * @param x    the value of the {@code X} register
         */
        void accept(int tick, long x);

    }

    /*
     * A processor of `Instruction`s, which can perform an action every tick.
     */
    private static final class CPU {

        // Private Members

        private final Deque<Instruction> instructions;

        private long x = 1;

        // Constructors

        CPU(final Deque<Instruction> instructions) {
            this.instructions = instructions;
        }

        // Public Methods

        /**
         * Run the {@link Instruction instructions}, allowing an
         * {@link TickConsumer action} to be performed each tick, between
         * starting and completing an {@link Instruction}.
         *
         * @param onTick the {@link TickConsumer action} to be performed
         */
        public void process(final TickConsumer onTick) {
            Instruction currentInstruction = null;
            for (int i = 1; !instructions.isEmpty(); i++) {
                if (currentInstruction == null)
                    currentInstruction = instructions.removeFirst();

                onTick.accept(i, x);

                currentInstruction = currentInstruction.onTick(inc -> this.x += inc);
            }
        }

        // Static Helper Methods

        /**
         * Create a new {@link CPU}, reading the {@link Instruction Instructions}
         * from the given {@link SolutionContext}.
         *
         * @param context the {@link SolutionContext} to create from
         * @return the new {@link CPU}
         */
        static CPU create(final SolutionContext context) {
            final List<Instruction> instructions = context.process(Instruction::parse);
            return new CPU(new ArrayDeque<>(instructions));
        }

    }

    /*
     * A display that renders a single pixel each tick.
     */
    private static final class CRT {

        // Private Members

        private final int width;

        private final List<String> rows = new ArrayList<>();
        private final StringBuilder current = new StringBuilder();

        // Constructors

        CRT(final int width) {
            this.width = width;
        }

        // Modifiers

        /**
         * Draw a pixel (i.e make it lit).
         */
        public void drawPixel() {
            render('#');
        }

        /**
         * Skip a pixel (i.e. leave it dark).
         */
        public void skipPixel() {
            render('.');
        }

        // Helper Methods

        /**
         * Convert the rendered pixels in this {@link CRT CRT display} to a
         * {@link Grid}.
         *
         * @return the {@link Grid}
         */
        public Grid toGrid() {
            // Should never call this whilst mid-render, but just in case...
            final List<String> rows = current.isEmpty()
                    ? new ArrayList<>(this.rows)
                    : Stream.concat(this.rows.stream(), Stream.of(current.toString())).toList();

            return Grid.size(width, rows.size(), c -> rows.get(c.y()).charAt(c.x()) == '#' ? 1 : 0);
        }


        // Private Helper Methods

        /*
         * Render a single pixel, either lit (`#`) or dark (`.`).
         *
         * If this is the last pixel on the row, automatically move on to a new
         * row.
         */
        private void render(final char c) {
            current.append(c);
            if (current.length() == width)
                nextRow();
        }

        /*
         * Move the display on to a new row, resetting the pixel back to the
         * first column (i.e. column 0).
         */
        private void nextRow() {
            rows.add(current.toString());
            current.setLength(0);
        }

    }

    /*
     * An instruction to be executed by the `CPU`.
     */
    private abstract static class Instruction {

        // Private Members

        private int remainingCycles;

        // Constructors

        Instruction(final int remainingCycles) {
            this.remainingCycles = remainingCycles;
        }

        // Helper Methods

        public Instruction onTick(final IntConsumer onComplete) {
            --remainingCycles;
            if (remainingCycles == 0) {
                fireOnComplete(onComplete);
                return null;
            }
            return this;
        }

        // Abstract Methods

        protected abstract void fireOnComplete(IntConsumer onComplete);

        // Static Helper Methods

        static Instruction parse(final String line) {
            if (line.equals("noop")) return new NoOp();
            else if (line.startsWith("addx")) return new AddX(Integer.parseInt(line.substring(5)));
            else throw new IllegalStateException("Invalid instruction: '" + line + "'");
        }

    }

    private static final class NoOp extends Instruction {

        // Constructors

        NoOp() {
            super(1);
        }

        // Instruction Methods

        @Override
        protected void fireOnComplete(final IntConsumer onComplete) {

        }

    }

    private static final class AddX extends Instruction {

        // Private Members

        private final int value;

        // Constructors

        AddX(final int value) {
            super(2);
            this.value = value;
        }

        // Instruction Methods

        @Override
        protected void fireOnComplete(final IntConsumer onComplete) {
            onComplete.accept(value);
        }

    }

}

