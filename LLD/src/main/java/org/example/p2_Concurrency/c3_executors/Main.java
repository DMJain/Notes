package org.example.p2_Concurrency.c3_executors;

import org.example.p2_Concurrency.c3_executors.examples.NumberPrinterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * =============================================================================
 * EXECUTORS IN JAVA
 * =============================================================================
 * 
 * This demo shows:
 * 1. The PROBLEM with manual thread creation
 * 2. The SOLUTION using ExecutorService
 * 3. Different thread pool types
 * 
 * Prerequisites:
 * - c1: Understanding of concurrency and processes
 * - c2: Manual thread creation with Thread and Runnable
 * 
 * =============================================================================
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("                     EXECUTORS DEMO                     ");
        System.out.println("=".repeat(70));

        // Demo 1: The Problem - Manual Thread Creation
        demoProblem();

        // Demo 2: The Solution - Using Executor Framework
        demoSolution();

        // Demo 3: Run full thread pool types demo
        System.out.println("\n" + "=".repeat(70));
        System.out.println("For detailed demos of all pool types, run ThreadPoolTypesDemo.main()");
        System.out.println("=".repeat(70));

        // Uncomment to see all pool types in action:
        // ThreadPoolTypesDemo.main(args);
    }

    /**
     * DEMO 1: The Problem with Manual Thread Creation
     * 
     * This is what we learned in c2. It works, but has problems:
     * - Creates 10 threads for 10 tasks (expensive!)
     * - Each thread created, used once, then destroyed
     * - No control over how many threads run simultaneously
     */
    private static void demoProblem() throws InterruptedException {
        System.out.println("\n--- DEMO 1: The Problem (Manual Thread Creation) ---");
        System.out.println("Creating 10 threads for 10 tasks...\n");

        long startTime = System.currentTimeMillis();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threads[i] = new Thread(() -> {
                System.out.println("Task " + taskId + " on " +
                        Thread.currentThread().getName());
            });
            threads[i].start();
        }

        // Wait for all to complete
        for (Thread t : threads) {
            t.join();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\nManual approach took: " + duration + "ms");
        System.out.println("Problem: Created 10 threads just for 10 small tasks!");
        System.out.println("         Each thread uses ~1MB stack. Not scalable!");
    }

    /**
     * DEMO 2: The Solution - ExecutorService with Thread Pool
     * 
     * Benefits:
     * - Only 3 threads handle all 10 tasks!
     * - Threads are REUSED (no create/destroy overhead)
     * - We control the concurrency level
     * - Clean shutdown API
     */
    private static void demoSolution() throws InterruptedException {
        System.out.println("\n--- DEMO 2: The Solution (ExecutorService) ---");
        System.out.println("Using FixedThreadPool with 3 threads for 10 tasks...\n");

        long startTime = System.currentTimeMillis();

        // Create a pool with only 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit 10 tasks - only 3 threads will handle them!
        for (int i = 0; i < 10; i++) {
            executor.execute(new NumberPrinterTask(i));
        }

        // Initiate orderly shutdown
        executor.shutdown();

        // Wait for all tasks to complete
        executor.awaitTermination(10, TimeUnit.SECONDS);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\nExecutor approach took: " + duration + "ms");
        System.out.println("Solution: Only 3 threads (pool-1-thread-1,2,3) handled 10 tasks!");
        System.out.println("          Threads were REUSED. Much more efficient!");
    }
}
