package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Shape(List<Pos> positions, Pos leftBottom, int width, int height) {

    public static Shape of(Pos... positions) {
        var minX = Arrays.stream(positions).mapToInt(Pos::x).min().getAsInt();
        var maxX = Arrays.stream(positions).mapToInt(Pos::x).max().getAsInt();
        var minY = Arrays.stream(positions).mapToInt(Pos::y).min().getAsInt();
        var maxY = Arrays.stream(positions).mapToInt(Pos::y).max().getAsInt();
        return new Shape(List.of(positions), new Pos(minX, minY), maxX - minX + 1, maxY - minY + 1);
    }

    public boolean overlaps(Set<Pos> world, Pos shapePosition) {
        return positions().stream().anyMatch(p -> world.contains(p.plus(shapePosition)));
    }
}

record ShapeJetKey(int shapeIndex, int jetIndex) {
}

record ShapeJetValue(int height, long rockNumber) {
}

public class Day17 {

    public static long solve1(String input) {
        return run(input, 2022);
    }

    public static long solve2(String input) {
        return run(input, 1000000000000L);
    }

    private static long run(String input, long totalRocks) {
        var jets = input.trim();
        var shapes = List.of(
                Shape.of(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(3, 0)),
                Shape.of(new Pos(1, 0), new Pos(0, 1), new Pos(1, 1), new Pos(2, 1), new Pos(1, 2)),
                Shape.of(new Pos(0, 0), new Pos(1, 0), new Pos(2, 0), new Pos(2, 1), new Pos(2, 2)),
                Shape.of(new Pos(0, 0), new Pos(0, 1), new Pos(0, 2), new Pos(0, 3)),
                Shape.of(new Pos(0, 0), new Pos(1, 0), new Pos(0, 1), new Pos(1, 1))
        );

        int height = 0;
        int chamberWidth = 7;
        long heightOffset = 0;
        boolean foundRepeat = false;

        var shapeJetIndex = new HashMap<ShapeJetKey, List<ShapeJetValue>>();

        var rocks = new HashSet<Pos>();

        int shapeIndex = 0;
        int jetIndex = 0;
        for (long rockNumber = 0; rockNumber < totalRocks; rockNumber++) {
            var key = new ShapeJetKey(shapeIndex, jetIndex);
            shapeJetIndex.putIfAbsent(key, new ArrayList<>());
            var values = shapeJetIndex.get(key);
            values.add(new ShapeJetValue(height, rockNumber));

            if (!foundRepeat && values.size() >= 3) {
                var diffs = new HashSet<>();
                for (int i = 0; i < values.size() - 1; i++) {
                    diffs.add(values.get(i + 1).height() - values.get(i).height());
                }

                if (diffs.size() == 1) {
                    foundRepeat = true;

                    var previousValues = values.get(values.size() - 2);
                    var previousHeight = previousValues.height();
                    var remainingRocks = totalRocks - rockNumber;
                    var rocksInRepeat = rockNumber - previousValues.rockNumber();

                    System.out.println("Found repeat, current height " + height + ":");
                    var newShape = shapes.get(shapeIndex);
                    var shapePos = newShape.leftBottom().plus(new Pos(2, height + 3));
                    draw(rocks, newShape, shapePos, height + 2, height - 20);
                    System.out.println();
                    System.out.println("Previous height " + previousHeight + ":");
                    draw(rocks, newShape, shapePos, previousHeight + 2, previousHeight - 20);

                    var repeats = remainingRocks / rocksInRepeat;
                    var todo = remainingRocks % rocksInRepeat;

                    heightOffset = repeats * (height - previousHeight);
                    if (todo == 0) {
                        break;
                    } else {
                        rockNumber = totalRocks - todo;
                    }
                }

            }

            var shape = shapes.get(shapeIndex);
            shapeIndex = (shapeIndex + 1) % shapes.size();

            var pos = shape.leftBottom().plus(new Pos(2, height + 3));
//            draw(rocks, shape, pos);
            while (true) {
//                draw(rocks, shape, pos);

                var c = jets.charAt(jetIndex);
                jetIndex = (jetIndex + 1) % jets.length();
                if (c == '<') {
                    var newPos = pos.plus(new Pos(-1, 0));
                    if (newPos.x() >= 0 && !shape.overlaps(rocks, newPos)) {
                        pos = newPos;
                    }
                } else if (c == '>') {
                    var newPos = pos.plus(new Pos(1, 0));
                    if (newPos.x() + shape.width() <= chamberWidth && !shape.overlaps(rocks, newPos)) {
                        pos = newPos;
                    }
                }

//                draw(rocks, shape, pos);

                var newPos = pos.plus(new Pos(0, -1));
                if (newPos.y() >= 0 && !shape.overlaps(rocks, newPos)) {
                    // Rock moving down, continue
                    pos = newPos;
                } else {
                    // Rock can't move down anymore, record height if higher than before, then do next.
                    height = Math.max(pos.y() + shape.height(), height);
                    var finalPos = pos;
                    shape.positions().forEach(p -> rocks.add(p.plus(finalPos)));
                    break;
                }
            }
        }

        return height + heightOffset;
    }

    private static void draw(Set<Pos> world, Shape shape, Pos pos) {
        var maxY = world.stream().mapToInt(Pos::y).max().getAsInt() + 10;
        draw(world, shape, pos, maxY, 0);
        System.out.println("     +-------+");
        System.out.println();
    }

    private static void draw(Set<Pos> world, Shape shape, Pos pos, int maxY, int minY) {
        var draw = new HashMap<Pos, Character>();
        world.forEach(p -> draw.put(p, '#'));
        shape.positions().forEach(p -> draw.put(pos.plus(p), '@'));
        for (int y = maxY; y >= minY; y--) {
            System.out.printf("%4s |", y);
            for (int x = 0; x < 7; x++) {
                System.out.print(draw.getOrDefault(new Pos(x, y), '.'));
            }
            System.out.print('|');
            System.out.println();
        }
    }

    @Test
    void example() {
        final String input = """
                >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
                """;
        assertEquals(3068, solve1(input));
        assertEquals(1514285714288L, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day17.txt"));
        assertEquals(3188, solve1(input));
        assertEquals(1591977077342L, solve2(input));
    }
}
