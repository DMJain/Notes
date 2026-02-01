package org.example.p3_Java_Advance_Concept.c16_try_with_resources.impl;

/**
 * Demonstrates try-with-resources functionality.
 */
public class TryWithResourcesDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            TRY-WITH-RESOURCES DEMONSTRATION                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateBasicUsage();
        demonstrateMultipleResources();
        demonstrateSuppressedExceptions();
    }

    private static void demonstrateBasicUsage() {
        System.out.println("📗 DEMO 1: Basic Try-with-Resources");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Using a custom AutoCloseable resource:");
        System.out.println();

        try (MockConnection conn = new MockConnection("db-server")) {
            conn.query("SELECT * FROM users");
        }

        System.out.println();
        System.out.println("  Notice: close() was called automatically!");
        System.out.println();
    }

    private static void demonstrateMultipleResources() {
        System.out.println("📘 DEMO 2: Multiple Resources (Reverse Close Order)");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Opening multiple resources...");
        System.out.println();

        try (
                MockConnection conn1 = new MockConnection("server-1");
                MockConnection conn2 = new MockConnection("server-2");
                MockConnection conn3 = new MockConnection("server-3")) {
            System.out.println("    All resources open, doing work...");
        }

        System.out.println();
        System.out.println("  Closed in REVERSE order (last opened = first closed)!");
        System.out.println();
    }

    private static void demonstrateSuppressedExceptions() {
        System.out.println("📙 DEMO 3: Suppressed Exceptions");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  What if BOTH try block AND close() throw?");
        System.out.println();

        try (ProblematicResource r = new ProblematicResource()) {
            throw new RuntimeException("Error in try block!");
        } catch (Exception e) {
            System.out.println("  Main exception: " + e.getMessage());
            System.out.println();
            System.out.println("  Suppressed exceptions:");
            Throwable[] suppressed = e.getSuppressed();
            if (suppressed.length == 0) {
                System.out.println("    (none)");
            }
            for (Throwable t : suppressed) {
                System.out.println("    - " + t.getMessage());
            }
        }
        System.out.println();
        System.out.println("  The close() exception was SUPPRESSED (not lost)!");
        System.out.println();
    }

    // Custom AutoCloseable for demonstration
    private static class MockConnection implements AutoCloseable {
        private final String name;

        MockConnection(String name) {
            this.name = name;
            System.out.println("    → Opening connection: " + name);
        }

        void query(String sql) {
            System.out.println("    → Executing on " + name + ": " + sql);
        }

        @Override
        public void close() {
            System.out.println("    → Closing connection: " + name);
        }
    }

    // Resource that throws on close
    private static class ProblematicResource implements AutoCloseable {
        ProblematicResource() {
            System.out.println("    → Opening problematic resource");
        }

        @Override
        public void close() {
            System.out.println("    → close() is throwing...");
            throw new RuntimeException("Error in close()!");
        }
    }
}
