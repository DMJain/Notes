package org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl;

/**
 * Demonstrates the 'finally' block behavior.
 * <p>
 * Key Learning Points:
 * <ul>
 * <li>Finally ALWAYS runs (with few exceptions)</li>
 * <li>Runs even when return/throw in try or catch</li>
 * <li>Perfect for resource cleanup</li>
 * <li>WARNING: Return in finally overrides try's return!</li>
 * </ul>
 * </p>
 */
public class FinallyBlockDemo {

    /**
     * Runs all finally block demonstrations.
     */
    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║               FINALLY BLOCK DEMONSTRATION                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateFinallyAlwaysRuns();
        demonstrateFinallyWithReturn();
        demonstrateFinallyForCleanup();
        demonstrateFinallyGotcha();
    }

    private static void demonstrateFinallyAlwaysRuns() {
        System.out.println("📗 DEMO 1: Finally ALWAYS Runs");
        System.out.println("─".repeat(60));
        System.out.println();

        // Scenario 1: No exception
        System.out.println("  SCENARIO A: No exception");
        try {
            System.out.println("    [try] Executing normally...");
        } catch (Exception e) {
            System.out.println("    [catch] This won't run");
        } finally {
            System.out.println("    [finally] ✓ Still runs!");
        }
        System.out.println();

        // Scenario 2: Exception caught
        System.out.println("  SCENARIO B: Exception caught");
        try {
            System.out.println("    [try] About to throw...");
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            System.out.println("    [catch] Caught exception: " + e.getMessage());
        } finally {
            System.out.println("    [finally] ✓ Still runs!");
        }
        System.out.println();

        // Scenario 3: Exception NOT caught (we catch it outside for demo)
        System.out.println("  SCENARIO C: Exception NOT caught (propagates up)");
        try {
            try {
                System.out.println("    [try] About to throw...");
                throw new RuntimeException("Uncaught!");
                // No catch block here!
            } finally {
                System.out.println("    [finally] ✓ STILL runs before propagating!");
            }
        } catch (RuntimeException e) {
            System.out.println("    [outer catch] Exception propagated up");
        }
        System.out.println();
    }

    private static void demonstrateFinallyWithReturn() {
        System.out.println("📙 DEMO 2: Finally Runs Even With Return");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Calling method with return in try block...");
        int result = returnFromTry();
        System.out.println("  Returned value: " + result);
        System.out.println();

        System.out.println("  Notice: finally ran BEFORE the return completed!");
        System.out.println();
    }

    private static int returnFromTry() {
        try {
            System.out.println("    [try] About to return 1...");
            return 1;
        } finally {
            System.out.println("    [finally] ✓ Runs after return is prepared, before it completes!");
        }
    }

    private static void demonstrateFinallyForCleanup() {
        System.out.println("📘 DEMO 3: Finally for Resource Cleanup");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Classic pattern (pre-Java 7):");
        System.out.println();

        FakeResource resource = null;
        try {
            resource = new FakeResource("DatabaseConnection");
            resource.open();
            resource.doWork();
            // Even if doWork throws, resource gets closed!
        } catch (Exception e) {
            System.out.println("    [catch] Error: " + e.getMessage());
        } finally {
            // CLEANUP: Always close the resource
            if (resource != null) {
                resource.close();
            }
        }
        System.out.println();
    }

    private static void demonstrateFinallyGotcha() {
        System.out.println("⚠️ DEMO 4: GOTCHA - Return in Finally");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  ❌ NEVER put return in finally!");
        System.out.println("  It OVERRIDES the try block's return:");
        System.out.println();

        int result = returnInFinally();
        System.out.println("  Result: " + result);
        System.out.println("  → Finally's return (2) OVERRODE try's return (1)!");
        System.out.println();

        System.out.println("  Even worse - return in finally can HIDE exceptions:");
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────┐");
        System.out.println("  │ try {                                          │");
        System.out.println("  │     throw new Exception(\"Important error!\");   │");
        System.out.println("  │ } finally {                                    │");
        System.out.println("  │     return 1;  // Exception is LOST! 😱       │");
        System.out.println("  │ }                                              │");
        System.out.println("  └────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  The exception disappears - terrible for debugging!");
        System.out.println();
    }

    @SuppressWarnings("finally")
    private static int returnInFinally() {
        try {
            System.out.println("    [try] Returning 1...");
            return 1;
        } finally {
            System.out.println("    [finally] Returning 2... (overrides!)");
            return 2; // DON'T DO THIS!
        }
    }

    /**
     * Simple fake resource for demonstration.
     */
    private static class FakeResource {
        private final String name;
        private boolean isOpen = false;

        FakeResource(String name) {
            this.name = name;
        }

        void open() {
            System.out.println("    → Opening " + name + "...");
            isOpen = true;
            System.out.println("    ✓ " + name + " opened");
        }

        void doWork() {
            System.out.println("    → Working with " + name + "...");
            System.out.println("    ✓ Work complete");
        }

        void close() {
            System.out.println("    → Closing " + name + "...");
            isOpen = false;
            System.out.println("    ✓ " + name + " closed (in finally!)");
        }
    }
}
