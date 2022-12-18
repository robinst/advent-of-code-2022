package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Pos3(int x, int y, int z) {

    List<Pos3> neighbors() {
        return Arrays.asList(new Pos3(x() + 1, y(), z()), new Pos3(x(), y() + 1, z()), new Pos3(x(), y(), z() + 1),
                new Pos3(x() - 1, y(), z()), new Pos3(x(), y() - 1, z()), new Pos3(x(), y(), z() - 1));
    }
}

record Bounds3(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {

    public static Bounds3 calculate(Collection<Pos3> positions) {
        var xStats = positions.stream().mapToInt(Pos3::x).summaryStatistics();
        var yStats = positions.stream().mapToInt(Pos3::y).summaryStatistics();
        var zStats = positions.stream().mapToInt(Pos3::z).summaryStatistics();
        return new Bounds3(xStats.getMin(), xStats.getMax(), yStats.getMin(), yStats.getMax(), zStats.getMin(), zStats.getMax());
    }

    public boolean contains(Pos3 pos) {
        return pos.x() >= minX && pos.x() <= maxX && pos.y() >= minY && pos.y() <= maxY && pos.z() >= minZ && pos.z() <= maxZ;
    }
}

public class Day18 {

    public static int solve1(String input) {
        var lines = input.lines().toList();
        var lava = new HashSet<Pos3>();

        // Instead of calculating exposed sides, we can count covered sides and then deduce the number of exposed
        // sides later.
        var covered = 0;

        for (String line : lines) {
            var numbers = Parsing.numbers(line);
            var pos = new Pos3(numbers.get(0), numbers.get(1), numbers.get(2));
            for (Pos3 neighbor : pos.neighbors()) {
                if (lava.contains(neighbor)) {
                    // We're adding a new lava block next to an existing one, which means 1 side of the new block and
                    // 1 side of the existing block will be covered.
                    covered += 2;
                }
            }
            lava.add(pos);
        }

        return lava.size() * 6 - covered;
    }

    public static int solve2(String input) {
        var lava = parse(input);
        var bounds = Bounds3.calculate(lava);

        var queue = new ArrayDeque<Pos3>();
        var air = new HashSet<>();

        for (int x = bounds.minX() - 1; x <= bounds.maxX() + 1; x++) {
            for (int y = bounds.minY() - 1; y <= bounds.maxY() + 1; y++) {
                for (int z = bounds.minZ() - 1; z <= bounds.maxZ() + 1; z++) {
                    var pos = new Pos3(x, y, z);
                    // There should be a better way to build a layer of air just outside the lava than to iterate
                    // over all positions, but I can't think of it right now.
                    if (bounds.contains(pos)) {
                        continue;
                    }
                    queue.add(pos);
                    air.add(pos);
                }
            }
        }

        int exposed = 0;

        while (!queue.isEmpty()) {
            var pos = queue.removeFirst();
            for (Pos3 neighbor : pos.neighbors()) {
                if (!bounds.contains(neighbor) || air.contains(neighbor)) {
                    continue;
                }

                if (lava.contains(neighbor)) {
                    exposed++;
                } else {
                    air.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return exposed;
    }

    private static Set<Pos3> parse(String input) {
        return input.lines().map(line -> {
            var numbers = Parsing.numbers(line);
            return new Pos3(numbers.get(0), numbers.get(1), numbers.get(2));
        }).collect(Collectors.toSet());
    }

    @Test
    void example() {
        final String input = """
                2,2,2
                1,2,2
                3,2,2
                2,1,2
                2,3,2
                2,2,1
                2,2,3
                2,2,4
                2,2,6
                1,2,5
                3,2,5
                2,1,5
                2,3,5
                """;
        assertEquals(64, solve1(input));
        assertEquals(58, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day18.txt"));
        assertEquals(4444, solve1(input));
        assertEquals(2530, solve2(input));
    }
}
