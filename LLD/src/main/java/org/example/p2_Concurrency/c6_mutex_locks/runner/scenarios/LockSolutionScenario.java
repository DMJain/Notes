package org.example.p2_Concurrency.c6_mutex_locks.runner.scenarios;

import org.example.p2_Concurrency.c6_mutex_locks.contracts.ILockableCounter;
import org.example.p2_Concurrency.c6_mutex_locks.impl.ReentrantLockCounter;

/**
 * Demonstrates how ReentrantLock solves the race condition problem.
 * 
 * <p>
 * Unlike c5's RaceConditionScenario which showed unpredictable results,
 * this scenario uses a thread-safe counter and shows consistent results.
 * </p>
 */
public class LockSolutionScenario {

    private static final int ITERATIONS = 10_000;

    /**
     * Executes the lock solution demonstration.
     * 
     * <p>
     * Creates Adder and Subtractor threads using a ReentrantLockCounter.
     * The result should always be 0.
     * </p>
     */
    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           LOCK SOLUTION SCENARIO                         ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  • Using ReentrantLockCounter (thread-safe)              ║");
        System.out.println("║  • Adder thread: increments " + ITERATIONS + " times              ║");
        System.out.println("║  • Subtractor thread: decrements " + ITERATIONS + " times          ║");
        System.out.println("║  • Expected result: 0 (always!)                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Run multiple times to show consistency
        for (int run = 1; run <= 5; run++) {
            ILockableCounter counter = new ReentrantLockCounter();

            // Adder thread
            Thread adder = new Thread(() -> {
                for (int i = 0; i < ITERATIONS; i++) {
                    counter.increment();
                }
            }, "Adder");

            // Subtractor thread
            Thread subtractor = new Thread(() -> {
                for (int i = 0; i < ITERATIONS; i++) {
                    counter.decrement();
                }
            }, "Subtractor");

            // Start both threads
            adder.start();
            subtractor.start();

            // Wait for both to complete
            try {
                adder.join();
                subtractor.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check result - should ALWAYS be 0!
            int result = counter.getCount();
            String status = (result == 0) ? "✅ CORRECT" : "❌ WRONG (bug!)";

            System.out.printf("  Run %d: Final count = %6d  (Expected: 0)  %s%n",
                    run, result, status);
        }

        System.out.println();
        System.out.println("  ✅ Notice: Result is ALWAYS 0 now!");
        System.out.println("  ✅ The ReentrantLock ensures mutual exclusion.");
        System.out.println();
    }
}
