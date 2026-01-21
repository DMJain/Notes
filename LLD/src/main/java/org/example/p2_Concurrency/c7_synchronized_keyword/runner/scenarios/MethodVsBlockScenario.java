package org.example.p2_Concurrency.c7_synchronized_keyword.runner.scenarios;

import org.example.p2_Concurrency.c7_synchronized_keyword.contracts.ISynchronizedCounter;
import org.example.p2_Concurrency.c7_synchronized_keyword.impl.SynchronizedMethodCounter;
import org.example.p2_Concurrency.c7_synchronized_keyword.impl.SynchronizedBlockCounter;

/**
 * Compares synchronized method vs synchronized block approaches.
 */
public class MethodVsBlockScenario {

    private static final int ITERATIONS = 10_000;

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       METHOD VS BLOCK SCENARIO                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Test synchronized method
        System.out.println("  Testing SynchronizedMethodCounter...");
        testCounter(new SynchronizedMethodCounter(), "SynchronizedMethod");

        // Test synchronized block
        System.out.println("  Testing SynchronizedBlockCounter...");
        testCounter(new SynchronizedBlockCounter(), "SynchronizedBlock");

        System.out.println();
        System.out.println("  ✅ Both approaches work correctly!");
        System.out.println("  💡 Block approach is more flexible and secure.");
        System.out.println();
    }

    private void testCounter(ISynchronizedCounter counter, String name) {
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
        String status = (result == 0) ? "✅" : "❌";
        System.out.printf("    %s: count = %d  %s%n", name, result, status);
    }
}
