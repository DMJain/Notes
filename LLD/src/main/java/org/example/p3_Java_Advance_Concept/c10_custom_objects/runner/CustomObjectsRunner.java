package org.example.p3_Java_Advance_Concept.c10_custom_objects.runner;

import org.example.p3_Java_Advance_Concept.c10_custom_objects.impl.*;

/**
 * Runner class that orchestrates all Custom Objects demonstrations.
 */
public class CustomObjectsRunner {

    public static void runAllScenarios() {
        System.out.println("🚀 Starting Custom Objects Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        new HashCodeEqualsDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All Custom Objects demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • Always override BOTH hashCode() AND equals()");
        System.out.println("   • Use Objects.hash() and Objects.equals()");
        System.out.println("   • Make keys immutable to avoid lost entries");
        System.out.println();
        System.out.println("🎉 Collections Framework Module Complete!");
    }
}
