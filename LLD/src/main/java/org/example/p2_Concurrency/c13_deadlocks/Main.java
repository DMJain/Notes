package org.example.p2_Concurrency.c13_deadlocks;

import org.example.p2_Concurrency.c13_deadlocks.runner.DeadlockRunner;

/**
 * Entry point for the Deadlocks module (c13).
 * 
 * <p>
 * Demonstrates what deadlocks are, the 4 Coffman conditions,
 * and strategies to prevent them.
 * </p>
 * 
 * <h3>What This Module Covers:</h3>
 * <ul>
 * <li>4 Coffman conditions for deadlock</li>
 * <li>Deliberate deadlock creation (commented out)</li>
 * <li>Lock ordering prevention</li>
 * <li>Timeout-based prevention</li>
 * </ul>
 * 
 * @see DeadlocksNotes.md for comprehensive documentation
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           c13_deadlocks Module                             ║");
        System.out.println("║       Understanding and Preventing Deadlocks               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📖 See: DeadlocksNotes.md for detailed explanation");
        System.out.println();
        System.out.println("This module demonstrates:");
        System.out.println("  1. 4 Coffman conditions for deadlock");
        System.out.println("  2. Lock ordering prevention strategy");
        System.out.println("  3. Timeout-based prevention strategy");
        System.out.println();
        System.out.println("⚠️  Note: Actual deadlock demo is disabled to prevent hanging.");
        System.out.println("   Uncomment the line below to see a real deadlock.");
        System.out.println();

        // UNCOMMENT TO SEE ACTUAL DEADLOCK (WARNING: Will hang forever!)
        // new DeadlockDemo().createDeadlock();

        DeadlockRunner.runAllScenarios();
    }
}
