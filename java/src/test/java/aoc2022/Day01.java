package aoc2022;

import org.junit.jupiter.api.Test;
import aoc2022.Resources;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01 {

    public static long solve(String input) {
        var elves = input.split("\n\n");
        return Arrays.stream(elves)
                .map(elf -> elf.lines()
                        .mapToLong(Long::parseLong)
                        .sum())
                .max(Long::compareTo)
                .get();
    }

    public static long solve2(String input) {
        var elves = input.split("\n\n");
        return Arrays.stream(elves)
                .map(elf -> elf.lines()
                        .mapToLong(Long::parseLong)
                        .sum())
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Long::longValue)
                .sum();
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day01.txt"));
        System.out.println(solve(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000
                """;
        assertEquals(24000, solve(input));
        assertEquals(45000, solve2(input));
    }
}
