package org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl;

import org.example.p3_Java_Advance_Concept.c12_try_catch_finally.contracts.IResourceHandler;

/**
 * A simple demonstration of try-catch basics.
 * <p>
 * Shows the fundamental mechanics of exception catching.
 * </p>
 */
public class SimpleTryCatchDemo implements IResourceHandler {

    private boolean isOpen = false;
    private boolean shouldFail = false;

    public SimpleTryCatchDemo() {
    }

    public SimpleTryCatchDemo(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    @Override
    public void open() throws Exception {
        System.out.println("  → Opening resource...");
        if (shouldFail) {
            throw new Exception("Failed to open resource!");
        }
        isOpen = true;
        System.out.println("  ✓ Resource opened successfully");
    }

    @Override
    public void process() throws Exception {
        System.out.println("  → Processing...");
        System.out.println("  ✓ Processing complete");
    }

    @Override
    public void close() {
        System.out.println("  → Closing resource...");
        isOpen = false;
        System.out.println("  ✓ Resource closed");
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public String getResourceName() {
        return "SimpleTryCatchDemo";
    }

    /**
     * Demonstrates basic try-catch flow.
     */
    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BASIC TRY-CATCH DEMONSTRATION                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Demo 1: No exception
        System.out.println("📗 DEMO 1: No Exception (Happy Path)");
        System.out.println("─".repeat(50));
        System.out.println();
        System.out.println("Code:");
        System.out.println("  try {");
        System.out.println("      int result = 10 / 2;");
        System.out.println("      System.out.println(\"Result: \" + result);");
        System.out.println("  } catch (ArithmeticException e) {");
        System.out.println("      System.out.println(\"Error!\");");
        System.out.println("  }");
        System.out.println();
        System.out.println("Execution:");

        try {
            int result = 10 / 2;
            System.out.println("  Result: " + result);
            System.out.println("  → catch block is SKIPPED (no exception)");
        } catch (ArithmeticException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();

        // Demo 2: Exception thrown
        System.out.println("📕 DEMO 2: Exception Thrown");
        System.out.println("─".repeat(50));
        System.out.println();
        System.out.println("Code:");
        System.out.println("  try {");
        System.out.println("      int result = 10 / 0;  // Division by zero!");
        System.out.println("      System.out.println(\"Result: \" + result);  // SKIPPED!");
        System.out.println("  } catch (ArithmeticException e) {");
        System.out.println("      System.out.println(\"Caught: \" + e.getMessage());");
        System.out.println("  }");
        System.out.println();
        System.out.println("Execution:");

        try {
            System.out.println("  → Attempting 10 / 0...");
            int result = 10 / 0; // This throws!
            System.out.println("  Result: " + result); // Never reached
        } catch (ArithmeticException e) {
            System.out.println("  💥 Exception! Control jumps to catch block");
            System.out.println("  Caught: " + e.getMessage());
        }
        System.out.println("  → Execution continues after catch block");
        System.out.println();
    }
}
