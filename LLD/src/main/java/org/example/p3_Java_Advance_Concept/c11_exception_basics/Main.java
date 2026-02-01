package org.example.p3_Java_Advance_Concept.c11_exception_basics;

import org.example.p3_Java_Advance_Concept.c11_exception_basics.runner.ExceptionBasicsRunner;

/**
 * Entry point for the Exception Basics module.
 * <p>
 * Run this class to see all exception fundamentals demonstrated:
 * <ol>
 *   <li>Exception Hierarchy (Throwable → Error/Exception)</li>
 *   <li>Checked vs Unchecked Exceptions</li>
 *   <li>Common Exception Types</li>
 *   <li>When to Use Each Type</li>
 * </ol>
 * </p>
 *
 * @see ExceptionBasicsRunner
 */
public class Main {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                   EXCEPTION BASICS MODULE                     ");
        System.out.println("         p3_Java_Advance_Concept/c11_exception_basics          ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        ExceptionBasicsRunner.runAllScenarios();
    }
}
