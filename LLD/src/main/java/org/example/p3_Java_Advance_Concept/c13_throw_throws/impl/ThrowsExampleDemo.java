package org.example.p3_Java_Advance_Concept.c13_throw_throws.impl;

import java.io.FileNotFoundException;

/**
 * Demonstrates the 'throws' keyword - declaring potential exceptions.
 */
public class ThrowsExampleDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║             THE 'throws' KEYWORD DEMONSTRATION                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printExplanation();
        demonstrateThrowsDeclaration();
        demonstratePropagation();
    }

    private static void printExplanation() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│ WHAT IS 'throws'?                                              │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                │");
        System.out.println("│ 'throws' is a DECLARATION - it warns callers that this        │");
        System.out.println("│ method might throw certain exceptions.                         │");
        System.out.println("│                                                                │");
        System.out.println("│ SYNTAX:                                                        │");
        System.out.println("│   public void foo() throws IOException, SQLException { }       │");
        System.out.println("│                      ↑                                         │");
        System.out.println("│                      In method signature                       │");
        System.out.println("│                                                                │");
        System.out.println("│ WHEN TO USE:                                                   │");
        System.out.println("│   • You can't meaningfully handle the exception here          │");
        System.out.println("│   • The caller should decide how to handle it                 │");
        System.out.println("│   • For checked exceptions you don't catch                    │");
        System.out.println("│                                                                │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private static void demonstrateThrowsDeclaration() {
        System.out.println("📘 DEMO 1: throws Declaration");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Method signature with throws:");
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────────┐");
        System.out.println("  │ // This method declares it might throw             │");
        System.out.println("  │ public Config loadConfig(String path)              │");
        System.out.println("  │         throws FileNotFoundException {             │");
        System.out.println("  │     return parse(new FileReader(path));            │");
        System.out.println("  │ }                                                  │");
        System.out.println("  │                                                    │");
        System.out.println("  │ // Caller MUST handle or propagate:                │");
        System.out.println("  │ try {                                              │");
        System.out.println("  │     Config c = loadConfig(\"app.conf\");             │");
        System.out.println("  │ } catch (FileNotFoundException e) {                │");
        System.out.println("  │     useDefaults();                                 │");
        System.out.println("  │ }                                                  │");
        System.out.println("  └────────────────────────────────────────────────────┘");
        System.out.println();

        // Live demo
        System.out.println("  LIVE DEMO:");
        try {
            System.out.println("    Calling loadConfig(\"missing.conf\")...");
            loadConfig("missing.conf");
        } catch (FileNotFoundException e) {
            System.out.println("    ✓ Exception propagated and caught!");
            System.out.println("      Message: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demonstratePropagation() {
        System.out.println("📙 DEMO 2: Exception Propagation Chain");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Watch how exception propagates up the call stack:");
        System.out.println();
        System.out.println("  Call chain: main → processOrder → validatePayment → chargeCard");
        System.out.println();

        try {
            System.out.println("  [main] Calling processOrder()...");
            processOrder();
            System.out.println("  [main] Order completed!"); // Never reached
        } catch (PaymentException e) {
            System.out.println("  [main] 💥 Caught exception that propagated up!");
            System.out.println("         Type: " + e.getClass().getSimpleName());
            System.out.println("         Message: " + e.getMessage());
        }
        System.out.println();

        System.out.println("  Flow:");
        System.out.println("    chargeCard() ─── throws ──▶ validatePayment()");
        System.out.println("                                     │");
        System.out.println("                               throws (propagates)");
        System.out.println("                                     ▼");
        System.out.println("                              processOrder()");
        System.out.println("                                     │");
        System.out.println("                               throws (propagates)");
        System.out.println("                                     ▼");
        System.out.println("                                  main()");
        System.out.println("                                     │");
        System.out.println("                                 catches!");
        System.out.println();
    }

    // Simulates loading config - throws checked exception
    private static String loadConfig(String path) throws FileNotFoundException {
        // Simulating file not found
        throw new FileNotFoundException("File not found: " + path);
    }

    // Payment exception for demo
    private static class PaymentException extends Exception {
        PaymentException(String message) {
            super(message);
        }
    }

    // Call chain demonstration
    private static void processOrder() throws PaymentException {
        System.out.println("    [processOrder] Calling validatePayment()...");
        validatePayment();
        System.out.println("    [processOrder] Payment validated!"); // Never reached
    }

    private static void validatePayment() throws PaymentException {
        System.out.println("      [validatePayment] Calling chargeCard()...");
        chargeCard();
        System.out.println("      [validatePayment] Card charged!"); // Never reached
    }

    private static void chargeCard() throws PaymentException {
        System.out.println("        [chargeCard] Attempting to charge...");
        System.out.println("        [chargeCard] 💥 Card declined! Throwing exception...");
        throw new PaymentException("Card declined: insufficient funds");
    }
}
