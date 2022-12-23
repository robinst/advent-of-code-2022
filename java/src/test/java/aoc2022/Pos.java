package aoc2022;

import java.util.Arrays;
import java.util.List;

public record Pos(int x, int y) {

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