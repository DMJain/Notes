package org.example.p3_Java_Advance_Concept.c14_custom_exceptions.runner;

import org.example.p3_Java_Advance_Concept.c14_custom_exceptions.impl.CustomExceptionDemo;

/**
 * Main runner for the Custom Exceptions module.
 */
public class CustomExceptionsRunner {

    public static void runAllScenarios() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                  CUSTOM EXCEPTIONS MODULE                        ║");
        System.out.println("║             Designing Domain-Specific Errors                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printWhenToCreate();
        CustomExceptionDemo.runDemo();
        printSummary();
    }

    private static void printWhenToCreate() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│              WHEN TO CREATE CUSTOM EXCEPTIONS?                 │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                │");
        System.out.println("│  ✅ CREATE when:                                              │");
        System.out.println("│     • Existing exceptions don't describe your error           │");
        System.out.println("│     • You need to carry domain-specific context               │");
        System.out.println("│     • Caller needs to handle this case specifically           │");
        System.out.println("│                                                                │");
        System.out.println("│  ❌ DON'T CREATE when:                                        │");
        System.out.println("│     • Standard exception works (IllegalArgumentException)     │");
        System.out.println("│     • You just want a custom message (use existing type)      │");
        System.out.println("│                                                                │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         MODULE SUMMARY                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                  ║");
        System.out.println("║  CHECKED (extends Exception):                                    ║");
        System.out.println("║    • Recoverable errors, caller MUST handle                     ║");
        System.out.println("║    • Example: InsufficientFundsException                        ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  UNCHECKED (extends RuntimeException):                           ║");
        System.out.println("║    • Programming bugs or fatal errors                           ║");
        System.out.println("║    • Example: InvalidAmountException                            ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  BEST PRACTICES:                                                 ║");
        System.out.println("║    • Include context fields (getBalance(), getOrderId())        ║");
        System.out.println("║    • Provide message + cause constructor for chaining           ║");
        System.out.println("║    • Create hierarchy for related exceptions                    ║");
        System.out.println("║                                                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  NEXT: c15_exception_chaining - Preserving root cause info      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
}
