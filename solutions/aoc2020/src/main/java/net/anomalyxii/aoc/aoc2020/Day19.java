package net.anomalyxii.aoc.aoc2020;

import net.anomalyxii.aoc.annotations.Part;
import net.anomalyxii.aoc.annotations.Solution;
import net.anomalyxii.aoc.context.SolutionContext;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.anomalyxii.aoc.annotations.Part.PartNumber.I;
import static net.anomalyxii.aoc.annotations.Part.PartNumber.II;

/**
 * Advent of Code 2020, Day 19.
 */
@Solution(year = 2020, day = 19, title = "Monster Messages")
public class Day19 {

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
        final List<String> rules = new ArrayList<>();
        final List<String> messages = new ArrayList<>();

        context.consume(line -> {
            if (!line.isBlank()) {
                if (looksLikeRule(line)) {
                    rules.add(line.replace(":", " -> "));
                } else {
                    messages.add(line.replaceAll("(.)", "$1 "));
                }
            }
        });

        final EarleyParser parser = EarleyParser.compile(rules.stream().sorted().toArray(String[]::new));
        return messages.stream()
                .filter(message -> isValid(parser, message))
                .count();
    }

    /**
     * Solution to part 2.
     *
     * @param context the {@link SolutionContext} to solve against
     * @return the part 2 solution
     */
    @Part(part = II)
    public long calculateAnswerForPart2(final SolutionContext context) {
        final List<String> rules = new ArrayList<>();
        final List<String> messages = new ArrayList<>();

        context.consume(line -> {
            if (!line.isBlank()) {
                if (looksLikeRule(line)) {
                    if (line.startsWith("8:")) {
                        rules.add("8 -> 42 | 42 8");
                    } else if (line.startsWith("11:")) {
                        rules.add("11 -> 42 31 | 42 11 31");
                    } else {
                        rules.add(line.replace(":", " -> "));
                    }
                } else {
                    messages.add(line.replaceAll("(.)", "$1 "));
                }
            }
        });

        final EarleyParser parser = EarleyParser.compile(rules.stream().sorted().toArray(String[]::new));
        return messages.stream()
                .filter(message -> isValid(parser, message))
                .count();
    }

    // ****************************************
    // Helper Methods
    // ****************************************

    /**
     * Check if the given message is valid for the specified rule.
     *
     * @param message the message to validate
     * @param rules   an indexed array of rules that may be referenced
     * @return {@literal true} if the message is valid; {@literal false} otherwise
     */
    static boolean isValid(final String message, final String[] rules) {
        final EarleyParser parser = EarleyParser.compile(rules);
        return isValid(parser, message);
    }

    /**
     * Check if the given message is valid for the specified rule.
     *
     * @param message the message to validate
     * @param root    the rule to validate against
     * @param refs    an indexed array of rules that may be referenced
     * @return {@literal true} if the message is valid; {@literal false} otherwise
     */
    static boolean isValid(final String message, final String root, final String[] refs) {
        final String[] rules = new String[refs.length + 1];
        rules[0] = "root -> " + root;
        IntStream.range(0, refs.length)
                .forEach(i -> rules[i + 1] = i + " -> " + refs[i]);

        return isValid(message, rules);
    }

    // ****************************************
    // Private Helper Classes
    // ****************************************

    private static final Pattern RULE_PATTERN = Pattern.compile("^([0-9]+):(.+)$");

    /*
     * Check if the given input looks like a rule.
     */
    private static boolean looksLikeRule(final String line) {
        return RULE_PATTERN.matcher(line).matches();
    }

    /*
     * Check if the given message is valid for the rules; specifically,
     * checking whether the rule is valid for the first rule in the provided
     * array of rules.
     */
    private static boolean isValid(final EarleyParser parser, final String message) {
        return parser.matches(message);
    }

    /*
     * An implementation of the Earley parsing algorithm.
     */
    private record EarleyParser(String root, Map<String, Production[]> productions) {

        // Helper Methods

        /*
         * Check if a given String matches the grammar.
         */
        public boolean matches(final String sentence) {
            // Format the input into the correct shape
            final String[] inputs = sentence.split(" +");
            return matches(inputs);
        }

        /*
         * Check if a given String matches the grammar.
         */
        public boolean matches(final String[] inputs) {
            // INIT(words)
            // S <- CREATE-ARRAY(LENGTH(words) + 1)
            //    for k <- from 0 to LENGTH(words) do
            final StateSet[] s = IntStream.rangeClosed(0, inputs.length)
                    // S[k] <- EMPTY-ORDERED-SET
                    .mapToObj(i -> new StateSet())
                    .toArray(StateSet[]::new);

            // ADD-TO-SET((γ -> •S, 0), S[0])
            s[0].add(new State(new Production("_", new String[]{root}), 0, 0));

            // for k <- from 0 to LENGTH(words) do
            IntStream.rangeClosed(0, inputs.length)
                    .forEachOrdered(k -> {
                        // for each state in S[k] do  // S[k] can expand during this loop
                        s[k].forEachFlux(state -> {
                            // if not FINISHED(state) then
                            if (!state.isFinished()) {
                                // if NEXT-ELEMENT-OF(state) is a nonterminal then
                                if (state.nextTokenIsNonTerminal()) {
                                    // PREDICTOR(state, k, grammar)         // non-terminal
                                    predictor(s, state, k, productions);
                                } else { // else do
                                    // SCANNER(state, k, words)             // terminal
                                    scanner(s, state, k, inputs);
                                }
                            } else { // else do
                                // COMPLETER(state, k)
                                completer(s, state, k);
                            }
                            // end
                        });
                        // end
                    });
            return s[inputs.length].containsCompletedProduction(root);
        }

        /*
         * Perform the predictor process, as defined by the Earley parsing
         * algorithm.
         */
        // procedure PREDICTOR((A → α•Bβ, j), k, grammar)
        private static void predictor(
                final StateSet[] s,
                final State state,
                final int k,
                final Map<String, Production[]> productions
        ) {
            final String token = state.nextToken();
            final Production[] productionsForNonTerminal = productions.get(token);

            // for each (B → γ) in GRAMMAR-RULES-FOR(B, grammar) do
            Stream.of(productionsForNonTerminal)
                    .forEach(p -> {
                        // ADD-TO-SET((B → •γ, k), S[k])
                        final State newState = new State(p, 0, k);
                        s[k].add(newState);
                    });
            // end
        }

        /*
         * Perform the scanner process, as defined by the Earley parsing
         * algorithm.
         */
        // procedure SCANNER((A → α•aβ, j), k, words)
        private static void scanner(final StateSet[] s, final State state, final int k, final String[] inputs) {
            final String input = k < inputs.length ? inputs[k] : null;
            final String token = state.nextToken();
            assert token != null : "Token was null!";

            // if a ⊂ PARTS-OF-SPEECH(words[k]) then
            if (terminalMatchesWord(input, token)) {
                // ADD-TO-SET((A → αa•β, j), S[k+1])
                final State newState = new State(state.production, state.position + 1, state.origin);
                s[k + 1].add(newState);
            }
            // end
        }

        /*
         * Perform the completer process, as defined by the Earley parsing
         * algorithm.
         */
        // procedure COMPLETER((B → γ•, x), k)
        private static void completer(final StateSet[] s, final State state, final int k) {
            // for each (A → α•Bβ, j) in S[x] do
            s[state.origin].forEachFlux(_state -> {
                if (state.production.id.equals(_state.nextToken())) {
                    // ADD-TO-SET((A → αB•β, j), S[k])
                    final State newState = new State(_state.production, _state.position + 1, _state.origin);
                    s[k].add(newState);
                }
            });
            // end
        }

        /*
         * Check if the given literal (non-terminal) token matches the given
         * input word.
         *
         * Since this is dealing with literals, we can simply strip the \"\"s off
         * the token and do a direct `String` comparison?
         */
        private static boolean terminalMatchesWord(final String input, final String token) {
            return token.substring(1, token.length() - 1).equals(input);
        }

        /*
         * Compile the given grammar rules into a format that can be used by this
         * Earley parser.
         *
         * Rules should be in the form: `A -> x y | "z"`.
         */
        private static EarleyParser compile(final String[] rules) {
            String root = null;
            final Map<String, Production[]> productions = new TreeMap<>();
            for (final String rule : rules) {
                final String[] parts = rule.split(" *-> *");
                final String id = parts[0];
                if (root == null) {
                    // For simplicity, assume the root rule is the first one defined...
                    root = id;
                }

                final String pattern = parts[1];
                final Production[] disjunctions = Stream.of(pattern.split(" *[|] *"))
                        .map(disjunction -> new Production(id, disjunction.split(" +")))
                        .toArray(Production[]::new);

                productions.put(id, disjunctions);
            }

            return new EarleyParser(root, productions);
        }

    }

    /*
     * A production, as defined by the Earley parsing algorithm.
     */
    private record Production(String id, String[] tokens) {

        // To String

        @Override
        public String toString() {
            return "(" + id + " -> " + String.join(" ", tokens) + ")";
        }

    }

    /*
     * A state in the Earley parsing process.
     *
     * In practice, this is a tuple of:
     *   - a production;
     *   - the current position within that production to match against;
     *   - the original offset that this production matched from.
     */
    private static final class State {

        // Private Members

        private final Production production;
        private final int position;
        private final int origin;

        private final String rep;

        // Constructors

        State(final Production production, final int position, final int origin) {
            this.production = production;
            this.position = position;
            this.origin = origin;

            final StringBuilder repBuilder = new StringBuilder(production.id);
            repBuilder.append(" -> ");
            for (int i = 0; i <= production.tokens.length; i++) {
                if (i == position) {
                    repBuilder.append("•");
                }
                if (i < production.tokens.length) {
                    repBuilder.append(production.tokens[i]);
                }
            }
            repBuilder.append(", ");
            repBuilder.append(origin);
            this.rep = repBuilder.toString();
        }

        // Helper Methods

        /*
         * Check if this production has finished.
         */
        public boolean isFinished() {
            return position >= production.tokens.length;
        }

        /*
         * Get the next token in the production.
         */
        public String nextToken() {
            return position < production.tokens.length ? production.tokens[position] : null;
        }

        /*
         * Check if the next token in the production is non-terminal.
         */
        public boolean nextTokenIsNonTerminal() {
            final String next = nextToken();
            assert next != null : "Next token was null!";

            return !(next.startsWith("\"") && next.endsWith("\""));
        }

        // Equals & Hash Code

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final State state = (State) o;
            return position == state.position
                    && origin == state.origin
                    && production.equals(state.production);
        }

        @Override
        public int hashCode() {
            return Objects.hash(production, position, origin);
        }

        // To String

        @Override
        public String toString() {
            return "((" + rep + "))";
        }

    }

    /*
     * A set of states.
     */
    private static final class StateSet extends LinkedHashSet<State> {

        // Helper Methods

        /*
         * Check if this StateSet contains a completed production with the
         * specified LHS>
         */
        boolean containsCompletedProduction(final String id) {
            return stream()
                    .filter(state -> state.production.id.equals(id))
                    .anyMatch(State::isFinished);
        }

        /*
         * Iterate over this StateSet, taking into account any changes that
         * have occurred during the current iteration.
         */
        void forEachFlux(final Consumer<State> consumer) {
            for (int i = 0; i < size(); i++) {
                // THIS IS REALLY HACKY...
                // Java doesn't really like iterating over a changing collection
                //    and we can't directly index the elements in a Set, so...
                //    ... yeah, this is a monstrosity but oh well!
                final List<State> copy = new ArrayList<>(this);
                final State state = copy.get(i);
                consumer.accept(state);
            }
        }

    }

}
