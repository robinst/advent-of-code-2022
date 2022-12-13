package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Monkey<T> {
    int inspections = 0;
    LinkedList<T> items;
    Operation operation;
    int divisibleBy;
    int trueThrow;
    int falseThrow;
}

sealed interface Operation {
    long apply(long input);
}

record Times(long n) implements Operation {

    @Override
    public long apply(long input) {
        return input * n;
    }
}

record Plus(long n) implements Operation {

    @Override
    public long apply(long input) {
        return input + n;
    }
}

record Squared() implements Operation {
    @Override
    public long apply(long input) {
        return input * input;
    }
}

class Item {

    final Map<Long, Long> remainders;

    public Item(List<Long> divisors, long number) {
        remainders = new HashMap<>();
        for (Long divisor : divisors) {
            remainders.put(divisor, number % divisor);
        }
    }

    public void apply(Operation operation) {
        if (operation instanceof Plus p) {
            var n = p.n();
            for (Map.Entry<Long, Long> entry : remainders.entrySet()) {
                entry.setValue((entry.getValue() + n) % entry.getKey());
            }
        } else if (operation instanceof Times t) {
            var n = t.n();
            for (Map.Entry<Long, Long> entry : remainders.entrySet()) {
                entry.setValue((entry.getValue() * n) % entry.getKey());
            }
        } else if (operation instanceof Squared) {
            for (Map.Entry<Long, Long> entry : remainders.entrySet()) {
                entry.setValue((entry.getValue() * entry.getValue()) % entry.getKey());
            }
        }
    }

    public boolean divisibleBy(long divisibleBy) {
        return remainders.get(divisibleBy) == 0;
    }
}

public class Day11 {

    public static long solve1(String input) {
        var monkeys = parse(input);

        for (int round = 0; round < 20; round++) {
            for (Monkey<Long> monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    var item = monkey.items.removeFirst();
                    var level = monkey.operation.apply(item);
                    level /= 3;

                    if (level % monkey.divisibleBy == 0) {
                        monkeys.get(monkey.trueThrow).items.add(level);
                    } else {
                        monkeys.get(monkey.falseThrow).items.add(level);
                    }

                    monkey.inspections++;
                }
            }
        }

        var sorted = monkeys.stream().mapToLong(m -> m.inspections).sorted().toArray();
        return sorted[sorted.length - 1] * sorted[sorted.length - 2];
    }

    public static long solve2(String input) {
        var numberMonkeys = parse(input);
        var divisors = numberMonkeys.stream().map(m -> Long.valueOf(m.divisibleBy)).toList();

        var monkeys = numberMonkeys.stream().map(m -> {
            var monkey = new Monkey<Item>();
            monkey.items = new LinkedList<>(m.items.stream().map(i -> new Item(divisors, i)).toList());
            monkey.operation = m.operation;
            monkey.divisibleBy = m.divisibleBy;
            monkey.trueThrow = m.trueThrow;
            monkey.falseThrow = m.falseThrow;
            return monkey;
        }).toList();

        for (int round = 0; round < 10000; round++) {
            for (Monkey<Item> monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    var item = monkey.items.removeFirst();
                    item.apply(monkey.operation);

                    if (item.divisibleBy(monkey.divisibleBy)) {
                        monkeys.get(monkey.trueThrow).items.add(item);
                    } else {
                        monkeys.get(monkey.falseThrow).items.add(item);
                    }

                    monkey.inspections++;
                }
            }
        }

        var sorted = monkeys.stream().mapToLong(m -> m.inspections).sorted().toArray();
        return sorted[sorted.length - 1] * sorted[sorted.length - 2];
    }

    public static long solve2MuchSimpler(String input) {
        var monkeys = parse(input);
        var lcm = monkeys.stream().map(m -> m.divisibleBy).reduce(1, (a, b) -> a * b);
        System.out.println(lcm);

        for (int round = 0; round < 10000; round++) {
            for (Monkey<Long> monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    var item = monkey.items.removeFirst();
                    var level = monkey.operation.apply(item);
                    level %= lcm;

                    if (level % monkey.divisibleBy == 0) {
                        monkeys.get(monkey.trueThrow).items.add(level);
                    } else {
                        monkeys.get(monkey.falseThrow).items.add(level);
                    }

                    monkey.inspections++;
                }
            }
        }

        var sorted = monkeys.stream().mapToLong(m -> m.inspections).sorted().toArray();
        return sorted[sorted.length - 1] * sorted[sorted.length - 2];

    }

    private static List<Monkey<Long>> parse(String input) {
        var lines = input.lines().toList();
        var monkeys = new ArrayList<Monkey<Long>>();
        // Monkey 0:
        //   Starting items: 79, 98
        //   Operation: new = old * 19
        //   Test: divisible by 23
        //     If true: throw to monkey 2
        //     If false: throw to monkey 3
        for (String line : lines) {
            if (line.startsWith("Monkey ")) {
                monkeys.add(new Monkey<>());
            } else if (line.startsWith("  Starting items: ")) {
                var numbers = Parsing.numbersLong(line);
                monkeys.get(monkeys.size() - 1).items = new LinkedList<>(numbers);
            } else if (line.startsWith("  Operation: new = ")) {
                if (line.contains(" * ")) {
                    var numbers = Parsing.numbers(line);
                    if (numbers.size() == 1) {
                        monkeys.get(monkeys.size() - 1).operation = new Times(numbers.get(0));
                    } else {
                        monkeys.get(monkeys.size() - 1).operation = new Squared();
                    }
                } else if (line.contains(" + ")) {
                    monkeys.get(monkeys.size() - 1).operation = new Plus(Parsing.numbers(line).get(0));
                }
            } else if (line.startsWith("  Test: divisible by ")) {
                var num = Parsing.numbers(line).get(0);
                monkeys.get(monkeys.size() - 1).divisibleBy = num;
            } else if (line.startsWith("    If true: throw to monkey ")) {
                var num = Parsing.numbers(line).get(0);
                monkeys.get(monkeys.size() - 1).trueThrow = num;
            } else if (line.startsWith("    If false: throw to monkey ")) {
                var num = Parsing.numbers(line).get(0);
                monkeys.get(monkeys.size() - 1).falseThrow = num;
            } else if (line.isBlank()) {
                // Ignore
            } else {
                throw new IllegalStateException("Can't parse line: " + line);
            }
        }
        return monkeys;
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day11.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                Monkey 0:
                  Starting items: 79, 98
                  Operation: new = old * 19
                  Test: divisible by 23
                    If true: throw to monkey 2
                    If false: throw to monkey 3
                                
                Monkey 1:
                  Starting items: 54, 65, 75, 74
                  Operation: new = old + 6
                  Test: divisible by 19
                    If true: throw to monkey 2
                    If false: throw to monkey 0
                                
                Monkey 2:
                  Starting items: 79, 60, 97
                  Operation: new = old * old
                  Test: divisible by 13
                    If true: throw to monkey 1
                    If false: throw to monkey 3
                                
                Monkey 3:
                  Starting items: 74
                  Operation: new = old + 3
                  Test: divisible by 17
                    If true: throw to monkey 0
                    If false: throw to monkey 1
                """;
        assertEquals(10605, solve1(input));
        assertEquals(2713310158L, solve2(input));
        assertEquals(2713310158L, solve2MuchSimpler(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day11.txt"));
        assertEquals(61005, solve1(input));
        assertEquals(20567144694L, solve2(input));
        assertEquals(20567144694L, solve2MuchSimpler(input));
    }
}
