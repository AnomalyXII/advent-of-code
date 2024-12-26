package net.anomalyxii.aoc.annotations;

import net.anomalyxii.aoc.annotations.Part.PartNumber;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.util.Arrays.stream;

/**
 * A {@link Processor} that registers an Advent of Code solution.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("net.anomalyxii.aoc.annotations.Solution")
public class SolutionGenerator extends AbstractProcessor {

    private final SolutionWriter solutionWriter = new SolutionWriter();
    private final SolutionsWriter solutionsWriter = new SolutionsWriter();

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
                    return new SolutionPartMethod(
                            part.getEnclosingElement(),
                            ((ExecutableElement) part).getReturnType(),
                            partNumber,
                            puzzleDescription,
                            part
                    );
                })
                .collect(Collectors.groupingBy(
                        SolutionPartMethod::solutionElement,
                        Collectors.toMap(SolutionPartMethod::partNumber, Function.identity())
                ));

        final Map<Element, OptimisedMethod> optimiseds = roundEnv.getElementsAnnotatedWith(Optimised.class).stream()
                .map(optimised -> new OptimisedMethod(
                        optimised.getEnclosingElement(),
                        optimised,
                        ((ExecutableElement) optimised).getReturnType()
                ))
                .collect(Collectors.toMap(
                        OptimisedMethod::solutionElement,
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

        solutions.forEach(solution -> solutionWriter.generateSolutionClass(processingEnv, solution));
        return solutionsWriter.generateSolutionsClass(processingEnv, year, solutions);
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
            if (solution.year() < minYear) minYear = solution.year();
            if (solution.year() > maxYear) maxYear = solution.year();
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
                .map(line -> line.startsWith("@return ") ? "<p>\n" + line.substring(8) : line)
                .filter(line -> !line.startsWith("@"))
                .collect(Collectors.joining("\n"));
    }

}
