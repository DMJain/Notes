package org.example.p3_Java_Advance_Concept.c11_exception_basics.impl;

import org.example.p3_Java_Advance_Concept.c11_exception_basics.contracts.IExceptionDemo;

/**
 * Demonstrates UNCHECKED exceptions (RuntimeExceptions).
 * <p>
 * Key Learning Points:
 * <ul>
 * <li>Unchecked exceptions extend RuntimeException</li>
 * <li>Compiler does NOT force you to handle them</li>
 * <li>Typically indicate programming bugs that should be FIXED, not caught</li>
 * </ul>
 * </p>
 */
public class UncheckedExceptionDemo implements IExceptionDemo {

    @Override
    public String getDemoName() {
        return "Unchecked Exception Demonstration";
    }

    @Override
    public String getDescription() {
        return "Shows RuntimeExceptions - optional to handle, usually indicate bugs to fix!";
    }

    @Override
    public void demonstrate() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║            UNCHECKED EXCEPTION DEMONSTRATION                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printExplanation();
        demonstrateNullPointerException();
        demonstrateArrayIndexOutOfBounds();
        demonstrateArithmeticException();
        demonstrateIllegalArgumentException();
        showHowToPreventInsteadOfCatch();
    }

    private void printExplanation() {
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ WHAT ARE UNCHECKED (RUNTIME) EXCEPTIONS?                         │");
        System.out.println("├──────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                  │");
        System.out.println("│ • Exceptions that extend RuntimeException                        │");
        System.out.println("│ • Compiler does NOT force you to handle them                    │");
        System.out.println("│ • They typically indicate PROGRAMMING BUGS                      │");
        System.out.println("│                                                                  │");
        System.out.println("│ PHILOSOPHY:                                                      │");
        System.out.println("│   \"Don't catch these bugs - FIX THEM!\"                          │");
        System.out.println("│                                                                  │");
        System.out.println("│ COMMON EXAMPLES:                                                 │");
        System.out.println("│   • NullPointerException    - using null reference              │");
        System.out.println("│   • ArrayIndexOutOfBounds   - invalid array index               │");
        System.out.println("│   • ArithmeticException     - division by zero                  │");
        System.out.println("│   • IllegalArgumentException - invalid method argument          │");
        System.out.println("│   • ClassCastException      - invalid type cast                 │");
        System.out.println("│                                                                  │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    /**
     * Demonstrates NullPointerException - the most infamous exception!
     */
    private void demonstrateNullPointerException() {
        System.out.println("🎯 Demo 1: NullPointerException (NPE)");
        System.out.println("─".repeat(60));
        System.out.println();

        String name = null; // Oops, forgot to initialize!

        System.out.println("String name = null;");
        System.out.println("name.length(); // What happens?");
        System.out.println();

        try {
            // This will throw NullPointerException
            int length = name.length();
            System.out.println("Length: " + length); // Never reached
        } catch (NullPointerException e) {
            System.out.println("💥 BOOM! NullPointerException!");
            System.out.println("  You tried to call a method on a NULL reference!");
            System.out.println();
            System.out.println("  This is a BUG - you should:");
            System.out.println("  ✓ Check for null before using: if (name != null)");
            System.out.println("  ✓ Or ensure the variable is never null");
            System.out.println("  ✗ Don't just catch NPE - FIX the code!");
        }
        System.out.println();
    }

    /**
     * Demonstrates ArrayIndexOutOfBoundsException.
     */
    private void demonstrateArrayIndexOutOfBounds() {
        System.out.println("📊 Demo 2: ArrayIndexOutOfBoundsException");
        System.out.println("─".repeat(60));
        System.out.println();

        int[] numbers = { 10, 20, 30 }; // indices 0, 1, 2 are valid

        System.out.println("int[] numbers = {10, 20, 30}; // size = 3");
        System.out.println("numbers[5]; // What happens?");
        System.out.println();

        try {
            int value = numbers[5]; // Index 5 doesn't exist!
            System.out.println("Value: " + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("💥 BOOM! ArrayIndexOutOfBoundsException!");
            System.out.println("  Index 5 out of bounds for length 3");
            System.out.println();
            System.out.println("  This is a BUG - you should:");
            System.out.println("  ✓ Check array length before accessing");
            System.out.println("  ✓ Use: if (index >= 0 && index < array.length)");
            System.out.println("  ✓ Or use enhanced for loop: for (int n : numbers)");
        }
        System.out.println();
    }

    /**
     * Demonstrates ArithmeticException (division by zero).
     */
    private void demonstrateArithmeticException() {
        System.out.println("➗ Demo 3: ArithmeticException (Division by Zero)");
        System.out.println("─".repeat(60));
        System.out.println();

        int numerator = 10;
        int denominator = 0;

        System.out.println("int result = 10 / 0; // What happens?");
        System.out.println();

        try {
            int result = numerator / denominator;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("💥 BOOM! ArithmeticException!");
            System.out.println("  Division by zero is undefined in integer arithmetic!");
            System.out.println();
            System.out.println("  This is a BUG - you should:");
            System.out.println("  ✓ Check if denominator is zero before dividing");
            System.out.println("  ✓ Use: if (denominator != 0) { result = num / denom; }");
        }
        System.out.println();
    }

    /**
     * Demonstrates IllegalArgumentException - for invalid method arguments.
     * <p>
     * This is one you SHOULD throw to validate method inputs!
     * </p>
     */
    private void demonstrateIllegalArgumentException() {
        System.out.println("⚠️ Demo 4: IllegalArgumentException (Validation)");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("This exception is special - YOU should throw it!");
        System.out.println("Use it to validate method arguments.");
        System.out.println();

        try {
            setAge(-5); // Invalid age!
        } catch (IllegalArgumentException e) {
            System.out.println("✓ CAUGHT: IllegalArgumentException!");
            System.out.println("  Message: " + e.getMessage());
            System.out.println();
            System.out.println("  This is GOOD - the method protected itself!");
            System.out.println("  The caller made a mistake, and we caught it early.");
        }
        System.out.println();
    }

    /**
     * Example of a method that validates its input and throws
     * IllegalArgumentException.
     *
     * @param age the age to set
     * @throws IllegalArgumentException if age is negative
     */
    private void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    /**
     * Shows the proper approach: PREVENT bugs instead of catching them.
     */
    private void showHowToPreventInsteadOfCatch() {
        System.out.println("═".repeat(60));
        System.out.println("✅ THE RIGHT APPROACH: PREVENT, DON'T CATCH");
        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│ ❌ BAD: Catching NPE                   ✅ GOOD: Checking null │");
        System.out.println("│                                                              │");
        System.out.println("│ try {                                  if (name != null) {   │");
        System.out.println("│     name.length();                         name.length();    │");
        System.out.println("│ } catch (NPE e) {                      } else {              │");
        System.out.println("│     // handle null                         // handle null    │");
        System.out.println("│ }                                      }                     │");
        System.out.println("│                                                              │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│ ❌ BAD: Catching AIOOB                 ✅ GOOD: Check bounds  │");
        System.out.println("│                                                              │");
        System.out.println("│ try {                                  if (i < arr.length) { │");
        System.out.println("│     arr[i];                                arr[i];           │");
        System.out.println("│ } catch (AIOOB e) {                    }                     │");
        System.out.println("│     // handle                                                │");
        System.out.println("│ }                                                            │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                              │");
        System.out.println("│ KEY TAKEAWAY:                                                │");
        System.out.println("│ RuntimeExceptions are PREVENTABLE - fix the code!           │");
        System.out.println("│                                                              │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
}
