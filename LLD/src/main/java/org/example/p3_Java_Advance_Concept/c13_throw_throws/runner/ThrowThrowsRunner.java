package org.example.p3_Java_Advance_Concept.c13_throw_throws.runner;

import org.example.p3_Java_Advance_Concept.c13_throw_throws.impl.ThrowExampleDemo;
import org.example.p3_Java_Advance_Concept.c13_throw_throws.impl.ThrowsExampleDemo;

/**
 * Main runner for the Throw vs Throws module.
 */
public class ThrowThrowsRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    THROW vs THROWS MODULE                        ║");
        System.out.println("║            Understanding Exception Propagation                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printKeyDifference();
        ThrowExampleDemo.runDemo();
        ThrowsExampleDemo.runDemo();
        printSummary();
    }

    private static void printKeyDifference() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    THE KEY DIFFERENCE                          │");
        System.out.println("├─────────────────────────────┬──────────────────────────────────┤");
        System.out.println("│          throw              │            throws                │");
        System.out.println("├─────────────────────────────┼──────────────────────────────────┤");
        System.out.println("│ ACTION (do it now)          │ DECLARATION (might happen)       │");
        System.out.println("│ Inside method body          │ In method signature              │");
        System.out.println("│ throw new Exception();      │ void foo() throws Exception {}   │");
        System.out.println("│ Pulls the fire alarm        │ Sign: \"Fire alarm installed\"    │");
        System.out.println("└─────────────────────────────┴──────────────────────────────────┘");
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         MODULE SUMMARY                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                  ║");
        System.out.println("║  throw = ACTION (creates & throws exception NOW)                 ║");
        System.out.println("║  throws = DECLARATION (warns that method MIGHT throw)            ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  HANDLE vs PROPAGATE:                                            ║");
        System.out.println("║    • Can YOU recover? → catch it                                ║");
        System.out.println("║    • Need CALLER to decide? → throws                            ║");
        System.out.println("║                                                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  NEXT: c14_custom_exceptions - Create your own exception types  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
}
