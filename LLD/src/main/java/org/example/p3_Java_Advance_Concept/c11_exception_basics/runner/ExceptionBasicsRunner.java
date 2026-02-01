package org.example.p3_Java_Advance_Concept.c11_exception_basics.runner;

import org.example.p3_Java_Advance_Concept.c11_exception_basics.contracts.IExceptionDemo;
import org.example.p3_Java_Advance_Concept.c11_exception_basics.impl.CheckedExceptionDemo;
import org.example.p3_Java_Advance_Concept.c11_exception_basics.impl.UncheckedExceptionDemo;
import org.example.p3_Java_Advance_Concept.c11_exception_basics.runner.scenarios.CheckedVsUncheckedScenario;
import org.example.p3_Java_Advance_Concept.c11_exception_basics.runner.scenarios.ExceptionHierarchyScenario;

import java.util.Arrays;
import java.util.List;

/**
 * Main runner for the Exception Basics module.
 * <p>
 * Orchestrates all demonstrations and scenarios to teach
 * the fundamentals of Java exceptions.
 * </p>
 */
public class ExceptionBasicsRunner {

    /**
     * Runs all exception basics scenarios and demonstrations.
     */
    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    EXCEPTION BASICS MODULE                       ║");
        System.out.println("║     Understanding Java's Exception Hierarchy & Categories        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printLearningObjectives();

        // Run hierarchy scenario first
        System.out.println();
        System.out.println("▶ PART 1: Exception Hierarchy");
        System.out.println("═".repeat(70));
        new ExceptionHierarchyScenario().execute();

        // Run checked vs unchecked scenario
        System.out.println();
        System.out.println("▶ PART 2: Checked vs Unchecked Comparison");
        System.out.println("═".repeat(70));
        new CheckedVsUncheckedScenario().execute();

        // Run individual demos
        System.out.println();
        System.out.println("▶ PART 3: Detailed Demonstrations");
        System.out.println("═".repeat(70));
        runDemos();

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
        System.out.println("│  1. WHAT exceptions are (objects representing errors)         │");
        System.out.println("│  2. WHY they exist (graceful error handling)                  │");
        System.out.println("│  3. The exception HIERARCHY (Throwable → Error/Exception)     │");
        System.out.println("│  4. CHECKED vs UNCHECKED exceptions (when to use each)        │");
        System.out.println("│  5. Common exception types and their purposes                 │");
        System.out.println("│                                                                │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    private static void runDemos() {
        List<IExceptionDemo> demos = Arrays.asList(
                new CheckedExceptionDemo(),
                new UncheckedExceptionDemo());

        for (int i = 0; i < demos.size(); i++) {
            IExceptionDemo demo = demos.get(i);
            System.out.println();
            System.out.println("┌" + "─".repeat(68) + "┐");
            System.out.printf("│ Demo %d/%d: %-55s │%n", i + 1, demos.size(), demo.getDemoName());
            System.out.println("├" + "─".repeat(68) + "┤");
            System.out.printf("│ %-66s │%n", demo.getDescription());
            System.out.println("└" + "─".repeat(68) + "┘");

            demo.demonstrate();

            if (i < demos.size() - 1) {
                System.out.println();
                System.out.println("─".repeat(70));
            }
        }
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         MODULE SUMMARY                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                  ║");
        System.out.println("║  EXCEPTION HIERARCHY:                                            ║");
        System.out.println("║    Throwable → Error (JVM dying - don't catch)                  ║");
        System.out.println("║             → Exception → Checked (must handle)                 ║");
        System.out.println("║                       → RuntimeException (optional)             ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  KEY INSIGHT:                                                    ║");
        System.out.println("║    • Checked = External failures (files, network, DB)           ║");
        System.out.println("║    • Unchecked = Programming bugs (NPE, bounds, cast)           ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  BEST PRACTICE:                                                  ║");
        System.out.println("║    For RuntimeExceptions: PREVENT the bug, don't catch it!      ║");
        System.out.println("║                                                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  NEXT: c12_try_catch_finally - How to HANDLE exceptions         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
