package org.example.p3_Java_Advance_Concept.c5_list_interface.runner;

import org.example.p3_Java_Advance_Concept.c5_list_interface.impl.*;
import org.example.p3_Java_Advance_Concept.c5_list_interface.runner.comparison.*;

/**
 * Runner class that orchestrates all List interface demonstrations.
 */
public class ListRunner {

    /**
     * Runs all demonstration scenarios for the List Interface module.
     */
    public static void runAllScenarios() {
        System.out.println("🚀 Starting List Interface Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        // Run implementation demos
        new ArrayListDemo().execute();
        new LinkedListDemo().execute();
        new VectorStackDemo().execute();

        System.out.println("═".repeat(60));

        // Run comparison
        new ArrayListVsLinkedListDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All List demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • ArrayList: Best for random access, add at end");
        System.out.println("   • LinkedList: Best for add/remove at ends, queue/stack");
        System.out.println("   • Vector/Stack: Legacy - avoid in new code");
        System.out.println();
        System.out.println("📖 Next: c6_set_interface (HashSet, LinkedHashSet, TreeSet)");
    }
}
