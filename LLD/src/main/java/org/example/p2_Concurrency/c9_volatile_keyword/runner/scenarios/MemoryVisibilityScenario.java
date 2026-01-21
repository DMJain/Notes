package org.example.p2_Concurrency.c9_volatile_keyword.runner.scenarios;

import org.example.p2_Concurrency.c9_volatile_keyword.impl.VolatileFlag;

/**
 * Demonstrates how volatile ensures memory visibility.
 * 
 * <p>
 * Note: The visibility problem is hard to reproduce reliably because
 * JVM optimizations vary. This demo shows the CONCEPT with volatile working.
 * </p>
 */
public class MemoryVisibilityScenario {

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           MEMORY VISIBILITY DEMO                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("  Testing with VOLATILE flag:");
        testVolatileFlag();

        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  volatile GUARANTEES:                                   │");
        System.out.println("  │                                                         │");
        System.out.println("  │  • Writes go directly to main memory                    │");
        System.out.println("  │  • Reads come directly from main memory                 │");
        System.out.println("  │  • All threads see the same value                       │");
        System.out.println("  │                                                         │");
        System.out.println("  │  BUT: volatile does NOT make count++ atomic!            │");
        System.out.println("  │  For counters, use AtomicInteger instead.               │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private void testVolatileFlag() {
        VolatileFlag flag = new VolatileFlag();

        // Reader thread
        Thread reader = new Thread(() -> {
            System.out.println("    [Reader] Starting, waiting for flag...");
            while (!flag.getValue()) {
                // Busy wait - with volatile, this will see the update
            }
            System.out.println("    [Reader] Flag detected as TRUE! ✅");
        });

        // Writer thread
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100); // Small delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("    [Writer] Setting flag to TRUE");
            flag.setTrue();
        });

        reader.start();
        writer.start();

        try {
            reader.join(2000); // Timeout after 2 seconds
            writer.join(100);

            if (reader.isAlive()) {
                System.out.println("    [Reader] TIMEOUT - still waiting (visibility issue!)");
                reader.interrupt();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
