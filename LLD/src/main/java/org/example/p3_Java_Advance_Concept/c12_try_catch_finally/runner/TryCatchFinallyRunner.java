package org.example.p3_Java_Advance_Concept.c12_try_catch_finally.runner;

import org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl.FinallyBlockDemo;
import org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl.MultipleCatchDemo;
import org.example.p3_Java_Advance_Concept.c12_try_catch_finally.impl.SimpleTryCatchDemo;

/**
 * Main runner for the Try-Catch-Finally module.
 * <p>
 * Orchestrates all demonstrations to teach the mechanics
 * of exception handling in Java.
 * </p>
 */
public class TryCatchFinallyRunner {

    /**
     * Runs all try-catch-finally scenarios.
     */
    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                  TRY-CATCH-FINALLY MODULE                        ║");
        System.out.println("║        Understanding Exception Handling Mechanics                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printLearningObjectives();

        // Run all demos
        SimpleTryCatchDemo.runDemo();
        MultipleCatchDemo.runDemo();
        FinallyBlockDemo.runDemo();

        // Summary
        printSummary();
    }

    private static void printLearningObjectives() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    LEARNING OBJECTIVES                         │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                │");
        System.out.println("│  By the end of this module, you will understand:              │");
        System.out.println("│                                                                │");
        System.out.println("│  1. How the TRY block monitors for exceptions                 │");
        System.out.println("│  2. How CATCH blocks handle specific exception types          │");
        System.out.println("│  3. Why catch ordering matters (specific before general)      │");
        System.out.println("│  4. Multi-catch syntax (Java 7+)                              │");
        System.out.println("│  5. Why FINALLY always runs and when to use it                │");
        System.out.println("│  6. The return-in-finally gotcha                              │");
        System.out.println("│                                                                │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         MODULE SUMMARY                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                  ║");
        System.out.println("║  STRUCTURE:                                                      ║");
        System.out.println("║    try { risky code }                                           ║");
        System.out.println("║    catch (SpecificEx e) { handle }   ← specific first!         ║");
        System.out.println("║    catch (GeneralEx e) { handle }    ← general last            ║");
        System.out.println("║    finally { cleanup }               ← ALWAYS runs!            ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  KEY RULES:                                                      ║");
        System.out.println("║    ✅ Catch specific exceptions first                           ║");
        System.out.println("║    ✅ Only ONE catch block executes                             ║");
        System.out.println("║    ✅ finally runs even with return/throw                       ║");
        System.out.println("║    ❌ Never put return in finally                               ║");
        System.out.println("║    ❌ Never use empty catch blocks                              ║");
        System.out.println("║                                                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  NEXT: c13_throw_throws - How to THROW exceptions               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
