package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

record SensorAndBeacon(Pos sensor, Pos beacon, int distance) {
}

public class Day15 {

    public static long solve1(String input, int y) {
        var sensorAndBeacons = parse(input);
        var empties = new HashSet<Pos>();
        for (SensorAndBeacon sensorAndBeacon : sensorAndBeacons) {
            var sensor = sensorAndBeacon.sensor();
            var beacon = sensorAndBeacon.beacon();

            var distance = sensorAndBeacon.distance();
            var distanceY = Math.abs(y - sensor.y());

            if (distanceY <= distance) {
                int offsetX = distance - distanceY;
                for (int x = sensor.x() - offsetX; x <= sensor.x() + offsetX; x++) {
                    var pos = new Pos(x, y);
                    if (!pos.equals(beacon) && !pos.equals(sensor)) {
                        empties.add(pos);
                    }
                }
            }
        }
        return empties.size();
    }

    record Range(int from, int to) implements Comparable<Range> {
        @Override
        public int compareTo(Range o) {
            var first = Integer.compare(from(), o.from());
            if (first != 0) {
                return first;
            }
            return Integer.compare(to(), o.to());
        }
    }

    public static long solve2(String input, int maxXY) {
        var sensorAndBeacons = parse(input);

        for (int y = 0; y <= maxXY; y++) {
            var ranges = new ArrayList<Range>();
            for (SensorAndBeacon sensorAndBeacon : sensorAndBeacons) {
                var sensor = sensorAndBeacon.sensor();
                var distance = sensorAndBeacon.distance();
                int distanceY = Math.abs(y - sensor.y());
                if (distanceY <= distance) {
                    int offsetX = distance - distanceY;
                    ranges.add(new Range(sensor.x() - offsetX, sensor.x() + offsetX));
                }
            }
            ranges.sort(Comparator.naturalOrder());

            var first = ranges.get(0);
            if (first == null) {
                return y;
            }
            int fromX = first.from();
            if (fromX > 0) {
                // Gap before any ranges
                return y;
            }
            int toX = first.to();
            for (Range range : ranges.subList(1, ranges.size())) {
                if (range.to() <= toX) {
                    continue;
                }

                if (range.from() > toX + 1) {
                    // Gap!
                    return (((long) toX + 1) * 4000000) + y;
                }

                toX = range.to();
            }
            if (toX < maxXY) {
                // Gap after ranges
                return (((long) toX + 1) * 4000000) + y;
            }
        }

        return 0;
    }

    private static List<SensorAndBeacon> parse(String input) {
        return input.lines().map(line -> {
            var parts = line.split(": ");
            var sensor = parsePos(parts[0]);
            var beacon = parsePos(parts[1]);
            return new SensorAndBeacon(sensor, beacon, sensor.distance(beacon));
        }).toList();
    }

    private static Pos parsePos(String s) {
        var numbers = Parsing.numbers(s);
        return new Pos(numbers.get(0), numbers.get(1));
    }

    @Test
    void example() {
        final String input = """
                Sensor at x=2, y=18: closest beacon is at x=-2, y=15
                Sensor at x=9, y=16: closest beacon is at x=10, y=16
                Sensor at x=13, y=2: closest beacon is at x=15, y=3
                Sensor at x=12, y=14: closest beacon is at x=10, y=16
                Sensor at x=10, y=20: closest beacon is at x=10, y=16
                Sensor at x=14, y=17: closest beacon is at x=10, y=16
                Sensor at x=8, y=7: closest beacon is at x=2, y=10
                Sensor at x=2, y=0: closest beacon is at x=2, y=10
                Sensor at x=0, y=11: closest beacon is at x=2, y=10
                Sensor at x=20, y=14: closest beacon is at x=25, y=17
                Sensor at x=17, y=20: closest beacon is at x=21, y=22
                Sensor at x=16, y=7: closest beacon is at x=15, y=3
                Sensor at x=14, y=3: closest beacon is at x=15, y=3
                Sensor at x=20, y=1: closest beacon is at x=15, y=3
                """;
        assertEquals(26, solve1(input, 10));
        assertEquals(56000011, solve2(input, 20));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day15.txt"));
        assertEquals(4873353, solve1(input, 2_000_000));
        assertEquals(11600823139120L, solve2(input, 4_000_000));
    }
}
