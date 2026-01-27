package org.example.p2_Concurrency.c11_semaphores.runner;

import org.example.p2_Concurrency.c11_semaphores.runner.scenarios.BasicSemaphoreScenario;
import org.example.p2_Concurrency.c11_semaphores.runner.scenarios.ConnectionPoolScenario;

/**
 * Main runner for all Semaphore demonstrations.
 * 
 * <p>
 * Orchestrates execution of various semaphore scenarios.
 * </p>
 */
public class SemaphoreRunner {

    public static void runAllScenarios() {
        printHeader();

        try {
            // Scenario 1: Basic Semaphore operations
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 1: Basic Rate Limiting");
            System.out.println("─".repeat(60));
            BasicSemaphoreScenario basicScenario = new BasicSemaphoreScenario();
            basicScenario.execute();

            Thread.sleep(1000); // Pause between scenarios

            // Scenario 2: Try-acquire (non-blocking)
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 2: Non-blocking tryAcquire");
            System.out.println("─".repeat(60));
            basicScenario.executeTryAcquireDemo();

            Thread.sleep(1000);

            // Scenario 3: Timeout-based acquire
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 3: Timeout-based Acquire");
            System.out.println("─".repeat(60));
            basicScenario.executeTimeoutDemo();

            Thread.sleep(1000);

            // Scenario 4: Connection Pool
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 4: Connection Pool");
            System.out.println("─".repeat(60));
            ConnectionPoolScenario poolScenario = new ConnectionPoolScenario();
            poolScenario.execute();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted during scenario execution");
        }

        printFooter();
    }

    private static void printHeader() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(15) + "SEMAPHORE DEMONSTRATIONS" + " ".repeat(19) + "║");
        System.out.println("║" + " ".repeat(10) + "c11_semaphores - p2_Concurrency" + " ".repeat(17) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
    }

    private static void printFooter() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("       ALL SEMAPHORE SCENARIOS COMPLETED ✅");
        System.out.println("═".repeat(60));
        System.out.println("\n📚 Key Takeaways:");
        System.out.println("   • Semaphore = permit counter (N threads, not just 1)");
        System.out.println("   • acquire() blocks until permit available");
        System.out.println("   • tryAcquire() is non-blocking (immediate return)");
        System.out.println("   • Use for: connection pools, rate limiting, bounded resources");
        System.out.println("\n→ Next: c12_producer_consumer (semaphores in action!)");
    }
}
