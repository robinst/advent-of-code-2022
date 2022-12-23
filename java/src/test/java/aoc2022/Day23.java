package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day23 {

    enum Direction {
        NORTH,
        SOUTH,
        WEST,
        EAST;

        public List<Pos> checkMoves() {
            return switch (this) {
                case NORTH -> List.of(new Pos(-1, -1), new Pos(0, -1), new Pos(1, -1));
                case SOUTH -> List.of(new Pos(-1, 1), new Pos(0, 1), new Pos(1, 1));
                case WEST -> List.of(new Pos(-1, 1), new Pos(-1, 0), new Pos(-1, -1));
                case EAST -> List.of(new Pos(1, 1), new Pos(1, 0), new Pos(1, -1));
            };
        }

        public Pos move() {
            return switch (this) {
                case NORTH -> new Pos(0, -1);
                case SOUTH -> new Pos(0, 1);
                case WEST -> new Pos(-1, 0);
                case EAST -> new Pos(1, 0);
            };
        }

        public Direction next() {
            return Direction.values()[(ordinal() + 1) % Direction.values().length];
        }

        public List<Direction> allStartingFromCurrent() {
            var values = Arrays.asList(Direction.values());
            var result = new ArrayList<Direction>();
            result.addAll(values.subList(ordinal(), values.size()));
            result.addAll(values.subList(0, ordinal()));
            return result;
        }
    }

    public static int solve1(String input) {
        var elves = parse(input);
        var direction = Direction.NORTH;

        for (int round = 0; round < 10; round++) {
            Set<Pos> newElves = calculateNewPositions(elves, direction);
            if (elves.equals(newElves)) {
                // No one moved, no one is going to move in next round either
                break;
            }

//            print(newElves);

            elves = newElves;
            direction = direction.next();
        }

        return PosBounds.calculate(elves).size() - elves.size();
    }

    public static long solve2(String input) {
        var elves = parse(input);
        var direction = Direction.NORTH;

        for (int round = 0; round < Integer.MAX_VALUE; round++) {
            Set<Pos> newElves = calculateNewPositions(elves, direction);
            if (elves.equals(newElves)) {
                return round + 1;
            }

//            print(newElves);

            elves = newElves;
            direction = direction.next();
        }

        return 0;
    }

    private static Set<Pos> calculateNewPositions(Set<Pos> elves, Direction direction) {
        var proposedMoves = new LinkedHashMap<Pos, List<Pos>>();
        for (Pos elf : elves) {
            var directions = direction.allStartingFromCurrent().stream().filter(dir -> canMove(elf, dir, elves)).toList();
            if (directions.isEmpty() || directions.size() == Direction.values().length) {
                // Can't move anywhere or all neighbors are free, so don't need to move
                proposedMoves.computeIfAbsent(elf, key -> new ArrayList<>()).add(elf);
            } else {
                var dir = directions.get(0);
                var newPos = elf.plus(dir.move());
                proposedMoves.computeIfAbsent(newPos, key -> new ArrayList<>()).add(elf);
            }
        }

        var newElves = new LinkedHashSet<Pos>();
        for (Map.Entry<Pos, List<Pos>> entry : proposedMoves.entrySet()) {
            var newPos = entry.getKey();
            var oldPositions = entry.getValue();
            if (oldPositions.size() == 1) {
                newElves.add(newPos);
            } else {
                newElves.addAll(oldPositions);
            }
        }
        return newElves;
    }

    private static boolean canMove(Pos pos, Direction dir, Set<Pos> elves) {
        for (Pos checkMove : dir.checkMoves()) {
            var newPos = pos.plus(checkMove);
            if (elves.contains(newPos)) {
                return false;
            }
        }
        return true;
    }

    private static void print(Set<Pos> elves) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Pos elf : elves) {
            minX = Math.min(elf.x(), minX);
            minY = Math.min(elf.y(), minY);
            maxX = Math.max(elf.x(), maxX);
            maxY = Math.max(elf.y(), maxY);
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (elves.contains(new Pos(x, y))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Set<Pos> parse(String input) {
        var result = new LinkedHashSet<Pos>();
        var lines = input.lines().toList();
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    result.add(new Pos(x, y));
                }
            }
        }
        return result;
    }

    @Test
    void example() {
        final String input = """
                ....#..
                ..###.#
                #...#.#
                .#...##
                #.###..
                ##.#.##
                .#..#..
                """;
        assertEquals(110, solve1(input));
        assertEquals(20, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day23.txt"));
        assertEquals(3906, solve1(input));
        assertEquals(895, solve2(input));
    }
}
