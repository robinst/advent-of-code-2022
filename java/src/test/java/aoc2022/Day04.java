package aoc2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Range(int from, int to) {
    boolean contains(Range other) {
        return from <= other.from && to >= other.to;
    }

    boolean overlaps(Range other) {
        return from >= other.from && from <= other.to ||
                from <= other.from && to >= other.from;
    }

    static Range parse(String s) {
        String[] split = s.split("-");
        return new Range(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}

public class Day04 {

    public static long solve1(String input) {
        return input.lines()
                .mapToLong(Day04::score1)
                .sum();
    }

    private static long score1(String s) {
        String[] ranges = s.split(",");
        Range a = Range.parse(ranges[0]);
        Range b = Range.parse(ranges[1]);
        return a.contains(b) || b.contains(a) ? 1 : 0;
    }

    public static long solve2(String input) {
        return input.lines()
                .mapToLong(Day04::score2)
                .sum();
    }

    public static long score2(String s) {
        String[] ranges = s.split(",");
        Range a = Range.parse(ranges[0]);
        Range b = Range.parse(ranges[1]);
        return a.overlaps(b) ? 1 : 0;
    }

    @Test
    void example() {
        final String input = """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8
                """;
        assertEquals(2, solve1(input));
        assertEquals(4, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day04.txt"));
        assertEquals(524, solve1(input));
        assertEquals(798, solve2(input));
    }
}
