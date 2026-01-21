package org.example.p2_Concurrency.c9_volatile_keyword.runner;

import org.example.p2_Concurrency.c9_volatile_keyword.runner.scenarios.MemoryVisibilityScenario;

/**
 * Orchestrates volatile keyword demonstrations.
 */
public class VolatileRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C9: VOLATILE KEYWORD - Memory Visibility Guarantee           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        new MemoryVisibilityScenario().execute();

        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. volatile ensures VISIBILITY (not atomicity!)");
        System.out.println("  2. Without volatile, threads may read stale cached values");
        System.out.println("  3. Use for simple flags and single-writer scenarios");
        System.out.println("  4. For count++, use AtomicInteger instead");
        System.out.println();
        System.out.println("  → Next: c10_concurrent_collections for thread-safe maps");
        System.out.println();
    }
}
