package org.example.p2_Concurrency.c4_callables.examples;

import java.util.concurrent.*;

/**
 * Demonstrates all Future methods:
 * - get() - blocking wait for result
 * - get(timeout) - wait with timeout
 * - isDone() - check completion status
 * - cancel() - attempt to cancel task
 * - isCancelled() - check if cancelled
 */
public class FutureDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("FUTURE METHODS DEMONSTRATION");
        System.out.println("=".repeat(60));

        demoGetBlocking();
        demoIsDone();
        demoGetWithTimeout();
        demoCancel();
    }

    /**
     * Demo 1: get() blocks until result is ready
     */
    private static void demoGetBlocking() throws Exception {
        System.out.println("\n--- Demo 1: get() - Blocking Wait ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> {
            System.out.println("Task started, working for 2 seconds...");
            Thread.sleep(2000);
            return 42;
        });

        System.out.println("Task submitted. Calling get() (will block)...");
        long start = System.currentTimeMillis();

        Integer result = future.get(); // BLOCKS HERE!

        long duration = System.currentTimeMillis() - start;
        System.out.println("Result: " + result + " (waited " + duration + "ms)");

        executor.shutdown();
    }

    /**
     * Demo 2: isDone() - non-blocking check
     */
    private static void demoIsDone() throws Exception {
        System.out.println("\n--- Demo 2: isDone() - Non-blocking Check ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(1000);
            return "Completed!";
        });

        System.out.println("Checking isDone() periodically...");
        while (!future.isDone()) {
            System.out.println("  isDone() = false, doing other work...");
            Thread.sleep(200);
        }
        System.out.println("  isDone() = true!");
        System.out.println("Result: " + future.get());

        executor.shutdown();
    }

    /**
     * Demo 3: get(timeout) - wait with timeout
     */
    private static void demoGetWithTimeout() throws Exception {
        System.out.println("\n--- Demo 3: get(timeout) - Timeout ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Task that takes 3 seconds
        Future<String> future = executor.submit(() -> {
            Thread.sleep(3000);
            return "Done!";
        });

        System.out.println("Waiting max 1 second for 3-second task...");
        try {
            String result = future.get(1, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (TimeoutException e) {
            System.out.println("TimeoutException! Task didn't complete in 1 second.");
            System.out.println("Cancelling the task...");
            future.cancel(true);
        }

        executor.shutdown();
    }

    /**
     * Demo 4: cancel() - cancel a running task
     */
    private static void demoCancel() throws Exception {
        System.out.println("\n--- Demo 4: cancel() - Cancel Task ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            System.out.println("Long task started...");
            for (int i = 0; i < 10; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Task detected interrupt, stopping!");
                    throw new InterruptedException("Cancelled!");
                }
                System.out.println("  Working... step " + (i + 1));
                Thread.sleep(500);
            }
            return "Finished all steps";
        });

        // Let it run for 1.5 seconds then cancel
        Thread.sleep(1500);
        System.out.println("\nCalling cancel(true)...");
        boolean cancelled = future.cancel(true);
        System.out.println("cancel() returned: " + cancelled);
        System.out.println("isCancelled() = " + future.isCancelled());

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
