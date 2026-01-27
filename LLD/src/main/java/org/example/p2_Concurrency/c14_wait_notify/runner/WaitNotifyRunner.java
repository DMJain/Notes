package org.example.p2_Concurrency.c14_wait_notify.runner;

import org.example.p2_Concurrency.c14_wait_notify.runner.comparison.SemaphoreVsWaitNotifyDemo;
import org.example.p2_Concurrency.c14_wait_notify.runner.scenarios.ProducerConsumerWaitNotifyScenario;

/**
 * Main runner for Wait/Notify demonstrations.
 */
public class WaitNotifyRunner {

    public static void runAllScenarios() {
        printHeader();

        try {
            // Scenario 1: Producer-Consumer
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 1: Producer-Consumer with Wait/Notify");
            System.out.println("─".repeat(60));
            ProducerConsumerWaitNotifyScenario pcScenario = new ProducerConsumerWaitNotifyScenario();
            pcScenario.execute();

            Thread.sleep(1000);

            // Scenario 2: Comparison with Semaphore
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 2: Semaphore vs Wait/Notify");
            System.out.println("─".repeat(60));
            SemaphoreVsWaitNotifyDemo comparisonDemo = new SemaphoreVsWaitNotifyDemo();
            comparisonDemo.execute();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted during scenario execution");
        }

        printFooter();
    }

    private static void printHeader() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(14) + "WAIT/NOTIFY DEMONSTRATIONS" + " ".repeat(18) + "║");
        System.out.println("║" + " ".repeat(10) + "c14_wait_notify - p2_Concurrency" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
    }

    private static void printFooter() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("       ALL WAIT/NOTIFY DEMOS COMPLETED ✅");
        System.out.println("═".repeat(60));
        System.out.println("\n📚 Key Takeaways:");
        System.out.println("   • wait() releases lock and sleeps until notify");
        System.out.println("   • ALWAYS use while loop, never if (spurious wakeups)");
        System.out.println("   • notifyAll() is safer than notify()");
        System.out.println("   • Must be inside synchronized block");
        System.out.println("\n🎉 CONCURRENCY MODULE COMPLETE!");
        System.out.println("   c11: Semaphores → c12: Producer-Consumer");
        System.out.println("   c13: Deadlocks  → c14: Wait/Notify");
    }
}
