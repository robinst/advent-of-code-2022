package aoc2022;

import aoc2022.benchmarks.Timing;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day06 {

    public static long solve1(String input) {
        return calculate(input, 4);
    }

    public static long solve2(String input) {
        return calculate(input, 14);
    }

    public static int calculate(String input, int length) {
        LinkedList<Character> chars = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            chars.add(c);

            if (chars.size() > length) {
                chars.removeFirst();
            }

            if (chars.size() == length) {
                if (new HashSet<>(chars).size() == length) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    public static int calculateFaster(String input, int length) {
        int[] counts = new int[256];
        int unique = 0;
        LinkedList<Character> chars = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            chars.add(c);

            var count = counts[c];
            if (count == 0) {
                unique++;
            }
            counts[c] = count + 1;

            if (chars.size() > length) {
                var removed = chars.removeFirst();
                if (removed != null) {
                    var removedCount = counts[removed];
                    if (removedCount == 1) {
                        unique--;
                    }
                    counts[removed] = removedCount - 1;
                }
            }

            if (unique == length) {
                return i + 1;
            }
        }
        return 0;
    }

    @Test
    void example() {
        assertEquals(7, solve1("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
        assertEquals(5, solve1("bvwbjplbgvbhsrlpgdmjqwftvncz"));
        assertEquals(6, solve1("nppdvjthqldpwncqszvftbrmjlhg"));
        assertEquals(10, solve1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
        assertEquals(11, solve1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));

        assertEquals(19, solve2("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
        assertEquals(23, solve2("bvwbjplbgvbhsrlpgdmjqwftvncz"));
        assertEquals(23, solve2("nppdvjthqldpwncqszvftbrmjlhg"));
        assertEquals(29, solve2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
        assertEquals(26, solve2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day06.txt"));

        Timing.time(() -> {
            assertEquals(1892, solve1(input));
            assertEquals(2313, solve2(input));
            return null;
        });

        Timing.time(() -> {
            assertEquals(1892, calculateFaster(input, 4));
            assertEquals(2313, calculateFaster(input, 14));
            return null;
        });
    }
}
