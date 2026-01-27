package org.example.p3_Java_Advance_Concept.c9_iterators.runner;

import org.example.p3_Java_Advance_Concept.c9_iterators.impl.*;

/**
 * Runner class that orchestrates all Iterator demonstrations.
 */
public class IteratorRunner {

    public static void runAllScenarios() {
        System.out.println("🚀 Starting Iterator Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        new IteratorDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All Iterator demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • Use Iterator.remove() for safe removal during iteration");
        System.out.println("   • Use removeIf() for clean conditional removal");
        System.out.println("   • Never modify collection inside for-each loop");
        System.out.println();
        System.out.println("📖 Next: c10_custom_objects (hashCode/equals)");
    }
}
