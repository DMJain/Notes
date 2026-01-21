package org.example.p2_Concurrency.c5_synchronization_problem.runner;

import org.example.p2_Concurrency.c5_synchronization_problem.runner.scenarios.RaceConditionScenario;

/**
 * Runner class that orchestrates all synchronization problem demonstrations.
 * 
 * <p>
 * This runner executes scenarios that demonstrate WHY synchronization
 * is necessary in concurrent programming.
 * </p>
 */
public class ProblemRunner {

    /**
     * Runs all scenarios demonstrating the synchronization problem.
     */
    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     C5: SYNCHRONIZATION PROBLEM - Understanding the Issue        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Scenario 1: Race Condition
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println("                     SCENARIO 1: Race Condition");
        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.println();

        RaceConditionScenario raceScenario = new RaceConditionScenario();
        raceScenario.execute();

        // Summary
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                           KEY TAKEAWAYS");
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. count++ is NOT atomic - it's READ → MODIFY → WRITE");
        System.out.println("  2. Threads can be interrupted between any of these steps");
        System.out.println("  3. This causes RACE CONDITIONS - unpredictable results");
        System.out.println("  4. We need SYNCHRONIZATION to fix this");
        System.out.println();
        System.out.println("  → Next: c6_mutex_locks for the solution using ReentrantLock");
        System.out.println();
    }
}
