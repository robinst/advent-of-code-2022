package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

record ParseResult(Packet packet, int index) {
}

sealed interface Packet extends Comparable<Packet> {
}

record IntegerPacket(int number) implements Packet {

    @Override
    public int compareTo(Packet o) {
        if (o instanceof IntegerPacket i) {
            return Integer.compare(number, i.number);
        } else {
            return new ListPacket(List.of(this)).compareTo(o);
        }
    }
}

record ListPacket(List<Packet> list) implements Packet {

    @Override
    public int compareTo(Packet o) {
        if (!(o instanceof ListPacket that)) {
            return compareTo(new ListPacket(List.of(o)));
        }

        for (int i = 0; i < Math.min(list.size(), that.list().size()); i++) {
            var cmp = list.get(i).compareTo(that.list().get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(list.size(), that.list().size());
    }
}

public class Day13 {

    public static int solve1(String input) {
        var pairs = input.split("\n\n");
        var sum = 0;
        for (int i = 0; i < pairs.length; i++) {
            var pair = pairs[i];
            var lines = pair.split("\n");
            var left = parseLine(lines[0]);
            var right = parseLine(lines[1]);

            if (left.compareTo(right) < 0) {
                sum += i + 1;
            }
        }
        return sum;
    }

    public static int solve2(String input) {
        var pairs = input.split("\n+");
        var values = Arrays.stream(pairs)
                .map(Day13::parseLine)
                .collect(Collectors.toCollection(ArrayList::new));
        var marker1 = parseLine("[[2]]");
        var marker2 = parseLine("[[6]]");
        values.add(marker1);
        values.add(marker2);

        values.sort(Comparator.naturalOrder());
        return (values.indexOf(marker1) + 1) * (values.indexOf(marker2) + 1);
    }

    private static Packet parseLine(String line) {
        return parse(line, 0).packet();
    }

    private static ParseResult parse(String s, int index) {
        if (s.charAt(index) == '[') {
            return parseList(s, index + 1);
        } else {
            var digitEnd = findDigitEnd(s, index);
            var number = Integer.parseInt(s.substring(index, digitEnd));
            return new ParseResult(new IntegerPacket(number), digitEnd);
        }
    }

    private static ParseResult parseList(String s, int index) {
        var result = new ArrayList<Packet>();
        while (s.charAt(index) != ']') {
            var parseResult = parse(s, index);
            result.add(parseResult.packet());
            index = parseResult.index();

            if (s.charAt(index) == ',') {
                index++;
            }
        }
        index++;
        return new ParseResult(new ListPacket(result), index);
    }

    private static int findDigitEnd(String s, int index) {
        for (int i = index; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return i;
            }
        }
        return s.length();
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day13.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                [1,1,3,1,1]
                [1,1,5,1,1]
                                
                [[1],[2,3,4]]
                [[1],4]
                                
                [9]
                [[8,7,6]]
                                
                [[4,4],4,4]
                [[4,4],4,4,4]
                                
                [7,7,7,7]
                [7,7,7]
                                
                []
                [3]
                                
                [[[]]]
                [[]]
                                
                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
                """;
        assertEquals(13, solve1(input));
        assertEquals(140, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day13.txt"));
        assertEquals(5506, solve1(input));
        assertEquals(21756, solve2(input));
    }
}
