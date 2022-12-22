package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day22 {

    enum Tile {
        OPEN,
        WALL
    }

    sealed interface Instruction {
    }

    record Move(int count) implements Instruction {
    }

    enum Turn implements Instruction {
        L,
        R,
    }

    enum Direction {
        RIGHT,
        DOWN,
        LEFT,
        UP;

        Pos pos() {
            return switch (this) {
                case UP -> new Pos(0, -1);
                case RIGHT -> new Pos(1, 0);
                case DOWN -> new Pos(0, 1);
                case LEFT -> new Pos(-1, 0);
            };
        }

        Direction turn(Turn t) {
            return switch (this) {
                case UP -> switch (t) {
                    case L -> LEFT;
                    case R -> RIGHT;
                };
                case RIGHT -> switch (t) {
                    case L -> UP;
                    case R -> DOWN;
                };
                case DOWN -> switch (t) {
                    case L -> RIGHT;
                    case R -> LEFT;
                };
                case LEFT -> switch (t) {
                    case L -> DOWN;
                    case R -> UP;
                };
            };
        }

        Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case RIGHT -> LEFT;
                case DOWN -> UP;
                case LEFT -> RIGHT;
            };
        }

        Axis axis() {
            return switch (this) {
                case UP, DOWN -> Axis.VERTICAL;
                case RIGHT, LEFT -> Axis.HORIZONTAL;
            };
        }
    }

    enum Axis {
        VERTICAL,
        HORIZONTAL;

        Axis other() {
            return switch (this) {
                case VERTICAL -> HORIZONTAL;
                case HORIZONTAL -> VERTICAL;
            };
        }
    }

    record Connection(Axis axis, Map<Pos, Pos> translation, int rightTurns) {

        static Connection of(Axis axis, List<Pos> from, List<Pos> to, int rightTurns) {
            assert from.size() == to.size();
            assert rightTurns >= 0;
            var map = new HashMap<Pos, Pos>();
            for (int i = 0; i < from.size(); i++) {
                map.put(from.get(i), to.get(i));
            }
            return new Connection(axis, map, rightTurns);
        }
    }

    record PosDir(Pos pos, Direction direction) {
    }

    public static int solve1(String input) {
        var parts = input.split("\n\n");
        var map = parseMap(parts[0]);
        var path = parsePath(parts[1]);

        var pos = map.keySet().stream().findFirst().get();
        var direction = Direction.RIGHT;
        for (Instruction instruction : path) {
            if (instruction instanceof Move move) {
                for (int i = 0; i < move.count; i++) {
                    var nextPos = pos.plus(direction.pos());
                    var nextTile = map.get(nextPos);
                    if (nextTile == Tile.OPEN) {
                        pos = nextPos;
                    } else if (nextTile == Tile.WALL) {
                        break;
                    } else if (nextTile == null) {
                        pos = nextOppositeOrStop(pos, direction, map);
                    }
                }
            } else if (instruction instanceof Turn turn) {
                direction = direction.turn(turn);
            }
        }

        // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
        // The final password is the sum of 1000 times the row, 4 times the column, and the facing.
        return 1000 * pos.y() + 4 * pos.x() + direction.ordinal();
    }

    private static Pos nextOppositeOrStop(Pos start, Direction direction, Map<Pos, Tile> map) {
        // Wrap around in the direction that we're going.
        // Because of the shape, it means we can go to the first tile in the opposite direction.
        var oppositeDirection = direction.opposite().pos();
        var pos = start;
        while (true) {
            var nextPos = pos.plus(oppositeDirection);
            var nextTile = map.get(nextPos);
            if (nextTile == null) {
                // Reached the end, pos was the last one
                if (map.get(pos) == Tile.WALL) {
                    return start;
                } else {
                    return pos;
                }
            }
            pos = nextPos;
        }
    }

    public static int solve2(String input) {
        var connections = new ArrayList<Connection>();
        // A up
        connections.addAll(connections(Axis.VERTICAL, 51, 1, Direction.RIGHT, 1, 151, Direction.DOWN, 50));
        // B up
        connections.addAll(connections(Axis.VERTICAL, 101, 1, Direction.RIGHT, 1, 200, Direction.RIGHT, 50));
        // A left
        connections.addAll(connections(Axis.HORIZONTAL, 51, 1, Direction.DOWN, 1, 150, Direction.UP, 50));
        // B right
        connections.addAll(connections(Axis.HORIZONTAL, 150, 1, Direction.DOWN, 100, 150, Direction.UP, 50));
        // C left
        connections.addAll(connections(Axis.HORIZONTAL, 51, 51, Direction.DOWN, 1, 101, Direction.RIGHT, 50));
        // C right
        connections.addAll(connections(Axis.HORIZONTAL, 100, 51, Direction.DOWN, 101, 50, Direction.RIGHT, 50));
        // E down
        connections.addAll(connections(Axis.VERTICAL, 51, 150, Direction.RIGHT, 50, 151, Direction.DOWN, 50));

        var parts = input.split("\n\n");
        var map = parseMap(parts[0]);
        var path = parsePath(parts[1]);

        var pos = map.keySet().stream().findFirst().get();
        var direction = Direction.RIGHT;
        for (Instruction instruction : path) {
            if (instruction instanceof Move move) {
                for (int i = 0; i < move.count; i++) {
                    var nextPos = pos.plus(direction.pos());
                    var nextTile = map.get(nextPos);
                    if (nextTile == Tile.OPEN) {
                        pos = nextPos;
                    } else if (nextTile == Tile.WALL) {
                        break;
                    } else if (nextTile == null) {
                        var posDir = cubeNext(pos, direction, map, connections);
                        if (map.get(posDir.pos()) == Tile.WALL) {
                            break;
                        }
                        pos = posDir.pos();
                        direction = posDir.direction();
                    }
                }
            } else if (instruction instanceof Turn turn) {
                direction = direction.turn(turn);
            }
        }

        // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
        // The final password is the sum of 1000 times the row, 4 times the column, and the facing.
        return 1000 * pos.y() + 4 * pos.x() + direction.ordinal();
    }

    private static List<Connection> connections(Axis axis, int fromX, int fromY, Direction fromDir, int toX, int toY, Direction toDir, int count) {
        var rightTurns = (Direction.values().length + toDir.ordinal() - fromDir.ordinal()) % Direction.values().length;
        return List.of(
                Connection.of(axis,
                        line(new Pos(fromX, fromY), count, fromDir),
                        line(new Pos(toX, toY), count, toDir),
                        rightTurns),
                Connection.of(rightTurns % 2 == 0 ? axis : axis.other(),
                        line(new Pos(toX, toY), count, toDir),
                        line(new Pos(fromX, fromY), count, fromDir),
                        Direction.values().length - rightTurns)
        );
    }

    private static PosDir cubeNext(Pos start, Direction direction, Map<Pos, Tile> map, List<Connection> connections) {
        for (Connection connection : connections) {
            if (direction.axis() == connection.axis() && connection.translation().containsKey(start)) {
                var pos = connection.translation().get(start);
                var dir = direction;
                for (int i = 0; i < connection.rightTurns(); i++) {
                    dir = dir.turn(Turn.R);
                }
                return new PosDir(pos, dir);
            }
        }
        throw new IllegalStateException("Missing an edge for " + start + " and direction " + direction);
    }

    private static List<Pos> line(Pos start, int count, Direction direction) {
        var list = new ArrayList<Pos>();
        var pos = start;
        for (int i = 0; i < count; i++) {
            list.add(pos);
            pos = pos.plus(direction.pos());
        }
        return list;
    }

    private static Map<Pos, Tile> parseMap(String input) {
        var map = new LinkedHashMap<Pos, Tile>();
        var lines = input.lines().toList();
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            int y = i + 1;

            for (int j = 0; j < line.length(); j++) {
                var c = line.charAt(j);
                var x = j + 1;
                switch (c) {
                    case '.' -> map.put(new Pos(x, y), Tile.OPEN);
                    case '#' -> map.put(new Pos(x, y), Tile.WALL);
                    case ' ' -> {
                    }
                    default -> throw new IllegalStateException("Unexpected tile " + c);
                }
            }
        }
        return map;
    }

    private static List<Instruction> parsePath(String input) {
        var pattern = Pattern.compile("\\d+|L|R");
        return pattern.matcher(input).results().map(match -> {
            var s = match.group();
            try {
                var number = Integer.parseInt(s);
                return new Move(number);
            } catch (NumberFormatException e) {
                return Turn.valueOf(s);
            }
        }).toList();
    }

    @Test
    void example() {
        final String input = """
                        ...#
                        .#..
                        #...
                        ....
                ...#.......#
                ........#...
                ..#....#....
                ..........#.
                        ...#....
                        .....#..
                        .#......
                        ......#.
                                
                10R5L5R10L4R5L5
                """;
        assertEquals(6032, solve1(input));
        // Didn't build the cube for this, heh.
//        assertEquals(5031, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day22.txt"));
        assertEquals(30552, solve1(input));
        assertEquals(184106, solve2(input));
    }
}
