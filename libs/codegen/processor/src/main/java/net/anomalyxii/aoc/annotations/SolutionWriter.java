package net.anomalyxii.aoc.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

/*
 * Write a `aocXXXX.SolutionXX` class file.
 */
class SolutionWriter {

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Generate a generic wrapper class for a given day.
     */
    void generateSolutionClass(
            final ProcessingEnvironment processingEnv,
            final SolutionClass solution
    ) {
        try {
            final JavaFileObject newClass = processingEnv.getFiler()
                    .createSourceFile("net.anomalyxii.aoc.aoc" + solution.year() + ".Solution" + solution.day());

            try (Writer writer = newClass.openWriter()) {
                writePackageDeclaration(writer, solution.year());
                writeBlankLine(writer);

                writeImports(solution, writer);
                writeBlankLine(writer);

                beginClassDefinition(solution, writer);

                writeSingletonDefinition(solution, writer);
                writeDayDefinition(solution, writer);

                writePrivateConstructor(solution, writer);
                writeWrapperMethods(solution, writer);

                writeMainMethod(solution, writer);

                endClassDefinition(writer);
            }
        } catch (final IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Failed to generate Solution file: " + e.getClass() + " => " + e.getMessage());

        }
    }

    /*
     * Write a blank line.
     */
    private void writeBlankLine(final Writer writer) throws IOException {
        writer.append('\n');
    }

    /*
     * Write the package definition line for a given year.
     */
    private void writePackageDeclaration(final Writer writer, final int year) throws IOException {
        writer.append("package net.anomalyxii.aoc.aoc").append(Integer.toString(year)).append(";\n");
        writeBlankLine(writer);
    }

    /*
     * Write the import lines.
     */
    private void writeImports(final SolutionClass solution, final Writer writer) throws IOException {
        writer.append("import net.anomalyxii.aoc.SolutionWrapper;\n");
        writer.append("import net.anomalyxii.aoc.context.SolutionContext;\n");
        writer.append("import net.anomalyxii.aoc.result.*;\n");
        writeBlankLine(writer);

        writer.append("import java.nio.file.Files;\n");
        writer.append("import java.nio.file.Path;\n");
        writeBlankLine(writer);

        if (!solution.packageElement().getQualifiedName().toString().equals("net.anomalyxii.aoc.aoc" + solution.year())) {
            writer.append("import ").append(solution.fqn()).append(";\n");
            writeBlankLine(writer);
        }
    }

    /*
     * Write the start of a class definition.
     */
    private void beginClassDefinition(
            final SolutionClass solution,
            final Writer writer
    ) throws IOException {
        writer.append("/**\n")
                .append(" * Solution for Advent of Code ")
                .append(Integer.toString(solution.year()))
                .append(" Day ")
                .append(Integer.toString(solution.day()))
                .append(" (\"")
                .append(solution.title())
                .append("\").\n")
                .append(" */\n");
        writer.append("public class Solution")
                .append(String.valueOf(solution.day()))
                .append(" implements SolutionWrapper<")
                .append(solution.part1().returnTypeObject())
                .append(", ")
                .append(solution.part2().returnTypeObject())
                .append("> {\n");
        writeBlankLine(writer);
    }

    /*
     * Write the singleton instance definition.
     */
    private void writeSingletonDefinition(final SolutionClass solution, final Writer writer) throws IOException {
        writer.append("    public static final Solution")
                .append(Integer.toString(solution.day()))
                .append(" AOC_")
                .append(Integer.toString(solution.year()))
                .append("_")
                .append(Integer.toString(solution.day()))
                .append(" = new Solution")
                .append(Integer.toString(solution.day()))
                .append("();\n");
        writeBlankLine(writer);
    }

    /*
     * Write the solution definitions.
     */
    private void writeDayDefinition(final SolutionClass solution, final Writer writer) throws IOException {
        writer.write("    private static final Day");
        writer.write(Integer.toString(solution.day()));
        writer.write(" DAY = new Day");
        writer.write(Integer.toString(solution.day()));
        writer.write("();\n");
        writeBlankLine(writer);
    }

    /*
     * Write the private constructor.
     */
    private void writePrivateConstructor(final SolutionClass solution, final Writer writer) throws IOException {
        writer.append("    private Solution")
                .append(Integer.toString(solution.day()))
                .append("() {}\n");
        writeBlankLine(writer);
    }

    /*
     * Write the solution definitions.
     */
    private void writeWrapperMethods(final SolutionClass solution, final Writer writer) throws IOException {
        writer.write("    @Override\n");
        writer.write("    public boolean hasOptimisedSolution() {\n");
        writer.write("        return ");
        writer.write(solution.optimised() == null ? "false" : "true");
        writer.write(";\n");
        writer.write("    }\n");
        writeBlankLine(writer);
        writer.write("    @Override\n");
        writer.write("    public ");
        writer.write(solution.part1().returnTypeObject());
        writer.write(" calculateAnswerForPart1(final SolutionContext context) {\n");
        writer.write("        return DAY.");
        writer.write(solution.part1().partElement().getSimpleName().toString());
        writer.write("(context);\n");
        writer.write("    }\n");
        writeBlankLine(writer);
        writer.write("    @Override\n");
        writer.write("    public ");
        writer.write(solution.part2().returnTypeObject());
        writer.write(" calculateAnswerForPart2(final SolutionContext context) {\n");
        writer.write("        return DAY.");
        writer.write(solution.part2().partElement().getSimpleName().toString());
        writer.write("(context);\n");
        writer.write("    }\n");
        writeBlankLine(writer);
        writer.write("    @Override\n");
        if (solution.optimised() != null) {
            writer.write("    public ");
            writer.write(solution.optimised().returnTypeObject());
            writer.write(" calculateAnswers(final SolutionContext context) {\n");
            writer.write("        return DAY.");
            writer.write(solution.optimised().optimisedElement().getSimpleName().toString());
            writer.write("(context);\n");
            writer.write("    }\n");
        } else {
            writer.write("    public Tuple<");
            writer.write(solution.part1().returnTypeObject());
            writer.write(",");
            writer.write(solution.part2().returnTypeObject());
            writer.write("> calculateAnswers(final SolutionContext context) {\n");
            writer.write("        return new ObjectTuple<>(\n");
            writer.write("            DAY.");
            writer.write(solution.part1().partElement().getSimpleName().toString());
            writer.write("(context),\n");
            writer.write("            DAY.");
            writer.write(solution.part2().partElement().getSimpleName().toString());
            writer.write("(context)\n");
            writer.write("        );\n");
            writer.write("    }\n");
        }
        writeBlankLine(writer);
    }

    /*
     * Write the "psvm"` method.
     */
    private void writeMainMethod(final SolutionClass solution, final Writer writer) throws IOException {
        writer.append("    public static void main(final String... args) {\n")
                .append("        if (args.length != 1) {\n")
                .append("            System.err.println(\"Usage: java net.anomalyxii.aoc.aoc")
                .append(Integer.toString(solution.year()))
                .append(".Solution")
                .append(Integer.toString(solution.day()))
                .append(" <input>\");\n")
                .append("            System.exit(1);\n")
                .append("            return;\n")
                .append("        }\n")
                .append("\n")
                .append("        final String input = args[0];\n")
                .append("        final Path inputPath = Path.of(input).toAbsolutePath();\n")
                .append("        if (!Files.exists(inputPath)) {\n")
                .append("            System.err.printf(\"Failed to find challenge input: %s%n\", input);\n")
                .append("            System.exit(2);\n")
                .append("            return;\n")
                .append("        }\n")
                .append("\n")
                .append("        final SolutionContext context = SolutionContext.builder().path(inputPath.toString()).build();\n")
                .append("\n")
                .append("        final Tuple<?, ?> results = AOC_")
                .append(Integer.toString(solution.year()))
                .append("_")
                .append(Integer.toString(solution.day()))
                .append(".calculateAnswers(context);\n")
                .append("        System.out.print(\"Part 1:\");\n")
                .append("        System.out.println(results.getAnswer1());\n")
                .append("\n")
                .append("        System.out.print(\"Part 2:\");\n")
                .append("        System.out.println(results.getAnswer2());\n")
                .append("    }\n");
        writeBlankLine(writer);
    }

    /*
     * Write the end of class definition.
     */
    private void endClassDefinition(final Writer writer) throws IOException {
        writer.append("}");
        writeBlankLine(writer);
    }

}
