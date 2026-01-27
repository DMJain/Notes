package org.example.p2_Concurrency.c11_semaphores;

import org.example.p2_Concurrency.c11_semaphores.runner.SemaphoreRunner;

/**
 * Entry point for the Semaphores module (c11).
 * 
 * <p>
 * Demonstrates semaphore-based synchronization for bounded
 * resource access including connection pooling and rate limiting.
 * </p>
 * 
 * <h3>What This Module Covers:</h3>
 * <ul>
 * <li>Semaphore basics: acquire(), release(), tryAcquire()</li>
 * <li>Connection Pool pattern using semaphores</li>
 * <li>Rate Limiting implementation</li>
 * <li>Difference between Semaphore and Mutex</li>
 * </ul>
 * 
 * @see SemaphoresNotes.md for comprehensive documentation
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           c11_semaphores Module                            ║");
        System.out.println("║       Semaphore-Based Synchronization in Java              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📖 See: SemaphoresNotes.md for detailed explanation");
        System.out.println();
        System.out.println("This module demonstrates:");
        System.out.println("  1. Basic semaphore operations (acquire/release)");
        System.out.println("  2. Non-blocking access (tryAcquire)");
        System.out.println("  3. Timeout-based access (tryAcquire with timeout)");
        System.out.println("  4. Real-world Connection Pool example");
        System.out.println();

        SemaphoreRunner.runAllScenarios();
    }
}
