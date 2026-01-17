package org.example.p3_Java_Advance_Concept.c3_streams.examples;

import java.util.*;
import java.util.stream.*;

/**
 * Demonstrates lazy evaluation and parallel streams.
 */
public class AdvancedStreamDemo {

    /**
     * Demonstrates lazy evaluation with peek().
     */
    public static void demoLazyEvaluation() {
        System.out.println("   🔹 Lazy Evaluation Demo:");
        System.out.println("      Finding first even number > 5...\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Optional<Integer> result = numbers.stream()
                .filter(n -> {
                    System.out.println("      Filter checking: " + n);
                    return n % 2 == 0;
                })
                .filter(n -> {
                    System.out.println("      Second filter (>5): " + n);
                    return n > 5;
                })
                .findFirst();

        System.out.println("\n      Result: " + result.orElse(null));
        System.out.println("      Notice: Processing stopped early (short-circuiting)!");
    }

    /**
     * Demonstrates sequential vs parallel streams.
     */
    public static void demoParallel() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);

        System.out.println("\n   🔹 Sequential stream:");
        numbers.stream()
                .forEach(n -> System.out.println("      " + n + " - " +
                        Thread.currentThread().getName()));

        System.out.println("\n   🔹 Parallel stream:");
        numbers.parallelStream()
                .forEach(n -> System.out.println("      " + n + " - " +
                        Thread.currentThread().getName()));

        System.out.println("\n      Notice: Parallel uses ForkJoinPool workers.");
    }

    /**
     * Demonstrates performance comparison.
     */
    public static void demoPerformance() {
        System.out.println("\n   🔹 Performance comparison (sum 1 to 10,000,000):");

        // Sequential
        long start = System.currentTimeMillis();
        long seqSum = LongStream.rangeClosed(1, 10_000_000).sum();
        long seqTime = System.currentTimeMillis() - start;

        // Parallel
        start = System.currentTimeMillis();
        long parSum = LongStream.rangeClosed(1, 10_000_000).parallel().sum();
        long parTime = System.currentTimeMillis() - start;

        System.out.println("      Sequential: " + seqSum + " in " + seqTime + "ms");
        System.out.println("      Parallel: " + parSum + " in " + parTime + "ms");
        System.out.println("\n      ⚠️ Parallel isn't always faster (overhead)!");
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 ADVANCED: LAZY EVALUATION & PARALLEL\n");
        demoLazyEvaluation();
        demoParallel();
        demoPerformance();
    }
}
