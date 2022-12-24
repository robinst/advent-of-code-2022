package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day24 {

    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        Pos step() {
            return switch (this) {
                case UP -> new Pos(0, -1);
                case RIGHT -> new Pos(1, 0);
                case DOWN -> new Pos(0, 1);
                case LEFT -> new Pos(-1, 0);
            };
        }
    }

    record Blizzard(Pos pos, Direction direction) {
    }

    record BlizzardMap(int width, int height, Pos start, Pos end, List<Blizzard> blizzards) {

        boolean contains(Pos pos) {
            // TODO: Is it legal to step back onto the starting tile?
            return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
        }
    }

    record BlizzardStates(Map<Integer, Set<Pos>> blizzardsAtMinute) {

        public Set<Pos> get(int minute) {
            return blizzardsAtMinute.get(minute % blizzardsAtMinute.size());
        }

        @Override
        public String toString() {
            return "";
        }
    }

    record State(int minute, Pos pos) {
    }

    public static long solve1(String input) {
        var map = parse(input);
        var blizzardStates = calculateBlizzardStates(map);

        return findWay(map, blizzardStates, 0, map.start(), map.end());
    }

    public static long solve2(String input) {
        var map = parse(input);
        var blizzardStates = calculateBlizzardStates(map);

        var toGoal = findWay(map, blizzardStates, 0, map.start(), map.end());
        var toStart = findWay(map, blizzardStates, toGoal, map.end(), map.start());
        return findWay(map, blizzardStates, toStart, map.start(), map.end());
    }

    private static int findWay(BlizzardMap map, BlizzardStates blizzardStates, int startingMinute, Pos start, Pos end) {
        var posAtMinute = new HashMap<Integer, Set<Pos>>();

        var states = new ArrayDeque<State>();
        states.add(new State(startingMinute, start));

        var best = Integer.MAX_VALUE;

        long iteration = 0;
        long startTime = System.currentTimeMillis();

        while (!states.isEmpty()) {
            iteration++;
            var state = states.poll();

            if (iteration % 100_000 == 0) {
                System.out.println("Iteration " + iteration + ", states: " + states.size());
            }

            if (iteration > 5000000) {
                return best;
            }

            var currentPos = state.pos();

            // Only try a certain position at a certain minute once, because it all repeats from there.
            var wrappedMinute = state.minute() % blizzardStates.blizzardsAtMinute().size();
            if (posAtMinute.getOrDefault(wrappedMinute, Set.of()).contains(currentPos)) {
                continue;
            }

            posAtMinute.computeIfAbsent(wrappedMinute, HashSet::new).add(currentPos);

            if (state.minute() + currentPos.distance(end) > best) {
                continue;
            }

            var blizzards = blizzardStates.get(state.minute() + 1);

            var possibleMoves = new ArrayList<>(currentPos.neighbors());
            if (possibleMoves.contains(end)) {
                if (state.minute() + 1 < best) {
                    best = state.minute() + 1;
                    long time = System.currentTimeMillis() - startTime;
                    System.out.println(best + " - iteration " + iteration + ", " + time + "ms");
                }
                continue;
            }

            possibleMoves.add(currentPos);

            for (Pos pos : possibleMoves) {
                if (!map.contains(pos) && !pos.equals(start) && !pos.equals(end)) {
                    continue;
                }
                if (blizzards.contains(pos)) {
                    continue;
                }
                states.add(new State(state.minute() + 1, pos));
            }
        }

        return best;
    }

    private static BlizzardStates calculateBlizzardStates(BlizzardMap map) {
        // We only need to calculate this many minutes, after that everything repeats.
        int minutesToCalculate = Maths.lcm(map.width, map.height);
        var blizzards = new ArrayList<>(map.blizzards);
        var result = new LinkedHashMap<Integer, Set<Pos>>();
        for (int minute = 0; minute < minutesToCalculate; minute++) {
            var set = blizzards.stream().map(Blizzard::pos).collect(Collectors.toCollection(LinkedHashSet::new));
            result.put(minute, set);

            // Move blizzards
            for (int i = 0; i < blizzards.size(); i++) {
                var blizzard = blizzards.get(i);
                var nextPos = blizzard.pos().plus(blizzard.direction().step());
                var x = nextPos.x();
                var y = nextPos.y();
                x = (map.width() + x) % map.width();
                y = (map.height() + y) % map.height();

                blizzards.set(i, new Blizzard(new Pos(x, y), blizzard.direction()));
            }
        }
        return new BlizzardStates(result);
    }

    private static BlizzardMap parse(String input) {
        var blizzards = new ArrayList<Blizzard>();
        var lines = input.lines().toList();
        int width = lines.get(0).length() - 2;
        int height = lines.size() - 2;
        for (int i = 1; i < lines.size() - 1; i++) {
            var line = lines.get(i);
            var y = i - 1;
            for (int j = 1; j < line.length() - 1; j++) {
                var c = line.charAt(j);
                var x = j - 1;
                switch (c) {
                    case '^' -> blizzards.add(new Blizzard(new Pos(x, y), Direction.UP));
                    case '>' -> blizzards.add(new Blizzard(new Pos(x, y), Direction.RIGHT));
                    case 'v' -> blizzards.add(new Blizzard(new Pos(x, y), Direction.DOWN));
                    case '<' -> blizzards.add(new Blizzard(new Pos(x, y), Direction.LEFT));
                    case '.' -> {
                    }
                    default -> throw new IllegalStateException("Unexpected tile: " + c);
                }
            }
        }
        return new BlizzardMap(width, height, new Pos(0, -1), new Pos(width - 1, height), blizzards);
    }

    @Test
    void example() {
        final String input = """
                #.######
                #>>.<^<#
                #.<..<<#
                #>v.><>#
                #<^v^^>#
                ######.#
                """;
        assertEquals(18, solve1(input));
        assertEquals(54, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day24.txt"));
        assertEquals(292, solve1(input));
        assertEquals(816, solve2(input));
    }
}
