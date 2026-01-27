package org.example.p2_Concurrency.c14_wait_notify.runner.comparison;

import org.example.p2_Concurrency.c11_semaphores.impl.ConnectionPoolController;
import org.example.p2_Concurrency.c14_wait_notify.impl.WaitNotifyBuffer;

/**
 * Side-by-side comparison of Semaphore vs Wait/Notify approaches.
 */
public class SemaphoreVsWaitNotifyDemo {

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("     SEMAPHORE vs WAIT/NOTIFY COMPARISON");
        System.out.println("═".repeat(60));

        System.out.println("\n📊 COMPARISON TABLE:");
        System.out.println("┌────────────────────┬─────────────────────┬─────────────────────┐");
        System.out.println("│ Feature            │ Semaphore           │ Wait/Notify         │");
        System.out.println("├────────────────────┼─────────────────────┼─────────────────────┤");
        System.out.println("│ Mechanism          │ Permit count        │ Condition-based     │");
        System.out.println("│ Lock requirement   │ None                │ Must hold lock      │");
        System.out.println("│ Use case           │ Resource limiting   │ State coordination  │");
        System.out.println("│ Producer-Consumer  │ 3 semaphores        │ 1 synchronized      │");
        System.out.println("│ Signaling          │ release()           │ notify/notifyAll    │");
        System.out.println("│ Waiting            │ acquire()           │ wait() in while     │");
        System.out.println("└────────────────────┴─────────────────────┴─────────────────────┘");

        System.out.println("\n📌 WHEN TO USE EACH:");
        System.out.println();
        System.out.println("   SEMAPHORE:");
        System.out.println("   • Limiting concurrent access (connection pools)");
        System.out.println("   • Signaling between unrelated threads");
        System.out.println("   • When you need N permits, not complex conditions");
        System.out.println();
        System.out.println("   WAIT/NOTIFY:");
        System.out.println("   • Already using synchronized blocks");
        System.out.println("   • Complex state-based conditions");
        System.out.println("   • Simpler code for producer-consumer");
        System.out.println();

        System.out.println("═".repeat(60));
        System.out.println("Both approaches are valid! Choose based on your context.");
        System.out.println("═".repeat(60));
    }
}
