package net.anomalyxii.aoc.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/*
 * Write a `SolutionsXXXX` class file.
 */
class SolutionsWriter {

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Generate the `SolutionsXXXX` class.
     */
    boolean generateSolutionsClass(
            final ProcessingEnvironment processingEnv,
            final int year,
            final Set<SolutionClass> solutions
    ) {
        try {
            final JavaFileObject newClass = processingEnv.getFiler()
                    .createSourceFile("net.anomalyxii.aoc.Solutions" + year);

            try (Writer writer = newClass.openWriter()) {
                writePackageDeclaration(writer);
                writeBlankLine(writer);

                writeImports(solutions, writer);
                writeBlankLine(writer);

                beginClassDefinition(year, solutions, writer);
                writeSingletonDefinition(year, writer);
                writeSolutionDefinitions(solutions, writer);
                writePublicConstructor(year, writer);
                writeInstanceMethods(solutions, writer);
                endClassDefinition(writer);
            }

            final FileObject resource = processingEnv.getFiler()
                    .createResource(
                            StandardLocation.CLASS_OUTPUT,
                            "",
                            "META-INF/services/net.anomalyxii.aoc.Solutions"
                    );
            try (Writer writer = resource.openWriter()) {
                writer.append("net.anomalyxii.aoc.Solutions")
                        .append(Integer.toString(year))
                        .append('\n');
            }
            return true;
        } catch (final IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Failed to generate Solution file: " + e.getClass() + " => " + e.getMessage());
            return false;
        }
    }

    /*
     * Write a blank line.
     */
    private void writeBlankLine(final Writer writer) throws IOException {
        writer.append('\n');
    }

    /*
     * Write the package definition line.
     */
    private void writePackageDeclaration(final Writer writer) throws IOException {
        writer.append("package net.anomalyxii.aoc;\n");
        writeBlankLine(writer);
    }

    /*
     * Write the import lines.
     */
    private void writeImports(final Set<SolutionClass> solutions, final Writer writer) throws IOException {
        writer.append("import net.anomalyxii.aoc.Challenge;\n");
        writer.append("import net.anomalyxii.aoc.Solutions;\n");
        writer.append("import net.anomalyxii.aoc.SolutionWrapper;\n");
        writer.append("import net.anomalyxii.aoc.context.SolutionContext;\n");
        writer.append("import net.anomalyxii.aoc.result.*;\n");
        for (final SolutionClass solution : solutions) {
            writeImport(writer, solution);
        }
        writeBlankLine(writer);
    }

    /*
     * Write a single import line.
     */
    private void writeImport(final Writer writer, final SolutionClass solution) throws IOException {
        writer.append("import net.anomalyxii.aoc.aoc")
                .append(Integer.toString(solution.year()))
                .append(".Solution")
                .append(Integer.toString(solution.day()))
                .append(";\n");
    }

    /*
     * Write the start of a class definition.
     */
    private void beginClassDefinition(final int year, final Set<SolutionClass> solutions, final Writer writer) throws IOException {
        writer.append("/**\n")
                .append(" * Solutions for Advent of Code ").append(Integer.toString(year)).append(".\n")
                .append(" * <p>\n")
                .append(" * Puzzles this year: \n")
                .append(" * <ul>\n");
        for (final SolutionClass solution : solutions) {
            writer.append(" * <li> <b>Day ")
                    .append(Integer.toString(solution.day()))
                    .append("</b>: ")
                    .append(solution.title())
                    .append(" </li>\n");
        }
        writer.append(" * </ul> \n")
                .append(" */\n");
        writer.append("public class Solutions")
                .append(String.valueOf(year))
                .append(" implements Solutions {\n");
        writeBlankLine(writer);
    }

    /*
     * Write the singleton instance definition.
     */
    private void writeSingletonDefinition(final int year, final Writer writer) throws IOException {
        writer.append("    public static final Solutions AOC_")
                .append(Integer.toString(year))
                .append(" = new Solutions")
                .append(Integer.toString(year))
                .append("();\n");
        writeBlankLine(writer);
    }

    /*
     * Write the private constructor.
     */
    private void writePublicConstructor(final int year, final Writer writer) throws IOException {
        writer.append("    public Solutions")
                .append(Integer.toString(year))
                .append("() {/* Needed for ServiceLoader to work!*/}\n");
        writeBlankLine(writer);
    }

    /*
     * Write the `solutions()`, `challenge()` and `allChallenges()` instance
     * methods.
     */
    private void writeInstanceMethods(final Set<SolutionClass> solutions, final Writer writer) throws IOException {
        writer.write("    @Override\n");
        writer.write("    public SolutionWrapper<?, ?> solutions(final int day) {\n");
        writer.write("        return switch(day) {\n");
        for (final SolutionClass solution : solutions) {
            writer.write("            case ");
            writer.write(Integer.toString(solution.day()));
            writer.write(" -> Solution");
            writer.write(Integer.toString(solution.day()));
            writer.write(".AOC_");
            writer.write(Integer.toString(solution.year()));
            writer.write("_");
            writer.write(Integer.toString(solution.day()));
            writer.write(";\n");
        }
        writer.write("            default -> throw new IllegalArgumentException(\"No solution found for Day \" + day);\n");
        writer.write("        };\n");
        writer.write("    }\n");
        writeBlankLine(writer);

        writer.write("    @Override\n");
        writer.write("    public Challenge<?, ?> challenge(final int day) {\n");
        writer.write("        return switch(day) {\n");
        for (final SolutionClass solution : solutions) {
            writer.write("            case ");
            writer.write(Integer.toString(solution.day()));
            writer.write(" -> DAY_");
            writer.write(Integer.toString(solution.day()));
            writer.write(";\n");
        }
        writer.write("            default -> throw new IllegalArgumentException(\"No solution found for Day \" + day);\n");
        writer.write("        };\n");
        writer.write("    }\n");
        writeBlankLine(writer);

        writer.write("    @Override\n");
        writer.write("    @SuppressWarnings(\"unchecked\")\n");
        writer.write("    public Challenge<?, ?>[] allChallenges() {\n");
        writer.write("        return new Challenge<?, ?>[] {\n");
        int i = 0;
        for (final SolutionClass solution : solutions) {
            if (i % 5 == 0) writer.write("               ");
            writer.write(" DAY_");
            writer.write(Integer.toString(solution.day()));
            writer.write(",");
            if (++i % 5 == 0 || i == solutions.size()) writer.write("\n");
        }
        writer.write("        };\n");
        writer.write("    }\n");
        writeBlankLine(writer);
    }

    /*
     * Write the solution definitions.
     */
    private void writeSolutionDefinitions(final Set<SolutionClass> solutions, final Writer writer) throws IOException {
        for (final SolutionClass solution : solutions) {
            writer.write("    public static final Challenge<?, ?> DAY_");
            writer.write(Integer.toString(solution.day()));
            writer.write(" = ");
            writeSolutionDefinition(writer, solution);
            writer.write("\n");
        }
    }

    /*
     * Write a solution definition.
     */
    private void writeSolutionDefinition(final Writer writer, final SolutionClass solution) throws IOException {
        writer.append(" new Challenge<>(\n")
                .append("            ")
                .append(Integer.toString(solution.year()))
                .append(",\n")
                .append("            ")
                .append(Integer.toString(solution.day()))
                .append(",\n")
                .append("            \"")
                .append(sanitizeJavaDoc(solution.title(), solution.year(), ""))
                .append("\",\n")
                .append("            \"\"\"\n")
                .append("            ")
                .append(sanitizeJavaDoc(solution.part1().puzzleDescription(), solution.year(), "            "))
                .append("\n")
                .append("            \"\"\",\n")
                .append("            \"\"\"\n")
                .append("            ")
                .append(sanitizeJavaDoc(solution.part2().puzzleDescription(), solution.year(), "            "))
                .append("\n")
                .append("            \"\"\",\n")
                .append("            Solution")
                .append(Integer.toString(solution.day()))
                .append(".AOC_")
                .append(Integer.toString(solution.year()))
                .append("_")
                .append(Integer.toString(solution.day()))
                .append("\n")
                .append("    );\n");
    }

    /*
     * Write the end of class definition.
     */
    private void endClassDefinition(final Writer writer) throws IOException {
        writer.append("}");
        writeBlankLine(writer);
    }

    /*
     * Sanitize the JavaDoc string.
     */
    private String sanitizeJavaDoc(final String puzzleDescription, final int year, final String indent) {
        return puzzleDescription
                .replaceAll("href=\"/(.*)\"", "href=\"https://adventofcode.com/$1\"")
                .replaceAll("href=\"([0-9]+)\"", "href=\"https://adventofcode.com/" + year + "/day/$1\"")
                .replaceAll("[\\\\]", "\\\\\\\\")
                .replaceAll("[\"]", "\\\\\"")
                .replaceAll("[\n]", "\n" + indent);
    }

}
