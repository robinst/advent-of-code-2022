package aoc2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10 {

    public static long solve1(String input) {
        // Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
        // What is the sum of these six signal strengths?
        var x = 1;
        var cycle = 0;
        var strength = 0;
        var lines = input.lines().toList();

        for (String line : lines) {
            var parts = line.split(" ");
            switch (parts[0]) {
                case "noop" -> {
                    cycle += 1;
                    strength += checkCycle(cycle, x);
                }
                case "addx" -> {
                    var num = Integer.parseInt(parts[1]);
                    cycle += 1;
                    strength += checkCycle(cycle, x);
                    cycle += 1;
                    strength += checkCycle(cycle, x);
                    x += num;
                }
            }
        }
        return strength;
    }

    private static int checkCycle(int cycle, int x) {
        switch (cycle) {
            case 20, 60, 100, 140, 180, 220 -> {
                return x * cycle;
            }
            default -> {
                return 0;
            }
        }
    }

    public static String solve2(String input) {
        var x = 1;
        var cycle = 0;
        var lines = input.lines().toList();
        var sb = new StringBuilder();

        for (String line : lines) {
            var parts = line.split(" ");
            switch (parts[0]) {
                case "noop" -> {
                    cycle += 1;
                    print(cycle, x, sb);
                }
                case "addx" -> {
                    var num = Integer.parseInt(parts[1]);
                    cycle += 1;
                    print(cycle, x, sb);
                    cycle += 1;
                    print(cycle, x, sb);
                    x += num;
                }
            }
        }
        return sb.toString();
    }

    private static void print(int cycle, int x, StringBuilder sb) {
        // I'm confused about these, but can't be bothered to figure out why this is necessary.
        int sprite = (x + 1);
        int cyclePos = ((cycle - 1) % 40) + 1;
        if (cyclePos >= sprite - 1 && cyclePos <= sprite + 1) {
            sb.append('#');
        } else {
            sb.append('.');
        }
        if (cyclePos % 40 == 0) {
            sb.append('\n');
        }
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day10.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                addx 15
                addx -11
                addx 6
                addx -3
                addx 5
                addx -1
                addx -8
                addx 13
                addx 4
                noop
                addx -1
                addx 5
                addx -1
                addx 5
                addx -1
                addx 5
                addx -1
                addx 5
                addx -1
                addx -35
                addx 1
                addx 24
                addx -19
                addx 1
                addx 16
                addx -11
                noop
                noop
                addx 21
                addx -15
                noop
                noop
                addx -3
                addx 9
                addx 1
                addx -3
                addx 8
                addx 1
                addx 5
                noop
                noop
                noop
                noop
                noop
                addx -36
                noop
                addx 1
                addx 7
                noop
                noop
                noop
                addx 2
                addx 6
                noop
                noop
                noop
                noop
                noop
                addx 1
                noop
                noop
                addx 7
                addx 1
                noop
                addx -13
                addx 13
                addx 7
                noop
                addx 1
                addx -33
                noop
                noop
                noop
                addx 2
                noop
                noop
                noop
                addx 8
                noop
                addx -1
                addx 2
                addx 1
                noop
                addx 17
                addx -9
                addx 1
                addx 1
                addx -3
                addx 11
                noop
                noop
                addx 1
                noop
                addx 1
                noop
                noop
                addx -13
                addx -19
                addx 1
                addx 3
                addx 26
                addx -30
                addx 12
                addx -1
                addx 3
                addx 1
                noop
                noop
                noop
                addx -9
                addx 18
                addx 1
                addx 2
                noop
                noop
                addx 9
                noop
                noop
                noop
                addx -1
                addx 2
                addx -37
                addx 1
                addx 3
                noop
                addx 15
                addx -21
                addx 22
                addx -6
                addx 1
                noop
                addx 2
                addx 1
                noop
                addx -10
                noop
                noop
                addx 20
                addx 1
                addx 2
                addx 2
                addx -6
                addx -11
                noop
                noop
                noop
                """;
        assertEquals(13140, solve1(input));
        assertEquals("""
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
                """, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day10.txt"));
        assertEquals(12640, solve1(input));
        assertEquals("""
                ####.#..#.###..####.#....###....##.###..
                #....#..#.#..#....#.#....#..#....#.#..#.
                ###..####.###....#..#....#..#....#.#..#.
                #....#..#.#..#..#...#....###.....#.###..
                #....#..#.#..#.#....#....#.#..#..#.#.#..
                ####.#..#.###..####.####.#..#..##..#..#.
                """, solve2(input));
    }
}
