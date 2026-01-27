package org.example.p2_Concurrency.c13_deadlocks.impl;

/**
 * Demonstrates deadlock prevention using lock ordering.
 * 
 * <p>
 * By always acquiring locks in the same order, circular wait
 * is broken and deadlock becomes impossible.
 * </p>
 * 
 * <h3>Prevention Strategy:</h3>
 * <ul>
 * <li>Define a consistent lock order (e.g., by ID or hash)</li>
 * <li>ALL threads acquire locks in this order</li>
 * <li>Circular wait becomes impossible</li>
 * </ul>
 */
public class DeadlockFreeDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    /**
     * Demonstrates deadlock-free execution using lock ordering.
     */
    public void executeWithoutDeadlock() {
        System.out.println("\n✅ DEADLOCK-FREE execution using lock ordering...\n");

        // Both threads acquire locks in SAME order: A → B
        Thread thread1 = new Thread(() -> {
            acquireInOrder("Thread-1");
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            acquireInOrder("Thread-2");
        }, "Thread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ Both threads completed successfully!");
        System.out.println("   No deadlock because both used A → B order.");
    }

    /**
     * Always acquires Lock A before Lock B.
     */
    private void acquireInOrder(String threadName) {
        System.out.printf("   [%s] Starting...%n", threadName);

        synchronized (lockA) {
            System.out.printf("   [%s] ✅ Holding Lock A%n", threadName);
            sleep(100);

            System.out.printf("   [%s] ⏳ Acquiring Lock B...%n", threadName);
            synchronized (lockB) {
                System.out.printf("   [%s] ✅ Holding Lock A and B%n", threadName);
                sleep(200); // Simulate work
            }
            System.out.printf("   [%s] 🔓 Released Lock B%n", threadName);
        }
        System.out.printf("   [%s] 🔓 Released Lock A%n", threadName);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
