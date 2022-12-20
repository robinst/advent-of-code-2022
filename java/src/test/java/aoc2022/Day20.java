package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day20 {

    static class Node {
        long number;
        // Turns out "prev" wasn't actually needed, we can translate backwards moves to forward moves.
        Node prev;
        Node next;

        public Node(long number) {
            this.number = number;
        }

        void moveInFrontOf(Node newPrev) {
            prev.next = next;
            next.prev = prev;

            prev = newPrev;
            next = newPrev.next;

            newPrev.next.prev = this;
            newPrev.next = this;
        }

        @Override
        public String toString() {
            return "Node{" + number + ", prev=" + prev.number + ", next=" + next.number + "}";
        }
    }

    public static long solve1(String input) {
        var numbers = input.lines().map(Long::parseLong).toList();
        var nodes = createNodes(numbers);
        var first = nodes.get(0);
        var last = nodes.get(nodes.size() - 1);
        first.prev = last;
        last.next = first;

        mix(nodes);

        var zero = nodes.stream().filter(n -> n.number == 0).findFirst().get();
        printNodes(nodes, zero);

        return resultSum(zero);
    }

    public static long solve2(String input) {
        var numbers = input.lines().map(l -> Long.parseLong(l) * 811589153L).toList();
        var nodes = createNodes(numbers);
        var first = nodes.get(0);
        var last = nodes.get(nodes.size() - 1);
        first.prev = last;
        last.next = first;

        var zero = nodes.stream().filter(n -> n.number == 0).findFirst().get();
        for (int mix = 0; mix < 10; mix++) {
            System.out.println("mix " + mix);
            mix(nodes);
            printNodes(nodes, zero);
        }

        return resultSum(zero);
    }

    private static List<Node> createNodes(List<Long> numbers) {
        var nodes = new ArrayList<Node>();
        for (Long number : numbers) {
            var node = new Node(number);
            if (!nodes.isEmpty()) {
                var prev = nodes.get(nodes.size() - 1);
                node.prev = prev;
                prev.next = node;
            }
            nodes.add(node);
        }
        return nodes;
    }

    private static void mix(List<Node> nodes) {
        for (Node node : nodes) {
            long moves = node.number % (nodes.size() - 1);
            if (moves < 0) {
                moves = nodes.size() - 1 + moves;
            }

            if (moves == 0) {
                continue;
            }

            var newPrev = node;
            for (long i = 0; i < moves; i++) {
                newPrev = newPrev.next;
            }
            node.moveInFrontOf(newPrev);
        }
    }

    private static long resultSum(Node zero) {
        long sum = 0;
        var node = zero;
        for (int i = 1; i <= 3000; i++) {
            node = node.next;
            if (i == 1000 || i == 2000 || i == 3000) {
                sum += node.number;
            }
        }
        return sum;
    }

    private static void printNodes(List<Node> nodes, Node zero) {
        var node = zero;
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(node.number);
            System.out.print(" ");
            node = node.next;
        }
        System.out.println();
    }

    @Test
    void example() {
        final String input = """
                1
                2
                -3
                3
                -2
                0
                4
                """;
        assertEquals(3, solve1(input));
        assertEquals(1623178306L, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day20.txt"));
        assertEquals(3466, solve1(input));
        assertEquals(9995532008348L, solve2(input));
    }
}
