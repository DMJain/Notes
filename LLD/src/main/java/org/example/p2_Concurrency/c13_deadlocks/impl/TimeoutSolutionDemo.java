package org.example.p2_Concurrency.c13_deadlocks.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates deadlock prevention using tryLock with timeout.
 * 
 * <p>
 * If a lock cannot be acquired within timeout, release all held
 * locks and retry. This breaks the "hold and wait" condition.
 * </p>
 */
public class TimeoutSolutionDemo {

    private final Lock lockA = new ReentrantLock();
    private final Lock lockB = new ReentrantLock();

    /**
     * Demonstrates timeout-based lock acquisition.
     */
    public void executeWithTimeout() {
        System.out.println("\n✅ Using TIMEOUT to prevent deadlock...\n");

        Thread thread1 = new Thread(() -> {
            acquireWithTimeout("Thread-1", lockA, lockB);
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            // Note: Opposite order, but timeout prevents deadlock
            acquireWithTimeout("Thread-2", lockB, lockA);
        }, "Thread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ Demo completed without permanent deadlock!");
    }

    /**
     * Attempts to acquire two locks with timeout.
     * If either fails, releases all and retries.
     */
    private void acquireWithTimeout(String threadName, Lock first, Lock second) {
        int attempts = 0;
        final int maxAttempts = 3;

        while (attempts < maxAttempts) {
            attempts++;
            System.out.printf("   [%s] Attempt %d to acquire both locks%n",
                    threadName, attempts);

            try {
                // Try to acquire first lock with timeout
                if (first.tryLock(500, TimeUnit.MILLISECONDS)) {
                    System.out.printf("   [%s] ✅ Got first lock%n", threadName);

                    try {
                        // Try to acquire second lock with timeout
                        if (second.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.printf("   [%s] ✅ Got both locks! Doing work...%n",
                                        threadName);
                                Thread.sleep(300); // Simulate work
                                System.out.printf("   [%s] ✅ Work complete!%n", threadName);
                                return; // Success!
                            } finally {
                                second.unlock();
                                System.out.printf("   [%s] 🔓 Released second lock%n", threadName);
                            }
                        } else {
                            System.out.printf("   [%s] ⏰ Timeout on second lock, retrying...%n",
                                    threadName);
                        }
                    } finally {
                        first.unlock();
                        System.out.printf("   [%s] 🔓 Released first lock%n", threadName);
                    }
                } else {
                    System.out.printf("   [%s] ⏰ Timeout on first lock, retrying...%n",
                            threadName);
                }

                // Random backoff to avoid livelock
                Thread.sleep((long) (Math.random() * 200));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.printf("   [%s] ❌ Failed after %d attempts%n", threadName, maxAttempts);
    }
}
