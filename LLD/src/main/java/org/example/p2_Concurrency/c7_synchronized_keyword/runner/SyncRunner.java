package org.example.p2_Concurrency.c7_synchronized_keyword.runner;

import org.example.p2_Concurrency.c7_synchronized_keyword.runner.scenarios.MethodVsBlockScenario;
import org.example.p2_Concurrency.c7_synchronized_keyword.runner.comparison.LockVsSyncDemo;

/**
 * Orchestrates synchronized keyword demonstrations.
 */
public class SyncRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C7: SYNCHRONIZED KEYWORD - Java's Built-in Locking           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Scenario 1
        new MethodVsBlockScenario().execute();

        // Comparison
        new LockVsSyncDemo().execute();

        // Summary
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. synchronized method: Locks on 'this', entire method protected");
        System.out.println("  2. synchronized block: Finer control, can use any lock object");
        System.out.println("  3. Auto-unlock: Even on exception, lock is released");
        System.out.println("  4. Use synchronized for simple cases, ReentrantLock for advanced");
        System.out.println();
        System.out.println("  → Next: c8_atomic_datatypes for lock-free thread safety");
        System.out.println();
    }
}
