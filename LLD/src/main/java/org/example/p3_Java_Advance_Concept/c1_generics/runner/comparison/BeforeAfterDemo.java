package org.example.p3_Java_Advance_Concept.c1_generics.runner.comparison;

import java.util.ArrayList;

/**
 * Demonstrates the difference between pre-generics (Java 4) and post-generics
 * (Java 5+) code.
 */
public class BeforeAfterDemo {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│       BEFORE vs AFTER COMPARISON       │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateBefore();
        demonstrateAfter();
        summarize();
    }

    /**
     * Shows how code looked BEFORE generics (Java 4 style).
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void demonstrateBefore() {
        System.out.println("▸ BEFORE Generics (Java 4 and earlier)");
        System.out.println("  ------------------------------------");
        System.out.println();

        // Raw ArrayList - stores Object
        ArrayList names = new ArrayList();
        names.add("Alice");
        names.add("Bob");
        names.add(123); // No error! This is dangerous!

        System.out.println("  ArrayList names = new ArrayList()  // Raw type");
        System.out.println("  names.add(\"Alice\")");
        System.out.println("  names.add(\"Bob\")");
        System.out.println("  names.add(123)  // ⚠️ No compile error!");
        System.out.println();

        // Retrieving requires casting
        System.out.println("  To retrieve:");
        System.out.println("  String first = (String) names.get(0);  // Cast required!");
        String first = (String) names.get(0);
        System.out.println("  Result: " + first);

        System.out.println();
        System.out.println("  ❌ PROBLEMS:");
        System.out.println("     • No compile-time type checking");
        System.out.println("     • Casts everywhere (ugly & error-prone)");
        System.out.println("     • ClassCastException at RUNTIME if wrong type");
        System.out.println("     • No IDE autocomplete help");
        System.out.println();
    }

    /**
     * Shows how code looks AFTER generics (Java 5+ style).
     */
    private void demonstrateAfter() {
        System.out.println("▸ AFTER Generics (Java 5+)");
        System.out.println("  ------------------------");
        System.out.println();

        // Generic ArrayList - type-safe
        ArrayList<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        // names.add(123); // ❌ COMPILE ERROR! Only String allowed

        System.out.println("  ArrayList<String> names = new ArrayList<>()  // Type-safe!");
        System.out.println("  names.add(\"Alice\")");
        System.out.println("  names.add(\"Bob\")");
        System.out.println("  // names.add(123)  // ✅ Compile error prevents this!");
        System.out.println();

        // No casting needed!
        System.out.println("  To retrieve:");
        System.out.println("  String first = names.get(0);  // No cast needed!");
        String first = names.get(0);
        System.out.println("  Result: " + first);

        System.out.println();
        System.out.println("  ✅ BENEFITS:");
        System.out.println("     • Compile-time type checking");
        System.out.println("     • No casts needed");
        System.out.println("     • Errors caught at compile time, not runtime");
        System.out.println("     • Full IDE autocomplete support");
        System.out.println();
    }

    private void summarize() {
        System.out.println("▸ Summary: Why Generics Matter");
        System.out.println("  ----------------------------");
        System.out.println();
        System.out.println("  ┌──────────────────────┬───────────────────┬───────────────────┐");
        System.out.println("  │      Feature          │    Before (Raw)   │   After (Generic) │");
        System.out.println("  ├──────────────────────┼───────────────────┼───────────────────┤");
        System.out.println("  │ Type Safety          │ ❌ Runtime only   │ ✅ Compile time   │");
        System.out.println("  │ Casting Required     │ ❌ Yes, always    │ ✅ No             │");
        System.out.println("  │ IDE Support          │ ❌ Limited        │ ✅ Full           │");
        System.out.println("  │ Error Discovery      │ ❌ Production     │ ✅ Development    │");
        System.out.println("  │ Code Readability     │ ❌ Poor           │ ✅ Clear intent   │");
        System.out.println("  └──────────────────────┴───────────────────┴───────────────────┘");
        System.out.println();
    }
}
