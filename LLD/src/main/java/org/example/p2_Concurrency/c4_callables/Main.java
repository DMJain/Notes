package org.example.p2_Concurrency.c4_callables;

import org.example.p2_Concurrency.c4_callables.examples.SumCalculator;

import java.util.concurrent.*;

/**
 * =============================================================================
 * CALLABLES & FUTURES IN JAVA
 * =============================================================================
 * 
 * This demo shows:
 * 1. The PROBLEM: Runnable can't return values
 * 2. The SOLUTION: Callable<V> returns values!
 * 3. Future<V>: Getting results from async tasks
 * 
 * Prerequisites:
 * - c2: Thread creation with Runnable (void run())
 * - c3: ExecutorService and thread pools
 * 
 * =============================================================================
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(70));
        System.out.println("                  CALLABLES & FUTURES DEMO                  ");
        System.out.println("=".repeat(70));

        // Demo 1: The Problem - Runnable can't return values
        demoProblem();

        // Demo 2: The Solution - Callable returns values!
        demoCallableSolution();

        // Demo 3: Parallel computation with multiple Futures
        demoParallelComputation();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("For more demos, check the examples/ folder:");
        System.out.println("  - FutureDemo.java    → All Future methods");
        System.out.println("  - MergeSorter.java   → Multi-threaded merge sort");
        System.out.println("=".repeat(70));
    }

    /**
     * DEMO 1: The Problem with Runnable
     * 
     * Runnable's void run() cannot return values.
     * We have to use ugly workarounds like shared variables.
     */
    private static void demoProblem() throws InterruptedException {
        System.out.println("\n--- DEMO 1: The Problem (Runnable can't return) ---");

        // Ugly workaround: shared array to store result
        final int[] result = new int[1];

        Runnable task = () -> {
            result[0] = 2 + 3; // Have to write to shared variable!
            System.out.println("Runnable computed: " + result[0]);
        };

        Thread t = new Thread(task);
        t.start();
        t.join();

        System.out.println("Main thread got: " + result[0]);
        System.out.println("⚠️ Problem: Required shared variable, no proper return!");
    }

    /**
     * DEMO 2: The Solution - Callable<V>
     * 
     * Callable's V call() can return any type!
     * We use Future.get() to retrieve the result.
     */
    private static void demoCallableSolution() throws Exception {
        System.out.println("\n--- DEMO 2: The Solution (Callable returns value) ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Callable<Integer> - returns an Integer!
        Callable<Integer> task = () -> {
            System.out.println("Callable computing 2 + 3...");
            Thread.sleep(500); // Simulate work
            return 2 + 3; // Clean return!
        };

        // submit() returns a Future - a "promise" of the result
        Future<Integer> future = executor.submit(task);

        System.out.println("Task submitted, Future received");
        System.out.println("isDone() = " + future.isDone());

        // get() blocks until result is ready
        Integer result = future.get();

        System.out.println("Result: " + result);
        System.out.println("isDone() = " + future.isDone());
        System.out.println("✅ Clean solution: Callable returns value directly!");

        executor.shutdown();
    }

    /**
     * DEMO 3: Parallel Computation
     * 
     * Submit multiple Callables and collect results.
     * Shows the power of Futures for parallel work.
     */
    private static void demoParallelComputation() throws Exception {
        System.out.println("\n--- DEMO 3: Parallel Computation ---");
        System.out.println("Computing sum(1..1M), sum(1..2M), sum(1..3M) in parallel");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        long start = System.currentTimeMillis();

        // Submit 3 tasks in parallel
        Future<Long> future1 = executor.submit(new SumCalculator(1_000_000));
        Future<Long> future2 = executor.submit(new SumCalculator(2_000_000));
        Future<Long> future3 = executor.submit(new SumCalculator(3_000_000));

        System.out.println("\nAll 3 tasks submitted, now collecting results...\n");

        // Collect results (blocks as needed)
        Long sum1 = future1.get();
        Long sum2 = future2.get();
        Long sum3 = future3.get();

        long duration = System.currentTimeMillis() - start;

        System.out.println("\nResults:");
        System.out.println("  sum(1..1M) = " + sum1);
        System.out.println("  sum(1..2M) = " + sum2);
        System.out.println("  sum(1..3M) = " + sum3);
        System.out.println("  Total time: " + duration + "ms (parallel!)");
        System.out.println("  If sequential, would be ~3x longer!");

        executor.shutdown();
    }
}
