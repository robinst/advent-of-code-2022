package aoc2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayNN {

    public static long solve1(String input) {
        return input.lines()
                .mapToLong(DayNN::score1)
                .sum();
    }

    private static long score1(String s) {
        return 0;
    }

    public static long solve2(String input) {
        return input.lines()
                .mapToLong(DayNN::score2)
                .sum();
    }

    public static long score2(String s) {
        return 0;
    }

    @Test
    void example() {
        final String input = """
                                
                """;
        assertEquals(0, solve1(input));
        assertEquals(0, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/dayNN.txt"));
        assertEquals(0, solve1(input));
        assertEquals(0, solve2(input));
    }
}
