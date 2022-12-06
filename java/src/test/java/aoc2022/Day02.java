package aoc2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


enum Hand {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    final int value;

    Hand(int value) {
        this.value = value;
    }
}

// A for Rock, B for Paper, and C for Scissors
public class Day02 {

    public static long solve1(String input) {
        return input.lines()
                .mapToLong(Day02::score1)
                .sum();
    }

    public static long score1(String game) {
        final String[] s = game.split(" ");
        var theirs = parse(s[0]);
        var mine = parse(s[1]);

        if ((mine == Hand.ROCK && theirs == Hand.SCISSORS) ||
                (mine == Hand.PAPER && theirs == Hand.ROCK) ||
                (mine == Hand.SCISSORS && theirs == Hand.PAPER)) {
            return mine.value + 6;
        } else if (mine == theirs) {
            return mine.value + 3;
        } else {
            return mine.value;
        }
    }

    public static Hand parse(String s) {
        return switch (s) {
            case "A", "X" -> Hand.ROCK;
            case "B", "Y" -> Hand.PAPER;
            case "C", "Z" -> Hand.SCISSORS;
            default -> throw new IllegalStateException("Unknown input: " + s);
        };
    }

    public static long solve2(String input) {
        return input.lines()
                .mapToLong(Day02::score2)
                .sum();
    }

    public static long score2(String game) {
        final String[] s = game.split(" ");
        var theirs = parse(s[0]);
        var mine = s[1];

        // X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
        switch (mine) {
            case "X" -> {
                switch (theirs) {
                    case ROCK -> {
                        return Hand.SCISSORS.value;
                    }
                    case PAPER -> {
                        return Hand.ROCK.value;
                    }
                    case SCISSORS -> {
                        return Hand.PAPER.value;
                    }
                }
            }
            case "Y" -> {
                return theirs.value + 3;
            }
            case "Z" -> {
                switch (theirs) {
                    case ROCK -> {
                        return Hand.PAPER.value + 6;
                    }
                    case PAPER -> {
                        return Hand.SCISSORS.value + 6;
                    }
                    case SCISSORS -> {
                        return Hand.ROCK.value + 6;
                    }
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day02.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                A Y
                B X
                C Z
                """;
        assertEquals(15, solve1(input));
        assertEquals(12, solve2(input));
    }
}
