package org.example.p2_Concurrency.c6_mutex_locks.runner;

import org.example.p2_Concurrency.c6_mutex_locks.runner.scenarios.LockSolutionScenario;
import org.example.p2_Concurrency.c6_mutex_locks.runner.scenarios.TryLockScenario;

/**
 * Runner class that orchestrates all mutex lock demonstrations.
 */
public class MutexRunner {

    /**
     * Runs all mutex lock scenarios.
     */
    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C6: MUTEX LOCKS - Solving Race Conditions with ReentrantLock ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Scenario 1: Lock Solution
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println("                     SCENARIO 1: Lock Solution");
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println();

        LockSolutionScenario lockScenario = new LockSolutionScenario();
        lockScenario.execute();

        // Scenario 2: tryLock()
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println("                     SCENARIO 2: tryLock() with Timeout");
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println();

        TryLockScenario tryLockScenario = new TryLockScenario();
        tryLockScenario.execute();

        // Summary
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. ReentrantLock provides mutual exclusion (only ONE thread)");
        System.out.println("  2. ALWAYS use try-finally to ensure unlock()");
        System.out.println("  3. tryLock() is useful for timeout-based locking");
        System.out.println("  4. Same thread can acquire lock multiple times (reentrant)");
        System.out.println();
        System.out.println("  → Next: c7_synchronized_keyword for simpler syntax");
        System.out.println();
    }
}
