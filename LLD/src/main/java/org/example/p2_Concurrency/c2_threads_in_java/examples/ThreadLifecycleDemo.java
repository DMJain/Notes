package org.example.p2_Concurrency.c2_threads_in_java.examples;

/**
 * Demonstrates all thread lifecycle states in Java.
 * 
 * Thread States:
 * - NEW: Thread created but not started
 * - RUNNABLE: Thread is running or ready to run
 * - BLOCKED: Waiting for monitor lock
 * - WAITING: Waiting indefinitely for another thread
 * - TIMED_WAITING: Waiting for specified time
 * - TERMINATED: Thread has completed execution
 * 
 * This demo shows how to observe and understand each state.
 */
public class ThreadLifecycleDemo {

    // Shared lock object for demonstrating BLOCKED state
    private static final Object lock = new Object();

    /**
     * Demonstrates all thread states with explanations.
     */
    public static void runDemo() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║      DEMO 3: Thread Lifecycle States          ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        demonstrateNewState();
        demonstrateRunnableState();
        demonstrateTimedWaitingState();
        demonstrateWaitingState();
        demonstrateTerminatedState();
        demonstrateBlockedState();
    }

    /**
     * NEW state: Thread created but start() not called yet.
     */
    private static void demonstrateNewState() {
        System.out.println("1️⃣ NEW STATE");
        System.out.println("─────────────────────────────────────────────");

        Thread t = new Thread(() -> {
        });
        System.out.println("   Thread t = new Thread(() -> {});");
        System.out.println("   State: " + t.getState()); // NEW
        System.out.println("   (Thread exists but start() not called)\n");
    }

    /**
     * RUNNABLE state: Thread is running or ready to run.
     */
    private static void demonstrateRunnableState() {
        System.out.println("2️⃣ RUNNABLE STATE");
        System.out.println("─────────────────────────────────────────────");

        Thread t = new Thread(() -> {
            // Busy work to stay runnable
            for (int i = 0; i < 1000000; i++) {
                Math.sqrt(i);
            }
        });

        t.start();
        System.out.println("   t.start() called");
        System.out.println("   State: " + t.getState()); // Likely RUNNABLE
        System.out.println("   (Thread is executing or ready to execute)\n");

        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * TIMED_WAITING state: Thread is sleeping or waiting with timeout.
     */
    private static void demonstrateTimedWaitingState() {
        System.out.println("3️⃣ TIMED_WAITING STATE");
        System.out.println("─────────────────────────────────────────────");

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(2000); // Sleep for 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t.start();

        try {
            Thread.sleep(100); // Give thread time to start sleeping
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("   Thread.sleep(2000) called inside thread");
        System.out.println("   State: " + t.getState()); // TIMED_WAITING
        System.out.println("   (Thread is waiting with a timeout)\n");

        t.interrupt(); // Stop the sleeping thread
    }

    /**
     * WAITING state: Thread is waiting indefinitely (join without timeout).
     */
    private static void demonstrateWaitingState() {
        System.out.println("4️⃣ WAITING STATE");
        System.out.println("─────────────────────────────────────────────");

        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread waiter = new Thread(() -> {
            try {
                sleeper.join(); // Wait indefinitely for sleeper
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        sleeper.start();
        waiter.start();

        try {
            Thread.sleep(100); // Give threads time to get into states
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("   waiter.join() called (waiting for sleeper)");
        System.out.println("   Waiter State: " + waiter.getState()); // WAITING
        System.out.println("   (Thread waiting indefinitely for another)\n");

        sleeper.interrupt(); // Cleanup
    }

    /**
     * TERMINATED state: Thread has finished execution.
     */
    private static void demonstrateTerminatedState() {
        System.out.println("5️⃣ TERMINATED STATE");
        System.out.println("─────────────────────────────────────────────");

        Thread t = new Thread(() -> {
            System.out.println("   Thread running... done!");
        });

        t.start();

        try {
            t.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("   State: " + t.getState()); // TERMINATED
        System.out.println("   (Thread has completed its execution)\n");
    }

    /**
     * BLOCKED state: Thread waiting to acquire a lock.
     */
    private static void demonstrateBlockedState() {
        System.out.println("6️⃣ BLOCKED STATE");
        System.out.println("─────────────────────────────────────────────");

        // Thread 1 holds the lock
        Thread holder = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(2000); // Hold lock for 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Thread 2 tries to acquire the same lock
        Thread blocker = new Thread(() -> {
            synchronized (lock) {
                System.out.println("   Blocker got the lock!");
            }
        });

        holder.start();

        try {
            Thread.sleep(100); // Let holder acquire lock first
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        blocker.start();

        try {
            Thread.sleep(100); // Let blocker try to acquire lock
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("   holder has the lock, blocker is waiting");
        System.out.println("   Blocker State: " + blocker.getState()); // BLOCKED
        System.out.println("   (Thread waiting to acquire monitor lock)\n");

        holder.interrupt(); // Cleanup
    }
}
