package aoc2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

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

    List<Pos> straightLineToIncluding(Pos to) {
        if (x() == to.x()) {
            // Vertical
            var fromY = Math.min(y(), to.y());
            var toY = Math.max(y(), to.y());
            return IntStream.rangeClosed(fromY, toY)
                    .mapToObj(y -> new Pos(x(), y))
                    .collect(toCollection(ArrayList::new));
        } else if (y() == to.y()) {
            // Horizontal
            var fromX = Math.min(x(), to.x());
            var toX = Math.max(x(), to.x());
            return IntStream.rangeClosed(fromX, toX)
                    .mapToObj(x -> new Pos(x, y()))
                    .collect(toCollection(ArrayList::new));
        } else {
            throw new IllegalStateException("Not a straight line from " + this + " to " + to);
        }
    }
}
