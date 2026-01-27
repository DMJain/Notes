package org.example.p3_Java_Advance_Concept.c1_generics.runner;

import org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios.*;
import org.example.p3_Java_Advance_Concept.c1_generics.runner.comparison.BeforeAfterDemo;

/**
 * Main runner that orchestrates all generics demonstrations.
 * <p>
 * This class runs all scenarios to demonstrate different aspects of Java
 * generics.
 * </p>
 */
public class GenericsRunner {

    /**
     * Runs all generics scenarios.
     */
    public static void runAllScenarios() {

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              JAVA GENERICS DEMONSTRATIONS                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

        // Run each scenario
        new GenericClassScenario().execute();
        printSeparator();

        new GenericMethodScenario().execute();
        printSeparator();

        new WildcardScenario().execute();
        printSeparator();

        new BoundedGenericScenario().execute();
        printSeparator();

        new GenericInterfaceScenario().execute();
        printSeparator();

        new TypeErasureScenario().execute();
        printSeparator();

        // Comparison demo
        new BeforeAfterDemo().execute();

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 ALL SCENARIOS COMPLETED ✓                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static void printSeparator() {
        System.out.println("\n" + "─".repeat(64) + "\n");
    }
}
