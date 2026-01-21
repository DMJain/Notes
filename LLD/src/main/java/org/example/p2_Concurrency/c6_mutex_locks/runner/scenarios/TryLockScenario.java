package org.example.p2_Concurrency.c6_mutex_locks.runner.scenarios;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates tryLock() with timeout - useful for avoiding deadlocks.
 * 
 * <p>
 * tryLock() attempts to acquire the lock without blocking forever.
 * If the lock is unavailable, it returns false immediately (or after timeout).
 * </p>
 */
public class TryLockScenario {

    private final Lock lock = new ReentrantLock();
    private int counter = 0;

    /**
     * Executes the tryLock demonstration.
     */
    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           TRYLOCK SCENARIO                               ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Demonstrating tryLock() with timeout                    ║");
        System.out.println("║  • Thread 1: Holds lock for 2 seconds                    ║");
        System.out.println("║  • Thread 2: Uses tryLock() with 500ms timeout           ║");
        System.out.println("║  • Thread 3: Uses tryLock() with 3s timeout              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Thread 1: Holds lock for 2 seconds
        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  [Holder] Lock acquired. Holding for 2 seconds...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  [Holder] Releasing lock.");
            } finally {
                lock.unlock();
            }
        }, "Holder");

        // Thread 2: Short timeout - will fail
        Thread shortWait = new Thread(() -> {
            try {
                Thread.sleep(100); // Let holder start first
                System.out.println("  [ShortWait] Trying to acquire lock (500ms timeout)...");

                if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("  [ShortWait] ✅ Got the lock!");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("  [ShortWait] ❌ Timeout! Could not acquire lock in 500ms.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ShortWait");

        // Thread 3: Long timeout - will succeed
        Thread longWait = new Thread(() -> {
            try {
                Thread.sleep(100); // Let holder start first
                System.out.println("  [LongWait] Trying to acquire lock (3s timeout)...");

                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("  [LongWait] ✅ Got the lock after waiting!");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("  [LongWait] ❌ Timeout!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "LongWait");

        // Start all threads
        holder.start();
        shortWait.start();
        longWait.start();

        // Wait for completion
        try {
            holder.join();
            shortWait.join();
            longWait.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
        System.out.println("  KEY INSIGHT: tryLock() prevents threads from waiting forever.");
        System.out.println("  → Use when you need timeout-based locking or deadlock avoidance.");
        System.out.println();
    }
}
