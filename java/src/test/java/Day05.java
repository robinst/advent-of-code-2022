import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Move(int count, int from, int to) {
    static Move parse(String s) {
        // move 1 from 2 to 1
        List<String> numbers = Arrays.stream(s.split("[^\\d]+")).filter(p -> !p.isEmpty()).toList();
        return new Move(Integer.parseInt(numbers.get(0)), Integer.parseInt(numbers.get(1)), Integer.parseInt(numbers.get(2)));
    }
}

public class Day05 {

    public static String solve1(ArrayList<ArrayList<String>> stacks, String instructions) {
        instructions.lines().forEach(line -> {
            var move = Move.parse(line);
            for (int i = 0; i < move.count(); i++) {
                var from = stacks.get(move.from() - 1);
                var to = stacks.get(move.to() - 1);
                String crate = from.remove(from.size() - 1);
                to.add(crate);
            }
        });
        return stacks.stream()
                .map(s -> s.get(s.size() - 1))
                .collect(Collectors.joining(""));
    }

    public static String solve2(ArrayList<ArrayList<String>> stacks, String instructions) {
        instructions.lines().forEach(line -> {
            var move = Move.parse(line);
            var from = stacks.get(move.from() - 1);
            var to = stacks.get(move.to() - 1);

            List<String> moving = new ArrayList<>();
            int toRemove = from.size() - move.count();
            for (int i = 0; i < move.count(); i++) {
                moving.add(from.remove(toRemove));
            }
            to.addAll(moving);
        });
        return stacks.stream()
                .map(s -> s.get(s.size() - 1))
                .collect(Collectors.joining(""));
    }

    @Test
    void example() {
        //     [D]
        // [N] [C]
        // [Z] [M] [P]
        //  1   2   3
        var stacks = new String[][]{{"N", "Z"}, {"D", "C", "M"}, {"P"}};
        var newStacks = reverseStacks(stacks);
        var newStacks2 = reverseStacks(stacks);

        final String input = """
                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
                """;
        assertEquals("CMZ", solve1(newStacks, input));
        assertEquals("MCD", solve2(newStacks2, input));
    }

    @Test
    void input() throws Exception {
        //         [F] [Q]         [Q]
        // [B]     [Q] [V] [D]     [S]
        // [S] [P] [T] [R] [M]     [D]
        // [J] [V] [W] [M] [F]     [J]     [J]
        // [Z] [G] [S] [W] [N] [D] [R]     [T]
        // [V] [M] [B] [G] [S] [C] [T] [V] [S]
        // [D] [S] [L] [J] [L] [G] [G] [F] [R]
        // [G] [Z] [C] [H] [C] [R] [H] [P] [D]
        //  1   2   3   4   5   6   7   8   9
        var stacks = new String[][]{
                {"B", "S", "J", "Z", "V", "D", "G"},
                {"P", "V", "G", "M", "S", "Z"},
                {"F", "Q", "T", "W", "S", "B", "L", "C"},
                {"Q", "V", "R", "M", "W", "G", "J", "H"},
                {"D", "M", "F", "N", "S", "L", "C"},
                {"D", "C", "G", "R"},
                {"Q", "S", "D", "J", "R", "T", "G", "H"},
                {"V", "F", "P"},
                {"J", "T", "S", "R", "D"}
        };

        var newStacks = reverseStacks(stacks);
        var newStacks2 = reverseStacks(stacks);

        var instructions = Resources.readString(Resources.class.getResource("/day05.txt"));
        assertEquals("WCZTHTMPS", solve1(newStacks, instructions));
        assertEquals("BLSGJSDTS", solve2(newStacks2, instructions));
    }

    private static ArrayList<ArrayList<String>> reverseStacks(String[][] stacks) {
        var newStacks = new ArrayList<ArrayList<String>>();
        for (String[] stack : stacks) {
            var list = new ArrayList<>(Arrays.asList(stack));
            Collections.reverse(list);
            newStacks.add(list);
        }
        return newStacks;
    }
}
