package aoc2022.benchmarks;

import java.util.function.Supplier;

public class Timing {

    /**
     * Quick one-shot timing without proper (but slow) microbenchmark.
     */
    public static <T> T time(Supplier<T> supplier) {
        var start = System.nanoTime();
        var result = supplier.get();
        var nanos = System.nanoTime() - start;
        System.out.println("Took " + nanos + " ns");
        return result;
    }
}
