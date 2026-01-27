package org.example.p2_Concurrency.c13_deadlocks.impl;

/**
 * Demonstrates how a deadlock occurs.
 * 
 * <p>
 * Two threads acquire locks in opposite order, causing deadlock.
 * </p>
 * 
 * <h3>4 Coffman Conditions Met:</h3>
 * <ol>
 * <li>Mutual Exclusion: synchronized blocks</li>
 * <li>Hold and Wait: Hold one lock, wait for another</li>
 * <li>No Preemption: Can't force release</li>
 * <li>Circular Wait: T1→T2→T1</li>
 * </ol>
 */
public class DeadlockDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    /**
     * Creates a deliberate deadlock for demonstration.
     * 
     * <p>
     * WARNING: This method will cause threads to hang forever!
     * </p>
     */
    public void createDeadlock() {
        System.out.println("\n⚠️  WARNING: Creating deliberate deadlock...");
        System.out.println("   Press Ctrl+C to terminate after seeing the deadlock.\n");

        // Thread 1: Acquires A, then B
        Thread thread1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("   [Thread-1] ✅ Holding Lock A");
                sleep(100); // Give Thread 2 time to get Lock B

                System.out.println("   [Thread-1] ⏳ Waiting for Lock B...");
                synchronized (lockB) {
                    System.out.println("   [Thread-1] Got Lock B (you won't see this)");
                }
            }
        }, "Thread-1");

        // Thread 2: Acquires B, then A (opposite order!)
        Thread thread2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("   [Thread-2] ✅ Holding Lock B");
                sleep(100); // Give Thread 1 time to get Lock A

                System.out.println("   [Thread-2] ⏳ Waiting for Lock A...");
                synchronized (lockA) {
                    System.out.println("   [Thread-2] Got Lock A (you won't see this)");
                }
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        // Wait a bit to show deadlock
        sleep(2000);

        System.out.println("\n💀 DEADLOCK DETECTED!");
        System.out.println("   - Thread-1: Holds A, waiting for B");
        System.out.println("   - Thread-2: Holds B, waiting for A");
        System.out.println("   - Both threads are stuck FOREVER");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
