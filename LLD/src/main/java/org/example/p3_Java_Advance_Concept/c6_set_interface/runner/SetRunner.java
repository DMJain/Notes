package org.example.p3_Java_Advance_Concept.c6_set_interface.runner;

import org.example.p3_Java_Advance_Concept.c6_set_interface.impl.*;

/**
 * Runner class that orchestrates all Set interface demonstrations.
 */
public class SetRunner {

    public static void runAllScenarios() {
        System.out.println("🚀 Starting Set Interface Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        new HashSetDemo().execute();
        new TreeSetDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All Set demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • HashSet: O(1) operations, no order");
        System.out.println("   • LinkedHashSet: O(1) + insertion order");
        System.out.println("   • TreeSet: O(log n), sorted + NavigableSet");
        System.out.println();
        System.out.println("📖 Next: c7_queue_interface (Queue, Deque, PriorityQueue)");
    }
}
