package aoc2022.benchmarks;

import aoc2022.Day06;
import aoc2022.Resources;
import org.openjdk.jmh.annotations.*;

public class Day06Benchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @State(Scope.Benchmark)
    public static class InputState {

        String input;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            input = Resources.readString(Resources.class.getResource("/day06.txt"));
        }
    }

    @Benchmark
    public int solutionHashSet(InputState state) {
        return Day06.calculate(state.input, 14);
    }

    @Benchmark
    public int solutionFaster(InputState state) {
        return Day06.calculateFaster(state.input, 14);
    }
}
