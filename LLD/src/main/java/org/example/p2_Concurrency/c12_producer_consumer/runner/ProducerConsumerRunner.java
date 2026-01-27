package org.example.p2_Concurrency.c12_producer_consumer.runner;

import org.example.p2_Concurrency.c12_producer_consumer.runner.scenarios.BasicScenario;
import org.example.p2_Concurrency.c12_producer_consumer.runner.scenarios.MultiProducerConsumerScenario;

/**
 * Main runner for Producer-Consumer demonstrations.
 */
public class ProducerConsumerRunner {

    public static void runAllScenarios() {
        printHeader();

        try {
            // Scenario 1: Basic (1 producer, 1 consumer)
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 1: Basic Producer-Consumer");
            System.out.println("─".repeat(60));
            BasicScenario basicScenario = new BasicScenario();
            basicScenario.execute();

            Thread.sleep(1500);

            // Scenario 2: Multi producer-consumer
            System.out.println("\n" + "─".repeat(60));
            System.out.println("SCENARIO 2: Multiple Producers and Consumers");
            System.out.println("─".repeat(60));
            MultiProducerConsumerScenario multiScenario = new MultiProducerConsumerScenario();
            multiScenario.execute();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted during scenario execution");
        }

        printFooter();
    }

    private static void printHeader() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(12) + "PRODUCER-CONSUMER DEMONSTRATIONS" + " ".repeat(14) + "║");
        System.out.println("║" + " ".repeat(8) + "c12_producer_consumer - p2_Concurrency" + " ".repeat(12) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
    }

    private static void printFooter() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("       ALL PRODUCER-CONSUMER SCENARIOS COMPLETED ✅");
        System.out.println("═".repeat(60));
        System.out.println("\n📚 Key Takeaways:");
        System.out.println("   • 3 semaphores: mutex + empty + full");
        System.out.println("   • Producer: empty.acquire() → add → full.release()");
        System.out.println("   • Consumer: full.acquire() → take → empty.release()");
        System.out.println("   • Bounded buffer prevents overflow/underflow");
        System.out.println("\n→ Next: c13_deadlocks (when synchronization goes wrong!)");
    }
}
