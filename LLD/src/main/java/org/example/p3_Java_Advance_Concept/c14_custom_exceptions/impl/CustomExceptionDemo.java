package org.example.p3_Java_Advance_Concept.c14_custom_exceptions.impl;

import org.example.p3_Java_Advance_Concept.c14_custom_exceptions.exception.InsufficientFundsException;
import org.example.p3_Java_Advance_Concept.c14_custom_exceptions.exception.InvalidAmountException;

/**
 * Demonstrates custom exceptions in action.
 */
public class CustomExceptionDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              CUSTOM EXCEPTIONS DEMONSTRATION                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateCheckedVsUnchecked();
        demonstrateExceptionContext();
        demonstrateHierarchy();
    }

    private static void demonstrateCheckedVsUnchecked() {
        System.out.println("📋 DEMO 1: Checked vs Unchecked Custom Exceptions");
        System.out.println("─".repeat(60));
        System.out.println();

        BankAccount account = new BankAccount(100.0);

        // Unchecked: InvalidAmountException
        System.out.println("  [Unchecked] InvalidAmountException:");
        System.out.println("  - Extends RuntimeException");
        System.out.println("  - Caller CAN validate before calling");
        System.out.println();
        try {
            System.out.println("    Calling deposit(-50)...");
            account.deposit(-50);
        } catch (InvalidAmountException e) {
            System.out.println("    ✓ Caught: " + e.getMessage());
            System.out.println("    → This is a bug in caller's code!");
        }
        System.out.println();

        // Checked: InsufficientFundsException
        System.out.println("  [Checked] InsufficientFundsException:");
        System.out.println("  - Extends Exception");
        System.out.println("  - Caller CANNOT always prevent (balance may change)");
        System.out.println("  - Caller CAN recover (show balance, suggest deposit)");
        System.out.println();
        try {
            System.out.println("    Calling withdraw(500) with balance=100...");
            account.withdraw(500);
        } catch (InsufficientFundsException e) {
            System.out.println("    ✓ Caught: " + e.getMessage());
            System.out.println("    → Caller can show: 'Need $" + e.getShortfall() + " more'");
        }
        System.out.println();
    }

    private static void demonstrateExceptionContext() {
        System.out.println("📋 DEMO 2: Exception Context");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Custom exceptions carry domain-specific data!");
        System.out.println();

        BankAccount account = new BankAccount(75.0);

        try {
            account.withdraw(200);
        } catch (InsufficientFundsException e) {
            // Context: we can access specific fields!
            System.out.println("  Exception caught with rich context:");
            System.out.println("    getCurrentBalance(): " + e.getCurrentBalance());
            System.out.println("    getRequestedAmount(): " + e.getRequestedAmount());
            System.out.println("    getShortfall(): " + e.getShortfall());
            System.out.println();
            System.out.println("  This enables smart error messages:");
            System.out.printf("    'You need $%.2f more to complete this transaction.'%n",
                    e.getShortfall());
        }
        System.out.println();
    }

    private static void demonstrateHierarchy() {
        System.out.println("📋 DEMO 3: Exception Hierarchy");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  Exception hierarchy allows flexible catching:");
        System.out.println();
        System.out.println("    BankingException (catches all banking errors)");
        System.out.println("        └── InsufficientFundsException");
        System.out.println("        └── (other banking exceptions...)");
        System.out.println();

        BankAccount account = new BankAccount(50);

        System.out.println("  Catching with base class BankingException:");
        try {
            account.withdraw(100);
        } catch (Exception e) {
            if (e.getClass().getSimpleName().equals("InsufficientFundsException")) {
                System.out.println("    ✓ Caught via BankingException hierarchy!");
            }
        }
        System.out.println();
    }

    /**
     * Simple bank account for demonstration.
     */
    private static class BankAccount {
        private double balance;

        BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            if (amount <= 0) {
                throw new InvalidAmountException(amount);
            }
            balance += amount;
        }

        void withdraw(double amount) throws InsufficientFundsException {
            if (amount <= 0) {
                throw new InvalidAmountException(amount);
            }
            if (amount > balance) {
                throw new InsufficientFundsException(balance, amount);
            }
            balance -= amount;
        }
    }
}
