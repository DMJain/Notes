package org.example.p2_Concurrency.c13_deadlocks.runner;

import org.example.p2_Concurrency.c13_deadlocks.impl.DeadlockFreeDemo;
import org.example.p2_Concurrency.c13_deadlocks.impl.TimeoutSolutionDemo;

/**
 * Main runner for Deadlock demonstrations.
 * 
 * <p>
 * Note: DeadlockDemo is NOT run by default as it hangs forever.
 * The other demos show prevention strategies.
 * </p>
 */
public class DeadlockRunner {

    public static void runAllScenarios() {
        printHeader();

        try {
            // Scenario 1: Lock Ordering Prevention
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 1: Lock Ordering (Prevention)");
            System.out.println("─".repeat(60));
            DeadlockFreeDemo orderingDemo = new DeadlockFreeDemo();
            orderingDemo.executeWithoutDeadlock();

            Thread.sleep(1000);

            // Scenario 2: Timeout Solution
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 2: Timeout + Retry (Prevention)");
            System.out.println("─".repeat(60));
            TimeoutSolutionDemo timeoutDemo = new TimeoutSolutionDemo();
            timeoutDemo.executeWithTimeout();

            Thread.sleep(500);

            // Note about the actual deadlock demo
            System.out.println("\n" + "─".repeat(60));
            System.out.println("NOTE: DeadlockDemo (actual deadlock) not run by default");
            System.out.println("─".repeat(60));
            System.out.println("\n   To see a real deadlock, uncomment the line in Main.java:");
            System.out.println("   // new DeadlockDemo().createDeadlock();");
            System.out.println("   WARNING: It will hang forever and require Ctrl+C!");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted during scenario execution");
        }

        printFooter();
    }

    private static void printHeader() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(16) + "DEADLOCK DEMONSTRATIONS" + " ".repeat(19) + "║");
        System.out.println("║" + " ".repeat(12) + "c13_deadlocks - p2_Concurrency" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
    }

    private static void printFooter() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("       ALL DEADLOCK DEMOS COMPLETED ✅");
        System.out.println("═".repeat(60));
        System.out.println("\n📚 Key Takeaways:");
        System.out.println("   • 4 Coffman conditions must ALL be true for deadlock");
        System.out.println("   • Lock Ordering: Always acquire locks in same order");
        System.out.println("   • Timeout: Use tryLock() with timeout, release + retry");
        System.out.println("   • Break ANY ONE condition → No deadlock!");
        System.out.println("\n→ Next: c14_wait_notify (inter-thread communication)");
    }
}
