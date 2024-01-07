package net.anomalyxii.aoc.annotations;

import net.anomalyxii.aoc.annotations.Part.PartNumber;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.util.Arrays.stream;

/**
 * An {@link Processor} that registers an Advent of Code solution.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("net.anomalyxii.aoc.annotations.Solution")
public class SolutionGenerator extends AbstractProcessor {

    // ****************************************
    // AnnotationProcessor Methods
    // ****************************************

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Map<Element, Map<PartNumber, SolutionPartMethod>> parts = roundEnv.getElementsAnnotatedWith(Part.class).stream()
                .map(part -> {
                    final Part annotation = part.getAnnotation(Part.class);
                    final PartNumber partNumber = annotation.part();
                    final String comment = processingEnv.getElementUtils().getDocComment(part);
                    final String puzzleDescription = sanitisePuzzleDescription(comment);
                    return new SolutionPartMethod(part.getEnclosingElement(), partNumber, puzzleDescription, part);
                })
                .collect(Collectors.groupingBy(
                        part -> part.solutionElement,
                        Collectors.toMap(part -> part.partNumber, Function.identity())
                ));

        final Map<Element, OptimisedMethod> optimiseds = roundEnv.getElementsAnnotatedWith(Optimised.class).stream()
                .map(optimised -> new OptimisedMethod(optimised.getEnclosingElement(), optimised))
                .collect(Collectors.toMap(
                        optimised -> optimised.solutionElement,
                        Function.identity()
                ));

        final Set<SolutionClass> solutions = roundEnv.getElementsAnnotatedWith(Solution.class).stream()
                .map(element -> {
                    final Solution annotation = element.getAnnotation(Solution.class);
                    final PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(element);

                    // Find corresponding `@Part`...
                    Map<PartNumber, SolutionPartMethod> elParts = parts.get(element);
                    if (elParts == null) {
                        elParts = Collections.emptyMap();
                        processingEnv.getMessager().printMessage(Kind.ERROR, "Could not find any parts", element);
                    }

                    final SolutionPartMethod part1 = elParts.get(PartNumber.I);
                    if (part1 == null) processingEnv.getMessager().printMessage(Kind.ERROR, "Missing part I", element);
                    final SolutionPartMethod part2 = elParts.get(PartNumber.II);
                    if (part2 == null) processingEnv.getMessager().printMessage(Kind.ERROR, "Missing part II", element);

                    final OptimisedMethod optimised = optimiseds.get(element);

                    return new SolutionClass(
                            annotation.year(),
                            annotation.day(),
                            annotation.title(),
                            packageElement,
                            element,
                            part1,
                            part2,
                            optimised
                    );
                })
                .collect(Collectors.toCollection(TreeSet::new));

        if (solutions.isEmpty())
            return false;

        final int year = validateChallengeYear(solutions);
        if (year == -1) return false; // :(

        try {
            final JavaFileObject newClass = processingEnv.getFiler().createSourceFile("net.anomalyxii.aoc.Solutions" + year);
            try (Writer writer = newClass.openWriter()) {
                writePackageDeclaration(writer);
                writeBlankLine(writer);

                writeImports(solutions, writer);
                writeBlankLine(writer);

                beginClassDefinition(year, solutions, writer);
                writeSingletonDefinition(year, writer);
                writeSolutionDefinitions(solutions, writer);
                endClassDefinition(writer);
            }

            final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/net.anomalyxii.aoc.Solutions");
            try (Writer writer = resource.openWriter()) {
                writer.append("net.anomalyxii.aoc.Solutions").append(Integer.toString(year)).append('\n');
            }
        } catch (final IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Failed to generate Solution file: " + e.getClass() + " => " + e.getMessage());
            return false;
        }

        return true;
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Validate that all solutions are in the same year.
     */
    private int validateChallengeYear(final Set<SolutionClass> solutions) {
        // Sanity check the year inputs...
        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;
        for (final SolutionClass solution : solutions) {
            if (solution.year < minYear) minYear = solution.year;
            if (solution.year > maxYear) maxYear = solution.year;
        }

        if (minYear != maxYear) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "All solutions must be in the same year!");
            return -1;
        }

        return minYear;
    }

    /*
     * Sanitise the puzzle description extracted from an `Element`'s JavaDoc.
     *
     * TODO: maybe this should convert the HTML into Markdown?
     */
    private String sanitisePuzzleDescription(final String comment) {
        final String[] lines = comment.split("\n");
        final boolean[] preTrimHackEwWhat = {true};
        return stream(lines)
                .peek(line -> {
                    if (line.contains("<pre>")) preTrimHackEwWhat[0] = false;
                    if (line.contains("</pre>")) preTrimHackEwWhat[0] = true;
                })
                .map(s -> preTrimHackEwWhat[0] ? s.trim() : s.substring(min(s.length(), 1)))
                .map(line -> line.startsWith("@return ") ? line.substring(8) : line)
                .filter(line -> !line.startsWith("@"))
                .collect(Collectors.joining("\n"));
    }

    /*
     * Write a blank line.
     */
    private void writeBlankLine(final Writer writer) throws IOException {
        writer.append('\n');
    }

    /*
     * write the package definition line.
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
        writer.append("import ")
                .append(solution.fqn())
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
                    .append(Integer.toString(solution.day))
                    .append("</b>: ")
                    .append(solution.title)
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
     * Write the solution definitions.
     */
    private void writeSolutionDefinitions(final Set<SolutionClass> solutions, final Writer writer) throws IOException {
        for (final SolutionClass solution : solutions) {
            writer.write("    public static final Challenge<?, ?> DAY_");
            writer.write(Integer.toString(solution.day));
            writer.write(" = ");
            writeSolutionDefinition(writer, solution);
            writer.write("\n");
        }

        writer.append("    @Override\n");
        writer.append("    @SuppressWarnings(\"unchecked\")\n");
        writer.append("    public Challenge<?, ?>[] allChallenges() {\n");
        writer.write("        return new Challenge<?, ?>[] {\n");
        for (final SolutionClass solution : solutions) {
            writer.write("                DAY_");
            writer.write(Integer.toString(solution.day));
            writer.write(",\n");
        }
        writer.append("        };\n");
        writer.append("    }\n");
        writeBlankLine(writer);
    }

    /*
     * Write a solution definition.
     */
    private void writeSolutionDefinition(final Writer writer, final SolutionClass solution) throws IOException {
        writer.append(" new Challenge<>(\n")
                .append("            ")
                .append(Integer.toString(solution.year))
                .append(",\n")
                .append("            ")
                .append(Integer.toString(solution.day))
                .append(",\n")
                .append("            \"")
                .append(makeJavaStringSafe(solution.title))
                .append("\",\n")
                .append("            \"")
                .append(makeJavaStringSafe(solution.part1.puzzleDescription))
                .append("\",\n")
                .append("            \"")
                .append(makeJavaStringSafe(solution.part2.puzzleDescription))
                .append("\",\n")
                .append("            sc -> new ")
                .append(solution.solutionElement.getSimpleName())
                .append("().")
                .append(solution.part1.partElement.getSimpleName())
                .append("(sc),\n")
                .append("            sc -> new ")
                .append(solution.solutionElement.getSimpleName())
                .append("().")
                .append(solution.part2.partElement.getSimpleName())
                .append("(sc),\n");
        if (solution.optimised != null) {
            writer.append("            sc -> new ")
                    .append(solution.solutionElement.getSimpleName())
                    .append("().")
                    .append(solution.optimised.optimisedElement.getSimpleName())
                    .append("(sc)\n");
        } else {
            writer.append("            null\n");
        }
        writer.append("    );\n");
    }

    private String makeJavaStringSafe(final String puzzleDescription) {
        return puzzleDescription
                .replaceAll("[\\\\]", "\\\\\\\\")
                .replaceAll("[\"]", "\\\\\"")
                .replaceAll("[\n]", "\\\\n");
    }

    /*
     * Write the end of class definition.
     */
    private void endClassDefinition(final Writer writer) throws IOException {
        writer.append("}");
        writeBlankLine(writer);
    }

    // ****************************************
    // Helper Classes
    // ****************************************

    /*
     * Holds information related to an Advent of Code solution.
     */
    private record SolutionClass(
            int year,
            int day,
            String title,
            PackageElement packageElement,
            Element solutionElement,
            SolutionPartMethod part1,
            SolutionPartMethod part2,
            OptimisedMethod optimised
    ) implements Comparable<SolutionClass> {

        private static final Comparator<SolutionClass> COMPARATOR = Comparator
                .comparing((SolutionClass sc) -> sc.year)
                .thenComparing(sc -> sc.day);

        // Helper Methods

        /*
         * The fully-qualified name of the solution class.
         */
        String fqn() {
            return !packageElement.isUnnamed()
                    ? packageElement.getQualifiedName() + "." + solutionElement.getSimpleName().toString()
                    : solutionElement.getSimpleName().toString();
        }

        // Comparable Methods

        @Override
        public int compareTo(final SolutionClass o) {
            return COMPARATOR.compare(this, o);
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final SolutionClass that = (SolutionClass) o;
            return year == that.year && day == that.day && packageElement.equals(that.packageElement) && solutionElement.equals(that.solutionElement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, day, packageElement, solutionElement);
        }

    }

    /*
     * Holds information related an individual part of a solution.
     */
    private record SolutionPartMethod(
            Element solutionElement,
            PartNumber partNumber,
            String puzzleDescription,
            Element partElement
    ) {

    }

    /*
     * Holds information related an optimised solution.
     */
    private record OptimisedMethod(
            Element solutionElement,
            Element optimisedElement
    ) {

    }

}
