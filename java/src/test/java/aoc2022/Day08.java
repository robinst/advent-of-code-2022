package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Pos(int x, int y) {

    Pos plus(Pos pos) {
        return new Pos(x + pos.x, y + pos.y);
    }

    static List<Pos> allDirections() {
        return Arrays.asList(new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0));
    }

    int distance(Pos other) {
        return Math.abs(x() - other.x()) + Math.abs(y() - other.y());
    }

    List<Pos> neighbors() {
        return Arrays.asList(new Pos(x(), y() + 1), new Pos(x() + 1, y()), new Pos(x(), y() - 1), new Pos(x() - 1, y()));
    }
}

record PosItem<T>(Pos pos, T item) {
}

record Grid<T>(List<List<T>> lines) {
    Optional<T> get(Pos pos) {
        if (pos.y() >= 0 && pos.y() < lines.size()) {
            var line = lines.get(pos.y());
            if (pos.x() >= 0 && pos.x() < line.size()) {
                return Optional.of(line.get(pos.x()));
            }
        }
        return Optional.empty();
    }

    T getUnchecked(Pos pos) {
        return get(pos).orElseThrow(() -> new IllegalStateException("No item at position " + pos));
    }

    int height() {
        return lines.size();
    }

    int width() {
        return lines.get(0).size();
    }

    List<Pos> allPositions() {
        return IntStream.range(0, width()).boxed().flatMap(x -> IntStream.range(0, height()).mapToObj(y -> new Pos(x, y))).toList();
    }

    List<PosItem<T>> neighbors(Pos ofPos) {
        var result = new ArrayList<PosItem<T>>();
        for (Pos pos : ofPos.neighbors()) {
            var optional = get(pos);
            optional.ifPresent(t -> result.add(new PosItem<>(pos, t)));
        }
        return result;
    }
}

public class Day08 {

    public static int solve1(String input) {
        Grid<Integer> grid = parse(input);

        var width = grid.width();
        var height = grid.height();

        var visible = new HashSet<Pos>();
        for (int y = 0; y < height; y++) {
            visible.addAll(visible(grid, new Pos(0, y), new Pos(1, 0)));
            visible.addAll(visible(grid, new Pos(width - 1, y), new Pos(-1, 0)));
        }
        for (int x = 0; x < width; x++) {
            visible.addAll(visible(grid, new Pos(x, 0), new Pos(0, 1)));
            visible.addAll(visible(grid, new Pos(x, height - 1), new Pos(0, -1)));
        }

        return visible.size();
    }

    private static Grid<Integer> parse(String input) {
        var grid = new Grid<Integer>(input.lines()
                .map(line -> line.chars()
                        .mapToObj(c -> Integer.parseInt(Character.toString(c)))
                        .toList())
                .toList());
        return grid;
    }

    private static Set<Pos> visible(Grid<Integer> grid, Pos start, Pos direction) {
        var visible = new HashSet<Pos>();

        var pos = start;
        var previousHeight = -1;
        while (true) {
            var optionalHeight = grid.get(pos);
            if (optionalHeight.isEmpty()) {
                break;
            }
            var height = optionalHeight.get();
            if (height > previousHeight) {
                visible.add(pos);
                previousHeight = height;
            }

            pos = pos.plus(direction);
        }

        return visible;
    }

    public static int solve2(String input) {
        Grid<Integer> grid = parse(input);

        var width = grid.width();
        var height = grid.height();

        return IntStream.range(0, width).boxed().flatMap(x -> IntStream.range(0, height).mapToObj(y -> new Pos(x, y)))
                .map(pos -> scenicScore(grid, pos))
                .max(Integer::compareTo)
                .get();
    }

    private static int scenicScore(Grid<Integer> grid, Pos start) {
        return Stream.of(new Pos(0, -1), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0))
                .map(direction -> viewingDistance(grid, start, direction))
                .reduce(1, (a, b) -> a * b);
    }

    private static int viewingDistance(Grid<Integer> grid, Pos start, Pos direction) {
        var houseHeight = grid.getUnchecked(start);

        var pos = start.plus(direction);

        var distance = 0;
        while (true) {
            var optionalHeight = grid.get(pos);
            if (optionalHeight.isEmpty()) {
                return distance;
            }
            var height = optionalHeight.get();
            // What if there's a tree and then a lower tree after it? Is that visible?
            if (height >= houseHeight) {
                return distance + 1;
            }

            distance++;

            pos = pos.plus(direction);
        }
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day08.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                30373
                25512
                65332
                33549
                35390
                """;
        assertEquals(21, solve1(input));
        assertEquals(8, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day08.txt"));
        assertEquals(1805, solve1(input));
        assertEquals(444528, solve2(input));
    }
}
