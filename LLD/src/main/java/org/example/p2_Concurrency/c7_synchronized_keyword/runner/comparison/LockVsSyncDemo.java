package org.example.p2_Concurrency.c7_synchronized_keyword.runner.comparison;

import org.example.p2_Concurrency.c6_mutex_locks.impl.ReentrantLockCounter;
import org.example.p2_Concurrency.c7_synchronized_keyword.impl.SynchronizedBlockCounter;

/**
 * Side-by-side comparison of ReentrantLock vs synchronized.
 */
public class LockVsSyncDemo {

    private static final int ITERATIONS = 100_000;

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       REENTRANTLOCK vs SYNCHRONIZED COMPARISON           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Test ReentrantLock
        long lockTime = testReentrantLock();

        // Test synchronized
        long syncTime = testSynchronized();

        System.out.println();
        System.out.println("  Performance comparison (not definitive):");
        System.out.printf("    ReentrantLock:  %d ms%n", lockTime);
        System.out.printf("    synchronized:   %d ms%n", syncTime);
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────────┐");
        System.out.println("  │  Both have similar performance.                     │");
        System.out.println("  │  Choose based on FEATURES you need, not speed.      │");
        System.out.println("  │                                                      │");
        System.out.println("  │  synchronized: Simpler, auto-unlock                  │");
        System.out.println("  │  ReentrantLock: tryLock, timeout, fairness           │");
        System.out.println("  └────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private long testReentrantLock() {
        ReentrantLockCounter counter = new ReentrantLockCounter();

        long start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++)
                counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++)
                counter.decrement();
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();
        System.out.printf("  ReentrantLockCounter: count = %d ✅%n", counter.getCount());
        return end - start;
    }

    private long testSynchronized() {
        SynchronizedBlockCounter counter = new SynchronizedBlockCounter();

        long start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++)
                counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++)
                counter.decrement();
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();
        System.out.printf("  SynchronizedBlockCounter: count = %d ✅%n", counter.getCount());
        return end - start;
    }
}
