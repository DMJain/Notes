package org.example.p3_Java_Advance_Concept.c12_try_catch_finally;

import org.example.p3_Java_Advance_Concept.c12_try_catch_finally.runner.TryCatchFinallyRunner;

/**
 * Entry point for the Try-Catch-Finally module.
 * <p>
 * Run this class to see all exception handling mechanics demonstrated:
 * <ol>
 * <li>Basic try-catch flow</li>
 * <li>Multiple catch blocks and ordering</li>
 * <li>Multi-catch syntax</li>
 * <li>Finally block behavior</li>
 * <li>Common gotchas</li>
 * </ol>
 * </p>
 *
 * @see TryCatchFinallyRunner
 */
public class Main {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                  TRY-CATCH-FINALLY MODULE                     ");
        System.out.println("        p3_Java_Advance_Concept/c12_try_catch_finally          ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        TryCatchFinallyRunner.runAllScenarios();
    }
}
