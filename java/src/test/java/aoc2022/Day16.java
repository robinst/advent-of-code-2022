package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

record Tunnel(String name, int minutes) {
}

record Valve(String name, int flow, List<Tunnel> tunnels) {
}

record State(Valve current, Set<Valve> open, int released, int minutes, Set<Valve> visited,
             int flowing) implements Comparable<State> {

    public State(Valve current, Set<Valve> open, int released, int minute, Set<Valve> visited) {
        this(current, open, released, minute, visited, open.stream().mapToInt(Valve::flow).sum());
    }


    int nextReleased() {
        return released + flowing;
    }

    int releaseWithCurrentFlow() {
        return released + flowing * minutes;
    }

    int releaseWithAllFlows(int allFlows) {
        return releaseWithCurrentFlow() + (allFlows - flowing) * minutes;
    }

    @Override
    public int compareTo(State o) {
        var releaseCmp = Integer.compare(o.releaseWithCurrentFlow(), releaseWithCurrentFlow());
        if (releaseCmp != 0) {
            return releaseCmp;
        }
        return Integer.compare(o.minutes, minutes);
    }
}

record State2(List<Valve> currents, Set<Valve> open, int released, int minutes, List<Set<Valve>> visited,
              int flowing) implements Comparable<State2> {

    public State2(List<Valve> currents, Set<Valve> open, int released, int minute, List<Set<Valve>> visited) {
        this(currents, open, released, minute, visited, open.stream().mapToInt(Valve::flow).sum());
    }

    int nextReleased() {
        return released + flowing;
    }

    int releaseWithCurrentFlow() {
        return released + flowing * minutes;
    }

    int releaseWithAllFlows(int allFlows) {
        return releaseWithCurrentFlow() + (allFlows - flowing) * minutes;
    }

    @Override
    public int compareTo(State2 o) {
        var releaseCmp = Integer.compare(o.releaseWithCurrentFlow(), releaseWithCurrentFlow());
        if (releaseCmp != 0) {
            return releaseCmp;
        }
        return Integer.compare(o.minutes, minutes);
    }
}

public class Day16 {

    public static long solve1(String input) {
        var graph = parse(input);
        var withFlow = graph.values().stream().filter(valve -> valve.flow() > 0).toList();
        var allFlows = withFlow.stream().mapToInt(Valve::flow).sum();

        var states = new PriorityQueue<State>();
        states.add(new State(graph.get("AA"), Set.of(), 0, 30, Set.of(graph.get("AA"))));

        var bestResult = 0;

        while (!states.isEmpty()) {
            var state = states.poll();
            var current = state.current();

            if (state.minutes() == 0) {
                // End of this candidate (even if not all flows have been turned on)
                var endReleased = state.released();
                if (endReleased > bestResult) {
                    System.out.println(endReleased);
                    bestResult = endReleased;
                }
                continue;
            }

            if (state.releaseWithCurrentFlow() > bestResult) {
                bestResult = state.releaseWithCurrentFlow();
                System.out.println(bestResult);
            } else if (state.releaseWithAllFlows(allFlows) <= bestResult) {
                // If we could instantly turn on all remaining valves, and we still wouldn't beat
                // our current best, we can prune this branch.
                continue;
            }

            if (current.flow() > 0 && !state.open().contains(current)) {
                // We can open this, add a candidate
                var open = new HashSet<>(state.open());
                open.add(current);
                // This is the last one, we don't have to explore this anymore, just calculate
                if (open.size() == withFlow.size()) {
                    var endReleased = new State(current, open, state.nextReleased(), state.minutes() - 1, Set.of(current)).releaseWithCurrentFlow();
                    if (endReleased > bestResult) {
                        System.out.println(bestResult);
                        bestResult = endReleased;
                    }
                    continue;
                } else {
                    states.add(new State(current, open, state.nextReleased(), state.minutes() - 1, Set.of(current)));
                }
            }

            // We can move to any tunnel, add candidates
            var tunnels = current.tunnels();
            for (Tunnel tunnel : tunnels) {
                var dest = graph.get(tunnel.name());
                if (!state.visited().contains(dest)) {
                    var visited = new HashSet<>(state.visited());
                    visited.add(dest);
                    states.add(new State(dest, state.open(), state.nextReleased(), state.minutes() - tunnel.minutes(), visited));
                }
            }
        }

        return bestResult;
    }

    public static long solve2(String input) {
        var graph = parse(input);
        var withFlow = graph.values().stream().filter(valve -> valve.flow() > 0).toList();
        var allFlows = withFlow.stream().mapToInt(Valve::flow).sum();

        var states = new PriorityQueue<State2>();
        var start = graph.get("AA");
        states.add(new State2(List.of(start, start), Set.of(), 0, 26, List.of(Set.of(start), Set.of(start))));

        var bestResult = 0;

        while (!states.isEmpty()) {
            var state = states.poll();

            if (state.minutes() == 0) {
                // End of this candidate (even if not all flows have been turned on)
                var endReleased = state.released();
                if (endReleased > bestResult) {
                    System.out.println(endReleased);
                }
                bestResult = Math.max(bestResult, endReleased);
                continue;
            }

            if (state.releaseWithCurrentFlow() > bestResult) {
                bestResult = state.releaseWithCurrentFlow();
                System.out.println(bestResult);
            } else if (state.releaseWithAllFlows(allFlows) <= bestResult) {
                // If we could instantly turn on all remaining valves, and we still wouldn't beat
                // our current best, we can prune this branch.
                continue;
            }

            // Opt: Don't add the ones that have all valves open, just calculate final flow and discard.
            var newStates = calculateStates(state, 0, graph);
            for (State2 newState : newStates) {
                if (newState.open().size() == withFlow.size()) {
                    // Don't need to progress these, already all open.
                    var endReleased = newState.releaseWithCurrentFlow();
                    if (endReleased > bestResult) {
                        System.out.println(endReleased);
                    }
                    bestResult = Math.max(bestResult, endReleased);
                } else {
                    states.add(newState);
                }
            }
        }

        return bestResult;
    }

    private static List<State2> calculateStates(State2 state, int who, Map<String, Valve> graph) {
        var states = new ArrayList<State2>();
        var current = state.currents().get(who);
        if (current.flow() > 0 && !state.open().contains(current)) {
            // We can open this, add a candidate
            var open = new HashSet<>(state.open());
            open.add(current);

            if (who == 0) {
                var newState = new State2(state.currents(), open, state.nextReleased(), state.minutes() - 1, List.of(Set.of(current), state.visited().get(1)));
                states.addAll(calculateStates(newState, 1, graph));
            } else {
                var newState = new State2(state.currents(), open, state.released(), state.minutes(), List.of(state.visited().get(0), Set.of(current)));
                states.add(newState);
            }
        }

        // We can move to any tunnel, add candidates
        var tunnels = current.tunnels();
        for (Tunnel tunnel : tunnels) {
            var dest = graph.get(tunnel.name());
            if (!state.visited().get(who).contains(dest)) {
                var visited = new HashSet<>(state.visited().get(who));
                visited.add(dest);
                if (who == 0) {
                    var newState = new State2(List.of(dest, state.currents().get(1)), state.open(), state.nextReleased(), state.minutes() - 1, List.of(visited, state.visited().get(1)));
                    states.addAll(calculateStates(newState, 1, graph));
                } else {
                    var newState = new State2(List.of(state.currents().get(0), dest), state.open(), state.released(), state.minutes(), List.of(state.visited().get(0), visited));
                    states.add(newState);
                }
            }
        }

        return states;
    }

    private static Map<String, Valve> parse(String input) {
        var lines = input.lines().toList();
        var result = new HashMap<String, Valve>();
        for (String line : lines) {
            var parts = line.split("; ");
            var name = parts[0].split(" ")[1];
            var flow = Integer.parseInt(parts[0].split("=")[1]);
            var tunnelNames = parts[1].split(" to valves? ")[1].split(", ");
            var tunnels = Arrays.stream(tunnelNames).map(t -> new Tunnel(t, 1)).toList();
            result.put(name, new Valve(name, flow, tunnels));
        }
        return result;
    }

    @Test
    void example() {
        final String input = """
                Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
                Valve BB has flow rate=13; tunnels lead to valves CC, AA
                Valve CC has flow rate=2; tunnels lead to valves DD, BB
                Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
                Valve EE has flow rate=3; tunnels lead to valves FF, DD
                Valve FF has flow rate=0; tunnels lead to valves EE, GG
                Valve GG has flow rate=0; tunnels lead to valves FF, HH
                Valve HH has flow rate=22; tunnel leads to valve GG
                Valve II has flow rate=0; tunnels lead to valves AA, JJ
                Valve JJ has flow rate=21; tunnel leads to valve II
                """;
        assertEquals(1651, solve1(input));
        assertEquals(1707, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day16.txt"));
        assertEquals(1792, solve1(input));
        // Runs in about 1 hour, heh. Should optimize by pre-calculating shortest paths.
//        assertEquals(2587, solve2(input));
    }
}
