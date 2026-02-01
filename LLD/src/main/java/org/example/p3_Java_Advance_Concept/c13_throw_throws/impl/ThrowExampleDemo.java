package org.example.p3_Java_Advance_Concept.c13_throw_throws.impl;

/**
 * Demonstrates the 'throw' keyword - creating and throwing exceptions.
 */
public class ThrowExampleDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              THE 'throw' KEYWORD DEMONSTRATION                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printExplanation();
        demonstrateThrowBasics();
        demonstrateGuardClauses();
    }

    private static void printExplanation() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│ WHAT IS 'throw'?                                               │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                │");
        System.out.println("│ 'throw' is an ACTION - it creates and throws an exception     │");
        System.out.println("│ at that exact point in the code.                               │");
        System.out.println("│                                                                │");
        System.out.println("│ SYNTAX:                                                        │");
        System.out.println("│   throw new ExceptionType(\"message\");                         │");
        System.out.println("│                                                                │");
        System.out.println("│ WHEN TO USE:                                                   │");
        System.out.println("│   • Validation fails                                          │");
        System.out.println("│   • Preconditions not met                                     │");
        System.out.println("│   • Invalid state detected                                    │");
        System.out.println("│   • Signaling an error condition                              │");
        System.out.println("│                                                                │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private static void demonstrateThrowBasics() {
        System.out.println("📕 DEMO 1: Basic throw");
        System.out.println("─".repeat(60));
        System.out.println();

        try {
            System.out.println("  Calling validateAge(-5)...");
            validateAge(-5);
            System.out.println("  This line never runs!");
        } catch (IllegalArgumentException e) {
            System.out.println("  💥 Exception caught!");
            System.out.println("     Type: " + e.getClass().getSimpleName());
            System.out.println("     Message: " + e.getMessage());
        }
        System.out.println();
    }

    private static void validateAge(int age) {
        System.out.println("    → Inside validateAge(" + age + ")");
        if (age < 0) {
            System.out.println("    → Age is negative! Throwing exception...");
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("    → Age is valid");
    }

    private static void demonstrateGuardClauses() {
        System.out.println("📗 DEMO 2: Guard Clauses Pattern");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Guard clauses validate at the START of a method.");
        System.out.println("  They 'guard' the happy path from invalid inputs.");
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────┐");
        System.out.println("  │ public void transfer(Account from, Account to, │");
        System.out.println("  │                      double amount) {          │");
        System.out.println("  │                                                │");
        System.out.println("  │     // GUARD CLAUSES - validate first!         │");
        System.out.println("  │     if (from == null)                          │");
        System.out.println("  │         throw new IllegalArgumentException();  │");
        System.out.println("  │     if (to == null)                            │");
        System.out.println("  │         throw new IllegalArgumentException();  │");
        System.out.println("  │     if (amount <= 0)                           │");
        System.out.println("  │         throw new IllegalArgumentException();  │");
        System.out.println("  │                                                │");
        System.out.println("  │     // HAPPY PATH - guards passed!             │");
        System.out.println("  │     from.withdraw(amount);                     │");
        System.out.println("  │     to.deposit(amount);                        │");
        System.out.println("  │ }                                              │");
        System.out.println("  └────────────────────────────────────────────────┘");
        System.out.println();

        // Live demo
        System.out.println("  LIVE DEMO:");
        try {
            System.out.println("    Calling createUser(null, \"test@email.com\")...");
            createUser(null, "test@email.com");
        } catch (IllegalArgumentException e) {
            System.out.println("    ✓ Caught: " + e.getMessage());
        }

        try {
            System.out.println("    Calling createUser(\"John\", \"invalid-email\")...");
            createUser("John", "invalid-email");
        } catch (IllegalArgumentException e) {
            System.out.println("    ✓ Caught: " + e.getMessage());
        }

        try {
            System.out.println("    Calling createUser(\"John\", \"john@example.com\")...");
            createUser("John", "john@example.com");
            System.out.println("    ✓ User created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("    Caught: " + e.getMessage());
        }
        System.out.println();
    }

    private static void createUser(String name, String email) {
        // Guard clauses
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // Happy path
        System.out.println("      → Creating user: " + name + " (" + email + ")");
    }
}
