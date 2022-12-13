package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09 {

    enum Direction {
        U,
        R,
        D,
        L
    }

    record Move(Direction direction, int steps) {
        static Move parse(String s) {
            var parts = s.split(" ");
            return new Move(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));
        }

        Pos step() {
            return switch (direction) {
                case U -> new Pos(0, -1);
                case R -> new Pos(1, 0);
                case D -> new Pos(0, 1);
                case L -> new Pos(-1, 0);
            };
        }
    }

    public static long solve1(String input) {
        var head = new Pos(0, 0);
        var tail = new Pos(0, 0);

        var visited = new HashSet<Pos>();
        visited.add(tail);

        var lines = input.lines().toList();
        for (String line : lines) {
            var move = Move.parse(line);

            var singleStep = move.step();
            for (int i = 0; i < move.steps(); i++) {
                head = head.plus(singleStep);

                tail = moveTail(tail, head);
                visited.add(tail);
            }
        }

        return visited.size();
    }

    public static long solve2(String input) {
        var knots = new ArrayList<Pos>();
        for (int i = 0; i < 10; i++) {
            var pos = new Pos(0, 0);
            knots.add(pos);
        }

        var visited = new HashSet<Pos>();
        visited.add(knots.get(0));

        var lines = input.lines().toList();
        for (String line : lines) {
            var move = Move.parse(line);

            var singleStep = move.step();
            for (int step = 0; step < move.steps(); step++) {
                knots.set(0, knots.get(0).plus(singleStep));

                for (int k = 1; k < knots.size(); k++) {
                    var knot = knots.get(k);
                    var follow = knots.get(k - 1);
                    knot = moveTail(knot, follow);
                    knots.set(k, knot);
                }

                visited.add(knots.get(knots.size() - 1));
            }
        }

        return visited.size();
    }


    private static Pos moveTail(Pos tail, Pos head) {
        // No need to move
        var xDiff = head.x() - tail.x();
        var yDiff = head.y() - tail.y();
        if (Math.abs(xDiff) <= 1 && Math.abs(yDiff) <= 1) {
            return tail;
        }

        if (xDiff > 0) {
            xDiff = 1;
        } else if (xDiff < 0) {
            xDiff = -1;
        }

        if (yDiff > 0) {
            yDiff = 1;
        } else if (yDiff < 0) {
            yDiff = -1;
        }

        return tail.plus(new Pos(xDiff, yDiff));
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/dayNN.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        var input = """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
                """;
        assertEquals(13, solve1(input));
        assertEquals(1, solve2(input));

        var example2 = """
                R 5
                U 8
                L 8
                D 3
                R 17
                D 10
                L 25
                U 20
                """;
        assertEquals(36, solve2(example2));
    }

    @Test
    void testMoveTail() {
        var head = new Pos(4, -2);
        var tail = new Pos(3, 0);
        assertEquals(new Pos(4, -1), moveTail(tail, head));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day09.txt"));
        assertEquals(6271, solve1(input));
        assertEquals(2458, solve2(input));
    }
}
