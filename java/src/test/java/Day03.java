import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03 {

    public static long solve1(String input) {
        return input.lines()
                .mapToLong(Day03::score1)
                .sum();
    }

    private static long score1(String s) {
        var len = s.length() / 2;
        var first = s.substring(0, len);
        var second = s.substring(len);
        var setA = getChars(first);
        var setB = getChars(second);

        setA.retainAll(setB);
        var common = setA.stream().findFirst().get();
        return priority(common);
    }

    private static HashSet<Character> getChars(String first) {
        var setA = new HashSet<Character>();
        for (char c : first.toCharArray()) {
            setA.add(c);
        }
        return setA;
    }

    private static int priority(Character common) {
        if (common >= 'a' && common <= 'z') {
            return common - 'a' + 1;
        } else {
            return common - 'A' + 27;
        }
    }

    public static long solve2(String input) {
        var lines = input.lines().toList();
        var sum = 0;
        for (int i = 0; i < lines.size(); i += 3) {
            sum += score2(lines.get(i), lines.get(i + 1), lines.get(i + 2));
        }
        return sum;
    }

    public static long score2(String a, String b, String c) {
        var set = getChars(a);
        set.retainAll(getChars(b));
        set.retainAll(getChars(c));
        var common = set.stream().findFirst().get();
        return priority(common);
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day03.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                vJrwpWtwJgWrhcsFMMfFFhFp
                jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
                PmmdzqPrVvPwwTWBwg
                wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
                ttgJtRGJQctTZtZT
                CrZsJsPPZsGzwwsLwLmpwMDw
                """;
        assertEquals(157, solve1(input));
        assertEquals(70, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day03.txt"));
        assertEquals(7674, solve1(input));
        assertEquals(2805, solve2(input));
    }
}
