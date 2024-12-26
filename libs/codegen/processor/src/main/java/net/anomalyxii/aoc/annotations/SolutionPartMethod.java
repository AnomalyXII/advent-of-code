package net.anomalyxii.aoc.annotations;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

/*
 * Holds information related an individual part of a solution.
 */
record SolutionPartMethod(
        Element solutionElement,
        TypeMirror returnType,
        Part.PartNumber partNumber,
        String puzzleDescription,
        Element partElement
) {

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Get the return type, as an Object, for part 1.
     */
    String returnTypeObject() {
        final String typeString = returnType.toString();
        return switch (typeString) {
            case "boolean" -> "Boolean";
            case "int" -> "Integer";
            case "long" -> "Long";
            case "short" -> "Short";
            case "byte" -> "Byte";
            case "char" -> "Character";
            case "void" -> "Void";
            default -> typeString.replaceAll("java[.]lang[.]", "");
        };
    }

}
