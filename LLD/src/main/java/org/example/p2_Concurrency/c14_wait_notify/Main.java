package org.example.p2_Concurrency.c14_wait_notify;

import org.example.p2_Concurrency.c14_wait_notify.runner.WaitNotifyRunner;

/**
 * Entry point for the Wait/Notify module (c14).
 * 
 * <p>
 * Demonstrates inter-thread communication using Java's built-in
 * wait/notify mechanism for condition-based coordination.
 * </p>
 * 
 * <h3>What This Module Covers:</h3>
 * <ul>
 * <li>wait(), notify(), notifyAll() explained</li>
 * <li>Why while loop is essential (spurious wakeups)</li>
 * <li>Producer-Consumer using wait/notify</li>
 * <li>Comparison with Semaphore approach</li>
 * </ul>
 * 
 * @see WaitNotifyNotes.md for comprehensive documentation
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           c14_wait_notify Module                           ║");
        System.out.println("║       Inter-Thread Communication in Java                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📖 See: WaitNotifyNotes.md for detailed explanation");
        System.out.println();
        System.out.println("This module demonstrates:");
        System.out.println("  1. wait/notify mechanism vs busy waiting");
        System.out.println("  2. Producer-Consumer pattern reimplemented");
        System.out.println("  3. Comparison with Semaphore approach");
        System.out.println();

        WaitNotifyRunner.runAllScenarios();
    }
}
