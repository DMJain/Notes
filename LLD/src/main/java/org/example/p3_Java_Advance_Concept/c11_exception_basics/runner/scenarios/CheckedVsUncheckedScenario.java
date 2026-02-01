package org.example.p3_Java_Advance_Concept.c11_exception_basics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c11_exception_basics.model.enums.ExceptionCategory;

/**
 * Demonstrates and compares Checked vs Unchecked exceptions.
 * <p>
 * This scenario helps understand when to use each type
 * and what the practical differences are.
 * </p>
 */
public class CheckedVsUncheckedScenario {

    /**
     * Executes the comparison scenario.
     */
    public void execute() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║         CHECKED vs UNCHECKED EXCEPTIONS COMPARISON             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        showCategories();
        showComparisonTable();
        showDecisionGuide();
        showRealWorldExamples();
    }

    private void showCategories() {
        System.out.println("📋 Exception Categories:");
        System.out.println();

        for (ExceptionCategory category : ExceptionCategory.values()) {
            category.printSummary();
            System.out.println();
        }
    }

    private void showComparisonTable() {
        System.out.println("═".repeat(70));
        System.out.println("                    SIDE-BY-SIDE COMPARISON");
        System.out.println("═".repeat(70));
        System.out.println();
        System.out.println("┌────────────────────────┬──────────────────────┬──────────────────────┐");
        System.out.println("│       ASPECT           │   CHECKED EXCEPTION  │ UNCHECKED EXCEPTION  │");
        System.out.println("├────────────────────────┼──────────────────────┼──────────────────────┤");
        System.out.println("│ Inherits from          │ Exception            │ RuntimeException     │");
        System.out.println("│ Compiler forces        │ YES - must handle    │ NO - optional        │");
        System.out.println("│ Typical cause          │ External failure     │ Programming bug      │");
        System.out.println("│ Preventable?           │ NO (we can't control)│ YES (fix the code)   │");
        System.out.println("│ Recoverable?           │ Usually YES          │ Usually NO (bug)     │");
        System.out.println("│ Common examples        │ IOException          │ NullPointerException │");
        System.out.println("│                        │ SQLException         │ ArrayIndexOutOfBoun  │");
        System.out.println("│                        │ ClassNotFoundException│ ArithmeticException │");
        System.out.println("│ Best practice          │ Handle gracefully    │ FIX THE BUG          │");
        System.out.println("└────────────────────────┴──────────────────────┴──────────────────────┘");
        System.out.println();
    }

    private void showDecisionGuide() {
        System.out.println("═".repeat(70));
        System.out.println("                    WHICH TYPE SHOULD I USE?");
        System.out.println("═".repeat(70));
        System.out.println();
        System.out.println("  Ask yourself these questions:");
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("  │ Q1: Can the programmer PREVENT this from happening?            │");
        System.out.println("  │                                                                 │");
        System.out.println("  │     YES → UNCHECKED (RuntimeException)                         │");
        System.out.println("  │           Example: NPE - check for null first!                 │");
        System.out.println("  │                                                                 │");
        System.out.println("  │     NO  → Continue to Q2...                                    │");
        System.out.println("  └─────────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("  │ Q2: Can the caller RECOVER from this error?                    │");
        System.out.println("  │                                                                 │");
        System.out.println("  │     YES → CHECKED (Exception)                                  │");
        System.out.println("  │           Example: File not found - try another file           │");
        System.out.println("  │                                                                 │");
        System.out.println("  │     NO  → UNCHECKED (or don't throw, just fail)                │");
        System.out.println("  │           Example: Config corrupted - can't continue           │");
        System.out.println("  └─────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private void showRealWorldExamples() {
        System.out.println("═".repeat(70));
        System.out.println("                    REAL-WORLD EXAMPLES");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("  SCENARIO 1: User enters invalid email");
        System.out.println("  ────────────────────────────────────");
        System.out.println("  • Is it preventable? YES, validate before processing");
        System.out.println("  → Use: IllegalArgumentException (UNCHECKED)");
        System.out.println();

        System.out.println("  SCENARIO 2: Database connection fails");
        System.out.println("  ────────────────────────────────────");
        System.out.println("  • Is it preventable? NO, network issues are external");
        System.out.println("  • Can caller recover? YES, retry or use cache");
        System.out.println("  → Use: SQLException (CHECKED)");
        System.out.println();

        System.out.println("  SCENARIO 3: Array access with user-provided index");
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.println("  • Is it preventable? YES, check bounds first");
        System.out.println("  → Use: ArrayIndexOutOfBoundsException (UNCHECKED)");
        System.out.println("         OR throw IllegalArgumentException for bad input");
        System.out.println();

        System.out.println("  SCENARIO 4: Reading a configuration file");
        System.out.println("  ─────────────────────────────────────────");
        System.out.println("  • Is it preventable? NO, file might not exist/be readable");
        System.out.println("  • Can caller recover? YES, use defaults or create file");
        System.out.println("  → Use: IOException/FileNotFoundException (CHECKED)");
        System.out.println();
    }
}
