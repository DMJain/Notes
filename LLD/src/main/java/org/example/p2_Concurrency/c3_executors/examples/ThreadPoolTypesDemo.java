package org.example.p2_Concurrency.c3_executors.examples;

import java.util.concurrent.*;

/**
 * Demonstrates all 5 types of thread pools in Java.
 * 
 * Run this to see how each pool type behaves differently!
 */
public class ThreadPoolTypesDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(60));
        System.out.println("THREAD POOL TYPES DEMONSTRATION");
        System.out.println("=".repeat(60));

        demoFixedThreadPool();
        demoCachedThreadPool();
        demoSingleThreadExecutor();
        demoScheduledThreadPool();
        demoWorkStealingPool();
    }

    /**
     * FixedThreadPool: Exactly n threads, tasks queue if all busy
     */
    private static void demoFixedThreadPool() throws InterruptedException {
        System.out.println("\n--- 1. FixedThreadPool (3 threads) ---");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit 6 tasks to 3 threads
        for (int i = 1; i <= 6; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " → " +
                        Thread.currentThread().getName());
                sleep(200);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Notice: Only 3 thread names appear (pool-X-thread-1,2,3)");
    }

    /**
     * CachedThreadPool: Creates threads as needed, reuses idle ones
     */
    private static void demoCachedThreadPool() throws InterruptedException {
        System.out.println("\n--- 2. CachedThreadPool (dynamic) ---");
        ExecutorService executor = Executors.newCachedThreadPool();

        // Submit 5 tasks rapidly - will likely create 5 threads
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " → " +
                        Thread.currentThread().getName());
                sleep(100);
            });
        }

        Thread.sleep(500); // Wait for completion

        // Submit 2 more - will reuse existing idle threads
        System.out.println("Submitting 2 more tasks (should reuse threads):");
        for (int i = 6; i <= 7; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " → " +
                        Thread.currentThread().getName() + " (REUSED!)");
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * SingleThreadExecutor: One thread, tasks execute in order
     */
    private static void demoSingleThreadExecutor() throws InterruptedException {
        System.out.println("\n--- 3. SingleThreadExecutor (sequential) ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit 4 tasks - all will run in order on same thread
        for (int i = 1; i <= 4; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " → " +
                        Thread.currentThread().getName() + " (in order!)");
                sleep(50);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Notice: All tasks on same thread, in submission order");
    }

    /**
     * ScheduledThreadPool: For delayed and periodic tasks
     */
    private static void demoScheduledThreadPool() throws InterruptedException {
        System.out.println("\n--- 4. ScheduledThreadPool (timed) ---");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        System.out.println("Scheduling task to run after 1 second...");
        executor.schedule(() -> {
            System.out.println("Delayed task executed after 1 second!");
        }, 1, TimeUnit.SECONDS);

        System.out.println("Scheduling periodic task every 500ms (will run 3 times)...");
        ScheduledFuture<?> periodicTask = executor.scheduleAtFixedRate(() -> {
            System.out.println("Periodic task at: " + System.currentTimeMillis() % 10000);
        }, 0, 500, TimeUnit.MILLISECONDS);

        // Let it run 3 times then cancel
        Thread.sleep(1600);
        periodicTask.cancel(false);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * WorkStealingPool: Uses all CPU cores, workers steal tasks
     */
    private static void demoWorkStealingPool() throws InterruptedException {
        System.out.println("\n--- 5. WorkStealingPool (all cores) ---");
        ExecutorService executor = Executors.newWorkStealingPool();

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available CPU cores: " + cores);

        // Submit tasks equal to number of cores * 2
        for (int i = 1; i <= cores * 2; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " → " +
                        Thread.currentThread().getName());
                sleep(100);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Notice: Thread names show ForkJoinPool workers");
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
