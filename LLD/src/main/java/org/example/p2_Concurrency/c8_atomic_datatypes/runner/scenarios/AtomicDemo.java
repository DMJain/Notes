package org.example.p2_Concurrency.c8_atomic_datatypes.runner.scenarios;

import org.example.p2_Concurrency.c8_atomic_datatypes.impl.AtomicIntegerCounter;

/**
 * Demonstrates AtomicInteger solving the race condition problem.
 */
public class AtomicDemo {

    private static final int ITERATIONS = 10_000;

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           ATOMIC INTEGER DEMO                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        for (int run = 1; run <= 5; run++) {
            AtomicIntegerCounter counter = new AtomicIntegerCounter();

            Thread adder = new Thread(() -> {
                for (int i = 0; i < ITERATIONS; i++)
                    counter.increment();
            });

            Thread subtractor = new Thread(() -> {
                for (int i = 0; i < ITERATIONS; i++)
                    counter.decrement();
            });

            adder.start();
            subtractor.start();

            try {
                adder.join();
                subtractor.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int result = counter.getCount();
            System.out.printf("  Run %d: count = %d  %s%n", run, result,
                    result == 0 ? "✅ CORRECT" : "❌");
        }

        System.out.println();
        System.out.println("  ✅ AtomicInteger uses CAS - no locks needed!");
        System.out.println();
    }
}
