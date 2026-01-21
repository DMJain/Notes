package org.example.p2_Concurrency.c8_atomic_datatypes.runner;

import org.example.p2_Concurrency.c8_atomic_datatypes.runner.scenarios.AtomicDemo;
import org.example.p2_Concurrency.c8_atomic_datatypes.runner.scenarios.CASScenario;

/**
 * Orchestrates atomic datatype demonstrations.
 */
public class AtomicRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C8: ATOMIC DATATYPES - Lock-Free Thread Safety               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        new AtomicDemo().execute();
        new CASScenario().execute();

        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. AtomicInteger uses hardware CAS - no locks!");
        System.out.println("  2. incrementAndGet() is atomic read-modify-write");
        System.out.println("  3. compareAndSet() is atomic check-then-update");
        System.out.println("  4. Use for simple single-variable operations only");
        System.out.println();
        System.out.println("  → Next: c9_volatile_keyword for memory visibility");
        System.out.println();
    }
}
