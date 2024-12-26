package net.anomalyxii.aoc.annotations;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

/*
 * Holds information related an optimised solution.
 */
record OptimisedMethod(
        Element solutionElement,
        Element optimisedElement,
        TypeMirror returnType
) {

    // ****************************************
    // Helper Methods
    // ****************************************

    /*
     * Get the return type, as an Object, for part 1.
     */
    String returnTypeObject() {
        final String typeString = returnType.toString();
        return typeString.replaceAll("net[.]anomalyxii[.]aoc[.]result[.]", "")
                .replaceAll("java[.]lang[.]", "")
                .replaceAll(",\\s*", ", ");
    }

}
