package org.example.p3_Java_Advance_Concept.c7_queue_interface.runner;

import org.example.p3_Java_Advance_Concept.c7_queue_interface.impl.*;

/**
 * Runner class that orchestrates all Queue interface demonstrations.
 */
public class QueueRunner {

    public static void runAllScenarios() {
        System.out.println("🚀 Starting Queue Interface Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        new ArrayDequeDemo().execute();
        new PriorityQueueDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All Queue demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • ArrayDeque: Best for Stack AND Queue");
        System.out.println("   • PriorityQueue: Min-heap, use poll() for sorted order");
        System.out.println();
        System.out.println("📖 Next: c8_map_interface (HashMap, LinkedHashMap, TreeMap)");
    }
}
