package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

sealed interface Term {
}

record NumberTerm(long number) implements Term {

}

record VariableTerm(String name) implements Term {

}

enum Op {
    PLUS,
    MINUS,
    TIMES,
    DIVIDE;

    public long run(long a, long b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case TIMES -> a * b;
            case DIVIDE -> a / b;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case PLUS -> "+";
            case MINUS -> "-";
            case TIMES -> "*";
            case DIVIDE -> "\\";
        };
    }
}

record OpTerm(String a, Op op, String b) implements Term {
}

record OpTermTree(Term a, Op op, Term b) implements Term {
}

public class Day21 {

    public static long solve1(String input) {
        Map<String, Term> terms = parse(input);

        return calculate(terms, "root");
    }

    public static long solve2(String input) {
        Map<String, Term> terms = parse(input);
        var term = (OpTerm) terms.get("root");

        terms.put("humn", new VariableTerm("humn"));

        var a = term.a();
        var b = term.b();
        try {
            return solve(calculate(terms, a), buildTree(terms, b));
        } catch (IllegalStateException e) {
            // Ok, try other one
        }

        try {
            return solve(calculate(terms, b), buildTree(terms, a));
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Variable was used in both sides, oh no");
        }
    }

    private static long calculate(Map<String, Term> terms, String name) {
        var term = terms.get(name);
        if (term instanceof NumberTerm t) {
            return t.number();
        } else if (term instanceof OpTerm o) {
            return o.op().run(calculate(terms, o.a()), calculate(terms, o.b()));
        } else {
            throw new IllegalStateException("Unknown term " + term);
        }
    }

    private static Term buildTree(Map<String, Term> terms, String name) {
        var term = terms.get(name);
        if (term instanceof OpTerm o) {
            try {
                // Simplify so that at least one side of each op is a number later
                return new NumberTerm(calculate(terms, name));
            } catch (IllegalStateException e) {
                return new OpTermTree(buildTree(terms, o.a()), o.op(), buildTree(terms, o.b()));
            }
        } else {
            return term;
        }
    }

    private static long solve(long result, Term term) {
        while (true) {
            if (term instanceof OpTermTree o) {
                switch (o.op()) {
                    case PLUS -> {
                        if (o.a() instanceof NumberTerm t) {
                            result -= t.number();
                            term = o.b();
                        } else if (o.b() instanceof NumberTerm t) {
                            result -= t.number();
                            term = o.a();
                        }
                    }
                    case MINUS -> {
                        if (o.a() instanceof NumberTerm t) {
                            result = t.number() - result;
                            term = o.b();
                        } else if (o.b() instanceof NumberTerm t) {
                            result += t.number();
                            term = o.a();
                        }
                    }
                    case TIMES -> {
                        if (o.a() instanceof NumberTerm t) {
                            result = result / t.number();
                            term = o.b();
                        } else if (o.b() instanceof NumberTerm t) {
                            result = result / t.number();
                            term = o.a();
                        }
                    }
                    case DIVIDE -> {
                        if (o.a() instanceof NumberTerm t) {
                            result = t.number() / result;
                            term = o.b();
                        } else if (o.b() instanceof NumberTerm t) {
                            result = result * t.number();
                            term = o.a();
                        }
                    }
                }
            } else if (term instanceof VariableTerm) {
                return result;
            } else {
                throw new IllegalStateException("Unexpected term " + term);
            }
        }
    }

    private static Map<String, Term> parse(String input) {
        var lines = input.lines().toList();
        var terms = new HashMap<String, Term>();
        for (String line : lines) {
            var colonSplit = line.split(": ");
            var name = colonSplit[0];
            var numbers = Parsing.numbersLong(colonSplit[1]);

            if (numbers.size() == 1) {
                terms.put(name, new NumberTerm(numbers.get(0)));
            } else {
                var eq = colonSplit[1];
                var parts = eq.split(" ");
                Op op = switch (parts[1]) {
                    case "+" -> Op.PLUS;
                    case "-" -> Op.MINUS;
                    case "*" -> Op.TIMES;
                    case "/" -> Op.DIVIDE;
                    default -> throw new IllegalStateException("Unknown operation: " + eq);
                };
                terms.put(name, new OpTerm(parts[0], op, parts[2]));
            }
        }
        return terms;
    }

    @Test
    void example() {
        final String input = """
                root: pppw + sjmn
                dbpl: 5
                cczh: sllz + lgvd
                zczc: 2
                ptdq: humn - dvpt
                dvpt: 3
                lfqf: 4
                humn: 5
                ljgn: 2
                sjmn: drzm * dbpl
                sllz: 4
                pppw: cczh / lfqf
                lgvd: ljgn * ptdq
                drzm: hmdt - zczc
                hmdt: 32
                """;
        assertEquals(152, solve1(input));
        assertEquals(301, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day21.txt"));
        assertEquals(72664227897438L, solve1(input));
        assertEquals(3916491093817L, solve2(input));
    }
}
