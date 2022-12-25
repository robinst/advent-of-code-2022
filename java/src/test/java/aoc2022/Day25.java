package aoc2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day25 {

    public static String solve1(String input) {
        var sum = input.lines().mapToLong(Day25::fromSnafu).sum();
        return toSnafu(sum);
    }

    private static long fromSnafu(String s) {
        long place = 1;
        long number = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            var c = s.charAt(i);
            switch (c) {
                case '2' -> number += 2 * place;
                case '1' -> number += place;
                case '0' -> {
                }
                case '-' -> number += -1 * place;
                case '=' -> number += -2 * place;
            }
            place *= 5;
        }
        return number;
    }

    private static String toSnafu(long number) {
        StringBuilder sb = new StringBuilder();
        long base = 5;
        while (number != 0) {
            var digit = number % base;
            if (digit >= 0 && digit <= 2) {
                sb.append(digit);
                number /= base;
            } else {
                // -1 or -2
                long value = digit - base;
                if (value == -1) {
                    sb.append('-');
                } else if (value == -2) {
                    sb.append('=');
                }
                number /= base;
                number++;
            }
        }
        sb.reverse();
        return sb.toString();
    }

    @Test
    void example() {
        final String input = """
                1=-0-2
                12111
                2=0=
                21
                2=01
                111
                20012
                112
                1=-1=
                1-12
                12
                1=
                122
                """;
        assertEquals("2=-1=0", solve1(input));
    }

    @Test
    void roundtrip() {
        for (long i = 0; i < 1_000_000; i++) {
            assertEquals(i, fromSnafu(toSnafu(i)));
        }
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day25.txt"));
        assertEquals("20-1-11==0-=0112-222", solve1(input));
    }
}
