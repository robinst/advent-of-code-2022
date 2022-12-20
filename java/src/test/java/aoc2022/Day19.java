package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Blueprint(int id, int oreRobotOres, int clayRobotOres, int obsidianRobotOres, int obsidianRobotClay,
                 int geodeRobotOres, int geodeRobotObsidian) {
}

record BlueprintState(int minutes, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, int ores,
                      int clay, int obsidian, int geodes) {

    public BlueprintState next(Blueprint blueprint, int newOreRobots, int newClayRobots, int newObsidianRobots, int newGeodeRobots) {
        int ores = this.ores;
        int clay = this.clay;
        int obsidian = this.obsidian;

        ores -= blueprint.oreRobotOres() * newOreRobots;
        ores -= blueprint.clayRobotOres() * newClayRobots;
        ores -= blueprint.obsidianRobotOres() * newObsidianRobots;
        clay -= blueprint.obsidianRobotClay() * newObsidianRobots;
        ores -= blueprint.geodeRobotOres() * newGeodeRobots;
        obsidian -= blueprint.geodeRobotObsidian() * newGeodeRobots;

        return new BlueprintState(minutes - 1, oreRobots + newOreRobots, clayRobots + newClayRobots, obsidianRobots + newObsidianRobots, geodeRobots + newGeodeRobots,
                ores + oreRobots, clay + clayRobots, obsidian + obsidianRobots, geodes + geodeRobots);
    }
}

public class Day19 {

    public static int solve1(String input) {
        var blueprints = parse(input);
        return blueprints.stream().mapToInt(b -> b.id() * best(b, 24)).sum();
    }

    public static int solve2(String input) {
        var blueprints = parse(input);
        return blueprints.stream().limit(3).mapToInt(b -> best(b, 32)).reduce(1, (a, b) -> a * b);
    }

    private static int best(Blueprint blueprint, int minutes) {
        System.out.println("Starting " + blueprint);
        int best = 0;

        var maxOres = Math.max(Math.max(Math.max(
                                blueprint.geodeRobotOres(),
                                blueprint.obsidianRobotOres()),
                        blueprint.clayRobotOres()),
                blueprint.oreRobotOres());
        var maxClay = blueprint.obsidianRobotClay();
        var maxObsidian = blueprint.geodeRobotObsidian();

        int untilFirstObsidianRobot = minutesUntilProduced(blueprint.obsidianRobotClay());
        int untilFirstGeodeRobot = minutesUntilProduced(blueprint.geodeRobotObsidian());

        long iteration = 0;
        long start = System.currentTimeMillis();

        var states = new ArrayDeque<BlueprintState>();
        states.add(new BlueprintState(minutes, 1, 0, 0, 0, 0, 0, 0, 0));
        while (!states.isEmpty()) {
            iteration++;

            var state = states.poll();

            if (state.clayRobots() == 0) {
                if (untilFirstObsidianRobot + untilFirstGeodeRobot >= state.minutes() || maxProduction(state.minutes() - untilFirstObsidianRobot - untilFirstGeodeRobot) <= best) {
                    continue;
                }
            } else if (state.obsidianRobots() == 0) {
                int minutesUntilObsidian = minutesUntilProduced(blueprint.obsidianRobotClay(), state.clay(), state.clayRobots());
                if (minutesUntilObsidian + untilFirstGeodeRobot >= state.minutes() || maxProduction(state.minutes() - untilFirstGeodeRobot - minutesUntilObsidian) <= best) {
                    continue;
                }
            } else if (state.geodeRobots() == 0 && maxProduction(state.minutes() - minutesUntilProduced(blueprint.geodeRobotObsidian(), state.obsidian(), state.obsidianRobots())) <= best) {
                continue;
            }

            int ores = state.ores();
            int clay = state.clay();
            int obsidian = state.obsidian();

            var nextStates = new LinkedList<BlueprintState>();

            // Try most advanced robot first
            // Reorder so that we try the most advanced robots first?
            var canBuildGeodeRobot = ores >= blueprint.geodeRobotOres() && obsidian >= blueprint.geodeRobotObsidian();
            var canBuildObsidianRobot = ores >= blueprint.obsidianRobotOres() && clay >= blueprint.obsidianRobotClay();
            var canBuildClayRobot = ores >= blueprint.clayRobotOres();
            var canBuildOreRobot = ores >= blueprint.oreRobotOres();

            if (canBuildGeodeRobot) {
                // Not sure why this works, but it does.
            } else if (canBuildOreRobot && canBuildClayRobot && canBuildObsidianRobot) {
                // Don't add a waiting step, doesn't make sense if we can build anything else we want.
            } else if ((canBuildOreRobot && canBuildClayRobot) && state.clayRobots() == 0) {
                // We can build either an ore or clay robot and don't have a clay one yet, doesn't make sense to wait.
            } else {
                // Don't build anything (wait).
                nextStates.add(state.next(blueprint, 0, 0, 0, 0));
            }

            // Not sure why this works, but it does.
            if (canBuildGeodeRobot) {
                nextStates.add(state.next(blueprint, 0, 0, 0, 1));
            } else {
                if (canBuildObsidianRobot && state.obsidianRobots() < maxObsidian && state.obsidian() < maxObsidian * state.minutes()) {
                    nextStates.add(state.next(blueprint, 0, 0, 1, 0));
                }
                if (canBuildClayRobot && state.clayRobots() < maxClay && state.clay() < maxClay * state.minutes()) {
                    nextStates.add(state.next(blueprint, 0, 1, 0, 0));
                }
                if (canBuildOreRobot && state.oreRobots() < maxOres && state.ores() < maxOres * state.minutes()) {
                    nextStates.add(state.next(blueprint, 1, 0, 0, 0));
                }
            }

            for (BlueprintState nextState : nextStates) {
                if (nextState.minutes() == 0) {
                    if (nextState.geodes() > best) {
                        var time = System.currentTimeMillis() - start;
                        System.out.println(nextState.geodes() + " - iteration " + iteration + ", " + time + "ms");
                        best = nextState.geodes();
                    }
                } else {
                    states.addFirst(nextState);
                }
            }
        }

        var time = System.currentTimeMillis() - start;
        System.out.println("Took " + iteration + " iterations, " + time + "ms");

        return best;
    }

    private static int maxProduction(int minutes) {
        int sum = 0;
        for (int i = 0; i < minutes; i++) {
            sum += i;
        }
        return sum;
    }

    private static int minutesUntilProduced(int count) {
        int minutes = 1;
        int sum = 0;
        while (true) {
            sum += minutes;
            if (sum >= count) {
                return minutes;
            }
            minutes++;
        }
    }

    private static int minutesUntilProduced(int count, int startingCount, int startingRobots) {
        int minutes = 0;
        int robots = startingRobots;
        int sum = startingCount;
        while (true) {
            sum += robots;
            if (sum >= count) {
                return minutes;
            }
            robots++;
            minutes++;
        }
    }

    private static List<Blueprint> parse(String input) {
        return input.lines().map(line -> {
            var numbers = Parsing.numbers(line);
            return new Blueprint(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3), numbers.get(4), numbers.get(5), numbers.get(6));
        }).toList();
    }

    @Test
    void examplePart1() {
        final String input = """
                Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
                Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
                """;
        assertEquals(33, solve1(input));
    }

    @Test
    void examplePart2() {
        final String input = """
                Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
                Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
                """;
        assertEquals(3472, solve2(input));
    }

    @Test
    void inputPart1() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day19.txt"));
        assertEquals(2160, solve1(input));
    }

    @Test
    void inputPart2() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day19.txt"));
        // took 12 minutes
        assertEquals(13340, solve2(input));
    }
}
