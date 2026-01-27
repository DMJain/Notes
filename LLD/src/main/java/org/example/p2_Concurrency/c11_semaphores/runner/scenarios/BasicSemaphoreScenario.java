package org.example.p2_Concurrency.c11_semaphores.runner.scenarios;

import org.example.p2_Concurrency.c11_semaphores.impl.RateLimiterController;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates basic semaphore operations for rate limiting.
 * 
 * <p>
 * Shows acquire(), tryAcquire(), and tryAcquire(timeout) behaviors.
 * </p>
 */
public class BasicSemaphoreScenario {

    private static final int MAX_CONCURRENT = 2;
    private static final int REQUEST_COUNT = 5;

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           BASIC SEMAPHORE SCENARIO");
        System.out.println("═".repeat(60));
        System.out.printf("Max Concurrent: %d | Requests: %d%n%n", MAX_CONCURRENT, REQUEST_COUNT);

        RateLimiterController rateLimiter = new RateLimiterController("API-Limiter", MAX_CONCURRENT);

        // Create threads that simulate API requests
        Thread[] requests = new Thread[REQUEST_COUNT];

        for (int i = 0; i < REQUEST_COUNT; i++) {
            final int requestId = i + 1;
            requests[i] = new Thread(() -> processRequest(rateLimiter, requestId),
                    "Request-" + requestId);
        }

        System.out.println("\n🚀 Sending all requests simultaneously...\n");

        // Start all requests at once
        for (Thread request : requests) {
            request.start();
        }

        // Wait for completion
        for (Thread request : requests) {
            try {
                request.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n✅ All requests processed!");
    }

    private void processRequest(RateLimiterController rateLimiter, int requestId) {
        try {
            // Acquire permit (blocks if none available)
            rateLimiter.acquirePermit();

            try {
                // Simulate API processing
                System.out.printf("   [Request-%d] 🔄 Processing...%n", requestId);
                Thread.sleep(1000); // 1 second processing time
                System.out.printf("   [Request-%d] ✅ Done!%n", requestId);
            } finally {
                rateLimiter.releasePermit();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates non-blocking tryAcquire behavior.
     */
    public void executeTryAcquireDemo() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           TRY-ACQUIRE DEMO (Non-blocking)");
        System.out.println("═".repeat(60));

        RateLimiterController limiter = new RateLimiterController("TryAcquire-Demo", 1);

        // First acquire succeeds
        System.out.println("\n📝 Attempt 1 (should succeed):");
        boolean first = limiter.tryAcquirePermit();
        System.out.printf("   Result: %s%n", first ? "GOT IT ✅" : "FAILED ❌");

        // Second acquire fails (non-blocking)
        System.out.println("\n📝 Attempt 2 (should fail immediately):");
        boolean second = limiter.tryAcquirePermit();
        System.out.printf("   Result: %s%n", second ? "GOT IT ✅" : "FAILED ❌");

        // Release and try again
        System.out.println("\n📝 Releasing permit...");
        limiter.releasePermit();

        System.out.println("\n📝 Attempt 3 (should succeed again):");
        boolean third = limiter.tryAcquirePermit();
        System.out.printf("   Result: %s%n", third ? "GOT IT ✅" : "FAILED ❌");

        if (third)
            limiter.releasePermit();
    }

    /**
     * Demonstrates tryAcquire with timeout.
     */
    public void executeTimeoutDemo() throws InterruptedException {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           TRY-ACQUIRE WITH TIMEOUT DEMO");
        System.out.println("═".repeat(60));

        RateLimiterController limiter = new RateLimiterController("Timeout-Demo", 1);

        // Take the only permit
        limiter.acquirePermit();

        // Start a thread that will try with timeout
        Thread waiter = new Thread(() -> {
            try {
                System.out.println("\n📝 Attempting to acquire with 2 second timeout...");
                boolean acquired = limiter.tryAcquirePermit(2, TimeUnit.SECONDS);
                System.out.printf("   Result: %s%n", acquired ? "GOT IT ✅" : "TIMEOUT ⌛");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Waiter");

        waiter.start();

        // Release after 1 second (within timeout)
        Thread.sleep(1000);
        System.out.println("\n📝 Main thread releasing permit after 1 second...");
        limiter.releasePermit();

        waiter.join();
    }
}
