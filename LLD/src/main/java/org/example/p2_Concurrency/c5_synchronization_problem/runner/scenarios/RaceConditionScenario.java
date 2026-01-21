package org.example.p2_Concurrency.c5_synchronization_problem.runner.scenarios;

import org.example.p2_Concurrency.c5_synchronization_problem.contracts.ICounter;
import org.example.p2_Concurrency.c5_synchronization_problem.impl.UnsafeCounter;

/**
 * Demonstrates the race condition problem with concurrent counter access.
 * 
 * <p>
 * This scenario creates two threads that simultaneously increment and
 * decrement a shared counter. Due to race conditions, the final result
 * is unpredictable and almost never the expected value of 0.
 * </p>
 * 
 * <h2>Expected vs Actual</h2>
 * <ul>
 * <li><b>Expected:</b> +10,000 - 10,000 = 0</li>
 * <li><b>Actual:</b> Random value each run!</li>
 * </ul>
 */
public class RaceConditionScenario {

    private static final int ITERATIONS = 10_000;

    /**
     * Executes the race condition demonstration.
     * 
     * <p>
     * Creates an Adder thread and Subtractor thread that operate
     * on the same UnsafeCounter. The result demonstrates lost updates.
     * </p>
     */
    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           RACE CONDITION SCENARIO                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  • Adder thread: increments " + ITERATIONS + " times              ║");
        System.out.println("║  • Subtractor thread: decrements " + ITERATIONS + " times          ║");
        System.out.println("║  • Expected result: 0                                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Run multiple times to show inconsistency
        for (int run = 1; run <= 5; run++) {
            ICounter counter = new UnsafeCounter();

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

            // Check result
            int result = counter.getCount();
            String status = (result == 0) ? "✅ (lucky!)" : "❌ WRONG";

            System.out.printf("  Run %d: Final count = %6d  (Expected: 0)  %s%n",
                    run, result, status);
        }

        System.out.println();
        System.out.println("  ⚠️  Notice how the result is DIFFERENT and UNPREDICTABLE each time!");
        System.out.println("  ⚠️  This is the RACE CONDITION - we need synchronization to fix this.");
        System.out.println();
    }
}
