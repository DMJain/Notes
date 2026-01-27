package org.example.p2_Concurrency.c12_producer_consumer;

import org.example.p2_Concurrency.c12_producer_consumer.runner.ProducerConsumerRunner;

/**
 * Entry point for the Producer-Consumer module (c12).
 * 
 * <p>
 * Demonstrates the classic Producer-Consumer problem and its
 * solution using semaphores for bounded buffer coordination.
 * </p>
 * 
 * <h3>What This Module Covers:</h3>
 * <ul>
 * <li>Producer-Consumer pattern explained</li>
 * <li>Three-semaphore solution (mutex, empty, full)</li>
 * <li>Bounded buffer implementation</li>
 * <li>Multiple producers and consumers</li>
 * </ul>
 * 
 * @see ProducerConsumerNotes.md for comprehensive documentation
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           c12_producer_consumer Module                     ║");
        System.out.println("║     The Classic Producer-Consumer Problem in Java          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📖 See: ProducerConsumerNotes.md for detailed explanation");
        System.out.println();
        System.out.println("This module demonstrates:");
        System.out.println("  1. T-Shirt Store analogy for bounded buffer");
        System.out.println("  2. Three-semaphore synchronization");
        System.out.println("  3. Single producer-consumer scenario");
        System.out.println("  4. Multi producer-consumer scenario");
        System.out.println();

        ProducerConsumerRunner.runAllScenarios();
    }
}
