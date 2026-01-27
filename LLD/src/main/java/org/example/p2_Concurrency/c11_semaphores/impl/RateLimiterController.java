package org.example.p2_Concurrency.c11_semaphores.impl;

import org.example.p2_Concurrency.c11_semaphores.contracts.ISemaphoreController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiter implementation using Semaphores.
 * 
 * <p>
 * Limits the number of concurrent operations. Demonstrates
 * semaphore usage for throttling/rate limiting scenarios.
 * </p>
 * 
 * <h3>Use Case:</h3>
 * 
 * <pre>
 *   Scenario: API with max 5 concurrent requests
 *   
 *   Request 1-5:  ✅ Proceed immediately
 *   Request 6+:   ⏳ Wait until earlier request completes
 * </pre>
 */
public class RateLimiterController implements ISemaphoreController {

    private final String name;
    private final Semaphore semaphore;
    private final int maxConcurrent;

    /**
     * Creates a rate limiter with specified concurrency limit.
     * 
     * @param name          descriptive name for this limiter
     * @param maxConcurrent maximum concurrent operations allowed
     */
    public RateLimiterController(String name, int maxConcurrent) {
        this.name = name;
        this.maxConcurrent = maxConcurrent;
        this.semaphore = new Semaphore(maxConcurrent, true);

        System.out.printf("⚡ RateLimiter '%s' created (max %d concurrent)%n",
                name, maxConcurrent);
    }

    @Override
    public void acquirePermit() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.printf("🚦 [%s] Requesting permit from '%s'...%n", threadName, name);

        semaphore.acquire();

        System.out.printf("✅ [%s] Got permit! (remaining: %d/%d)%n",
                threadName, semaphore.availablePermits(), maxConcurrent);
    }

    @Override
    public boolean tryAcquirePermit() {
        String threadName = Thread.currentThread().getName();
        boolean acquired = semaphore.tryAcquire();

        if (acquired) {
            System.out.printf("✅ [%s] Instantly got permit!%n", threadName);
        } else {
            System.out.printf("❌ [%s] No permit available, skipping.%n", threadName);
        }

        return acquired;
    }

    /**
     * Tries to acquire a permit with timeout.
     * 
     * @param timeout maximum time to wait
     * @param unit    time unit for timeout
     * @return true if permit acquired within timeout
     * @throws InterruptedException if interrupted while waiting
     */
    public boolean tryAcquirePermit(long timeout, TimeUnit unit) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.printf("⏱️ [%s] Waiting up to %d %s for permit...%n",
                threadName, timeout, unit);

        boolean acquired = semaphore.tryAcquire(timeout, unit);

        if (acquired) {
            System.out.printf("✅ [%s] Got permit within timeout!%n", threadName);
        } else {
            System.out.printf("⌛ [%s] Timeout! No permit acquired.%n", threadName);
        }

        return acquired;
    }

    @Override
    public void releasePermit() {
        String threadName = Thread.currentThread().getName();
        semaphore.release();
        System.out.printf("🔄 [%s] Released permit (available: %d/%d)%n",
                threadName, semaphore.availablePermits(), maxConcurrent);
    }

    @Override
    public int getAvailablePermits() {
        return semaphore.availablePermits();
    }
}
