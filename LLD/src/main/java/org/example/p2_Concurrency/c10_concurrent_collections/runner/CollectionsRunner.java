package org.example.p2_Concurrency.c10_concurrent_collections.runner;

import org.example.p2_Concurrency.c10_concurrent_collections.runner.comparison.HashMapVsConcurrentHashMapDemo;

/**
 * Orchestrates concurrent collections demonstrations.
 */
public class CollectionsRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C10: CONCURRENT COLLECTIONS - Thread-Safe Data Structures    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        new HashMapVsConcurrentHashMapDemo().execute();

        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. HashMap is NOT thread-safe - never use in concurrent code");
        System.out.println("  2. ConcurrentHashMap uses bucket-level locking for best performance");
        System.out.println("  3. Use putIfAbsent() and compute() for atomic operations");
        System.out.println("  4. synchronizedMap is simpler but locks entire map");
        System.out.println();
        System.out.println("  ✅ CONCURRENCY MODULE COMPLETE!");
        System.out.println();
    }
}
