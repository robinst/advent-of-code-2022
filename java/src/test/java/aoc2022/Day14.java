package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14 {

    public static long solve1(String input) {
        var rocks = parse(input);
        var sand = new HashSet<Pos>();
        var abyssY = rocks.stream().mapToInt(Pos::y).max().getAsInt();

        var units = 0;
        while (true) {
            var pos = new Pos(500, 0);
            var resting = fall(pos, rocks, sand, abyssY);
            if (resting.y() >= abyssY) {
                return units;
            }
            sand.add(resting);

            units++;
        }
    }

    public static long solve2(String input) {
        var rocks = parse(input);
        var sand = new HashSet<Pos>();
        var maxY = rocks.stream().mapToInt(Pos::y).max().getAsInt() + 1;

        var units = 0;
        while (true) {
            var start = new Pos(500, 0);
            var resting = fall(start, rocks, sand, maxY);
            if (resting.equals(start)) {
                return units + 1;
            }
            sand.add(resting);

            units++;
        }
    }

    private static Pos fall(Pos pos, Set<Pos> rocks, Set<Pos> sand, int maxY) {
        var directions = List.of(new Pos(0, 1), new Pos(-1, 1), new Pos(1, 1));
        var moved = true;
        while (moved && pos.y() < maxY) {
            moved = false;
            for (Pos direction : directions) {
                var next = pos.plus(direction);
                if (!rocks.contains(next) && !sand.contains(next)) {
                    pos = next;
                    moved = true;
                    break;
                }
            }
        }
        return pos;
    }

    private static Set<Pos> parse(String input) {
        var result = new HashSet<Pos>();
        var lines = input.lines().toList();
        for (String line : lines) {
            var path = line.split(" -> ");
            for (int i = 0; i < path.length - 1; i++) {
                var from = parsePos(path[i]);
                var to = parsePos(path[i + 1]);

                if (from.x() == to.x()) {
                    // Vertical
                    var fromY = Math.min(from.y(), to.y());
                    var toY = Math.max(from.y(), to.y());
                    IntStream.rangeClosed(fromY, toY).mapToObj(y -> new Pos(from.x(), y)).forEach(result::add);
                } else if (from.y() == to.y()) {
                    // Horizontal
                    var fromX = Math.min(from.x(), to.x());
                    var toX = Math.max(from.x(), to.x());
                    IntStream.rangeClosed(fromX, toX).mapToObj(x -> new Pos(x, from.y())).forEach(result::add);
                } else {
                    throw new IllegalStateException("Unknown line from " + from + " to " + to);
                }
            }
        }
        return result;
    }

    private static Pos parsePos(String s) {
        var numbers = Parsing.numbers(s);
        return new Pos(numbers.get(0), numbers.get(1));
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day14.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
                """;
        assertEquals(24, solve1(input));
        assertEquals(93, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day14.txt"));
        assertEquals(1330, solve1(input));
        assertEquals(26139, solve2(input));
    }
}
