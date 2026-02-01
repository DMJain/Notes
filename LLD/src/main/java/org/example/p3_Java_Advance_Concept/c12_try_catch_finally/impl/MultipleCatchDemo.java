package org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Demonstrates handling multiple different exceptions.
 * <p>
 * Key Learning Points:
 * <ul>
 * <li>Order matters: specific exceptions before general</li>
 * <li>Only ONE catch block executes</li>
 * <li>Multi-catch syntax (Java 7+) for same handling</li>
 * </ul>
 * </p>
 */
public class MultipleCatchDemo {

    /**
     * Runs the multiple catch demonstration.
     */
    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            MULTIPLE CATCH BLOCKS DEMONSTRATION                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateOrderMatters();
        demonstrateMultiCatch();
        demonstrateOnlyOneCatchRuns();
    }

    private static void demonstrateOrderMatters() {
        System.out.println("📋 DEMO 1: Order Matters!");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("  Exception Hierarchy for this demo:");
        System.out.println("  ");
        System.out.println("    Exception");
        System.out.println("       └── IOException");
        System.out.println("              └── FileNotFoundException");
        System.out.println();
        System.out.println("  ✅ CORRECT ORDER (specific first):");
        System.out.println("  ┌────────────────────────────────────────────┐");
        System.out.println("  │ try {                                      │");
        System.out.println("  │     riskyOperation();                      │");
        System.out.println("  │ } catch (FileNotFoundException e) {        │  ← Most specific");
        System.out.println("  │     // handle file not found               │");
        System.out.println("  │ } catch (IOException e) {                  │  ← Less specific");
        System.out.println("  │     // handle other IO errors              │");
        System.out.println("  │ } catch (Exception e) {                    │  ← Most general");
        System.out.println("  │     // handle everything else              │");
        System.out.println("  │ }                                          │");
        System.out.println("  └────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  ❌ WRONG ORDER (won't compile!):");
        System.out.println("  ┌────────────────────────────────────────────┐");
        System.out.println("  │ catch (Exception e) { }        ← Catches   │");
        System.out.println("  │ catch (IOException e) { }      ← UNREACHABLE!");
        System.out.println("  └────────────────────────────────────────────┘");
        System.out.println();

        // Live demonstration
        System.out.println("  LIVE DEMO: Throwing FileNotFoundException");
        System.out.println();
        try {
            throwFileNotFound();
        } catch (FileNotFoundException e) {
            System.out.println("  ✓ Caught by FileNotFoundException handler");
            System.out.println("    Message: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("  Caught by IOException handler");
        } catch (Exception e) {
            System.out.println("  Caught by Exception handler");
        }
        System.out.println();
    }

    private static void demonstrateMultiCatch() {
        System.out.println("📋 DEMO 2: Multi-Catch (Java 7+)");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("  When you want to handle multiple exceptions the SAME way:");
        System.out.println();
        System.out.println("  ❌ BEFORE (repetitive):");
        System.out.println("  ┌────────────────────────────────────────────┐");
        System.out.println("  │ catch (IOException e) {                    │");
        System.out.println("  │     log(e); retry();    // Same code!      │");
        System.out.println("  │ } catch (SQLException e) {                 │");
        System.out.println("  │     log(e); retry();    // Duplicated!     │");
        System.out.println("  │ }                                          │");
        System.out.println("  └────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  ✅ AFTER (using multi-catch):");
        System.out.println("  ┌────────────────────────────────────────────┐");
        System.out.println("  │ catch (IOException | SQLException e) {     │");
        System.out.println("  │     log(e); retry();    // Single handler! │");
        System.out.println("  │ }                                          │");
        System.out.println("  └────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  Note: e is implicitly 'final' in multi-catch");
        System.out.println();

        // Live demonstration
        System.out.println("  LIVE DEMO: Multi-catch in action");
        System.out.println();

        try {
            // Simulate throwing one of multiple possible exceptions
            if (Math.random() > 0.5) {
                throw new IllegalArgumentException("Bad argument");
            } else {
                throw new IllegalStateException("Bad state");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  ✓ Caught with multi-catch!");
            System.out.println("    Exception type: " + e.getClass().getSimpleName());
            System.out.println("    Message: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demonstrateOnlyOneCatchRuns() {
        System.out.println("📋 DEMO 3: Only ONE Catch Runs");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("  Important: Only the FIRST matching catch block executes!");
        System.out.println();

        try {
            throw new NullPointerException("Test NPE");
        } catch (NullPointerException e) {
            System.out.println("  ✓ NullPointerException caught FIRST");
        } catch (RuntimeException e) {
            System.out.println("  RuntimeException caught"); // Never runs!
        } catch (Exception e) {
            System.out.println("  Exception caught"); // Never runs!
        }

        System.out.println("    → Other catch blocks were SKIPPED");
        System.out.println();
    }

    private static void throwFileNotFound() throws FileNotFoundException {
        throw new FileNotFoundException("config.properties not found");
    }
}
