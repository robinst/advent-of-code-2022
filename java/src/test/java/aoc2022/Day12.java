package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

enum SquareType {
    START,
    END,
    NORMAL
}

class Square {
    SquareType squareType;
    int elevation;

    Square(SquareType squareType, int elevation) {
        this.squareType = squareType;
        this.elevation = elevation;
    }

    SquareType squareType() {
        return squareType;
    }

    int elevation() {
        return elevation;
    }
}

record PosLength(Pos pos, int length) {
}

public class Day12 {

    public static long solve1(String input) {
        var grid = parse(input);
        var start = findType(grid, SquareType.START);
        var end = findType(grid, SquareType.END);

        return bfs(grid, start, end);
    }

    public static long solve2(String input) {
        var grid = parse(input);
        var end = findType(grid, SquareType.END);

        var possibleStarts = grid.allPositions().stream()
                .filter(p -> grid.get(p).get().elevation == 0)
                .toList();
        return possibleStarts.stream().mapToLong(p -> bfs(grid, p, end)).min().getAsLong();
    }

    private static int bfs(Grid<Square> grid, Pos start, Pos end) {
        var queue = new ArrayDeque<PosLength>();
        var visited = new HashSet<Pos>();
        visited.add(start);
        queue.add(new PosLength(start, 0));

        while (!queue.isEmpty()) {
            var posLength = queue.removeFirst();
            var pos = posLength.pos();
            if (pos.equals(end)) {
                return posLength.length();
            }

            for (PosItem<Square> next : grid.neighbors(pos)) {
                if (visited.contains(next.pos())) {
                    continue;
                }

                if (next.item().elevation() <= grid.getUnchecked(pos).elevation() + 1) {
                    visited.add(next.pos());
                    queue.addLast(new PosLength(next.pos(), posLength.length() + 1));
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private static Grid<Square> parse(String input) {
        var grid = new Grid<>(input.lines()
                .map(line -> line.chars()
                        .mapToObj(Day12::parseSquare)
                        .toList())
                .toList());
        return grid;
    }

    private static Square parseSquare(int c) {
        return switch (c) {
            case 'S' -> new Square(SquareType.START, 0);
            case 'E' -> new Square(SquareType.END, 'z' - 'a');
            default -> new Square(SquareType.NORMAL, c - 'a');
        };
    }

    private static Pos findType(Grid<Square> grid, SquareType type) {
        for (Pos pos : grid.allPositions()) {
            if (grid.getUnchecked(pos).squareType() == type) {
                return pos;
            }
        }
        throw new IllegalStateException("Unable to find square " + type);
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day12.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
                """;
        assertEquals(31, solve1(input));
        assertEquals(29, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day12.txt"));
        assertEquals(370, solve1(input));
        assertEquals(363, solve2(input));
    }
}
